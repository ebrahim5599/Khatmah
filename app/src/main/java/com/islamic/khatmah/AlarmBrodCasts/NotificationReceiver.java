package com.islamic.khatmah.AlarmBrodCasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.islamic.khatmah.pojo.Constant;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // load shared Preferences
        SharedPreferences preferences = context.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(Constant.STOP_DOWNLOAD, true).apply();
    }
}