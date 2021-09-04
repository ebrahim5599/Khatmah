package com.islamic.khatmah.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.islamic.khatmah.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StartActivityTwo extends AppCompatActivity {

    Button btnSearch;
    ArrayList<String> aaa;
    Spinner spinnerJuz,spinnerPage,spinnerSurah;
    ArrayList<String> juz,surah,page;
    //    ArrayList<Integer> page;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TextView schedule,schedule2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        schedule = findViewById(R.id.schedule);
        schedule2 = findViewById(R.id.schedule2);
        spinnerJuz = findViewById(R.id.spinnerJuz);
        spinnerPage = findViewById(R.id.spinnerPage);
        spinnerSurah = findViewById(R.id.spinnerSurah);
        ////
        aaa = new ArrayList<>();
        aaa.add("111");
        aaa.add("222");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aaa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapter);
        spinnerSurah.setAdapter(adapter);
        ////
        juz = new ArrayList<>();

        try {

            jsonObject = new JSONObject(JsonDataFromAsset());
            jsonArray = jsonObject.getJSONArray("data");
//            Log.i("ffffff",String.valueOf( 11111));
//            Log.i("ffffff",String.valueOf( jsonArray.length()));
            for (int j =0;j<9;j++){
                JSONObject surahData = jsonArray.getJSONObject(j);
                juz.add(surahData.getString("juz"));
            }
            if (juz.size()!=0){
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, juz);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerJuz.setAdapter(adapter1);
                spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                        surah = new ArrayList<>();
                        for (int s =0;s<juz.size();s++){
                            JSONObject surahData = null;
                            try {
                                surahData = jsonArray.getJSONObject(s);
                                if (spinnerJuz.getSelectedItem() == surahData.getString("juz")){
                                    surah.add(surahData.getString("name"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (surah.size()!=0){
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, surah);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerSurah.setAdapter(adapter2);
                            spinnerSurah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    page = new ArrayList<>();
                                    for (int p =0;p<juz.size();p++){
                                        JSONObject surahData = null;
                                        try {
                                            surahData = jsonArray.getJSONObject(p);
                                            if (spinnerSurah.getSelectedItem() == surahData.getString("name")){
                                                page.add(String.valueOf(surahData.getInt("start")));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (page.size()!=0) {
                                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, page);
                                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerPage.setAdapter(adapter3);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Log.i("fffff",String.valueOf( jsonException));
        }
        ////
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule.setText(spinnerPage.getSelectedItem().toString());
            }
        });


    }
    private String JsonDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("surah.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
