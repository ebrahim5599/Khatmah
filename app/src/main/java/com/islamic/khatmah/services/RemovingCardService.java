package com.islamic.khatmah.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import com.islamic.khatmah.constants.Constant;
import java.util.Calendar;

public class RemovingCardService extends Service {
    public RemovingCardService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constant.FINISH_DAILY_PROGRESS, false).apply();
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            sharedPreferences.edit().putInt(Constant.WEEKLY_PROGRESS, 0).apply();
        return START_STICKY;
    }
}