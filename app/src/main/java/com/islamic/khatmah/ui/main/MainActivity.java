package com.islamic.khatmah.ui.main;

import static com.islamic.khatmah.constants.Constant.PREV_STARTED;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayoutMediator;
import com.islamic.khatmah.R;
import com.islamic.khatmah.ui.first_start.StartActivity;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.ui.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public static ArrayList<String> surahName;



    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(PREV_STARTED, false)) {
            moveToSecondary();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sharedPreference.
        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        boolean isFirstRun = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show Start activity
            startActivity(new Intent(MainActivity.this, StartActivity.class));

        }

        getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();


            setContentView(R.layout.activity_main);


//            preferences.edit().putInt(Constant.WEEKLY_PROGRESS,0).apply();
//            preferences.edit().putInt(Constant.TOTAL_PROGRESS,0).apply();
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        if (getIntent().getIntExtra("fromDailyPortionActivity", 1) == 0) {
            viewPager.setCurrentItem(1);
        }
        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(setTextOfTheTab(position))
        ).attach();

        new Thread(new Runnable() {
            @Override
            public void run() {
                preparingSurahNames();
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_setting) {

            startActivity(new Intent(getBaseContext(), SettingActivity.class));
            return true;
        } else if (itemId == R.id.item_about_us) {

            startActivity(new Intent(getBaseContext(), StartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //set the tabBar Text
    private String setTextOfTheTab(int position) {
        switch (position) {
            case 2:
                return getString(R.string.your_progress);
            case 1:
                return getString(R.string.daily_portion);
            case 0:
                return getString(R.string.Free_Reading);
            default:
                return "";
        }
    }

    public void moveToSecondary() {
        // use an intent to travel from one activity to another.
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }


    private void preparingSurahNames() {
        surahName = new ArrayList<>();
        JSONObject jsonObject, pageData;
        JSONArray jsonArray;

        try {
            jsonObject = new JSONObject(Objects.requireNonNull(JsonDataFromAsset("page_details.json")));
            jsonArray = jsonObject.getJSONArray("page_details");
            surahName.add(jsonArray.getJSONObject(0).getString("name"));
            for (int k = 1; k < jsonArray.length(); k++) {
                pageData = jsonArray.getJSONObject(k);
                for (int i = pageData.getInt("start"); i <= pageData.getInt("end"); i++)
                    surahName.add(pageData.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String JsonDataFromAsset(String fileName) {
        String json;
        try {
            InputStream inputStream = getBaseContext().getAssets().open(fileName);
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
