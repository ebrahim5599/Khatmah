package com.islamic.khatmah.ui.setting;

import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.widget.TextView;
import android.widget.Toast;

import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch reminderSwitch;
    private Spinner spinnerJuz, spinnerPage;
    private TextView time_textView, reset_textView;
    private int no_of_pages, selected_juz, selected_page;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // sharedPreference.
        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        selected_juz = preferences.getInt(Constant.PARTS_PER_DAY_SPINNER_POSITION,0);
        selected_page= preferences.getInt(Constant.PAGES_PER_DAY_SPINNER_POSITION,0);

        LinearLayout reminderLayout = findViewById(R.id.reminder_body);
        reminderSwitch = findViewById(R.id.reminder_switch);

        spinnerJuz = findViewById(R.id.setting_spinnerJuz);
        spinnerPage = findViewById(R.id.setting_spinnerPages);
        reset_textView = findViewById(R.id.reset_textView);
        time_textView = findViewById(R.id.time_textView);

        if(preferences.getBoolean(Constant.REMINDER_SWITCH_CASE, false)){
            reminderSwitch.setChecked(true);
            reminderLayout.setVisibility(View.VISIBLE);
            time_textView.setText(preferences.getString(Constant.NOTIFICATION_TIME, "00:00 AM"));
        }else{
            reminderSwitch.setChecked(false);
            reminderLayout.setVisibility(View.GONE);
        }

        reminderSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderSwitch.isChecked()) {
                    TransitionManager.beginDelayedTransition(reminderLayout, new AutoTransition());
                    reminderLayout.setVisibility(View.VISIBLE);
                    time_textView.setText(preferences.getString(Constant.NOTIFICATION_TIME, "00:00 AM"));
                    editor.putBoolean(Constant.REMINDER_SWITCH_CASE, true).commit();
                } else {
                    TransitionManager.beginDelayedTransition(reminderLayout, new AutoTransition());
                    reminderLayout.setVisibility(View.GONE);
                    editor.putBoolean(Constant.REMINDER_SWITCH_CASE, false).commit();
                }
            }
        });

        String[] juz = {"أقل من جزء", "جــــــزء", "جزء ونصف", "جــــزءان", "جزءان ونصف", "ثلاثة أجزاء"};
        ArrayList<String> pages = new ArrayList<>();
        pages.add("صفحة");
        pages.add("صفحتان");
        for(int i = 3; i < 11; i++)
            pages.add(convertToArbNum(i)+" صفحات");
        for(int i = 11; i < 20; i++)
            pages.add(convertToArbNum(i)+" صفحة");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1, juz);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapter1);
        Toast.makeText(getApplicationContext(), selected_juz+"", Toast.LENGTH_SHORT).show();
        spinnerJuz.setSelection(selected_juz);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1, pages);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapter2);
        spinnerPage.setSelection(selected_page);

        spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerJuz.getSelectedItem() == juz[0]) {
                    spinnerPage.setVisibility(View.VISIBLE);
                    no_of_pages = 1;
                }else {
                    spinnerPage.setVisibility(View.GONE);
                    switch (spinnerJuz.getSelectedItemPosition()) {
                        case 1:
                            // juz
                            no_of_pages = 20;
                            break;
                        case 2:
                            // 1.5 juz.
                            no_of_pages = 30;
                            break;
                        case 3:
                            // 2 juz.
                            no_of_pages = 40;
                            break;
                        case 4:
                            // 2.5 juz.
                            no_of_pages = 50;
                            break;
                        case 5:
                            // 3 juz.
                            no_of_pages = 60;
                            break;
                    }
                }
                editor.putInt(PAGES_PER_DAY, no_of_pages);
                editor.putInt(Constant.PARTS_PER_DAY_SPINNER_POSITION, spinnerJuz.getSelectedItemPosition());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt(PAGES_PER_DAY, spinnerPage.getSelectedItemPosition()+1);
                editor.putInt(Constant.PAGES_PER_DAY_SPINNER_POSITION, spinnerPage.getSelectedItemPosition());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reset_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_message)
                        .setIcon(R.drawable.ic_round_warning_24)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });
    }

    // This method converts English numbers to Indian number [Arabic].
    private String convertToArbNum(int number) {

        String stNum = String.valueOf(number);
        String result = "";

        for (int i = 0; i < stNum.length(); i++) {
            char num = String.valueOf(stNum).charAt(i);
            int ArabicNum = num + 1584;
            result += (char) ArabicNum;
        }
        return result;
    }
}