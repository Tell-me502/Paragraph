package com.example.liyun.paragraph;


import java.util.List;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * 封装请求接口
 */
public interface ProjectAPI {

    @GET("content/text.php")
    Call<FunsApi_translation> getfuns(@Query("key") String key, @Query("page") int page1, @Query("pageSize") int pageSize);

    @GET("joke/content/text.php")
    Observable<FunsApi_translation> getDatas(@Query("key") String key, @Query("page") int page1, @Query("pageSize") int pageSize);

    @GET("joke/content/text.php")
    Observable<FunsApi_translation> getDatas(@Query("page") int page1, @Query("pageSize") int pageSize);

    @GET("content/text.php?key={key}&page=1&pagesize=10")
    Call<FunsApi_translation> getfuns(@Path("key") String key);
}
