package com.example.liyun.testlayout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.liyun.testlayout.Bean.ItemBean;
import com.example.liyun.testlayout.R;

import java.util.List;

/**
 * 通栏布局-横向列表
 */
public class HorizontalListLayoutAdapter extends DelegateAdapter.Adapter {
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private List<ItemBean> itemBeanList;
    private int mCount=1;

    public HorizontalListLayoutAdapter(Context context, LayoutHelper layoutHelper, List<ItemBean> itemBeans){
        mContext=context;
        mLayoutHelper=layoutHelper;
        itemBeanList=itemBeans;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext.getApplicationContext());
        View view=inflater.inflate(R.layout.vlayout_adapter_item_horizontal,viewGroup,false);

        return new HorizontalViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        HorizontalViewholder mHViewHolder= (HorizontalViewholder) viewHolder;
        //绑定数据
        bindHorizontalList(mHViewHolder);
    }

    /**
     * 横向列表样式
     * @param mHViewHolder
     */
    private void bindHorizontalList(HorizontalViewholder mHViewHolder) {
        //设置布局管理器（现行管理器ListView样式，支持横向、纵向）
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置横向布局
        mHViewHolder.rv_horizontal.setLayoutManager(linearLayoutManager);

        //设置适配器
        HorizontalListViewAdapte mHorizontalAdapter=new HorizontalListViewAdapte(mContext,itemBeanList);
        mHViewHolder.rv_horizontal.setAdapter(mHorizontalAdapter);
        mHorizontalAdapter.setOnHorizontalListItemClickLitener(new HorizontalListViewAdapte.OnHorizontalListItemClickLitener() {
            @Override
            public void onItemClick(ItemBean itemBean) {
                if (mHorizontalListCallBack!=null){
                    mHorizontalListCallBack.ClickHorizontalItem(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class HorizontalViewholder extends RecyclerView.ViewHolder{
        private RecyclerView rv_horizontal;
        public HorizontalViewholder(@NonNull View itemView) {
            super(itemView);
            rv_horizontal=itemView.findViewById(R.id.rv_horizontal);

        }


    }

    //监听回调接口
    public interface HorizontalListCallBack{
        void ClickHorizontalItem(ItemBean itemBean);
    }

    private HorizontalListCallBack mHorizontalListCallBack;

    public void setHorizontalListCallBack(HorizontalListCallBack horizontalListCallBack){
        this.mHorizontalListCallBack=horizontalListCallBack;
    }


}
