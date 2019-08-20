package com.example.tuan.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("I'm in Receiver", "Hello");
        String receiveKey =  intent.getExtras().getString("switch");
        int soundId = intent.getExtras().getInt("sound");
        Intent myIntent = new Intent(context, AlarmClockService.class);
        myIntent.putExtra("switch",receiveKey);
        myIntent.putExtra("sound",soundId);
        context.startService(myIntent);
    }
}
