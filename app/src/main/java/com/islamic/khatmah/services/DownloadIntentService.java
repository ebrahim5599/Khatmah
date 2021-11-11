package com.islamic.khatmah.services;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

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

        ShowProgress();
        int start = 605;
        for (int i = 1; i < 604; i++) {
            try {
                is = openFileInput(String.valueOf(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                start = i / 4;
                break;
            }
        }

        for (int i = (start - 1); i < 152; i++) {
            new ThreadDownload(i + 151).start();
            new ThreadDownload(i + 302).start();
            new ThreadDownload(i + 453).start();


//            for (int i = (start - 1); i < 605; i++) {
            try {
                url = new URL("https://quran-images-api.herokuapp.com/show/page/" + i);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                FileOutputStream os = openFileOutput(String.valueOf(i), MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                publishProgress((int) (((i) / (float) 152) * 100));
//                publishProgress(i*2);

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
            mProgressDialog = new ProgressDialog(mContext);
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
                    dismissProgress();
                }
            });
            mProgressDialog.show();
//            }

        });
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
                Toast.makeText(DownloadIntentService.this, "Download runs in background", Toast.LENGTH_SHORT).show();
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