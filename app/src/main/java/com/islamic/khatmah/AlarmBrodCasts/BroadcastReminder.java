package com.islamic.khatmah.AlarmBrodCasts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.islamic.khatmah.services.NotificationService;
import com.islamic.khatmah.ui.main.MainActivity;
import com.islamic.khatmah.R;

public class BroadcastReminder extends BroadcastReceiver {

    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MainActivity.class);
        Log.d("Remind", "BroadcastReminder");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "DAILY REMINDER", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Khatmah BroadcastReminder");
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }


        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon_khatmah)
                .setContentTitle("Khatmah - ختــمة")
                .setContentText("تذكير بمعاد الورد اليومي")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setAutoCancel(true);

        Notification incomingCallNotification = notification.build();

        NotificationManagerCompat notificationManger = NotificationManagerCompat.from(context);
        notificationManger.notify(0, incomingCallNotification);

//// TODO: MAKE NOTIFICATION RUN IN FOREGROUND SERVICE.
//        Intent intentService = new Intent(context, NotificationService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intentService);
//        } else {
//            context.startService(intentService);
//        }

    }
}




