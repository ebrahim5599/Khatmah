package com.islamic.khatmah.quran_activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import com.islamic.khatmah.R;

public class QuranActivity extends AppCompatActivity {

    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        Intent n = getIntent();
        j = n.getIntExtra("PAGE_NUMBER",0);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

            viewPager.setAdapter(new QuranPageAdapter(getBaseContext()));


        viewPager.setCurrentItem(j-1, true);
    }

}