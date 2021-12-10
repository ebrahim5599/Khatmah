//package com.islamic.khatmah.daily_portion;
//
//import static com.islamic.khatmah.ui.main.MainActivity.CURRENT_PAGE;
//import static com.islamic.khatmah.ui.main.MainActivity.PAGES_PER_DAY;
//import static com.islamic.khatmah.ui.main.MainActivity.editor;
//import static com.islamic.khatmah.ui.main.MainActivity.sharedPreferences;
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

import static com.islamic.khatmah.constants.Constant.CURRENT_PAGE;
import static com.islamic.khatmah.constants.Constant.CURRENT_SURAH;
import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islamic.khatmah.R;
import com.islamic.khatmah.alarm.AlarmReminder;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.ui.main.MainActivity;

public class DailyPortionActivity extends AppCompatActivity {

    public static int currentPageNum;
    private int number_of_pages;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_portion);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
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
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirmation_message)
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Save the last page, Surah and Juz in SharedPreference.
                            // [CURRENT_PAGE + number of PAGES_PER_DAY].

//                            int cp = sharedPreferences.getInt(CURRENT_PAGE, 1);
                            int cP;
                            if (number_of_pages + currentPageNum > 604) {
                                cP = number_of_pages + currentPageNum - 604;
                                editor.putInt(CURRENT_PAGE, number_of_pages + currentPageNum - 604);
                            } else {
                                cP = number_of_pages + currentPageNum;
                                editor.putInt(CURRENT_PAGE, number_of_pages + currentPageNum);
                            }
                            editor.putString(CURRENT_SURAH, MainActivity.surahName.get(cP - 1)).apply();

                            //editor.putInt(Constant.PROGRESS_COUNT, 0);


                            editor.putInt(Constant.DAILY_PROGRESS, 0);
                            editor.putInt(Constant.WEEKLY_PROGRESS, preferences.getInt(Constant.WEEKLY_PROGRESS, 0) - counter + preferences.getInt(PAGES_PER_DAY,0));
                            editor.putInt(Constant.TOTAL_PROGRESS, preferences.getInt(Constant.TOTAL_PROGRESS, 0) - counter + preferences.getInt(PAGES_PER_DAY,0));
                            editor.putBoolean(Constant.FINISH_DAILY_PROGRESS,true);
//                            AlarmReminder.removeFinishDailyPortion(DailyPortionActivity.this);
                            editor.apply();
                            resetValues();

                            DailyPortionActivity.super.onBackPressed();
                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Leave the activity without change sharedPreference data.
                            // TODO: I need to know number of read pages.
//                            editor.putInt(Constant.WEEKLY_PROGRESS, counter); // by Ebrahim
//                            editor.commit();
                            DailyPortionActivity.super.onBackPressed();
                        }
                    })
                    .show();

        } else {
            editor.putInt(CURRENT_PAGE, number_of_pages + currentPageNum);
            editor.putInt(Constant.DAILY_PROGRESS, 0);
            editor.commit();
            resetValues();
            DailyPortionActivity.super.onBackPressed();
        }
//        DailyPortionActivity.super.onBackPressed();
//        new AlertDialog.Builder(this)
//                .setTitle("Confirmation Message")
//                .setMessage("Have you finished reading the entire daily portion?")
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Save the last page, Surah and Juz in SharedPreference.
//                        // [CURRENT_PAGE + number of PAGES_PER_DAY].
//                        int cp = sharedPreferences.getInt(CURRENT_PAGE, 1);
//                        editor.putInt(CURRENT_PAGE, number_of_pages + cp);
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

    }
    //Load boolean array from shared preferences

    public boolean[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, 0);
        int num_pages = prefs.getInt(arrayName + "_size", 0);
        boolean[] array = new boolean[num_pages];
        for (int i = 0; i < number_of_pages; i++)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false);
        return array;
    }

    public boolean storeArray(boolean[] array, String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);

        return editor.commit();
    }

    private void resetValues() {
        boolean[] arr = new boolean[number_of_pages];
        storeArray(arr, Constant.ARRAY_NAME, this);
    }

}
