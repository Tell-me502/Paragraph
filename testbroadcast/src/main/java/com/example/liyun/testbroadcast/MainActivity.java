package com.example.liyun.testbroadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static MainActivity activity=null;

    public static MainActivity getInstance(){
        if (activity==null){
            synchronized (MainActivity.class){
                if (activity==null){
                    activity=new MainActivity();
                }
            }
        }
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    Intent intent;
    public void sendBroad(View view){
        intent=new Intent(this,MyService.class);
        intent.putExtra("service","第一次启动服务");
        startService(intent);
    }

    public void stopService(View view){
        stopService(intent);
        Log.i("MyBroadcast","关闭服务");
    }
    private MyBroadcast myBroadcast;
    @Override
    protected void onResume() {
        myBroadcast=new MyBroadcast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("INTENT_BROADCAST");
        registerReceiver(myBroadcast,intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(myBroadcast);
        super.onPause();
    }
}
