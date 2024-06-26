package com.islamic.khatmah.alarm;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.islamic.khatmah.AlarmBrodCasts.BroadcastReminder;
import com.islamic.khatmah.AlarmBrodCasts.RemoveFinishDailyPortionBCR;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AlarmReminder {
    @NonNull
    private int alarmId;
    private int hour, minute;

    public AlarmReminder(int hour, int minute) {
        this.alarmId = 0;
        this.hour = hour;
        this.minute = minute;
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void rebootSchedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, BroadcastReminder.class);
        PendingIntent alarmPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
//        String Time = simpleDateFormat.format(calendar.getTime());
//        Toast.makeText(context, Time, Toast.LENGTH_SHORT).show();
        final long RUN_DAILY = 24 * 60 * 60 * 1000;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                RUN_DAILY,
                alarmPendingIntent
        );
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void schedule(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, BroadcastReminder.class);
        PendingIntent alarmPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK) + 1);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
        String Time = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(context, Time, Toast.LENGTH_SHORT).show();
        final long RUN_DAILY = 24 * 60 * 60 * 1000L;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                RUN_DAILY,
                alarmPendingIntent
        );
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void removeFinishDailyPortion(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RemoveFinishDailyPortionBCR.class);
        PendingIntent alarmPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmPendingIntent = PendingIntent.getBroadcast(context, 22, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }else{
            alarmPendingIntent = PendingIntent.getBroadcast(context, 22, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK) + 1);
        }

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
//        String Time = simpleDateFormat.format(calendar.getTime());
//        Toast.makeText(context, Time, Toast.LENGTH_LONG).show();

        final long RUN_DAILY = 24 * 60 * 60 * 1000L;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                RUN_DAILY,
                alarmPendingIntent
        );
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastReminder.class);
        PendingIntent alarmPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.cancel(alarmPendingIntent);
    }
}
