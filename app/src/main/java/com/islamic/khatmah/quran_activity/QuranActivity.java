package com.islamic.khatmah.quran_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.islamic.khatmah.MainActivity;
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

        Intent n = getIntent();
        j = n.getIntExtra("PAGE_NUMBER", 0);
        click_position = n.getIntExtra("POSITION", 0);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new QuranPageAdapter(getBaseContext(), MainActivity.surahName));

        viewPager.setCurrentItem(j - 1, true);
    }

    // this method to fetch surah name from JSON file.
    private String JsonDataFromAsset(String fileName) {
        String json = null;
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