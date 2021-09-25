package com.islamic.khatmah;

import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.Models.AlarmReminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlertActivity extends AppCompatActivity {

    ImageButton alarm_btn;
    int sHour, sMinute;
    private TextView txt_time, p;
    TextView btn_start;
    Spinner spinnerJuz, spinnerPages;
    Switch aSwitch;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        aSwitch = findViewById(R.id.switch1);
        p = findViewById(R.id.p);

        //spinner set number of pages.
        spinnerJuz =  findViewById(R.id.spinnerJuz);
        spinnerPages = findViewById(R.id.spinnerPages);

        spinnerJuz.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        spinnerPages.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        String[] juz = {"أقل من جزء", "1", "1.5", "2", "2.5", "3"};
        String[] pages = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1, juz);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapter1);
        spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); // to make spinner text in white color.
                if (spinnerJuz.getSelectedItem() == juz[0]) {
                    spinnerPages.setVisibility(View.VISIBLE);
                    p.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1, pages);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPages.setAdapter(adapter2);
                    spinnerPages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); // to make spinner text in white color.
                            editor.putInt(PAGES_PER_DAY, Integer.parseInt(spinnerPages.getSelectedItem().toString()));
                            editor.commit();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    spinnerPages.setVisibility(View.GONE);
                    p.setVisibility(View.GONE);
                    int no_of_pages = (int) (Double.parseDouble(spinnerJuz.getSelectedItem().toString()) * 20);
                    preferences.edit().putInt(PAGES_PER_DAY, no_of_pages).apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txt_time = findViewById(R.id.txt_time);
        alarm_btn = findViewById(R.id.img_alarm);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aSwitch.isChecked()) {
                    txt_time.setVisibility(View.VISIBLE);
                    popTimePiker();
                    createNotificationchannel();
                } else {
                    txt_time.setVisibility(View.INVISIBLE);
                }
            }
        });
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlertActivity.this, MainActivity.class));
                if (aSwitch.isChecked()) {
                    AlarmReminder alarmReminder = new AlarmReminder(sHour,sMinute);
                    alarmReminder.cancelAlarm(AlertActivity.this);
                    alarmReminder.schedule(AlertActivity.this);
                }
//                startActivity(new Intent(AlertActivity.this, DownloadDialogActivity.class));
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
                MainActivity.sharedPreferences.edit().putInt(Constant.ALARM_HOUR, sHour).apply();
                MainActivity.sharedPreferences.edit().putInt(Constant.ALARM_MINUTE, sMinute).apply();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                String Time = simpleDateFormat.format(cal.getTime());
                txt_time.setText(Time);

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


    private void createNotificationchannel() {
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
}
