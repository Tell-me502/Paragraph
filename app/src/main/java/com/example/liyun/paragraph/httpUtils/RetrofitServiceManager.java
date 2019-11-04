package com.example.liyun.paragraph.httpUtils;

import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;


import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit请求，生成接口实例的管理类
 */
public class RetrofitServiceManager {
    //设置连接超时
    private static final int DEFAULT_TIME_OUT=5;
    //设置读取超时
    private static final int DEFAULT_READ_TIME_OUT=5;
    //创建retrofit对象
    private Retrofit mRetrofit;

    /**
     * 在构造方法中实现初始化，并配置公共参数
     */
    public RetrofitServiceManager(){
        //创建OkHttpClient
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.callTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时，TimeUnit.SECONDS：代表时间数值的单位（h，min,s,ms等）
        builder.readTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);//读取超时

        //添加公共参数拦截器  需要实现implements Interceptor
        HttpInterceptor interceptorParams=new HttpInterceptor.Builder()
                            .addUrlParams("key",ApiConfig.key)
                            .build();

        /*Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                //设置请求，加入header
                Request newrequest = request.newBuilder()
                        .header( "key", ApiConfig.key )
                        .build();

                //可以处理回应体
                Response response=chain.proceed(newrequest );
                Log.d("xxx",response.request().url()+":"+response.body().string());

                //返回
                return response;
            }
        };*/
        builder.addInterceptor(interceptorParams);
        //创建retrofit
        mRetrofit=new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加Rxjava订阅处理返回的结果,为什么是RxJava2呢？
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiConfig.BASE_URL)
                .build();

    }
    /**
     * 创建 一个静态类，用于将RetrofitServiceManager对象设置为常量，这样就不用每次调用对象时都要去new
     */
    private static class SigleHolder{
        private static final RetrofitServiceManager INSTANCE=new RetrofitServiceManager();
    }
    /**
     * 创建被一个方法，提供外部类获取RetrofitServiceManager 对象
     */
    public static RetrofitServiceManager getInstance(){
        return SigleHolder.INSTANCE;
    }

    /**
     * 获取对应的service接口对象，因为还不知道传入的service返回的是什么类型，所以这里使用泛型适配所有类型的对象
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }
}
