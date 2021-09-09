package com.islamic.khatmah.free_reading;

import static android.content.Context.MODE_PRIVATE;
import static com.islamic.khatmah.MainActivity.DATA_EXIST;
import static com.islamic.khatmah.MainActivity.bitmaps;
import static com.islamic.khatmah.MainActivity.editor;
import static com.islamic.khatmah.MainActivity.fileNotFound;
import static com.islamic.khatmah.MainActivity.pages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.islamic.khatmah.R;
import com.islamic.khatmah.pojo.Surah;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FreeReadingFragment extends Fragment {

    private FreeReadingViewModel mViewModel;
    private SurahAdapter surahAdapter;
    private RecyclerView recyclerView;
    private List<Surah> list;
    ProgressDialog mProgressDialog;

    public static FreeReadingFragment newInstance() {
        return new FreeReadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        mViewModel = new ViewModelProvider(this).get(FreeReadingViewModel.class);
        View view = inflater.inflate(R.layout.free_reading_fragment, container, false);

        if (fileNotFound) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Download")
                    .setMessage("For a better experience, there are some files that must be downloaded in order for the application to work without an Internet connection.")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            downloadAllPics();
                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Cancel", null)
                    .setIcon(R.drawable.ic_baseline_cloud_download_24)
                    .show();
        }

//        int i = 1;
//        ArrayList<SurahClass> surahArrayList = new ArrayList<SurahClass>();
//        surahArrayList.add(new SurahClass(ArbNum(i++),"الفاتحة","مكية",ArbNum(7)+" آيات"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"البقرة","مدنية",ArbNum(286)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"آل عمران","مدنية",ArbNum(200)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"النساء","مدنية",ArbNum(176)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"المائدة","مدنية",ArbNum(120)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"الأنعام","مكية",ArbNum(165)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"الأعراف","مكية",ArbNum(206)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"الأنفال","مدنية",ArbNum(75)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"التوبة","مدنية",ArbNum(129)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"يونس","مكية",ArbNum(109)+" آية"));
//        surahArrayList.add(new SurahClass(ArbNum(i++),"هود","مكية",ArbNum(123)+" آية"));
//
//
//        // Find a reference to the {@link ListView} in the layout
//        ListView listView = view.findViewById(R.id.listView);
//        // Create a new adapter that takes an empty list of surah as input
//        surahAdapter = new SurahAdapter(getContext(),surahArrayList);
//        // Set the adapter on the {@link ListView}
//        // so the list can be populated in the user interface
//        listView.setAdapter(surahAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Toast.makeText(getContext(), surahArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
//                Intent quranActivityIntent = new Intent(getContext(), QuranActivity.class);
//                quranActivityIntent.putExtra("SURAH_NAME", surahArrayList.get(i).getName());
//                startActivity(quranActivityIntent);
//            }
//        });

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

//    private String ArbNum(int number) {
//
//        String stNum = String.valueOf(number);
//        String result ="";
//
//        for (int i = 0; i < stNum.length(); i++) {
//            char num = String.valueOf(stNum).charAt(i);
//            int ArabicNum = num + 1584;
//            result += (char) ArabicNum;
//        }
//        return result;
//    }

    private void downloadAllPics() {
        new DownloadAllPics().execute("https://quran-images-api.herokuapp.com/download/page/");
    }

    private class DownloadAllPics extends AsyncTask<String, Integer, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... urls) {

            URL url;
            HttpURLConnection httpURLConnection = null;
            InputStream is;
            Bitmap bitmap;

            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            for (int i = 1; i < 605; i++) {
                try {
                    url = new URL(urls[0] + i);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    is = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                    FileOutputStream os = getContext().openFileOutput(String.valueOf(i), MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                    publishProgress((int) (((i) / (float) 604) * 100));

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

            return bitmaps;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setTitle("Download the quran images");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            mProgressDialog.dismiss();
            fileNotFound = false;
        }
    }
}
