package com.islamic.khatmah;

import static com.islamic.khatmah.MainActivity.CURRENT_JUZ;
import static com.islamic.khatmah.MainActivity.CURRENT_PAGE;
import static com.islamic.khatmah.MainActivity.CURRENT_SURAH;
import static com.islamic.khatmah.MainActivity.editor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import android.widget.Toast;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {
    Button btn_StartFromPoint, btn_next, btn_StartFromBegin;
    LinearLayout l1, l2;
    Spinner spinnerJuz, spinnerPage, spinnerSurah;
    ArrayList<String> aaa;

    ArrayList<String> juz,surah,page,juzTemp,pageTemp;

    JSONObject jsonObject;
    JSONArray jsonArray;
    int juzNum = 0,pageNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        btn_next = findViewById(R.id.button2);
        btn_StartFromPoint = findViewById(R.id.button1);
        spinnerJuz = findViewById(R.id.الجزء);
        spinnerPage = findViewById(R.id.الصفحة);
        spinnerSurah = findViewById(R.id.السورة);


        btn_StartFromPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (l1.getVisibility() == View.GONE && l2.getVisibility() == View.GONE && btn_next.getVisibility() == View.GONE) {
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.VISIBLE);
                } else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                }
            }
        });


        btn_StartFromBegin = findViewById(R.id.button);
        btn_StartFromBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, AlertActivity.class));

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, AlertActivity.class));
                editor.putInt(CURRENT_PAGE, Integer.parseInt(spinnerPage.getSelectedItem().toString()));
                editor.putString(CURRENT_SURAH, spinnerSurah.getSelectedItem().toString());
                editor.putString(CURRENT_JUZ, spinnerJuz.getSelectedItem().toString());
                editor.commit();
            }
        });

        aaa = new ArrayList<>();
        aaa.add("111");
        aaa.add("222");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aaa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapter);
        spinnerSurah.setAdapter(adapter);
        ////
        juz = new ArrayList<>();
        juzTemp = new ArrayList<>();
        surah = new ArrayList<>();
        page = new ArrayList<>();
        pageTemp = new ArrayList<>();
        try {
            jsonObject = new JSONObject(JsonDataFromAsset());
            jsonArray = jsonObject.getJSONArray("data");
//            Log.i("ffffff",String.valueOf( 11111));
//            Log.i("ffffff",String.valueOf( jsonArray.length()));

            for (int j =0;j<jsonArray.length();j++){
                Log.i("ff12fff",String.valueOf(jsonArray.length()));

                JSONObject surahData = jsonArray.getJSONObject(j);
                if (juzNum != surahData.getInt("juz")){
                    juz.add(String.valueOf(surahData.getInt("juz")));
                }
                if (pageNum != surahData.getInt("start")){
                    page.add(String.valueOf(surahData.getInt("start")));
                }
                juzTemp.add(surahData.getString("juz"));
                surah.add(surahData.getString("name"));
                pageTemp.add(surahData.getString("start"));
                juzNum = surahData.getInt("juz");
                pageNum = surahData.getInt("start");
            }

            if (juz.size()!=0 && surah.size()!=0 && page.size()!=0){
                Log.i("ff12fff",String.valueOf(juz.size()));
                //adapters

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, juz);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerJuz.setAdapter(adapter1);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, surah);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSurah.setAdapter(adapter2);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, page);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPage.setAdapter(adapter3);
                //clicks
                spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                        for (int s =0;s<jsonArray.length();s++){
                            JSONObject surahData = null;
                            try {
                                surahData = jsonArray.getJSONObject(s);
                                if (spinnerJuz.getSelectedItem() == surahData.getString("juz")){
                                    Log.i("llll",String.valueOf(s));
                                    spinnerSurah.setSelection(s);
                                    //spinnerPage.setSelection(s);
                                    for (int i = 0 ;i< juz.size();i++){
                                        if (pageTemp.get(s) == page.get(i)){
                                            spinnerPage.setSelection(i);
                                        }
                                    }
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//                        if (surah.size()!=0){
//                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, surah);
//                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinnerSurah.setAdapter(adapter2);
//                            spinnerSurah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    for (int p =0;p<juz.size();p++){
//                                        JSONObject surahData = null;
//                                        try {
//                                            surahData = jsonArray.getJSONObject(p);
//                                            if (spinnerSurah.getSelectedItem() == surahData.getString("name")){
//                                                page.add(String.valueOf(surahData.getInt("start")));
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                    if (page.size()!=0) {
//                                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, page);
//                                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                        spinnerPage.setAdapter(adapter3);
//                                    }
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });
//                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerSurah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                        for (int s =0;s<jsonArray.length();s++){
                            JSONObject surahData = null;
                            try {
                                surahData = jsonArray.getJSONObject(s);
                                if (spinnerSurah.getSelectedItem() == surahData.getString("name")){
                                    //spinnerJuz.setSelection(s);
                                    //spinnerPage.setSelection(s);
                                    for (int i = 0 ;i< juz.size();i++){
                                        if (juzTemp.get(s) == juz.get(i)){
                                            spinnerJuz.setSelection(i);
                                        }
                                    }

                                    for (int i = 0 ;i< juz.size();i++){
                                        if (pageTemp.get(s) == page.get(i)){
                                            spinnerPage.setSelection(i);
                                        }

//                                    if (page.size() != 0) {
//                                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, page);
//                                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                        spinnerPage.setAdapter(adapter3);

                                    }
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                        for (int s =0;s<jsonArray.length();s++){
                            JSONObject surahData = null;
                            try {
                                surahData = jsonArray.getJSONObject(s);
                                if (Integer.parseInt(spinnerPage.getSelectedItem().toString())== surahData.getInt("start")){
                                    Log.i("kk",surahData.getString("start"));
                                    //spinnerJuz.setSelection(s);
                                    spinnerSurah.setSelection(s);
                                    for (int i = 0 ;i< juz.size();i++){
                                        if (juzTemp.get(s) == juz.get(i)){
                                            spinnerJuz.setSelection(i);
                                        }
                                    }
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();

            Log.i("ff1fff",String.valueOf( jsonException));

        }
        /*
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule.setText(spinnerPage.getSelectedItem().toString());
            }
        });*/


    }

    private String JsonDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("surah.json");
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