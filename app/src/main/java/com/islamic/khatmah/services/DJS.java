package com.islamic.khatmah.services;

import android.app.ProgressDialog;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


// DJS: Download Job Service.

public class DJS extends JobService {

    URL url;
    HttpURLConnection httpURLConnection = null;
    InputStream is;
    Bitmap bitmap;
    static final int JOB_ID = 1000;
    private ProgressDialog mProgressDialog;
    static Context mcontext;

    public DJS() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Toast.makeText(getBaseContext(), "onStartJob", Toast.LENGTH_SHORT).show();

        ShowProgress();
        int start = 605;
        for (int i = 1; i < 604; i++) {
            try {
                is = openFileInput(String.valueOf(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                start = i;
                break;
            }
        }

        for (int i = (start - 1); i < 605; i++) {
            try {
                url = new URL("https://quran-images-api.herokuapp.com/show/page/" + i);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);

                FileOutputStream os = openFileOutput(String.valueOf(i), MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                publishProgress(i);
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
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Toast.makeText(getBaseContext(), "onStopJob", Toast.LENGTH_SHORT).show();
        return false;
    }


    final Handler mHandler = new Handler();
    // Helper for showing tests
    void ShowProgress() {
        mHandler.post(() -> {

//            if (!mProgressDialog.isShowing()){
            mProgressDialog = new ProgressDialog(mcontext);
            mProgressDialog.setTitle("Download the quran images");
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setMax(604);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
//            }

        });
    }

    void dismissProgress() {
        mHandler.post(() -> {
            mProgressDialog.dismiss();
        });
    }

    void publishProgress(int value) {
        mHandler.post(() -> {
            mProgressDialog.setProgress(value);
        });
    }

}