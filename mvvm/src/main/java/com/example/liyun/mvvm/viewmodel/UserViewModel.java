package com.example.liyun.mvvm.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * 作者：YG_Li
 */
public class UserViewModel extends ViewModel {
    //创建一个保存数据类型的LiveData对象
    private MutableLiveData<String> userLiveData;
}
