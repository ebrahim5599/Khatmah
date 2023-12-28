package com.islamic.khatmah.AlarmBrodCasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.islamic.khatmah.alarm.AlarmReminder;
import com.islamic.khatmah.pojo.Constant;

public class OnRebootReceiver extends BroadcastReceiver {
    int sHour;
    int sMinute;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, 0);
        sHour = sharedPreferences.getInt(Constant.ALARM_HOUR, 0);
        sMinute = sharedPreferences.getInt(Constant.ALARM_MINUTE, 0);
        if (intent.getAction() == "android.intent.action.BOOT_COMPLETED") {
            AlarmReminder alarmReminder = new AlarmReminder(sHour, sMinute);
            alarmReminder.rebootSchedule(context);
            alarmReminder.removeFinishDailyPortion(context);
        }
    }
}