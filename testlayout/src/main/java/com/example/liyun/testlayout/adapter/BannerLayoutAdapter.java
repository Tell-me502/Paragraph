package com.example.liyun.testlayout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.liyun.testlayout.Banner.BannerImageLoader;
import com.example.liyun.testlayout.Bean.ItemBean;
import com.example.liyun.testlayout.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 通栏布局轮播图
 */
public class BannerLayoutAdapter extends DelegateAdapter.Adapter {
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private List<ItemBean> itemBeanList;
    private int mCount=1;

    public BannerLayoutAdapter(Context context, LayoutHelper layoutHelper, List<ItemBean> datas){
        mContext=context;
        mLayoutHelper=layoutHelper;
        itemBeanList=datas;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.vlayout_adapter_item_banner,viewGroup,false);

        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        BannerViewHolder bannerViewHolder= (BannerViewHolder) viewHolder;
        //绑定轮播图数据
        bindBanner(bannerViewHolder);

    }

    /**
     * 轮播图样式
     * @param bannerViewHolder
     */
    private void bindBanner(final BannerViewHolder bannerViewHolder) {
        //轮播图常规设置
        bannerViewHolder.mBannerView.setIndicatorGravity(BannerConfig.CENTER);//设置指示器居中显示
        //设置只显示指示器
        bannerViewHolder.mBannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //加载Banner数据
        bannerViewHolder.mBannerView.setImageLoader(new BannerImageLoader());//设置图片加载器

        //设置图片轮播图数据
        setBannerData(bannerViewHolder);
    }

    /**
     * 设置轮播图数据
     * @param bannerViewHolder
     */
    private void setBannerData(final BannerViewHolder bannerViewHolder) {
        List<String> images=new ArrayList<String>();
        final List<String> titles=new ArrayList<String>();
        for (ItemBean itemBean:itemBeanList){
            images.add(itemBean.getImage());
            titles.add(itemBean.getTitle());
        }
        bannerViewHolder.mBannerView.setImages(images);
        bannerViewHolder.mBannerView.setBannerTitles(titles);
        //banner设置方法全部调用完毕时最后调用
        bannerViewHolder.mBannerView.start();

        //设置ViewPager滑动监听
        bannerViewHolder.mBannerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //设置提示标题
                bannerViewHolder.mBannerTitle.setText(titles.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //设置点击事件，下标从0开始
        bannerViewHolder.mBannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //这里用一个接口回调的监听方式
                if (bannerCallBack!=null){
                    bannerCallBack.onBannerCallBack(itemBeanList.get(position));
                }
            }
        });

        //设置标题文本为默认第一条数据的标题
        bannerViewHolder.mBannerTitle.setText(titles.get(0));

    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{
        Banner mBannerView;
        TextView mBannerTitle;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            mBannerView=itemView.findViewById(R.id.home_banner);
            mBannerTitle=itemView.findViewById(R.id.tv_banner_title);
        }
    }

    public interface BannerCallBack{
        void onBannerCallBack(ItemBean itemBean);
    }
    private BannerCallBack bannerCallBack;

    public void setOnBannerCallBack(BannerCallBack bannerCallBack){
        this.bannerCallBack=bannerCallBack;
    }
}
