package com.example.liyun.testlayout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.liyun.testlayout.R;

public class ScrollLayoutAdapter extends DelegateAdapter.Adapter {
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private String content;
    private int mCount=1;

    public ScrollLayoutAdapter(Context context,LayoutHelper layoutHelper,String content){
        mContext=context;
        mLayoutHelper=layoutHelper;
        this.content=content;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext.getApplicationContext());
        View view=inflater.inflate(R.layout.vlayout_adapter_item_scroll_fix,viewGroup,false);
        return new ScrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ScrollViewHolder holder= (ScrollViewHolder) viewHolder;
        holder.tv_content.setText(content);
        holder.scrollLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scrollCallBack!=null){
                    scrollCallBack.ClickScroll();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class ScrollViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout scrollLayout;
        private TextView tv_content;
        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            scrollLayout=itemView.findViewById(R.id.vlayout_scroll);
            tv_content=itemView.findViewById(R.id.tv_scroll_fix);
        }
    }

    //接口回调
    public interface ScrollCallBack{
        void ClickScroll();
    }

    private ScrollCallBack scrollCallBack;

    public void setScrollCallBack(ScrollCallBack scrollCallBack){
        this.scrollCallBack=scrollCallBack;
    }
}
