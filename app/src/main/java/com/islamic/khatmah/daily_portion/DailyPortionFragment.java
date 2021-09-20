//package com.islamic.khatmah.daily_portion;
//
//import static com.islamic.khatmah.MainActivity.CURRENT_JUZ;
//import static com.islamic.khatmah.MainActivity.CURRENT_PAGE;
//import static com.islamic.khatmah.MainActivity.CURRENT_SURAH;
//import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;
//import static com.islamic.khatmah.MainActivity.sharedPreferences;
//
//import androidx.lifecycle.ViewModelProvider;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.preference.PreferenceManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import com.islamic.khatmah.R;
//
//public class DailyPortionFragment extends Fragment{
//
////    private DailyPortionViewModel mViewModel;
//
//    private TextView page_number, surah_name, juz_number, number_of_pages;
//    private int current_page, pages_per_day;
//    private String current_surah, current_juz;
//    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
//
//    public static DailyPortionFragment newInstance() {
//        return new DailyPortionFragment();
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
////        mViewModel = new ViewModelProvider(this).get(DailyPortionViewModel.class);
//        View view = inflater.inflate(R.layout.daily_portion_fragment, container, false);
//
//        // Define TextViews.
//        page_number = view.findViewById(R.id.page_number);
//        surah_name  = view.findViewById(R.id.surah_name);
//        juz_number  = view.findViewById(R.id.juz_number);
//        number_of_pages = view.findViewById(R.id.number_of_pages);
//
//        Button start_btn = view.findViewById(R.id.start);
//        start_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent n = new Intent(getActivity(), DailyPortionActivity.class);
//                n.putExtra("PAGE_NUMBER",Integer.parseInt(page_number.getText().toString()));
//                startActivity(n);
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Load data from SharedPreferences.
//        current_page = sharedPreferences.getInt(CURRENT_PAGE,1);
//        current_surah = sharedPreferences.getString(CURRENT_SURAH, "سورة الفاتحة");
//        current_juz = sharedPreferences.getString(CURRENT_JUZ,"الجزء الأول");
//        pages_per_day = sharedPreferences.getInt(PAGES_PER_DAY,1);
//
//        // TextViews setText().
//        page_number.setText(current_page+"");
//        surah_name.setText(current_surah);
//        juz_number.setText(current_juz);
//        number_of_pages.setText(pages_per_day+"");
//
//    }
//
//}


 package com.islamic.khatmah.daily_portion;

import static android.content.Context.MODE_PRIVATE;
import static com.islamic.khatmah.MainActivity.CURRENT_JUZ;
import static com.islamic.khatmah.MainActivity.CURRENT_PAGE;
import static com.islamic.khatmah.MainActivity.CURRENT_SURAH;
import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;
import static com.islamic.khatmah.MainActivity.sharedPreferences;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.islamic.khatmah.R;

public class DailyPortionFragment extends Fragment{

//    private DailyPortionViewModel mViewModel;

    private TextView page_number, surah_name, juz_number, number_of_pages, hadithTextView;
    private int current_page, pages_per_day;
    private String current_surah, current_juz;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static DailyPortionFragment newInstance() {
        return new DailyPortionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        mViewModel = new ViewModelProvider(this).get(DailyPortionViewModel.class);
        View view = inflater.inflate(R.layout.daily_portion_fragment, container, false);

        preferences = getActivity().getSharedPreferences("preferences_file", MODE_PRIVATE);
        editor = preferences.edit();

        // Define TextViews...
        page_number = view.findViewById(R.id.page_number);
        surah_name  = view.findViewById(R.id.surah_name);
        juz_number  = view.findViewById(R.id.juz_number);
        number_of_pages = view.findViewById(R.id.number_of_pages);
        hadithTextView = view.findViewById(R.id.daily_hadith_container);
        hadithTextView.setText("عن عائشة رضي اللَّه عنها قالَتْ: قالَ رسولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: "+"\"الَّذِي يَقْرَأُ القُرْآنَ وَهُو ماهِرٌ بِهِ معَ السَّفَرةِ الكِرَامِ البَرَرَةِ، وَالَّذِي يقرَأُ القُرْآنَ ويَتَتَعْتَعُ فِيهِ وَهُو عليهِ شَاقٌّ لَهُ أَجْران متفقٌ عَلَيْه.\"");

        TextView start_btn = view.findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getActivity(), DailyPortionActivity.class);
                n.putExtra("PAGE_NUMBER",Integer.parseInt(page_number.getText().toString()));
                startActivity(n);
            }
        });
        return view;
    }
//
    @Override
    public void onResume() {
        super.onResume();

        // Load data from SharedPreferences.
        current_page = sharedPreferences.getInt(CURRENT_PAGE,1);
        current_surah = sharedPreferences.getString(CURRENT_SURAH, "سورة الفاتحة");
        current_juz = sharedPreferences.getString(CURRENT_JUZ,"الجزء الأول");
        pages_per_day = preferences.getInt(PAGES_PER_DAY,1);

        // TextViews setText().
        page_number.setText(current_page+"");
        surah_name.setText(current_surah);
        juz_number.setText(current_juz);
        number_of_pages.setText(pages_per_day+"");

    }

}
