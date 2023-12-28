package com.islamic.khatmah.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayoutMediator;
import com.islamic.khatmah.R;
import com.islamic.khatmah.ui.first_start.StartActivity;
import com.islamic.khatmah.pojo.Constant;
import com.islamic.khatmah.ui.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public static ArrayList<String> surahName;
    private AlertDialog materialAlertDialogBuilder;
    private boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surahName = new ArrayList<>();

        // sharedPreference.
        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();
        isFirstRun = preferences.getBoolean(Constant.FIRST_RUN, true);
        if (isFirstRun) {
            //show Start activity
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        }

        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(1);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (viewPager.getCurrentItem() == 0){
//                    || viewPager.getCurrentItem() == 2
                    TransitionManager.beginDelayedTransition(toolbar, new AutoTransition());
                    toolbar.setVisibility(View.GONE);
                }else{
                    TransitionManager.beginDelayedTransition(toolbar, new AutoTransition());
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(setTextOfTheTab(position))
        ).attach();

        new Thread(new Runnable() {
            @Override
            public void run() {
                preparingSurahNames();
            }
        }).start();

        InputStream is;
        try {
            is = this.openFileInput("" + 604);
            Bitmap bit = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {

//            if (!preferences.getBoolean(Constant.DOWNLOAD_IS_RUNNING, false)) {
//                Intent downloadIntent = new Intent(this, DownloadService.class);
//                materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.Theme_MyApp_Dialog_Alert)
//                        .setTitle(R.string.download)
//                        .setMessage(R.string.download_message)
//                        // Specifying a listener allows you to take an action before dismissing the dialog.
//                        // The dialog is automatically dismissed when a dialog button is clicked.
//                        .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//                                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
////                                boolean isMetered = cm.isActiveNetworkMetered();
//                                if (isConnected) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                                        getBaseContext().startForegroundService(downloadIntent);
//                                    else
//                                        ContextCompat.startForegroundService(getBaseContext(), downloadIntent);
//                                } else {
//                                    Toast.makeText(getBaseContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        })
//                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .setIcon(R.drawable.ic_baseline_cloud_download_24)
//                        .setCancelable(false)
//                        .show();
//            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume()");
        if (isFirstRun) {
            //show Start activity
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (materialAlertDialogBuilder != null)
            materialAlertDialogBuilder.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_setting) {
            startActivity(new Intent(getBaseContext(), SettingActivity.class));
            return true;
        }
//        else if (itemId == R.id.item_about_us) {
//            Toast.makeText(getBaseContext(), R.string.will_be_added_soon, Toast.LENGTH_SHORT).show();
////            startActivity(new Intent(getBaseContext(), StartActivity.class));
//            return true;
//        } else if (itemId == R.id.item_feedback) {
////            startActivity(new Intent(getBaseContext(), StartActivity.class));
//            Toast.makeText(getBaseContext(), R.string.will_be_added_soon, Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    //set the tabBar Text
    private String setTextOfTheTab(int position) {
        switch (position) {
            case 2:
                return "الإحصائيات";
            case 1:
                return "الورد اليومي";
            case 0:
                return "القرآن الكريم";
            default:
                return "";
        }
    }

    private void preparingSurahNames() {
        JSONObject jsonObject, pageData;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject(Objects.requireNonNull(JsonDataFromAsset("page_details.json")));
            jsonArray = jsonObject.getJSONArray("page_details");
            surahName.add(jsonArray.getJSONObject(0).getString("name"));
            for (int k = 1; k < jsonArray.length(); k++) {
                pageData = jsonArray.getJSONObject(k);
                for (int i = pageData.getInt("start"); i <= pageData.getInt("end"); i++)
                    surahName.add(pageData.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String JsonDataFromAsset(String fileName) {
        String json;
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
