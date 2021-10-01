package com.islamic.khatmah.ui.setting;


import static com.islamic.khatmah.constants.Constant.DAILY_PROGRESS;
import static com.islamic.khatmah.constants.Constant.CURRENT_PAGE;
import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;
import static com.islamic.khatmah.constants.Constant.TOTAL_PROGRESS;
import static com.islamic.khatmah.constants.Constant.WEEKLY_PROGRESS;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.islamic.khatmah.Models.AlarmReminder;
import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;

import com.islamic.khatmah.ui.first_start.StartActivity;

import com.islamic.khatmah.ui.first_start.AlertActivity;


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
    public static boolean isPagesNumberChanged = false; // مؤقتا يا ورد الورود

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // sharedPreference.
        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        selected_juz = preferences.getInt(Constant.PARTS_PER_DAY_SPINNER_POSITION, 0);
        selected_page = preferences.getInt(Constant.PAGES_PER_DAY_SPINNER_POSITION, 0);

        LinearLayout reminderLayout = findViewById(R.id.reminder_body);
        reminderSwitch = findViewById(R.id.reminder_switch);

        spinnerJuz = findViewById(R.id.setting_spinnerJuz);
        spinnerPage = findViewById(R.id.setting_spinnerPages);
        reset_textView = findViewById(R.id.reset_textView);
        time_textView = findViewById(R.id.time_textView);

        if (preferences.getBoolean(Constant.REMINDER_SWITCH_CASE, false)) {
            reminderSwitch.setChecked(true);
            reminderLayout.setVisibility(View.VISIBLE);
            time_textView.setText(preferences.getString(Constant.NOTIFICATION_TIME, "00:00 AM"));
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
                    no_of_pages = spinnerPage.getSelectedItemPosition()+1;
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
                isPagesNumberChanged = true;
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

        reminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePiker();
                createNotificationChannel();
            }
        });

        reset_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_message)
                        .setIcon(R.drawable.ic_round_warning_24)
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putInt(WEEKLY_PROGRESS,0);
                                editor.putInt(TOTAL_PROGRESS,0);
                                editor.putInt(CURRENT_PAGE,1);
                                editor.putInt(PAGES_PER_DAY,1);
                                editor.putInt(Constant.DAILY_PROGRESS,0);
                                editor.putBoolean(Constant.FIRST_RUN,true);
                                reminderSwitch.setChecked(false);
                                editor.apply();
                                Intent intent = new Intent(SettingActivity.this, StartActivity.class);
                                startActivity(intent);
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                String Time = simpleDateFormat.format(cal.getTime());
                time_textView.setText(Time);
                editor.putString(Constant.NOTIFICATION_TIME, Time);
                editor.commit();
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "KhatmaChannel";
            String description = "ختمه";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);

            channel.setDescription(description);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        AlarmReminder alarmReminder = new AlarmReminder(sHour, sMinute);
        if (reminderSwitch.isChecked())
            alarmReminder.schedule(SettingActivity.this);
        else
            alarmReminder.cancelAlarm(SettingActivity.this);

    }
}