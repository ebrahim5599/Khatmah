package com.islamic.khatmah.services;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.islamic.khatmah.R;

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

//        ShowProgress();
////////////////////////////////////
        // This for displaying DownloadNotification.
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DOWNLOAD_NOTIFICATION_CHANNEL_ID");
        builder.setContentTitle("Download Quran Pages")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true);

        // Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(2, builder.build());
////////////////////////////////////

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
//                publishProgress(current_progress);

                builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                notificationManager.notify(2, builder.build());

                os.flush();
                os.close();
                if (stop) {
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
        builder.setContentText("Download complete")
                .setProgress(0, 0, false);
        notificationManager.notify(2, builder.build());
    }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    void StartDownloadNotification() {

        // This for displaying DownloadNotification.
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DOWNLOAD_NOTIFICATION");
        builder.setContentTitle("Download Quran Pages")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        // Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(2, builder.build());

// Do the job here that tracks the progress.
// Usually, this should be in a
// worker thread
// To show progress, update PROGRESS_CURRENT and update the notification with:
// builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
// notificationManager.notify(notificationId, builder.build());

// When done, update the notification one more time to remove the progress bar
        builder.setContentText("Download complete")
                .setProgress(0, 0, false);
        notificationManager.notify(2, builder.build());
    }

    void UpdateNotification(){

    }

    void FinishDownloadNotification(){

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