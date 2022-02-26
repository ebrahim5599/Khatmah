package com.islamic.khatmah.daily_portion;

import static android.content.Context.MODE_PRIVATE;
import static com.islamic.khatmah.constants.Constant.CURRENT_JUZ;
import static com.islamic.khatmah.constants.Constant.CURRENT_PAGE;
import static com.islamic.khatmah.constants.Constant.CURRENT_SURAH;
import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;
import java.util.Calendar;

public class DailyPortionFragment extends Fragment {

    private TextView page_number, surah_name, juz_number, number_of_pages, percentage, hadithTextView, close;
    CardView finishProgressHint;
    private ProgressBar progressBar_daily;
    private int current_page, pages_per_day;
    private String current_surah, current_juz;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RelativeLayout dailyProgressRelative;

    public static DailyPortionFragment newInstance() {
        return new DailyPortionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        mViewModel = new ViewModelProvider(this).get(DailyPortionViewModel.class);
        View view = inflater.inflate(R.layout.daily_portion_fragment, container, false);
        finishProgressHint = view.findViewById(R.id.finish_progress_hint);
        preferences = requireActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        // Define TextViews...
        page_number = view.findViewById(R.id.page_number);
        surah_name = view.findViewById(R.id.surah_name);
        juz_number = view.findViewById(R.id.juz_number);
        number_of_pages = view.findViewById(R.id.number_of_pages);
        percentage = view.findViewById(R.id.precentage);
        hadithTextView = view.findViewById(R.id.daily_hadith_container);
        close = view.findViewById(R.id.close);
        dailyProgressRelative = view.findViewById(R.id.daily_progress_relative);
        hadithTextView.setText("عن عائشة رضي اللَّه عنها قالَتْ: قالَ رسولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: " + "\"الَّذِي يَقْرَأُ القُرْآنَ وَهُو ماهِرٌ بِهِ معَ السَّفَرةِ الكِرَامِ البَرَرَةِ، وَالَّذِي يقرَأُ القُرْآنَ ويَتَتَعْتَعُ فِيهِ وَهُو عليهِ شَاقٌّ لَهُ أَجْران متفقٌ عَلَيْه.\"");

        // Define progressBar [Daily].
        progressBar_daily = view.findViewById(R.id.progressBar_daily);

        // Load data from SharedPreferences.
        current_page = preferences.getInt(CURRENT_PAGE, 1);
        current_surah = preferences.getString(CURRENT_SURAH, "سورة الفاتحة");
        current_juz = preferences.getString(CURRENT_JUZ, "الجزء الأول");
        pages_per_day = preferences.getInt(PAGES_PER_DAY, 1);

        TextView start_btn = view.findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getActivity(), DailyPortionActivity.class);
                n.putExtra("PAGE_NUMBER", Integer.parseInt(page_number.getText().toString()));
                startActivity(n);
            }
        });

        // To close "لقد أتممت الورد اليوم" CardView.
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(Constant.FINISH_DAILY_PROGRESS, false).commit();
                finishProgressHint.setVisibility(View.GONE);
            }
        });
        return view;
    }

    //
    @Override
    public void onResume() {
        super.onResume();

        // Load data from SharedPreferences.
        current_page = preferences.getInt(CURRENT_PAGE, 1);
        current_surah = preferences.getString(CURRENT_SURAH, "سورة الفاتحة");
        current_juz = preferences.getString(CURRENT_JUZ, "الجزء الأول");
        pages_per_day = preferences.getInt(PAGES_PER_DAY, 1);

        // TextViews setText().
        page_number.setText(convertToArbNum(current_page));
        surah_name.setText(current_surah);
        juz_number.setText(current_juz);
        number_of_pages.setText(convertToArbNum(pages_per_day));
        progressBar_daily.setMax(preferences.getInt(PAGES_PER_DAY, 0));
        if (!preferences.getBoolean(Constant.FINISH_DAILY_PROGRESS, false)) {
            finishProgressHint.setVisibility(View.GONE);
        } else {
            finishProgressHint.setVisibility(View.VISIBLE);
        }

        if (pages_per_day == 1)
            percentage.setVisibility(View.INVISIBLE);
        else
            percentage.setVisibility(View.VISIBLE);

        int daily_progress = preferences.getInt(Constant.DAILY_PROGRESS, 0);
        percentage.setText(String.format("%%%s", convertToArbNum((int) (daily_progress * 100 / (double) pages_per_day))));

        if (daily_progress == 0) {
            progressBar_daily.setProgress(1);
            progressBar_daily.setProgress(0);
        } else {
            progressBar_daily.setProgress(daily_progress);
        }
    }

    // this method converts English numbers to Indian number [Arabic].
    private String convertToArbNum(int number) {

        String stNum = String.valueOf(number);
        String result = "";

        for (int i = 0; i < stNum.length(); i++) {
            char num = stNum.charAt(i);
            int ArabicNum = num + 1584;
            result += (char) ArabicNum;
        }
        return result;
    }

}
