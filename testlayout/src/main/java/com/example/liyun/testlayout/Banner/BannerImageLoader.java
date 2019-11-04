package com.example.liyun.testlayout.Banner;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.liyun.testlayout.R;
import com.youth.banner.loader.ImageLoader;

import myapp.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * 自定义Banner图片加载器
 */
public class BannerImageLoader extends ImageLoader {
    private static BannerImageLoader imageLoader;
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideApp.with(context.getApplicationContext())
                .load(path)
                //设置加载等待时的图片
                .placeholder(R.drawable.img_loading)
                //设置加载出错时显示图片
                .error(R.drawable.placeholder_banner)
                .fitCenter()
                //默认淡入淡出动画
                .transition(withCrossFade())
                //缓存策略，跳过内存缓存（此处应该设置为false，否则列表刷新时会闪一下）
                .skipMemoryCache(false)
                //缓存策略，硬盘缓存，仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置图片加载优先级
                .priority(Priority.HIGH)
                .into(imageView);
    }



    public static BannerImageLoader getImageLoader(){
        if (imageLoader==null){
            imageLoader=new BannerImageLoader();
        }
        return imageLoader;
    }
}
