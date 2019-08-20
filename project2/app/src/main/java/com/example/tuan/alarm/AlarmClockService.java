package com.example.tuan.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class AlarmClockService extends Service {
    MediaPlayer mediaPlayer;
    int startId;
    boolean isRunning;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("I'm in Service","Hello");
        String receiveKey =  intent.getExtras().getString("switch");
        int soundId = intent.getExtras().getInt("sound");
        assert receiveKey !=null;
        switch (receiveKey){
            case "on":
                startId = 1;
                break;
            case "off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }
        if (!this.isRunning && startId == 1) {
            Log.e("There is no music ","and you want start");
            if (soundId == 0){
                mediaPlayer = MediaPlayer.create(this, R.raw.rs1);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            } else if (soundId == 1){
                mediaPlayer = MediaPlayer.create(this, R.raw.rs2);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
            this.isRunning = true;
        }
        else if (this.isRunning && startId == 0){
                Log.e("There is music ","and you want end");
                mediaPlayer.stop();
                mediaPlayer.reset();
                this.isRunning = false;
        }
        else if (!this.isRunning && startId == 0){
            Log.e("There is no music ","and you want end");
        }
        else if (this.isRunning && startId == 1){
                Log.e("There is music, ","and you want start");
            }
        return START_NOT_STICKY;
    }
}
 /*if (!this.isRunning && startId == 1) {
            Log.e("There is no music ","and you want start");
            if (soundId == 0){
                mediaPlayer = MediaPlayer.create(this, R.raw.rs1);
                mediaPlayer.start();
           } else if (soundId == 1){
                mediaPlayer = MediaPlayer.create(this, R.raw.rs2);
                mediaPlayer.start();
            }
            this.isRunning = true;
        }
        else if (this.isRunning && startId == 0){
            if (mediaPlayer.isPlaying()){
                Log.e("There is music ","and you want end");
                mediaPlayer.stop();
                mediaPlayer.reset();
                this.isRunning = false;
            } else {
                Log.e("There is no music ","and you want end");
            }
        }
        else if (!this.isRunning && startId == 0){
            Log.e("There is no music ","and you want end");
        }
        else if (this.isRunning && startId == 1){
            if (!mediaPlayer.isPlaying()){
                Log.e("There is no music ","and you want start");
                if (soundId == 0){
                    mediaPlayer = MediaPlayer.create(this, R.raw.rs1);
                    mediaPlayer.start();
                } else if (soundId == 1){
                    mediaPlayer = MediaPlayer.create(this, R.raw.rs2);
                    mediaPlayer.start();
                }
            } else {
                Log.e("There is music, ","and you want start");
            }
        }*/
