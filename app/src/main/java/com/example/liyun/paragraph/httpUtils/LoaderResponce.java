package com.example.liyun.paragraph.httpUtils;



import android.util.Log;

import com.example.liyun.paragraph.FunsApi_translation;
import com.example.liyun.paragraph.ProjectAPI;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;



/**
 * 创建一个返回数据处理的Loaderlei，统一处理数据，继承ObjectLoader
 */
public class LoaderResponce extends ObjectLoader{
    //创建网络请求接口实例
    private ProjectAPI request;
    public LoaderResponce(){
        request=RetrofitServiceManager.getInstance().create(ProjectAPI.class);
    }
    //发送请求，设置参数
    public Observable<List<FunsApi_translation.Datas>> getData(int page, int pageSize){
        Log.d("datas", "进入: ");
        return observa(request.getDatas(page,pageSize))
                .map(new Function<FunsApi_translation, List<FunsApi_translation.Datas>>() {

                    @Override
                    public List<FunsApi_translation.Datas> apply(FunsApi_translation funsApi_translation) throws Exception {
                        Log.d("datas", "observa: "+funsApi_translation.toString());
                        return funsApi_translation.getResult().getDatas();
                    }
                });
    }

}
