package com.islamic.khatmah.ui.first_start;

import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.alarm.AlarmReminder;
import com.islamic.khatmah.ui.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlertActivity extends AppCompatActivity {

    int sHour, sMinute;
    private TextView txt_time;
    TextView btn_start;
    Spinner spinnerJuz, spinnerPages;
    Switch aSwitch;
    private LinearLayout alertReminderLayout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int no_of_pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        aSwitch = findViewById(R.id.alert_switch);

        //spinner set number of pages.
        spinnerJuz = findViewById(R.id.spinnerJuz);
        spinnerPages = findViewById(R.id.spinnerPages);

//        spinnerJuz.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        spinnerPages.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        String[] juz = {"أقل من جزء", "جــــــزء", "جزء ونصف", "جــــزءان", "جزءان ونصف", "ثلاثة أجزاء"};
        ArrayList<String> pages = new ArrayList<>();
        pages.add("صفحة");
        pages.add("صفحتان");
        for (int i = 3; i < 11; i++)
            pages.add(convertToArbNum(i) + " صفحات");
        for (int i = 11; i < 20; i++)
            pages.add(convertToArbNum(i) + " صفحة");

        // Juz spinner.
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AlertActivity.this, R.layout.spinner_text_dark, juz);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapter1);
        spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); // to make spinner text in white color..
                if (spinnerJuz.getSelectedItem() == juz[0]) {
                    spinnerPages.setVisibility(View.VISIBLE);
                    no_of_pages = spinnerPages.getSelectedItemPosition()+1;
                } else {
                    spinnerPages.setVisibility(View.GONE);
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
                editor.putInt(Constant.PARTS_PER_DAY_SPINNER_POSITION, spinnerJuz.getSelectedItemPosition());
                editor.putInt(PAGES_PER_DAY, no_of_pages);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Pages spinner.
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AlertActivity.this, R.layout.spinner_text_dark, pages);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPages.setAdapter(adapter2);
        spinnerPages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); // to make spinner text in white color.
                editor.putInt(Constant.PAGES_PER_DAY_SPINNER_POSITION, spinnerPages.getSelectedItemPosition());
                editor.putInt(PAGES_PER_DAY, spinnerPages.getSelectedItemPosition() + 1);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertReminderLayout = findViewById(R.id.alert_reminder_body);
        txt_time = findViewById(R.id.alert_text_time);

        if (preferences.getBoolean(Constant.REMINDER_SWITCH_CASE, false)) {
            aSwitch.setChecked(true);
            alertReminderLayout.setVisibility(View.VISIBLE);
            txt_time.setText(preferences.getString(Constant.NOTIFICATION_TIME, "06:00 AM"));
        } else {
            aSwitch.setChecked(false);
            alertReminderLayout.setVisibility(View.GONE);
        }

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    TransitionManager.beginDelayedTransition(alertReminderLayout, new AutoTransition());
                    alertReminderLayout.setVisibility(View.VISIBLE);
                    editor.putBoolean(Constant.REMINDER_SWITCH_CASE, true).commit();
                } else {
                    TransitionManager.beginDelayedTransition(alertReminderLayout, new AutoTransition());
                    alertReminderLayout.setVisibility(View.GONE);
                    editor.putBoolean(Constant.REMINDER_SWITCH_CASE, false).commit();
                }
            }
        });

        AlarmReminder alarmReminder = new AlarmReminder(sHour, sMinute);

        alertReminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    popTimePiker();
                    createNotificationChannel();
                    alarmReminder.schedule(AlertActivity.this);

                }else{
                    alarmReminder.cancelAlarm(AlertActivity.this);

                }
            }
        });

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int hour = preferences.getInt(Constant.ALARM_HOUR,0);
//                int minute = preferences.getInt(Constant.ALARM_MINUTE,0);
//                AlarmReminder alarmReminder = new AlarmReminder(hour, minute);

                AlarmReminder alarmReminder = new AlarmReminder(sHour, sMinute);

                if (aSwitch.isChecked()) {
//                    alarmReminder.schedule(AlertActivity.this);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    editor.putInt(Constant.FIRST_DAY,calendar.get(Calendar.DAY_OF_WEEK)).apply();
                    AlarmReminder.resetWeeklyProgress(AlertActivity.this,calendar.get(Calendar.DAY_OF_WEEK));
                }
//                else{
//                    alarmReminder.cancelAlarm(AlertActivity.this);
//
//                }
                editor.putBoolean(Constant.FIRST_RUN, false).apply();
                startActivity(new Intent(AlertActivity.this, MainActivity.class));
                finish();
            }
        });
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
                txt_time.setText(Time);

                editor.putString(Constant.NOTIFICATION_TIME, Time).commit();
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
