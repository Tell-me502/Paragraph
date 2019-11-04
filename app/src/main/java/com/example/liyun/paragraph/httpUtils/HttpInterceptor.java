package com.example.liyun.paragraph.httpUtils;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 公共拦截器
 * 向请求头添加公共参数
 */
public class HttpInterceptor implements Interceptor {
    //创建map集合，保存添加到请求头的公共参数
    private Map<String,String> mHeaderParamsMap=new HashMap<>();
    private Map<String,String> mUrlParamsMap=new HashMap<>();
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("request",chain.request().toString());
        Request oldRequest=chain.request();
        Request newRequest;

        /*//新请求(为什么说新操作是从旧请求里面提取内容信息)
        Request.Builder requestBuilder=oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(),oldRequest.body());

        //将公共参数添加到请求头header中去
        if (mHeaderParamsMap.size()>0){//如果添加了请求头公共参数，Map.size>0
            //遍历集合，将参数添加到请求头header中
            for (Map.Entry<String,String> params:mHeaderParamsMap.entrySet()){
                requestBuilder.header(params.getKey(),params.getValue());
            }
        }
        //将公共参数添加到请求头后，建立新请求连接
        Request newRequest=requestBuilder.build();*/

        //获取并判断网络请求的方法
        String method=oldRequest.method();//获取请求方法
        if (method.equals("GET")&&mUrlParamsMap.size()>0){//判断
            //添加参数
            HttpUrl modifieUrl = null;//注意，就算下面没有添加成功也会执行下去
            //遍历集合，将参数添加到Url中
            for (Map.Entry<String,String> params:mUrlParamsMap.entrySet()){
                modifieUrl=oldRequest.url().newBuilder()
                        .addQueryParameter(params.getKey(),params.getValue())
                        .build();
            }
            Log.d("modifieUrl","是否为空："+modifieUrl);
            newRequest=oldRequest.newBuilder().url(modifieUrl).build();//构建新request
            Response response=chain.proceed(newRequest);//进行网络请求，并获得返回结果
            String content=response.body().string();//拿到返回结果，进行分析
            //获得返回结果的类型type
            MediaType mediaType=response.body().contentType();
            //生成新的response返回。注意：如果网络请求的response，如果去除后直接返回，将会抛异常
            //注意：response只去一次值，取完后立即清空销毁，所以最好别在这打印日志
            return response.newBuilder()
                        .body(ResponseBody.create(mediaType,content))
                        .build();

        }

        return chain.proceed(oldRequest);
    }

    //建立静态类，将一些公共参数类型添加到集合里缓存
    public static class Builder{
        //创建对象，用于得到Map存储添加进来的公共参数
        HttpInterceptor mHttpInterceptor;
        public Builder(){
            mHttpInterceptor=new HttpInterceptor();
        }
        public Builder addHeaderParams(String key,String value){
            mHttpInterceptor.mHeaderParamsMap.put(key,value);
            return this;
        }
        public Builder addHeaderParams(String key,int value){
            return addHeaderParams(key,String.valueOf(value));
        }
        public Builder addHeaderParams(String key,long value){
            return addHeaderParams(key,String.valueOf(value));
        }
        public Builder addHeaderParams(String key,float value){
            return addHeaderParams(key,String.valueOf(value));
        }

        public Builder addHeaderParams(String key,double value){
            return addHeaderParams(key,String.valueOf(value));
        }
        public Builder addUrlParams(String key,String value){
            mHttpInterceptor.mUrlParamsMap.put(key,value);
            return this;
        }
        public Builder addUrlParams(String key,double value){
            return addUrlParams(key,String.valueOf(value));
        }
        public Builder addUrlParams(String key,int value){
            return addUrlParams(key,String.valueOf(value));
        }
        public Builder addUrlParams(String key,long value){
            return addUrlParams(key,String.valueOf(value));
        }
        public Builder addUrlParams(String key,float value){
            return addUrlParams(key,String.valueOf(value));
        }
        public HttpInterceptor build(){
            return mHttpInterceptor;
        }
    }
}
