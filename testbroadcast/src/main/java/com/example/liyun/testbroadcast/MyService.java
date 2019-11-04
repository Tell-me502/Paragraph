package com.example.liyun.testbroadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("MyBroadcast","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*Intent intent1=new Intent();
        intent1.putExtra("msg","发送广播信息");
        intent1.setAction("INTENT_BROADCAST");
        //发送给静态注册的广播时，需要加上包名
        //intent1.setPackage("com.example.liyun.testbroadcast");
        sendBroadcast(intent1);*/
        Log.i("MyBroadcast","onStartCommand"+startId);
        Log.i("MyBroadcast",intent.getStringExtra("service"));
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Intent intent1=new Intent("INTENT_BROADCAST");
        intent1.putExtra("msg","发送广播信息");
        //intent1.setAction("INTENT_BROADCAST");
        //发送给静态注册的广播时，需要加上包名
        intent1.setPackage("com.example.liyun.testbroadcast");
        sendBroadcast(intent1);
        Log.i("MyBroadcast","onDestroy");
        return;
    }
}
