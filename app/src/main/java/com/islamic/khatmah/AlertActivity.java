package com.islamic.khatmah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
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
    Spinner spinnerJuz,spinnerPages;
    Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        aSwitch = findViewById(R.id.switch1);
        //spinner set number of pages.
        spinnerJuz = (Spinner) findViewById(R.id.spinnerJuz);
        spinnerPages = (Spinner) findViewById(R.id.spinnerPages);
        String[] juz = {"less than one", "1", "1.5", "2","2.5","3"};
        String[] pages = {"1", "2", "3", "4","5","6","7","8","9","10",
                "11", "12", "13", "14","15","16","17","18","19","20"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1,juz);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapter1);
        spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerJuz.getSelectedItem()==juz[0]){
                    spinnerPages.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1,pages);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPages.setAdapter(adapter2);
                }else {
                    spinnerPages.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txt_time = (TextView) findViewById(R.id.txt_time);
        alarm_btn=findViewById(R.id.img_alarm);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aSwitch.isChecked()){
                    txt_time.setVisibility(View.VISIBLE);
                    popTimePiker();
                }else {
                    txt_time.setVisibility(View.GONE);
                }
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