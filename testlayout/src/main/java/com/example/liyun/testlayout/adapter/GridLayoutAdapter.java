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
 * 九宫格布局
 */
public class GridLayoutAdapter extends DelegateAdapter.Adapter {
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private List<ItemBean> itemBeanList;
    private int mCount=1;

    public GridLayoutAdapter(Context context, LayoutHelper layoutHelper, List<ItemBean> itemBeans,int count){
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
        View view=inflater.inflate(R.layout.vlayout_adapter_item_grid,viewGroup,false);

        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ItemBean itemBean=itemBeanList.get(i);
        GridViewHolder gridViewHolder= (GridViewHolder) viewHolder;
        //绑定数据
        bindGrid(gridViewHolder,itemBean);
    }

    private void bindGrid(GridViewHolder gridViewHolder, final ItemBean itemBean) {
        BannerImageLoader.getImageLoader().displayImage(mContext,itemBean.getImage(),gridViewHolder.iv_grid);
        gridViewHolder.tv_title.setText(itemBean.getTitle());

        //点击监听事件
        gridViewHolder.gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridCallBack!=null){
                    gridCallBack.ClickGrid(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class GridViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout gridLayout;
        private ImageView iv_grid;
        private TextView tv_title;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            gridLayout=itemView.findViewById(R.id.layout_grid);
            iv_grid=itemView.findViewById(R.id.iv_grid);
            tv_title=itemView.findViewById(R.id.tv_title_grid);
        }
    }

    //监听接口回调
    public interface GridCallBack{
        void ClickGrid(ItemBean itemBean);
    }
    private GridCallBack gridCallBack;

    public void setGridCallBack(GridCallBack gridCallBack){
        this.gridCallBack=gridCallBack;
    }
}
