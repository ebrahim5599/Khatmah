package com.islamic.khatmah.ui.first_start;

import static com.islamic.khatmah.constants.Constant.CURRENT_JUZ;
import static com.islamic.khatmah.constants.Constant.CURRENT_PAGE;
import static com.islamic.khatmah.constants.Constant.CURRENT_SURAH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.islamic.khatmah.R;
import com.islamic.khatmah.alarm.AlarmReminder;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private TextView specific_start_button, button_next, start_from_portion1_button, hadith, empty;
    private LinearLayout start_activity_buttons;
    private CardView first_card_view;
    RelativeLayout first_linear_layout;
    Spinner spinnerJuz, spinnerPage, spinnerSurah, spinnerChoose;
    ArrayList<String> juz, surah, page;
    JSONObject jsonObject, jsonObjectJuzDetails;
    JSONArray jsonArray, jsonArrayJuzDetails;
    boolean isPageChecked, isSurahChecked, isJuzChecked;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        preferences = getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        // Link views to XML file.
        start_activity_buttons = findViewById(R.id.first_screen_buttons);
        start_from_portion1_button = findViewById(R.id.start_from_portion1_button);
        first_card_view = findViewById(R.id.first_card_view);

        specific_start_button = findViewById(R.id.specific_start_button);
        button_next = findViewById(R.id.next_button);
        first_linear_layout = findViewById(R.id.linear_layout_1);
        hadith = findViewById(R.id.hadith_container);
        hadith.setText("عَن عَبْدَ اللهِ بْنَ مَسْعُودٍ رَضِيَ اللَّهُ عَنْهُ: أنَّ النَّبيِ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ قال: « مَنْ قَرَأَ حَرْفًا مِنْ كِتَابِ اللهِ فَلَهُ بِهِ حَسَنَةٌ وَالْحَسَنَةُ بِعَشْرِ أَمْثَالِهَا؛ لَا أَقُولُ: الم حَرْفٌ وَلَكِنْ أَلِفٌ حَرْفٌ وَلَامٌ حَرْفٌ وَمِيمٌ حَرْفٌ ».");

        spinnerJuz = findViewById(R.id.juz_spinner);
        spinnerPage = findViewById(R.id.page_spinner);
        spinnerSurah = findViewById(R.id.surah_spinner);

        // Start from first juz listener.
        start_from_portion1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, AlertActivity.class));
                editor.putInt(CURRENT_PAGE, 1);
                editor.putString(CURRENT_SURAH, MainActivity.surahName.get(0));
                editor.putString(CURRENT_JUZ, String.format("الجزء %s", convertToArbNum(1)));
                editor.commit();
                startActivity(new Intent(StartActivity.this, AlertActivity.class));
            }
        });

        // Start from specific point listener.
        specific_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_linear_layout.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(first_linear_layout, new AutoTransition());
                    first_linear_layout.setVisibility(View.VISIBLE);
                    TransitionManager.beginDelayedTransition(first_card_view, new AutoTransition());
                    first_card_view.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(first_linear_layout, new AutoTransition());
                    first_linear_layout.setVisibility(View.GONE);
                    TransitionManager.beginDelayedTransition(first_card_view, new AutoTransition());
                    first_card_view.setVisibility(View.VISIBLE);
                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, AlertActivity.class));
                editor.putInt(CURRENT_PAGE, Integer.parseInt(spinnerPage.getSelectedItem().toString()));
                editor.putString(CURRENT_SURAH, spinnerSurah.getSelectedItem().toString());
                editor.putString(CURRENT_JUZ, spinnerJuz.getSelectedItem().toString());
                editor.commit();
            }
        });

        // Add quran portions to ArrayList.
        juz = new ArrayList<>();
        for (int i = 1; i < 31; i++)
            juz.add("الجزء " + convertToArbNum(i));

        // Add quran pages to ArrayList.
        page = new ArrayList<>();
        for (int i = 1; i < 605; i++)
            page.add(convertToArbNum(i));

        // Add quran "surah"s names to ArrayList from JSON file.
        surah = new ArrayList<>();
        try {
            jsonObject = new JSONObject(Objects.requireNonNull(JsonDataFromAsset("surah.json")));
            jsonArray = jsonObject.getJSONArray("data");
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject surahData = jsonArray.getJSONObject(j);
                surah.add(surahData.getString("name"));
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        // Extract juz details from juz_details JSON file.
        try {
            jsonObjectJuzDetails = new JSONObject(Objects.requireNonNull(JsonDataFromAsset("juz_details.json")));
            jsonArrayJuzDetails = jsonObjectJuzDetails.getJSONArray("juz_details");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set juz adapter to juz spinner.
        ArrayAdapter<String> adapterJuz = new ArrayAdapter<>(this, R.layout.spinner_text, juz);
        adapterJuz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJuz.setAdapter(adapterJuz);

        // Set surah adapter to surah spinner.
        ArrayAdapter<String> adapterSurah = new ArrayAdapter<>(this, R.layout.spinner_text, surah);
        adapterSurah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSurah.setAdapter(adapterSurah);

        // Set page adapter to page spinner.
        ArrayAdapter<String> adapterPage = new ArrayAdapter<>(this, R.layout.spinner_text, page);
        adapterPage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapterPage);

        isJuzChecked = true;

        spinnerJuz.setEnabled(true);
        spinnerPage.setEnabled(false);
        spinnerSurah.setEnabled(false);
        // Make spinner arrows in teal_200 color.
//        spinnerJuz.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        spinnerJuz.setBackgroundResource(R.drawable.spinner_border);
        spinnerSurah.setBackgroundResource(R.drawable.rounded_button);
        spinnerPage.setBackgroundResource(R.drawable.rounded_button);
//        spinnerSurah.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);
//        spinnerPage.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);
//        ContextThemeWrapper newContext = new ContextThemeWrapper(getBaseContext(), R.style.spinner_style);


        // When choose juz other 2 spinners will change to related page and surah.
        spinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                // to make spinner text in white color.
                // TODO: [Error occur when orientation changes].
//                ((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
                for (int i = 0; i < jsonArrayJuzDetails.length(); i++) {
                    try {
                        if (isJuzChecked) {
                            JSONObject surahData = jsonArrayJuzDetails.getJSONObject(arg2);
                            spinnerSurah.setSelection(surahData.getInt("pos") - 1);
                            spinnerPage.setSelection(surahData.getInt("start") - 1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // When choose surah other 2 spinners will change to related start page and juz.
        spinnerSurah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                // to make spinner text in white color.
                // TODO: [Error occur when orientation changes].
//                ((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
                try {
                    if (isSurahChecked) {
                        JSONObject surahData = jsonArray.getJSONObject(arg2);
                        spinnerPage.setSelection(surahData.getInt("start") - 1);
                        spinnerJuz.setSelection(surahData.getInt("juz") - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // When choose page number other 2 spinners will change to related juz and surah.
        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                // to make spinner text in white color.
                // TODO: [Error occur when orientation changes].
//                ((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
                for (int s = 0; s < jsonArray.length(); s++) {
                    JSONObject surahData = null;
                    try {
                        surahData = jsonArray.getJSONObject(s);
                        if (Integer.parseInt(spinnerPage.getSelectedItem().toString()) >= surahData.getInt("start") &&
                                Integer.parseInt(spinnerPage.getSelectedItem().toString()) <= surahData.getInt("end")) {

                            if (isPageChecked) {
                                spinnerSurah.setSelection(s);
                                spinnerJuz.setSelection((int) Math.min(((Integer.parseInt(spinnerPage.getSelectedItem().toString()) - 2) / 20), 29));
                            }
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
//        System.exit(0);
    }

    private String JsonDataFromAsset(String fileName) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open(fileName);
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.juz_radio_button:
                if (checked) {
                    isJuzChecked = true;
                    isSurahChecked = false;
                    isPageChecked = false;
                    spinnerJuz.setEnabled(true);
                    spinnerPage.setEnabled(false);
                    spinnerSurah.setEnabled(false);
                    // Make spinner arrows in teal_200 color.
//                    spinnerSurah.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);
//                    spinnerJuz.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                    spinnerJuz.setBackgroundResource(R.drawable.spinner_border);
                    spinnerSurah.setBackgroundResource(R.drawable.rounded_button);
                    spinnerPage.setBackgroundResource(R.drawable.rounded_button);
//                    spinnerPage.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);

                }
                break;
            case R.id.surah_radio_button:
                if (checked) {
                    isJuzChecked = false;
                    isSurahChecked = true;
                    isPageChecked = false;
                    spinnerJuz.setEnabled(false);
                    spinnerPage.setEnabled(false);
                    spinnerSurah.setEnabled(true);
                    // Make spinner arrows in teal_200 color.
//                    spinnerSurah.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                    spinnerSurah.setBackgroundResource(R.drawable.spinner_border);
                    spinnerJuz.setBackgroundResource(R.drawable.rounded_button);
                    spinnerPage.setBackgroundResource(R.drawable.rounded_button);
//                    spinnerJuz.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);
//                    spinnerPage.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);
                }
                break;
            case R.id.page_radio_button:
                if (checked) {
                    isJuzChecked = false;
                    isSurahChecked = false;
                    isPageChecked = true;
                    spinnerJuz.setEnabled(false);
                    spinnerPage.setEnabled(true);
                    spinnerSurah.setEnabled(false);
                    // Make spinner arrows in teal_200 color.
//                    spinnerSurah.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);
//                    spinnerJuz.getBackground().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_ATOP);
//                    spinnerPage.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                    spinnerPage.setBackgroundResource(R.drawable.spinner_border);
                    spinnerSurah.setBackgroundResource(R.drawable.rounded_button);
                    spinnerJuz.setBackgroundResource(R.drawable.rounded_button);
                }
                break;
        }
    }
}
