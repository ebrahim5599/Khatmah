package com.islamic.khatmah;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Reminder extends BroadcastReceiver {
    int MID=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1= new Intent( context, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notify")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Khatmah Reminder")
                .setContentText("تذكير بمعاد الورد اليومي")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManger= NotificationManagerCompat.from(context);
        notificationManger.notify(MID,builder.build());
         MID++;
    }


    }




