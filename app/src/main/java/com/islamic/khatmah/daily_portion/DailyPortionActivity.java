package com.islamic.khatmah.daily_portion;

import static com.islamic.khatmah.MainActivity.CURRENT_PAGE;
import static com.islamic.khatmah.MainActivity.editor;
import static com.islamic.khatmah.MainActivity.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.islamic.khatmah.R;
import com.islamic.khatmah.quran_activity.QuranPageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DailyPortionActivity extends AppCompatActivity{

    private int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_portion);

        Intent n = getIntent();
        j = n.getIntExtra("PAGE_NUMBER",0);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.animate().translationX(500f);
        ImageView img = findViewById(R.id.img);
        Picasso.get().load("https://quran-images-api.herokuapp.com/show/page/"+j).into(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(floatingActionButton.getAlpha() == 1f){
                    floatingActionButton.animate().alpha(0f).translationXBy(500f).setDuration(500);
                }else{
                    Snackbar.make(floatingActionButton, R.string.portion_completed, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplicationContext(), "أحسنت", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    floatingActionButton.animate().alpha(1f).translationXBy(-500f).setDuration(500);
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
                floatingActionButton.animate().alpha(0f).translationXBy(500f).setDuration(500);
                j++;
                Picasso.get().load("https://quran-images-api.herokuapp.com/show/page/"+j).into(img);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
//        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        editor.putInt(CURRENT_PAGE, j);
        editor.apply();

    }

}