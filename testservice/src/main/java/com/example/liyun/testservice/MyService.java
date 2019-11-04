package com.example.liyun.testservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends Service {

    private final String TAG="MyService";

    private MediaPlayer mediaPlayer;

    //实例化广播子类
    private MyBroadcast myBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

    private int startID;

    public enum Control{
        PLAY, PAUSE, STOP
    }

    /**
     * 初始化资源
     */
    @Override
    public void onCreate() {
        if (mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(this,R.raw.luzhouyue);
            mediaPlayer.setLooping(false);
        }
        Log.i(TAG,"onCreate");
        /*//注册广播
        Log.i("MyService","注册广播");
        myBroadcastReceiver=new MyBroadcast();
        IntentFilter intentFilter=new IntentFilter();
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        intentFilter.addAction("INTENT_MYSERVICE");
        localBroadcastManager.registerReceiver(myBroadcastReceiver,intentFilter);*/
        super.onCreate();
    }

    /**
     * onStartCommand方式中，返回START_STICKY或则START_REDELIVER_INTENT
     *
     * START_STICKY：如果返回START_STICKY，表示Service运行的进程被Android系统强制杀掉之后，Android系统会将该Service依然设置为started状态（即运行状态），但是不再保存onStartCommand方法传入的intent对象
     * START_NOT_STICKY：如果返回START_NOT_STICKY，表示当Service运行的进程被Android系统强制杀掉之后，不会重新创建该Service
     * START_REDELIVER_INTENT：如果返回START_REDELIVER_INTENT，其返回情况与START_STICKY类似，但不同的是系统会保留最后一次传入onStartCommand方法中的Intent再次保留下来并再次传入到重新创建后的Service的onStartCommand方法中
     *
     * 重写此方法做接收数据处理
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //useStartForeground();
        //useStartForeground(intent);
        this.startID=startId;
        Log.i(TAG,"onStartCommand---startId: " + startId);
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            Control control= (Control) bundle.getSerializable("KEY");
            if (control!=null){
                switch (control){
                    case PLAY:
                        play();
                        break;
                    case PAUSE:
                        pause();
                        break;
                    case STOP:
                        stop();
                        break;
                }
            }

        }
        //保证不被杀死
        return Service.START_REDELIVER_INTENT;
    }
    private void useStartForeground(){
        //这里要注意一点：channelid必须要一致，否者服务还是一样会被杀死
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = mNotificationManager.getNotificationChannel("MyService");
            if (notificationChannel == null) {
                NotificationChannel channel = new NotificationChannel("MyService",
                        "com.example.liyun.testservice", NotificationManager.IMPORTANCE_HIGH);
                //是否在桌面icon右上角展示小红点
                channel.enableLights(true);
                //小红点颜色
                channel.setLightColor(Color.RED);
                //通知显示
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                //是否在久按桌面图标时显示此渠道的通知
                //channel.setShowBadge(true);
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        int notifyId = (int) System.currentTimeMillis();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "MyService");
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mBuilder.setContentTitle(getResources().getString(R.string.app_name));
        }
        //当id设置为0时，隐藏不显示通知，那么服务在60s后一样时会被杀死的。
        //要如何隐藏通知而服务不被杀死，这个还在学习中。
        startForeground(1,mBuilder.build());
    }

    private void useStartForeground(Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            /**
             * api参考链接：https://developer.android.google.cn/reference/android/app/NotificationChannel.html
             * 参考链接：https://www.imooc.com/article/29252
             */
            NotificationChannel channel = new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.setBypassDnd(false);
            channel.setLightColor(Color.RED);
            channel.enableLights(true);
            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

            PendingIntent pendingResult = PendingIntent.getActivity(this, 0, intent, 0);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("你有一条新的消息")
                    .setContentText("this is normal notification style")
                    .setTicker("notification ticker")
                    .setPriority(1000)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setNumber(3)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingResult)
                    .setOngoing(true);

            Notification notification = mBuilder.build();
            notificationManager.notify(0,notification);



            //Notification notification=new Notification();
            notification.flags=Notification.FLAG_FOREGROUND_SERVICE;
            startForeground(0, notification);
        }
    }


    public void play(){
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }
    public void pause(){
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    public void stop(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
        }
        //终止服务
        stopSelf();
        //stopService(intent);  intent为onStartConmmand传入的intent
    }



    /**
     * 释放回收资源
     */
    @Override
    public void onDestroy() {
        /*if (mediaPlayer.isPlaying()){
            //当歌曲还没播完时，发送启动服务广播
            Intent intent=new Intent();
            intent.setAction("INTENT_MYSERVICE");
            intent.setPackage("com.example.liyun.testservice");
            intent.putExtra("broad","接收成功！！！");
            sendBroadcast(intent);
            Log.i(TAG,"发送广播成功");
            return;
        }*/
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(true);
        }

        Log.i(TAG,"onDestroy");
    }

    /**
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        return (IBinder) new UnsupportedOperationException("Not yet implemented");
    }



}
