package com.islamic.khatmah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlertActivity extends AppCompatActivity {

    ImageButton alarm_btn;
    int sHour, sMinute;
    TextView txt_time ;
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        //spinner set number of pages.
        Spinner spinner = (Spinner) findViewById(R.id.spin_num);
        String array[] = {"1", "2", "3", "4","5","6","7","8","9","10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1,array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        txt_time = (TextView) findViewById(R.id.txt_time);
        alarm_btn=findViewById(R.id.img_alarm);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popTimePiker();


            }
        });
        btn_start=findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                String Time = simpleDateFormat.format(cal.getTime());
                txt_time.setText(Time);


            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
}