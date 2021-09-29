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

import com.islamic.khatmah.MainActivity;
import com.islamic.khatmah.R;

public class Reminder extends BroadcastReceiver {
    int MID = 0;
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notify")
//                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
//                .setContentTitle("Khatmah Reminder")
//                .setContentText("تذكير بمعاد الورد اليومي")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent);
//        NotificationManagerCompat notificationManger= NotificationManagerCompat.from(context);
//        notificationManger.notify(MID,builder.build());
//         MID++;

        Log.d("Remind", "Reminder");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CHANNEL DISPLAY NAME", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Khatmah Reminder");
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }


        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Khatmah Reminder")
                .setContentText("تذكير بمعاد الورد اليومي")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true);

        Notification incomingCallNotification = notification.build();


        NotificationManagerCompat notificationManger = NotificationManagerCompat.from(context);
        notificationManger.notify(0, incomingCallNotification);
    }


}




