package com.islamic.khatmah.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.islamic.khatmah.AlarmBrodCasts.NotificationReceiver;
import com.islamic.khatmah.R;
import com.islamic.khatmah.pojo.Constant;
import com.islamic.khatmah.ui.main.MainActivity;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadIntentService extends JobIntentService {

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;
    private ProgressDialog mProgressDialog;
    static Context mContext;
    boolean stop;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DownloadIntentService.class, JOB_ID, work);
        mContext = context;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        URL url;
        HttpURLConnection httpURLConnection = null;
        InputStream is;
        Bitmap bitmap;
        stop = false;

        // load shared Preferences
        preferences = getBaseContext().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(Constant.STOP_DOWNLOAD, false).apply();
        editor.putBoolean(Constant.DOWNLOAD_IS_RUNNING, true).apply();

        ShowProgress();

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
                .addAction(R.mipmap.ic_launcher,"Cancel", actionIntent)
                .setColor(Color.YELLOW);

        // Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(2, builder.build());

        int start = 605;
        for (int i = 1; i < 152; i++) {
            try {
                is = openFileInput(String.valueOf(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                start = i;
                break;
            }
        }

        for (int i = (start - 1); i < 152; i++) {
            new ThreadDownload(i + 151).start();
            new ThreadDownload(i + 302).start();
            new ThreadDownload(i + 453).start();

            try {
                url = new URL("https://quran-images-api.herokuapp.com/show/page/" + i);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                FileOutputStream os = openFileOutput(String.valueOf(i), MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                PROGRESS_CURRENT = (int) (((i) / (float) 152) * 100);
                publishProgress(PROGRESS_CURRENT);

                builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                builder.setContentText(PROGRESS_CURRENT+"%");
                notificationManager.notify(2, builder.build());

                os.flush();
                os.close();
                if (preferences.getBoolean(Constant.STOP_DOWNLOAD, false)) {
                    break;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("Catch", e.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is = null;
                httpURLConnection.disconnect();
            }
        }

        if (preferences.getBoolean(Constant.STOP_DOWNLOAD, false)) {
            builder.setContentText("Download canceled")
                    .setProgress(0, 0, false);
        }else{
            builder.setContentText("Download complete")
                    .setProgress(0, 0, false);
        }
        editor.putBoolean(Constant.DOWNLOAD_IS_RUNNING, false).apply();
        notificationManager.notify(2, builder.build());
    }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.putBoolean(Constant.DOWNLOAD_IS_RUNNING, false).apply();
        dismissProgress();
    }


    final Handler mHandler = new Handler();

    // Helper for showing tests
    void ShowProgress() {
        mHandler.post(() -> {
//            if (!mProgressDialog.isShowing()){
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_DARK);
            mProgressDialog.setTitle("Download the quran images");
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setIndeterminate(false);
            // Whether progress dialog can be canceled or not.
            mProgressDialog.setCancelable(true);
            // When user touch area outside progress dialog whether the progress dialog will be canceled or not.
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    stop = true;
                    editor.putBoolean(Constant.STOP_DOWNLOAD, true).apply();
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Hide", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mProgressDialog.dismiss();
                }
            });
            mProgressDialog.show();
//            }
        });
    }

    void createNotificationChannel(){
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

    void dismissProgress() {
        mHandler.post(() -> {
            mProgressDialog.dismiss();
            InputStream is = null;
            try {
                is = getBaseContext().openFileInput("" + 604);
                Bitmap bit = BitmapFactory.decodeStream(is);
                Toast.makeText(DownloadIntentService.this, "Download finished", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(DownloadIntentService.this, "Download stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void publishProgress(int value) {
        mHandler.post(() -> {
            mProgressDialog.setProgress(value);
//                Toast.makeText(MyIntentService.this, text, Toast.LENGTH_SHORT).show();
        });
    }

    public class ThreadDownload extends Thread {
        private int index;
        URL url;
        HttpURLConnection httpURLConnection;
        InputStream is;

        public ThreadDownload(int startIndex) {
            this.index = startIndex;
        }

        @Override
        public void run() {
            super.run();
            try {
                url = new URL("https://quran-images-api.herokuapp.com/show/page/" + index);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                FileOutputStream os = openFileOutput(String.valueOf(index), MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("Catch", e.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is = null;
                httpURLConnection.disconnect();
            }
        }
    }
}