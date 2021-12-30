package com.islamic.khatmah.AlarmBrodCasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.services.RemovingCardService;

import java.util.Calendar;

public class RemoveFinishDailyPortionBCR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "لا تنسَ قراءة الورد اليومي", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constant.FINISH_DAILY_PROGRESS, false).apply();

        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            sharedPreferences.edit().putInt(Constant.WEEKLY_PROGRESS, 0).apply();

    }
}