package com.islamic.khatmah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class StartActivity extends AppCompatActivity {
    Button btn1,btn2,btn3;
    LinearLayout l1,l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        btn2=findViewById(R.id.button2);
        btn1=findViewById(R.id.button1);

        btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(l1.getVisibility() == View.GONE && l2.getVisibility()==View.GONE && btn2.getVisibility()==View.GONE){

                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                    }else{
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.GONE);
                        btn2.setVisibility(View.GONE);
                    }
                }
            });
        btn3=findViewById(R.id.button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, AlertActivity.class));

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, AlertActivity.class));
            }
        });

        //spinner 1.
        Spinner first_spinner = (Spinner) findViewById(R.id.الصفحة);
        String first_array[] = {"1", "2", "3", "4"};
        ArrayAdapter<String> first_adapter = new ArrayAdapter<String>(StartActivity.this, android.R.layout.simple_list_item_1, first_array);
        first_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_spinner.setAdapter(first_adapter);

        // Spinner 2 .
        Spinner second_spinner = (Spinner) findViewById(R.id.السورة);
        String second_array[] = {"الفاتحة", "البقرة"};
        ArrayAdapter<String> second_adapter = new ArrayAdapter<String>(StartActivity.this, android.R.layout.simple_list_item_1, second_array);
        second_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        second_spinner.setAdapter(second_adapter);
        //spinner 3.
        Spinner third_spinner = (Spinner) findViewById(R.id.الجزء);
        String third_array[] = {"الاول", "الثاني"};
        ArrayAdapter<String> third_adapter = new ArrayAdapter<String>(StartActivity.this, android.R.layout.simple_list_item_1, third_array);
        third_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        third_spinner.setAdapter(third_adapter);

    }



}