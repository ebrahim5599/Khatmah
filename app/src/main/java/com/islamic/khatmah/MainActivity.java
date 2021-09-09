package com.islamic.khatmah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayoutMediator;
import com.islamic.khatmah.ui.main.SectionsPagerAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static final String CURRENT_PAGE = "current page";
    public static final String CURRENT_SURAH = "current surah";
    public static final String CURRENT_JUZ = "current juz";
    public static final String PAGES_PER_DAY = "pages per day";
    public static final String DATA_EXIST = "data exist";
    public static boolean fileNotFound = false;
    public static ArrayList<Bitmap> bitmaps;
    public static ArrayList<String> pages;


    String prevStarted = "yes";
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
            moveToSecondary();
        }

        Log.i("Life", "onResume()");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show Start activity
            startActivity(new Intent(MainActivity.this, StartActivity.class));

        }*/

        Log.i("Life", "onCreate()");
        // sharedPreference.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

            setContentView(R.layout.activity_main);
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ViewPager2 viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            new TabLayoutMediator(tabs, viewPager,
                    (tab, position) -> tab.setText(setTextOfTheTab(position))
            ).attach();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    preparingQuranPages();
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
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getBaseContext(), StartActivity.class));
            return true;
        } else if (itemId == R.id.item_about_us) {

            Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
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

    public void moveToSecondary(){
        // use an intent to travel from one activity to another.
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }

    private void preparingQuranPages(){

        InputStream is;
        try {
            is = getBaseContext().openFileInput(String.valueOf(604));
        } catch (FileNotFoundException e) {
            fileNotFound = true;
        }
    }
}
