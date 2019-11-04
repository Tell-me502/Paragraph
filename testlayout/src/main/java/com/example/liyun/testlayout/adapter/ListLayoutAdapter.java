package com.example.liyun.testlayout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.liyun.testlayout.Banner.BannerImageLoader;
import com.example.liyun.testlayout.Bean.ItemBean;
import com.example.liyun.testlayout.R;

import java.util.List;

/**
 * 列表布局
 */
public class ListLayoutAdapter extends DelegateAdapter.Adapter {
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private List<ItemBean> itemBeanList;
    private int mCount=1;

    public ListLayoutAdapter(Context context, LayoutHelper layoutHelper, List<ItemBean> itemBeans,int count){
        mContext=context;
        mLayoutHelper=layoutHelper;
        itemBeanList=itemBeans;
        mCount=count;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext.getApplicationContext());
        View view=inflater.inflate(R.layout.vlayout_adapter_item_list,viewGroup,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ItemBean itemBean=itemBeanList.get(i);
        ListViewHolder listViewHolder= (ListViewHolder) viewHolder;
        //绑定数据
        bindList(listViewHolder,itemBean);
    }

    /**
     * 绑定数据
     * @param listViewHolder
     * @param itemBean
     */
    private void bindList(ListViewHolder listViewHolder, final ItemBean itemBean) {
        //缩略图
        BannerImageLoader.getImageLoader().displayImage(mContext,itemBean.getImage(),listViewHolder.iv_list);
        //标题
        listViewHolder.titleList.setText(itemBean.getTitle());
        //监听事件
        listViewHolder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listCallBack!=null){
                    listCallBack.clickList(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout listLayout;
        private ImageView iv_list;
        private TextView titleList;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            listLayout=itemView.findViewById(R.id.vlayout_list);
            iv_list=itemView.findViewById(R.id.iv_list);
            titleList=itemView.findViewById(R.id.tv_title_list);
        }
    }

    //列表点击回调事件接口
    public interface ListCallBack{
        void clickList(ItemBean itemBean);
    }
    private ListCallBack listCallBack;

    public void setListCallBack(ListCallBack listCallBack){
        this.listCallBack=listCallBack;
    }
}
