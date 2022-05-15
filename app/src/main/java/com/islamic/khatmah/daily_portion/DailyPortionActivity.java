package com.islamic.khatmah.daily_portion;

import static com.islamic.khatmah.constants.Constant.CURRENT_PAGE;
import static com.islamic.khatmah.constants.Constant.CURRENT_SURAH;
import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.ui.main.MainActivity;

public class DailyPortionActivity extends AppCompatActivity {

    public static int currentPageNum;
    private int number_of_pages;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_portion);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        // Receive intent from DailyPortionFragment.
        Intent n = getIntent();
        currentPageNum = n.getIntExtra("PAGE_NUMBER", 0);

        // Load sharedPreferences [number of pages per day].
        number_of_pages = preferences.getInt(PAGES_PER_DAY, 1);
        if (preferences.getInt(Constant.ARRAY_NAME + "_size", 0) != number_of_pages) {
            boolean[] arr = new boolean[number_of_pages];
            storeArray(arr, Constant.ARRAY_NAME, this);
        }
        boolean[] isChecked = loadArray(Constant.ARRAY_NAME, this);

        // Create viewPager.
        ViewPager2 viewPager = findViewById(R.id.viewPager2);
        viewPager.setAdapter(new DailyPortionAdapter(this, currentPageNum, isChecked, number_of_pages, viewPager));
        viewPager.setCurrentItem(preferences.getInt(Constant.DAILY_PROGRESS, 0));
    }

    @Override
    public void onBackPressed() {

        // Loading or saving data to shared preferences.
        SharedPreferences preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, 0);

        // Number of read pages.
        int counter = preferences.getInt(Constant.DAILY_PROGRESS, 0);
        if (counter < number_of_pages) {
            // If the user hasn't finished his portion
            new MaterialAlertDialogBuilder(this, R.style.Theme_MyApp_Dialog_Alert)
                    .setTitle("رسالة تأكيد")
                    .setMessage("هل أتممت وردك اليومي ؟")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Save the last page, Surah and Juz in SharedPreference.
                            // [CURRENT_PAGE + number of PAGES_PER_DAY].

                            int cP;
                            if (number_of_pages + currentPageNum > 604) {
                                cP = number_of_pages + currentPageNum - 604;
                                editor.putInt(CURRENT_PAGE, number_of_pages + currentPageNum - 604);
                            } else {
                                cP = number_of_pages + currentPageNum;
                                editor.putInt(CURRENT_PAGE, number_of_pages + currentPageNum);
                            }
                            editor.putString(CURRENT_SURAH, MainActivity.surahName.get(cP - 1)).apply();

                            editor.putInt(Constant.DAILY_PROGRESS, 0);
                            editor.putInt(Constant.WEEKLY_PROGRESS, preferences.getInt(Constant.WEEKLY_PROGRESS, 0) - counter + preferences.getInt(PAGES_PER_DAY,0));
                            editor.putInt(Constant.TOTAL_PROGRESS, preferences.getInt(Constant.TOTAL_PROGRESS, 0) - counter + preferences.getInt(PAGES_PER_DAY,0));
                            editor.putBoolean(Constant.FINISH_DAILY_PROGRESS,true);
                            editor.apply();
                            resetValues();

                            DailyPortionActivity.super.onBackPressed();
                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Leave the activity without change sharedPreference data.
                            // I need to know number of read pages.
                            editor.putBoolean(Constant.FINISH_DAILY_PROGRESS,false).apply();
                            DailyPortionActivity.super.onBackPressed();
                        }
                    })
                    .show();

        } else {
            editor.putInt(CURRENT_PAGE, number_of_pages + currentPageNum);
            editor.putInt(Constant.DAILY_PROGRESS, 0);
            editor.putBoolean(Constant.FINISH_DAILY_PROGRESS,true);
            editor.commit();
            resetValues();
            DailyPortionActivity.super.onBackPressed();
        }
    }

    public boolean[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, 0);
        int num_pages = prefs.getInt(arrayName + "_size", 0);
        boolean[] array = new boolean[num_pages];
        for (int i = 0; i < number_of_pages; i++)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false);
        return array;
    }

    public void storeArray(boolean[] array, String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);
        editor.apply();
    }

    private void resetValues() {
        boolean[] arr = new boolean[number_of_pages];
        storeArray(arr, Constant.ARRAY_NAME, this);
    }

}
