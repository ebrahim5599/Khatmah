package com.islamic.khatmah.quran_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.islamic.khatmah.R;

public class QuranActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);

        TextView textView = findViewById(R.id.surah_name);
        Intent n = getIntent();
        textView.setText(n.getStringExtra("SURAH_NAME"));

    }
}