package com.example.liyun.testlayout;

import android.content.Context;
import android.graphics.Point;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.example.liyun.testlayout.Bean.ItemBean;
import com.example.liyun.testlayout.Bean.ModelBean;
import com.example.liyun.testlayout.adapter.BannerLayoutAdapter;
import com.example.liyun.testlayout.adapter.GridLayoutAdapter;
import com.example.liyun.testlayout.adapter.HorizontalListLayoutAdapter;
import com.example.liyun.testlayout.adapter.ListLayoutAdapter;
import com.example.liyun.testlayout.adapter.ScrollLayoutAdapter;
import com.example.liyun.testlayout.adapter.StickyTitleAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppBarLayout app_bar;
    Toolbar toolbar;

    //Vlayout所需要的变量
    private RecyclerView mVLayoutRV; //列表布局
    private VirtualLayoutManager mLayoutManager;//布局管理器
    private DelegateAdapter delegateAdapter;  //RecycleView布局适配器
    private List<DelegateAdapter.Adapter> adapters;  //Item布局适配器列表

    private RecyclerView.RecycledViewPool mViewPool;  //设置复用池大小
    private int itemType;  //一个Item对应一个类型，采用自增1模式实现唯一性

    private List<ModelBean> modelBeanList;  //列表数据集合


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        app_bar=findViewById(R.id.app_bar);
        //设置监听
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i)<appBarLayout.getTotalScrollRange()){
                    //去掉默认的标题
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    toolbar.setVisibility(View.GONE);
                }else if (Math.abs(i)>=appBarLayout.getTotalScrollRange()){
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        initView();
        try {
            initDatas();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化布局控件
     */
    private void initView() {
        mVLayoutRV=findViewById(R.id.vlayout_rv);
    }

    /**
     * 初始化数据
     */
    private void initDatas() throws JSONException {
        //初始化列表数据
        modelBeanList=new ArrayList<ModelBean>();
        getDatas();

        //1、初始化布局管理器 Layout Manager
        mLayoutManager=new VirtualLayoutManager(this);
        mLayoutManager.setRecycleOffset(300);
        mVLayoutRV.setLayoutManager(mLayoutManager);
        /*2、设置回收复用池,并设置其大小
        * 如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View
        * 针对type=0的item设置了复用池的大小，如果你的页面有多种type，
        * 需要为每一种类型的分别调整复用池大小参数。
        * 这里的设置其大小根据后面的type再设置。
        * */
        mViewPool=new RecyclerView.RecycledViewPool();
        mVLayoutRV.setRecycledViewPool(mViewPool);

        //3、创建适配器，适配展示数据。加载数据，通过创建adapter集合实现布局
        ////必须使用false，实现每一个分组的类型不同
        delegateAdapter=new DelegateAdapter(mLayoutManager,false);
        mVLayoutRV.setAdapter(delegateAdapter);

        //设置适配器
        setVLayoutAdapter();
    }


    /**
     * 设置适配器
     */
    private void setVLayoutAdapter() {
        itemType=0;//自增加1
        if (adapters!=null){
            adapters.clear();
        }else {
            adapters=new LinkedList<>();
        }


        //根据类型不同，采用不同的adapter并添加到集合中
        for (int i=0;i<modelBeanList.size();i++){
            String modelName=modelBeanList.get(i).getModelName();
            String type=modelBeanList.get(i).getType();
            List<ItemBean> itemBeanList=modelBeanList.get(i).getItemDataList();

            //控制是否显示modelname，即吸顶布局
            boolean showModelName=true;
            if (showModelName){
                //stick布局，可以配置吸顶或吸底
                //设置各个区域复用池大小，因为只有一个元素，所以就设置大小为1
                mViewPool.setMaxRecycledViews(itemType++,1);
                StickyLayoutHelper stickyLayoutHelper=new StickyLayoutHelper();
                StickyTitleAdapter stickyTitleAdapter=new StickyTitleAdapter(this,stickyLayoutHelper,modelName);
                adapters.add(stickyTitleAdapter);
            }

            //用样式判断类分组，展示不同样式
            //每一个item对应一个样式
            switch (type){
                case "banner":
                    //通栏布局——轮播图
                    //设置各个区域的复用池的大小，设置子集合的总个数为复用池大小
                    mViewPool.setMaxRecycledViews(itemType++,itemBeanList.size());
                    //通栏布局，只会显示一个组件View
                    SingleLayoutHelper singleLayoutHelper=new SingleLayoutHelper();
                    //设置外边距，实现分割效果
                    singleLayoutHelper.setMargin(0,0,0,dip2px(this,10));
                    BannerLayoutAdapter bannerLayoutAdapter=new BannerLayoutAdapter(this,singleLayoutHelper,itemBeanList);
                    //设置自定义回调，用于点击监听事件
                    bannerLayoutAdapter.setOnBannerCallBack(new BannerLayoutAdapter.BannerCallBack() {
                        @Override
                        public void onBannerCallBack(ItemBean itemBean) {
                            Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    adapters.add(bannerLayoutAdapter);
                    break;
                case "hori":
                    //通栏布局——横向列表
                    mViewPool.setMaxRecycledViews(itemType++,itemBeanList.size());
                    SingleLayoutHelper horiSingleLayoutHelper=new SingleLayoutHelper();
                    horiSingleLayoutHelper.setMargin(0,0,0,dip2px(this,10));
                    HorizontalListLayoutAdapter horizontalListLayoutAdapter=new HorizontalListLayoutAdapter(this,horiSingleLayoutHelper,itemBeanList);
                    horizontalListLayoutAdapter.setHorizontalListCallBack(new HorizontalListLayoutAdapter.HorizontalListCallBack() {
                        @Override
                        public void ClickHorizontalItem(ItemBean itemBean) {
                            Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    adapters.add(horizontalListLayoutAdapter);
                    //设置ScrollFixLayoutHelper返回顶部,adapter在哪个布局之后添加，滑到那个布局就会显示出来
                    //设置ScrollFixLayoutHelper返回顶部,
                    /**
                     * TOP_RIGHT:顶部右侧对齐
                     * TOP_LEFT:顶部左侧对齐
                     * BOTTOM_RIGHT:底部右侧对齐
                     * BOTTOM_LEFT:底部左侧对齐
                     */
                    ScrollFixLayoutHelper scrollFixLayoutHelper=new ScrollFixLayoutHelper(ScrollFixLayoutHelper.BOTTOM_RIGHT,60,100);
                    /**
                     * SHOW_ALWAYS：与FixLayoutHelper的行为一致，固定在某个位置；
                     * SHOW_ON_ENTER：默认不显示视图，当页面滚动到这个视图的位置的时候，才显示；
                     * SHOW_ON_LEAVE：默认不显示视图，当页面滚出这个视图的位置的时候显示；
                     */
                    scrollFixLayoutHelper.setShowType(ScrollFixLayoutHelper.SHOW_ON_LEAVE);
                    ScrollLayoutAdapter scrollLayoutAdapter=new ScrollLayoutAdapter(this,scrollFixLayoutHelper,"返回顶部");
                    scrollLayoutAdapter.setScrollCallBack(new ScrollLayoutAdapter.ScrollCallBack() {
                        @Override
                        public void ClickScroll() {
                            if (mVLayoutRV!=null&&mVLayoutRV.getChildAt(0).getY()!=0){
                                mVLayoutRV.scrollToPosition(0);//快速滚动到顶部
                                //mVLayoutRV.smoothScrollToPosition(0);//缓慢滚动到顶部
                            }
                            Toast.makeText(MainActivity.this,"返回顶部",Toast.LENGTH_SHORT).show();
                        }
                    });
                    adapters.add(scrollLayoutAdapter);
                    break;
                case "grid":
                    //九宫格布局
                    mViewPool.setMaxRecycledViews(itemType++,itemBeanList.size());
                    //Grid布局， 支持横向的colspan
                    GridLayoutHelper gridLayoutHelper=new GridLayoutHelper(2);
                    //解决单数的时候，最后一张居中显示的问题
                    gridLayoutHelper.setAutoExpand(false);
                    gridLayoutHelper.setMargin(0,0,0,dip2px(this,10));
                    GridLayoutAdapter gridLayoutAdapter=new GridLayoutAdapter(this,gridLayoutHelper,itemBeanList,itemBeanList.size());
                    gridLayoutAdapter.setGridCallBack(new GridLayoutAdapter.GridCallBack() {
                        @Override
                        public void ClickGrid(ItemBean itemBean) {
                            Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    adapters.add(gridLayoutAdapter);
                    break;
                case "list":
                    //列表布局(默认布局)
                    mViewPool.setMaxRecycledViews(itemType++,itemBeanList.size());
                    LinearLayoutHelper linearLayoutHelper=new LinearLayoutHelper();
                    linearLayoutHelper.setMargin(0,0,0,dip2px(this,10));
                    ListLayoutAdapter listLayoutAdapter=new ListLayoutAdapter(this,linearLayoutHelper,itemBeanList,itemBeanList.size());
                    listLayoutAdapter.setListCallBack(new ListLayoutAdapter.ListCallBack() {
                        @Override
                        public void clickList(ItemBean itemBean) {
                            Toast.makeText(MainActivity.this,itemBean.getTitle(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    adapters.add(listLayoutAdapter);
                    break;
            }
        }


        //为vLayout设置适配
        delegateAdapter.setAdapters(adapters);
    }

    public Point getMaxXY(){
        Display display=getWindow().getWindowManager().getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        return point;
    }

    public static int px2pd(Context context,float vlaue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(vlaue/scale+0.5f);
    }
    /**
     * dp转px
     * 16dp - 48px
     * 17dp - 51px*/
    public static int dip2px(Context context,float dpVlaue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) ((dpVlaue*scale)+0.5f);
    }

    /**
     * 获取测试数据
     */
    private void getDatas() throws JSONException {
        String ListDatas=getStringFromAssert(MainActivity.this,"vlayoutDatas.txt");

            //JSONObject listzObj=new JSONObject(ListDatas.substring(1));
        //因为vlayoutDatas.txt是utf-8的BOM格式编码，所以要去掉头部的"\ufeff"，方法：ListDatas.substring(1)
            JsonObject listObj=(JsonObject)new JsonParser().parse(ListDatas.substring(1));
            //解析获得data节点的数据存入list
            JsonArray listArray=listObj.getAsJsonArray("data");
            for (int i=0;i<listArray.size();i++){
                JsonObject itemObj= (JsonObject) listArray.get(i);
                ModelBean modelBean=new ModelBean();
                modelBean.setModelName(itemObj.get("modelname").getAsString());
                modelBean.setType(itemObj.get("type").getAsString());
                JsonArray childArray=itemObj.getAsJsonArray("data");
                List<ItemBean> itemBeanList=new ArrayList<ItemBean>();
                for (int j=0;j<childArray.size();j++){
                    JsonObject childObj= (JsonObject) childArray.get(j);
                    ItemBean itemBean=new ItemBean();
                    itemBean.setId(childObj.get("id").getAsString());
                    itemBean.setImage(childObj.get("image").getAsString());
                    itemBean.setOrder(childObj.get("order").getAsString());
                    itemBean.setTitle(childObj.get("title").getAsString());
                    itemBean.setUrl(childObj.get("url").getAsString());
                    itemBeanList.add(itemBean);
                }
                modelBean.setItemDataList(itemBeanList);
                modelBeanList.add(modelBean);
            }

    }

    /**
     * 访问assets目录下的资源文件，获取文件中的字符串
     * @param context
     * @param path assetsFilePath - 文件的相对路径，例如："listitemdata.txt或者"/testlayout/listdata.txt"
     * @return  内容字符串
     */
    public static String getStringFromAssert(Context context,String path){
        String content="";//内容字符串
        try {
            //打开文件
            InputStream is=context.getResources().getAssets().open(path);
            //实现一个输出流
            ByteArrayOutputStream os=new ByteArrayOutputStream();
            int ch=0;
            while ((ch=is.read())!=-1){
                // 将指定的字节写入此 byte 数组输出流
                os.write(ch);
            }
            // 以 byte 数组的形式返回此输出流的当前内容
            byte[] buff=os.toByteArray();
            //关闭流
            os.close();
            is.close();
            // 设置字符串编码
            content=new String(buff,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "对不起，没有找到指定文件！", Toast.LENGTH_SHORT)
                    .show();
        }
        return content;
    }


}
