package com.example.liyun.paragraph.httpUtils;



import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.schedulers.Schedulers;




/**
 * 将一些重复操作的提出来，放到父类以免Loader里每个接口都有重复的代码。
 * 此处处理数据使用的时Rxjava订阅数据处理
 */
public class ObjectLoader {
    protected <T> Observable<T> observa(Observable<T> observable){
        Log.d("datas", "observa: Objections");
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
