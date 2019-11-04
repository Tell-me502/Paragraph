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

import com.example.liyun.testlayout.Banner.BannerImageLoader;
import com.example.liyun.testlayout.Bean.ItemBean;
import com.example.liyun.testlayout.R;

import java.util.List;

public class HorizontalListViewAdapte extends RecyclerView.Adapter {
private Context mContext;
private List<ItemBean> itemBeanList;

public HorizontalListViewAdapte(Context context,List<ItemBean> itemBeans){
    mContext=context;
    itemBeanList=itemBeans;
}


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext.getApplicationContext());
        View view=inflater.inflate(R.layout.vlayout_adapter_item_horizontal_item,viewGroup,false);

        return new ListViewHolder(view);
    }

    /**
     * 将数据绑定至ViewHolder
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ItemBean itemBean=itemBeanList.get(i);
        final ListViewHolder holder= (ListViewHolder) viewHolder;
        //缩略图
        BannerImageLoader.getImageLoader().displayImage(mContext,itemBean.getImage(),holder.imageView);

        holder.tv_title.setText(itemBean.getTitle());

        //绑定监听事件
        holder.layout_hori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在增加数据或者减少数据时候，position和index就不一样了
                int position=holder.getLayoutPosition();
                if (onItemClickLitener!=null){
                    onItemClickLitener.onItemClick(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemBeanList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout_hori;
        private ImageView imageView;
        private TextView tv_title;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_hori=itemView.findViewById(R.id.layout_hori);
            imageView=itemView.findViewById(R.id.img_thumb);
            tv_title=itemView.findViewById(R.id.tv_title);
        }
    }

    /**
     * 添加Item  -用于动画的展现
     * @param itemModel
     * @param position
     */
    public void addItem(ItemBean itemModel,int position){
        itemBeanList.add(position,itemModel);
        notifyItemInserted(position);
    }

    /**
     * 删除Item   -用于动画展现
     * @param position
     */
    public void removeItem(int position){
        itemBeanList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 添加Item的监听，OnItemClickListener回调
     */
    public interface OnHorizontalListItemClickLitener{
        //列表点击事件
        void onItemClick(ItemBean itemBean);
    }

    private OnHorizontalListItemClickLitener onItemClickLitener;

    public void setOnHorizontalListItemClickLitener(OnHorizontalListItemClickLitener onItemClickLitener){
        this.onItemClickLitener=onItemClickLitener;
    }
}
