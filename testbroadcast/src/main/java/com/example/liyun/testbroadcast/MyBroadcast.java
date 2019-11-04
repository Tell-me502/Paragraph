package com.example.liyun.testbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class MyBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg=intent.getStringExtra("msg");
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        Log.i("MyBroadcast",msg);
        Intent intentService=new Intent(context,MyService.class);
        intentService.putExtra("service","第二次启动服务");
        intentService.setAction("MYSERVICE");
        context.startService(intentService);
    }
}
