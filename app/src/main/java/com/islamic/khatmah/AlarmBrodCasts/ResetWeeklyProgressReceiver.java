package com.islamic.khatmah.AlarmBrodCasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.islamic.khatmah.constants.Constant;

public class ResetWeeklyProgressReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        preferences.edit().putInt(Constant.WEEKLY_PROGRESS,0).apply();
    }
}