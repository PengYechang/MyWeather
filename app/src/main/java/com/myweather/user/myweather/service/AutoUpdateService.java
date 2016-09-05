package com.myweather.user.myweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.myweather.user.myweather.receiver.AutoUpdateReceiver;
import com.myweather.user.myweather.util.HttpCallbackLister;
import com.myweather.user.myweather.util.HttpUtil;

/**
 * Created by User on 2016/9/5.
 */
public class AutoUpdateService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 4*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String cityCode = prefs.getString("cityCode","");
        String address = "https://api.heweather.com/x3/weather?cityid="+cityCode+"&key=84d5a64a3a8e481a91cde07c8bfe5fe1";
        HttpUtil.sendHttpRequest(address, new HttpCallbackLister() {
            @Override
            public void onFinish(String response) {
                HttpUtil.handleWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
