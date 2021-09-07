package com.islamic.khatmah.quran_activity;

import static com.islamic.khatmah.MainActivity.DATA_EXIST;
import static com.islamic.khatmah.MainActivity.bitmaps;
import static com.islamic.khatmah.MainActivity.pages;
import static com.islamic.khatmah.MainActivity.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.islamic.khatmah.MainActivity;
import com.islamic.khatmah.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class QuranActivity extends AppCompatActivity {

    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        Intent n = getIntent();
        j = n.getIntExtra("PAGE_NUMBER",0);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        if(MainActivity.fileNotFound == false){
            viewPager.setAdapter(new QuranPageAdapter(getBaseContext(), viewPager));
        }else{
            String url = "https://quran-images-api.herokuapp.com/show/page/";
            viewPager.setAdapter(new QuranPageAdapter(url, viewPager));
        }

        viewPager.setCurrentItem(j-1, true);
    }

}