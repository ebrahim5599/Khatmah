package com.islamic.khatmah.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.islamic.khatmah.AlarmBrodCasts.NotificationReceiver;
import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.ui.main.MainActivity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadJobService extends JobService {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_SHORT).show();

        // load shared Preferences
        preferences = getBaseContext().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(Constant.STOP_DOWNLOAD, false).apply();
        editor.putBoolean(Constant.DOWNLOAD_IS_RUNNING, true).apply();

        // Notification intent & pendingIntent.
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Action button intent & pendingIntent.
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // This for displaying DownloadNotification.
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DOWNLOAD_NOTIFICATION_CHANNEL_ID");
        builder.setContentTitle("Download Quran Pages")
                .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true)
                .setContentIntent(notificationPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Cancel", actionIntent)
                .setColor(Color.YELLOW);


        // Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(2, builder.build());
//        startForeground(2, builder.build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection httpURLConnection = null;
                InputStream is;
                Bitmap bitmap;
                for (int t1 = 1; t1 < 152; t1++) {
                    try {
                        url = new URL("https://quran-images-api.herokuapp.com/show/page/" + t1);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.connect();
                        is = httpURLConnection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        FileOutputStream os = openFileOutput(String.valueOf(t1), MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                        int PROGRESS_CURRENT = (int) (((t1) / (float) 152) * 100);

                        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                        builder.setContentText(PROGRESS_CURRENT + "%");
                        notificationManager.notify(2, builder.build());

                        os.flush();
                        os.close();

                        if (preferences.getBoolean(Constant.STOP_DOWNLOAD, false)) {
                            httpURLConnection.disconnect();
                            jobFinished(jobParameters, false);
                            builder.setContentText("Download canceled")
                                    .setProgress(0, 0, false);
                            notificationManager.notify(2, builder.build());
//                            notificationManager.cancel(2);
                            break;
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        is = null;
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
        downloadInThread(152);
        downloadInThread(303);
        downloadInThread(454);

        if (!preferences.getBoolean(Constant.STOP_DOWNLOAD, false)) {
            builder.setContentText("Download complete")
                    .setProgress(0, 0, false);

        }
        editor.putBoolean(Constant.DOWNLOAD_IS_RUNNING, false).apply();
        notificationManager.notify(2, builder.build());
        notificationManager.cancel(2);
        // to call onStopJob();
        jobFinished(jobParameters, false);
        return true; // if process takes long time make it true.
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
        editor.putBoolean(Constant.DOWNLOAD_IS_RUNNING, false).apply();
        return false; // if you want this process to restart make it true.
    }

    void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "DOWNLOAD_NOTIFICATION_CHANNEL_ID",
                    "Download Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void downloadInThread(int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection httpURLConnection = null;
                InputStream is;
                Bitmap bitmap;
                for (int i = index; i < (index + 151); i++) {
                    try {
                        url = new URL("https://quran-images-api.herokuapp.com/show/page/" + i);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.connect();
                        is = httpURLConnection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        FileOutputStream os = openFileOutput(String.valueOf(i), MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                        os.flush();
                        os.close();

                        if (preferences.getBoolean(Constant.STOP_DOWNLOAD, false)) {
                            break;
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        is = null;
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
}