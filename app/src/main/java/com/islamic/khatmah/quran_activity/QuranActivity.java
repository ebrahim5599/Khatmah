package com.islamic.khatmah.quran_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import com.islamic.khatmah.ui.main.MainActivity;
import com.islamic.khatmah.R;
import java.io.IOException;
import java.io.InputStream;

public class QuranActivity extends AppCompatActivity {

    int j;
    int click_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent n = getIntent();
        j = n.getIntExtra("PAGE_NUMBER", 0);
        click_position = n.getIntExtra("POSITION", 0);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new QuranPageAdapter(getBaseContext(), MainActivity.surahName));

        viewPager.setCurrentItem(j - 1, true);
    }

}