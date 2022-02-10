package com.islamic.khatmah.ui.setting;

import static com.islamic.khatmah.constants.Constant.ALARM_HOUR;
import static com.islamic.khatmah.constants.Constant.ALARM_MINUTE;
import static com.islamic.khatmah.constants.Constant.CURRENT_PAGE;
import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;
import static com.islamic.khatmah.constants.Constant.TOTAL_PROGRESS;
import static com.islamic.khatmah.constants.Constant.WEEKLY_PROGRESS;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islamic.khatmah.R;
import com.islamic.khatmah.alarm.AlarmReminder;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.ui.first_start.StartActivity;
import com.islamic.khatmah.ui.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch reminderSwitch;
    private Spinner spinnerJuz, spinnerPage;
    private TextView time_textView, reset_textView;
    private int no_of_pages, selected_juz, selected_page;
    private int sHour, sMinute;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int last_num_of_pages, last_alarm_hour, last_alarm_minute;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // sharedPreference.
        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();
        last_num_of_pages = preferences.getInt(PAGES_PER_DAY, 0);
        last_alarm_hour = preferences.getInt(ALARM_HOUR, 0);
        last_alarm_minute = preferences.getInt(ALARM_MINUTE, 0);
        selected_juz = preferences.getInt(Constant.PARTS_PER_DAY_SPINNER_POSITION, 0);
        selected_page = preferences.getInt(Constant.PAGES_PER_DAY_SPINNER_POSITION, 0);

        LinearLayout reminderLayout = findViewById(R.id.reminder_body);
        reminderSwitch = findViewById(R.id.reminder_switch);
        spinnerJuz = findViewById(R.id.setting_spinnerJuz);
        spinnerPage = findViewById(R.id.setting_spinnerPages);
        reset_textView = findViewById(R.id.reset_textView);
        time_textView = findViewById(R.id.time_textView);

        String[] juz = {"أقل من جزء", "جــــــزء", "جزء ونصف", "جــــزءان", "جزءان ونصف", "ثلاثة أجزاء"};
        ArrayList<String> pages = new ArrayList<>();
        pages.add("صفحة");
        pages.add("صفحتان");
        for (int i = 3; i < 11; i++)
            pages.add(convertToArbNum(i) + " صفحات");
        for (int i = 11; i < 20; i++)
            pages.add(convertToArbNum(i) + " صفحة");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1, juz);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapter1);
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
                    no_of_pages = spinnerPage.getSelectedItemPosition() + 1;
                } else {
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
                editor.putInt(PAGES_PER_DAY, spinnerPage.getSelectedItemPosition() + 1);
                editor.putInt(Constant.PAGES_PER_DAY_SPINNER_POSITION, spinnerPage.getSelectedItemPosition());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // take time from alert activity.
        if (preferences.getBoolean(Constant.REMINDER_SWITCH_CASE, false)) {
            reminderSwitch.setChecked(true);
            reminderLayout.setVisibility(View.VISIBLE);
            time_textView.setText(preferences.getString(Constant.NOTIFICATION_TIME, "06:00 AM"));
        } else {
            reminderSwitch.setChecked(false);
            reminderLayout.setVisibility(View.GONE);
        }

        reminderSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderSwitch.isChecked()) {
                    TransitionManager.beginDelayedTransition(reminderLayout, new AutoTransition());
                    reminderLayout.setVisibility(View.VISIBLE);
                    editor.putBoolean(Constant.REMINDER_SWITCH_CASE, true).commit();
                    time_textView.setText(preferences.getString(Constant.NOTIFICATION_TIME, "06:00 AM"));
                } else {
                    TransitionManager.beginDelayedTransition(reminderLayout, new AutoTransition());
                    reminderLayout.setVisibility(View.GONE);
                    editor.putBoolean(Constant.REMINDER_SWITCH_CASE, false).commit();
                }
            }
        });

        reminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePiker();

            }
        });

        AlarmReminder alarmReminder = new AlarmReminder(sHour, sMinute); // 8888888888888888888888888888888888888888888888
        reset_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(SettingActivity.this, R.style.Theme_MyApp_Dialog_Alert)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_message)
                        .setIcon(R.drawable.ic_round_warning_24)
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putInt(WEEKLY_PROGRESS, 0);
                                editor.putInt(TOTAL_PROGRESS, 0);
                                editor.putInt(CURRENT_PAGE, 1);
                                editor.putInt(PAGES_PER_DAY, 1);
                                editor.putInt(Constant.DAILY_PROGRESS, 0);
                                editor.putInt(Constant.KHATMAH_COUNTER, 0);
                                editor.putBoolean(Constant.FINISH_DAILY_PROGRESS, false);
                                editor.putBoolean(Constant.FIRST_RUN, true);
                                editor.putBoolean(Constant.REMINDER_SWITCH_CASE, false);
                                resetValues();
                                alarmReminder.cancelAlarm(SettingActivity.this);
                                reminderSwitch.setChecked(false);
                                editor.apply();
                                Intent intent = new Intent(SettingActivity.this, StartActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
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

    private void popTimePiker() {

        Calendar cal = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                cal.set(Calendar.MINUTE, selectedMinute);

                sHour = cal.get(Calendar.HOUR_OF_DAY);
                sMinute = cal.get(Calendar.MINUTE);

                editor.putInt(Constant.ALARM_HOUR, sHour);
                editor.putInt(Constant.ALARM_MINUTE, sMinute);
                editor.commit();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                String Time = simpleDateFormat.format(cal.getTime());
                time_textView.setText(Time);
                editor.putString(Constant.NOTIFICATION_TIME, Time).commit();
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
//        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int hour = preferences.getInt(Constant.ALARM_HOUR, 0);
        int minute = preferences.getInt(Constant.ALARM_MINUTE, 0);
        boolean isTimeChanged = preferences.getBoolean(Constant.IS_TIME_CHANGED, true);

        AlarmReminder alarmReminder = new AlarmReminder(hour, minute);

        if (reminderSwitch.isChecked()) {
            if (hour != last_alarm_hour || minute != last_alarm_minute || isTimeChanged) {
                alarmReminder.schedule(SettingActivity.this);
                editor.putBoolean(Constant.IS_TIME_CHANGED, false).commit();
            }
        } else {
            alarmReminder.cancelAlarm(SettingActivity.this);
            editor.putBoolean(Constant.IS_TIME_CHANGED, true).commit();
        }

        if (last_num_of_pages != preferences.getInt(PAGES_PER_DAY, 1)) {
            editor.putInt(WEEKLY_PROGRESS, 0).commit();
            editor.putInt(Constant.DAILY_PROGRESS, 0).commit();
            resetValues();
        }
        finish();
//        MainActivity mainActivity = MainActivity.getInstance();
//        mainActivity.finish();
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
        boolean[] arr = new boolean[no_of_pages];
        storeArray(arr, Constant.ARRAY_NAME, this);
    }
}