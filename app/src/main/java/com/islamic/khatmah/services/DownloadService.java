package com.islamic.khatmah.services;

import android.app.ProgressDialog;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.islamic.khatmah.ui.main.MainActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService extends JobService {

    static Context mContext;
    private ProgressDialog mProgressDialog;

    public static void getContext(Context context) {
        mContext = context;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
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
                mProgressDialog.dismiss();
                jobFinished(jobParameters,false);
            }
        });

        mProgressDialog.show();

//        int start = 605;
//        for (int i = 1; i < 604; i++) {
//            try {
//                is = openFileInput(String.valueOf(i));
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                start = i / 4;
//                break;
//            }
//        }
        for (int i = 1; i < 152; i++) {
            new ThreadDownload(i).start();
            new ThreadDownload(i + 151).start();
            new ThreadDownload(i + 302).start();
            new ThreadDownload(i + 453).start();

            mProgressDialog.setProgress((int) (((i) / (float) 152) * 100));
//            try {
//                url = new URL("https://quran-images-api.herokuapp.com/show/page/" + i);
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.connect();
//                is = httpURLConnection.getInputStream();
//                bitmap = BitmapFactory.decodeStream(is);
//                FileOutputStream os = openFileOutput(String.valueOf(i), MODE_PRIVATE);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//                mProgressDialog.setProgress((int) (((i) / (float) 152) * 100));
//                os.flush();
//                os.close();
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                Log.i("Catch", e.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                is = null;
//                httpURLConnection.disconnect();
//            }
        }
        jobFinished(jobParameters, false);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public class ThreadDownload extends Thread {
        private final int index;
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