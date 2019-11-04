package com.example.liyun.testservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String a=intent.getStringExtra("broad");
        Toast.makeText(context,a,Toast.LENGTH_LONG).show();
        Intent intentService=new Intent(context,MyService.class);
        context.startService(intentService);
        Log.i("MyService","接收广播成功并启动了服务"+a);
    }
}
