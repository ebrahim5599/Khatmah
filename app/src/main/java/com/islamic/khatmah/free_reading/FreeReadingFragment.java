package com.islamic.khatmah.free_reading;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.PersistableBundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islamic.khatmah.services.DownloadIntentService;
import com.islamic.khatmah.R;
import com.islamic.khatmah.pojo.Surah;

import com.islamic.khatmah.services.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FreeReadingFragment extends Fragment {

    private FreeReadingViewModel mViewModel;
    private SurahAdapter surahAdapter;
    private RecyclerView recyclerView;
    private List<Surah> list;
    ProgressDialog mProgressDialog;
    private final int JOB_SERVICE_ID = 1000;

    public static FreeReadingFragment newInstance() {
        return new FreeReadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        mViewModel = new ViewModelProvider(this).get(FreeReadingViewModel.class);
        View view = inflater.inflate(R.layout.free_reading_fragment, container, false);

        InputStream is = null;
        try {
            is = getContext().openFileInput("" + 604);
            Bitmap bit = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            Intent intent = new Intent(getContext(), DownloadIntentService.class);
            new MaterialAlertDialogBuilder(getContext(), R.style.Theme_MyApp_Dialog_Alert)
                    .setTitle(R.string.download)
                    .setMessage(R.string.download_message)
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        // download quran images.
//                        downloadQuranImages("pages");
                            DownloadIntentService.enqueueWork(getActivity(), intent);
//                            DownloadService.getContext(getActivity());
//                            ComponentName componentName = new ComponentName(getContext(), DownloadService.class);
//
//                            JobInfo jobInfo = new JobInfo.Builder(0, componentName)
//                                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                                    .build();
//                            JobScheduler jobScheduler;
//                            jobScheduler = (JobScheduler) requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                            jobScheduler.schedule(jobInfo);
                        }

                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.cancel, null)
                    .setIcon(R.drawable.ic_baseline_cloud_download_24)
                    .show();
        }
//        if (fileNotFound) {
//            Intent intent = new Intent(getContext(), DownloadIntentService.class);
//            new AlertDialog.Builder(getContext())
//                    .setTitle("Download")
//                    .setMessage("For a better experience, there are some files that must be downloaded in order for the application to work without an Internet connection.")
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Continue with delete operation
////                            downloadAllPics();
//                            DownloadIntentService.enqueueWork(getActivity(),intent);
//                        }
//                    })
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton("Cancel", null)
//                    .setIcon(R.drawable.ic_baseline_cloud_download_24)
//                    .show();
//        }

        recyclerView = view.findViewById(R.id.surahRV);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

        list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JsonDataFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject surahData = jsonArray.getJSONObject(i);
                list.add(new Surah(surahData.getInt("number"),
                        surahData.getString("name"),
                        surahData.getString("englishName"),
                        surahData.getString("englishNameTranslation"),
                        surahData.getInt("numberOfAyahs"),
                        surahData.getString("revelationType"),
                        surahData.getInt("start")
                ));
            }
            if (list.size() != 0) {
                surahAdapter = new SurahAdapter(getContext(), list);
                recyclerView.setAdapter(surahAdapter);
                surahAdapter.notifyDataSetChanged();

            }

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return view;
    }

    private String JsonDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getActivity().getAssets().open("surah.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

//    private void downloadQuranImages(String fileName){
//        String i = "1";
//        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri downloadUri = Uri.parse("https://quran-images-api.herokuapp.com/show/page/1");
//        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
//                | DownloadManager.Request.NETWORK_MOBILE)
//                .setTitle(fileName)
//                .setDescription("Downloading "+fileName)
//                .setAllowedOverMetered(true)
//                .setAllowedOverRoaming(true)
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                .setDestinationInExternalFilesDir(getContext(),"pages", File.separator+i+".png");
//        downloadManager.enqueue(request);
//    }

//    private void downloadAllPics() {
//        for(int i = 1; i < 605; i+=4){
//            new ThreadDownload(i);
//            new ThreadDownload(i+1);
//            new ThreadDownload(i+2);
//            new ThreadDownload(i+4);
//        }
//        new DownloadAllPics().execute("https://quran-images-api.herokuapp.com/download/page/");
//    }


//    private class ThreadDownload extends Thread {
//        private int picNum;
//
//        //The constructor receives three parameters to initialize local variables: thread ID, start position and end position
//        public ThreadDownload(int picNum) {
//            this.picNum = picNum;
//        }
//
//        @Override
//        public void run() {
//            try {
//                URL url = new URL("https://quran-images-api.herokuapp.com/show/page/" + picNum);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setConnectTimeout(5000);
//                conn.setReadTimeout(5000);
////                System.out.println("thread"+threadId+"Download start location"+startIndex+" End position:"+endIndex);
//
//                //Set request header and request some resources to download
////                conn.setRequestProperty("Range", "bytes:"+startIndex+"-"+endIndex);
//                //Server response code 206 indicates that some resources are successfully requested
//                if (conn.getResponseCode() == 206) {
//                    InputStream inputStream = conn.getInputStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    //Find write start position
//
//
//                    FileOutputStream os = getContext().openFileOutput(String.valueOf(picNum), MODE_PRIVATE);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//                    inputStream.close();
//                    os.flush();
//                    os.close();
//                    conn.disconnect();
//                }
////                else{
//////                    System.out.println("Server response code:"+conn.getResponseCode());
////                }
//            } catch (IOException e) {
//                //
//                e.printStackTrace();
//            }
//        }
//    }

//    private class DownloadAllPics extends AsyncTask<String, Integer, ArrayList<Bitmap>> {
//
//        @Override
//        protected ArrayList<Bitmap> doInBackground(String... urls) {
//
//            URL url;
//            HttpURLConnection httpURLConnection = null;
//            InputStream is;
//            Bitmap bitmap;
//            for (int i = 1; i < 605; i++) {
//                try {
//                    url = new URL(urls[0] + i);
//                    httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.connect();
//                    is = httpURLConnection.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//                    FileOutputStream os = getContext().openFileOutput(String.valueOf(i), MODE_PRIVATE);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//
//                    publishProgress((int) (((i) / (float) 604) * 100));
//
//                    os.flush();
//                    os.close();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    Log.i("Catch", e.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    is = null;
//                    httpURLConnection.disconnect();
//                }
//            }
////            for (int i = 1; i < 605; i++) {
////                FileOutputStream os = null;
////                try {
////                    os = getContext().openFileOutput(String.valueOf(i), MODE_PRIVATE);
////                    bitmaps.get(i).compress(Bitmap.CompressFormat.PNG, 100, os);
////                    os.flush();
////                    os.close();
////                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////
////                    mProgressDialog.setProgress((int) (((i) / (float) 604) * 100));
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            mProgressDialog = new ProgressDialog(getContext());
//            mProgressDialog.setTitle("Download the quran images");
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            mProgressDialog.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
//            mProgressDialog.dismiss();
//            fileNotFound = false;
//        }
//    }
}
