package com.example.liyun.testlayout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.liyun.testlayout.R;

/**
 * sticky布局，可一配置吸顶或吸底
 */
public class StickyTitleAdapter extends DelegateAdapter.Adapter {
    private Context mContent;
    private LayoutHelper mLayoutHelper;
    private String mTitle;
    private int mCount=1;

    public StickyTitleAdapter(Context context,LayoutHelper layoutHelper,String title){
        mContent=context;
        mLayoutHelper=layoutHelper;
        mTitle=title;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContent.getApplicationContext());
        View view=inflater.inflate(R.layout.vlayout_adapter_item_sticky,viewGroup,false);

        return new StickyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        StickyViewHolder stickyViewHolder= (StickyViewHolder) viewHolder;
        stickyViewHolder.stickyTitle.setText(mTitle);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class StickyViewHolder extends RecyclerView.ViewHolder{
        private TextView stickyTitle;
        public StickyViewHolder(@NonNull View itemView) {
            super(itemView);
            stickyTitle=itemView.findViewById(R.id.vlayout_sticky_title);
        }
    }
}
