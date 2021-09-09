//package com.islamic.khatmah.daily_portion;
//
//import static com.islamic.khatmah.MainActivity.CURRENT_PAGE;
//import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;
//import static com.islamic.khatmah.MainActivity.editor;
//import static com.islamic.khatmah.MainActivity.sharedPreferences;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager2.widget.ViewPager2;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//import com.islamic.khatmah.R;
//import com.islamic.khatmah.quran_activity.QuranPageAdapter;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//public class DailyPortionActivity extends AppCompatActivity{
//
//    public static int j;
//    public static int pos = 1;
//    int number_of_pages;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_daily_portion);
//
//        Intent n = getIntent();
//        j = n.getIntExtra("PAGE_NUMBER",0);
//
//        ViewPager2 viewPager = findViewById(R.id.viewPager2);
//
//        viewPager.setAdapter(new DailyPortionAdapter(getBaseContext(), j, viewPager));
//        number_of_pages = sharedPreferences.getInt(PAGES_PER_DAY,1);
//
//        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewPager.setCurrentItem(pos++);
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Confirmation Message")
//                .setMessage("Have you finished reading the entire daily portion?")
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Save the last page, Surah and Juz in SharedPreference.
//                        // [CURRENT_PAGE + number of PAGES_PER_DAY].
//                        int cp = sharedPreferences.getInt(CURRENT_PAGE,1);
//                        editor.putInt(CURRENT_PAGE, number_of_pages+cp);
//                        editor.commit();
//                        DailyPortionActivity.super.onBackPressed();
//                    }
//                })
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // Leave the activity without change sharedPreference data.
//                        DailyPortionActivity.super.onBackPressed();
//                    }
//                })
//                .show();
//        pos = 1;
//    }
//
//}


package com.islamic.khatmah.daily_portion;

import static com.islamic.khatmah.MainActivity.CURRENT_PAGE;
import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;
import static com.islamic.khatmah.MainActivity.editor;
import static com.islamic.khatmah.MainActivity.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.islamic.khatmah.R;
import com.islamic.khatmah.quran_activity.QuranPageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DailyPortionActivity extends AppCompatActivity{

    public static int j;
    int number_of_pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_portion);

        Intent n = getIntent();
        j = n.getIntExtra("PAGE_NUMBER",0);

        ViewPager2 viewPager = findViewById(R.id.viewPager2);

        viewPager.setAdapter(new DailyPortionAdapter(getBaseContext(), j, viewPager));
        number_of_pages = sharedPreferences.getInt(PAGES_PER_DAY,1);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation Message")
                .setMessage("Have you finished reading the entire daily portion?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Save the last page, Surah and Juz in SharedPreference.
                        // [CURRENT_PAGE + number of PAGES_PER_DAY].
                        int cp = sharedPreferences.getInt(CURRENT_PAGE,1);
                        editor.putInt(CURRENT_PAGE, number_of_pages+cp);
                        editor.commit();
                        DailyPortionActivity.super.onBackPressed();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Leave the activity without change sharedPreference data.
                        DailyPortionActivity.super.onBackPressed();
                    }
                })
                .show();

    }

}
