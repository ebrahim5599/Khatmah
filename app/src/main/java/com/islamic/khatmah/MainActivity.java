package com.islamic.khatmah;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayoutMediator;
import com.islamic.khatmah.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if (!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.apply();
            moveToSecondary();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}