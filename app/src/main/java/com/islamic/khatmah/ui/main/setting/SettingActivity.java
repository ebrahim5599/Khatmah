package com.islamic.khatmah.ui.main.setting;

import static com.islamic.khatmah.MainActivity.CURRENT_PAGE;
import static com.islamic.khatmah.MainActivity.editor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.islamic.khatmah.AlertActivity;
import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.daily_portion.DailyPortionActivity;

public class SettingActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch reminderSwitch;
    private Spinner spinnerJuz, spinnerPage;
    private TextView reset_textView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout reminderLayout = findViewById(R.id.reminder_body);
        reminderSwitch = findViewById(R.id.reminder_switch);
        spinnerJuz = findViewById(R.id.setting_spinnerJuz);
        spinnerPage = findViewById(R.id.setting_spinnerPages);
        reset_textView = findViewById(R.id.reset_textView);

        reminderSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderSwitch.isChecked()) {
                    TransitionManager.beginDelayedTransition(reminderLayout, new AutoTransition());
                    reminderLayout.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(reminderLayout, new AutoTransition());
                    reminderLayout.setVisibility(View.GONE);
                }
            }
        });

        String[] juz = {"أقل من جزء", "جــــــزء", "جزء ونصف", "جــــزءان", "جزءان ونصف", "ثلاثة أجزاء"};
        String[] pages = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1, juz);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1, pages);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapter2);

        spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerJuz.getSelectedItem() == juz[0]) {
                    spinnerPage.setVisibility(View.VISIBLE);
                }else {
                    spinnerPage.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
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
}