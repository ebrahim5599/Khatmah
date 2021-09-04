package com.islamic.khatmah.quran_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.islamic.khatmah.R;

import java.util.ArrayList;

public class QuranActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        Intent n = getIntent();
        int j = n.getIntExtra("PAGE_NUMBER",0);

        ArrayList<String> pages = new ArrayList<>();
        for (int i = 1; i < 605; i++)
            pages.add("https://quran-images-api.herokuapp.com/show/page/"+i);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new QuranPageAdapter(pages, viewPager));
        viewPager.setCurrentItem(j-1, true);
    }

}