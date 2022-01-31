package com.islamic.khatmah.free_reading;


//import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.R;
import com.islamic.khatmah.pojo.Surah;
import com.islamic.khatmah.services.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FreeReadingFragment extends Fragment {


//    ProgressDialog mProgressDialog;
//    private final int JOB_SERVICE_ID = 1000;

    public static FreeReadingFragment newInstance() {
        return new FreeReadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        mViewModel = new ViewModelProvider(this).get(FreeReadingViewModel.class);
        View view = inflater.inflate(R.layout.free_reading_fragment, container, false);
        SharedPreferences preferences = requireActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        InputStream is ;
        try {
            is = requireContext().openFileInput("" + 604);
            Bitmap bit = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {

            if (!preferences.getBoolean(Constant.DOWNLOAD_IS_RUNNING, false)) {
//                Intent intent = new Intent(getContext(), DownloadIntentService.class);
                Intent downloadIntent = new Intent(getContext(), DownloadService.class);
                new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
                        .setTitle(R.string.download)
                        .setMessage(R.string.download_message)
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ConnectivityManager cm = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                }
                                assert cm != null;
                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                                boolean isMetered = cm.isActiveNetworkMetered();
                                if (isConnected && !isMetered) {

//                                    DownloadIntentService.enqueueWork(getActivity(), intent);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        requireContext().startForegroundService(downloadIntent);
                                    else
                                        ContextCompat.startForegroundService(requireContext(), downloadIntent);

                                } else {
                                    if (!isConnected)
                                        Toast.makeText(requireContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                                }

//                            ComponentName componentName = new ComponentName(getContext(), DownloadJobService.class);
//                            JobInfo jobInfo = new JobInfo.Builder(10, componentName)
//                                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                                    .build();
//
////                          if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N){ }else{ }
//                            JobScheduler jobScheduler = (JobScheduler) requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                            jobScheduler.schedule(jobInfo);


                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(R.string.cancel, null)
                        .setIcon(R.drawable.ic_baseline_cloud_download_24)
                        .show();

            }
        }


        RecyclerView recyclerView = view.findViewById(R.id.surahRV);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

        List<Surah> list = new ArrayList<>();
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
                SurahAdapter surahAdapter = new SurahAdapter(getContext(), list);
                recyclerView.setAdapter(surahAdapter);
                surahAdapter.notifyDataSetChanged();

            }

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return view;
    }

    private String JsonDataFromAsset() {
        String json;
        try {
            InputStream inputStream = requireActivity().getAssets().open("surah.json");
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
}
