package com.example.liyun.testservice;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(this,R.raw.luzhouyue);
            mediaPlayer.setLooping(false);
        }
        Log.i("TAG","onCreate");*/


    }

    public void playMusic(View view){
        Intent intent=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("KEY",MyService.Control.PLAY);
        intent.putExtras(bundle);
        startService(intent);
        /*if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        Log.i("TAG","play");*/
    }

    public void pauseMusic(View view){
        Intent intent=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("KEY",MyService.Control.PAUSE);
        intent.putExtras(bundle);
        startService(intent);
        /*if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        Log.i("TAG","pause");*/
    }

    public void stopMusic(View view){
        Intent intent=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("KEY",MyService.Control.STOP);
        intent.putExtras(bundle);
        startService(intent);
        //或者这样停止服务
        /*Intent intent=new Intent(this,MyService.class);
        stopService(intent);*/
        /*if (mediaPlayer!=null){
            mediaPlayer.stop();
        }
        Log.i("TAG","stop");*/
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        /*Log.i("TAG","onDestroy");
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }*/
        super.onDestroy();
    }
}
