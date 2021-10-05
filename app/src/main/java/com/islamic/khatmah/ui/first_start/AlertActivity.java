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
        alertReminderLayout = findViewById(R.id.alert_reminder_body);
        alertReminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    popTimePiker();
                    createNotificationChannel();
                }
            }
        });

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
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1, juz);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapter1);
        spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); // to make spinner text in white color..
                if (spinnerJuz.getSelectedItem() == juz[0]) {
                    spinnerPages.setVisibility(View.VISIBLE);
                    no_of_pages = 1;
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
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1, pages);
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

        txt_time = findViewById(R.id.alert_text_time);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlertActivity.this, MainActivity.class));

                AlarmReminder alarmReminder = new AlarmReminder(sHour, sMinute);
                if (aSwitch.isChecked()) {
                    alarmReminder.schedule(AlertActivity.this);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    preferences.edit().putInt(Constant.FIRST_DAY,calendar.get(Calendar.DAY_OF_WEEK)).apply();
                    AlarmReminder.resetWeeklyProgress(AlertActivity.this,calendar.get(Calendar.DAY_OF_WEEK));
                    preferences.edit().putBoolean(Constant.PREV_STARTED,true).apply();

                }else{
                    alarmReminder.cancelAlarm(AlertActivity.this);

                }
//                startActivity(new Intent(AlertActivity.this, DownloadDialogActivity.class));
                editor.putBoolean(Constant.FIRST_RUN, false).apply();
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
                editor.putInt(Constant.ALARM_HOUR, sHour).apply();
                editor.putInt(Constant.ALARM_MINUTE, sMinute).apply();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                String Time = simpleDateFormat.format(cal.getTime());
                txt_time.setText(Time);
                editor.putString(Constant.NOTIFICATION_TIME, Time);
                editor.commit();
//                if (cal.getTime().compareTo(new Date()) < 0)
//                    cal.add(Calendar.DAY_OF_MONTH, 1);

//                Intent intent = new Intent(AlertActivity.this, Reminder.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlertActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                long timeAlert = 1000L * 60 * sMinute + 1000L * 60 * 60 * sHour;
//                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAlert, pendingIntent);
//                if (alarmManager != null) {
//                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeAlert, AlarmManager.INTERVAL_DAY, pendingIntent);

//                 Intent intent = new Intent(AlertActivity.this, Reminder.class);
//                 PendingIntent pendingIntent = PendingIntent.getBroadcast(AlertActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                 AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                 long timeAlert = 1000 * 60 * sMinute + 1000 * 60 * 60 * sHour;
//                 long timeButtonClick = System.currentTimeMillis();
//                 alarmManager.set(AlarmManager.RTC_WAKEUP, timeAlert, pendingIntent);
//                 if (alarmManager != null) {
//                     alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


//                }
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
