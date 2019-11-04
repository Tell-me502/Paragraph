package com.example.liyun.paragraph;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liyun.paragraph.httpUtils.LoaderResponce;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String key="08a0835495acf02af2a84cb68a75a964";
    private String baseUrl="http://v.juhe.cn/joke/";
    private TextView tv_resutlt;
    private SearchView mSearchView;
    boolean i=true;

    //获得fragment 的管理对象FragmentManager:用来再fragment和ativity之间交互的接口
    private FragmentManager mFragmentManager;
    //创建一个管理fragment的事务对象，其中将add，remove，replace等称作为事务
    private FragmentTransaction transaction;
    public Fragment mWebFragment;
    LinearLayout mLinearLayout;
    //得到SearchView返回键
    private SearchView.SearchAutoComplete mSearchBackMenu;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv_resutlt=findViewById(R.id.tv_result);


        //获得Fragment管理对象
        mFragmentManager=getSupportFragmentManager();

        mLinearLayout=findViewById(R.id.main_content);
        /*toolbar.setNavigationIcon(getDrawable(R.drawable.ic_launcher_background));*/
        //设置toolbar返回按钮的点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击左侧返回按钮",Toast.LENGTH_SHORT).show();
                i=true;
                searchBack();

            }
        });



    }

    /**
     * 网络请求
     * @param v
     */
    public void httpClient(View v){
        httpRequest1();
    }

    private void httpRequest() {
        Log.d("http", "httpRequest: 进入");
        //创建网络连接方式OkHttpClient
        OkHttpClient client=new OkHttpClient();
        //创建Retrofit对象
        Retrofit retrofit=new Retrofit.Builder()
                .client(client)  //设置网络请求方式
                .baseUrl(baseUrl)  //设置接口请求连接
                .addConverterFactory(GsonConverterFactory.create())  //设置使用Gson解析器
                .build();
        //创建网络请求接口实例
        ProjectAPI request=retrofit.create(ProjectAPI.class);
        //发送请求，进行封装，设置参数
        Call<FunsApi_translation> call=request.getfuns(key,1,10);

        //发送网络请求
        call.enqueue(new Callback<FunsApi_translation>() {
            @Override
            public void onResponse(Call<FunsApi_translation> cal, Response<FunsApi_translation> response) {
//                FunsApi_translation bean;
//                bean = new Gson().fromJson(String.valueOf(response),FunsApi_translation.class);
                Log.d("response", "onResponse: "+response.body().toString());
                Log.d("tojson", (new Gson()).toJson(response.body()));
                final String content=response.body().getResult().getDatas().get(1).getContent();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_resutlt.setText(content);
                    }
                });
            }

            @Override
            public void onFailure(Call<FunsApi_translation> call, Throwable t) {
                Log.d("Failure","请求失败"+t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 显示Fragment，隐藏主界面main_content
     */
    public void initFragment(){
        //开启Fragment事务
        transaction=mFragmentManager.beginTransaction();
        //显示之前，将所有的界面（main_content）或fragment先隐藏，再去显示我们想要显示的fragment
        hideFragment(transaction);
        if (mLinearLayout.getVisibility()==View.VISIBLE){
            //先隐藏mLinearLayout
            mLinearLayout.setVisibility(View.GONE);
        }

        if (mWebFragment==null){
            //新建fragment
            mWebFragment=new webView_fragment();
            //将fragment加入事务管理
            transaction.add(R.id.fg_webView,mWebFragment);
        }else {
            //如果fragment不为空，直接显示
            transaction.show(mWebFragment);
        }
        //提交事务
        transaction.commit();
    }

    /**
     * 用于隐藏Fragment，显示主界面main_content
     * @param transaction
     */
    public void hideFragment(final FragmentTransaction transaction){
        if (mLinearLayout.getVisibility()==View.GONE){
            //显示主界面
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        //隐藏Fragment
        if (mWebFragment!=null){
            transaction.hide(mWebFragment);
        }

    }

    /**
     * 当SearchView展开搜索时，toolbar返回键按钮点击返回事件
     */
    public void searchBack(){
        if (mSearchBackMenu.isShown()){
            try {
                hideFragment(transaction);
                Toast.makeText(MainActivity.this,"返回主界面，隐藏Fragment",Toast.LENGTH_SHORT).show();
                //如果搜索框中有文字，先清空文字。但网易云音乐是在点击返回键时直接关闭搜索框
                mSearchBackMenu.setText("");
                //获取Search View关闭方法
                Method method=mSearchView.getClass().getDeclaredMethod("onCloseClicked");
                //将此关闭方法设置为true
                method.setAccessible(true);
                //关联到mSearchView视图
                method.invoke(mSearchView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            hideFragment(transaction);
            Toast.makeText(MainActivity.this,"返回主界面，隐藏Fragment",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        /*MenuItem menuItem=findViewById(R.id.action_search);//得到对应的item*/
        mSearchView= (SearchView) menu.findItem(R.id.action_search).getActionView();



        //通过ID得到搜索框返回控件
        mSearchBackMenu=mSearchView.findViewById(R.id.search_src_text);



        mSearchBackMenu.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                mSearchView.onActionViewCollapsed();
                Toast.makeText(MainActivity.this,"返回主界面，隐藏Fragment",Toast.LENGTH_SHORT).show();
                hideFragment(transaction);
            }
        });

        /*//关闭SearchView
        mSearchBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBack();
            }
        });*/
        //searchView折叠监听，当前项目中点击返回键时执行
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //searchBack();
                hideFragment(transaction);
                Toast.makeText(MainActivity.this,"点击左侧返回按钮",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        //mSearchView.onActionViewCollapsed();收起SearchView

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             *提交文本时触发该方法，即按下enter键或点击搜索按钮
             * @param s
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String s) {

                /*Intent intent=new Intent(MainActivity.this,WebView_Activity.class);
                startActivity(intent);*/
                //fragment.isAdded();

                if (i){
                    initFragment();
                    i=false;
                    Toast.makeText(MainActivity.this,"跳转到搜索结果展示界面webView。",Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            /**
             * 当search框文本发生变化时触发。更新搜索下拉框缓存提示
             * @param s
             * @return
             */
            @Override
            public boolean onQueryTextChange(String s) {

                //hideFragment(transaction);
                Toast.makeText(MainActivity.this,"触发搜索下拉框提示",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            //网络请求
            //httpRequest();
            /*hideFragment(transaction);
            Toast.makeText(MainActivity.this,"点击左侧返回按钮",Toast.LENGTH_SHORT).show();*/
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void httpRequest1() {
        LoaderResponce mLoader=new LoaderResponce();
        Log.d("datas", "accept: "+mLoader);
        mLoader.getData(1,10).subscribe(new Consumer<List<FunsApi_translation.Datas>>() {

            @Override
            public void accept(final List<FunsApi_translation.Datas> datas) throws Exception {
                Log.d("datas", "accept: "+datas.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_resutlt.setText(datas.get(1).getContent());
                    }
                });
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("error","trowable"+throwable.toString());
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
