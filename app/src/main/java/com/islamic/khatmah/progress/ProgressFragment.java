package com.islamic.khatmah.progress;

import static android.content.Context.MODE_PRIVATE;
import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;
import java.util.Locale;

public class ProgressFragment extends Fragment {
    private TextView txtWeeklyProgressRatio, txtWeeklyProgressPages;
    private TextView txtTotalPagesProgress, txtTotalPagesProgressRatio;
    private TextView txtTotalPartsProgress, txtTotalPartsProgressRatio;
    private TextView khatmahCounter;
    private ProgressBar weeklyProgressBar, totalPagesProgressBar, totalPartsProgressBar;
    private int pagesPerDay, weeklyProgress, totalProgress, khatmahCounterValue;
    SharedPreferences preferences;

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.progress_fragment, container, false);

        // load shared Preferences
        preferences = getActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        // Define views of weekly progress bar.
        txtWeeklyProgressRatio = view.findViewById(R.id.txtWeeklyProgressRatio);
        txtWeeklyProgressPages = view.findViewById(R.id.txtWeeklyProgressPages);
        weeklyProgressBar = view.findViewById(R.id.weeklyProgressBar);

        // Define views of total progress bar.
        txtTotalPagesProgressRatio = view.findViewById(R.id.txtAllProgressRatio);
        txtTotalPartsProgressRatio = view.findViewById(R.id.txtAllProgressRatio2);
        txtTotalPagesProgress = view.findViewById(R.id.txtAllProgressPages);
        txtTotalPartsProgress = view.findViewById(R.id.txtAllProgressPages2);
        totalPagesProgressBar = view.findViewById(R.id.allProgressBar);
        totalPartsProgressBar = view.findViewById(R.id.allProgressBar2);

        // Define khatmahCounter TextView.
        khatmahCounter = view.findViewById(R.id.khatmah_counter);

        // load shared Preferences
        preferences = getActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        // Set progress bar Maximum value.
        totalPagesProgressBar.setMax(604);
        totalPartsProgressBar.setMax(30);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();

        weeklyProgress = preferences.getInt(Constant.WEEKLY_PROGRESS, 0);
        totalProgress = preferences.getInt(Constant.TOTAL_PROGRESS, 0);
        pagesPerDay = preferences.getInt(PAGES_PER_DAY, 1);
        weeklyProgressBar.setMax(pagesPerDay * 7);

        // 1st progress bar [weekly target].
        weeklyProgressBar.setProgress(weeklyProgress);
        txtWeeklyProgressPages.setText(weeklyProgressBar.getProgress() + " صفحة");
        int weeklyPercentage = (int) (((float) weeklyProgress) / weeklyProgressBar.getMax() * 100);
        if (weeklyPercentage < 100) {
            txtWeeklyProgressRatio.setText(weeklyPercentage + " % ");
        } else
            txtWeeklyProgressRatio.setText(100 + " % ");

        // 2nd progress bar [no. of read pages].
        totalPagesProgressBar.setProgress(totalProgress);
        txtTotalPagesProgress.setText(totalPagesProgressBar.getProgress() + " صفحة");
        int totalPagesPercentage = (int) (((float) totalPagesProgressBar.getProgress()) / totalPagesProgressBar.getMax() * 100);
        if (totalPagesPercentage < 100)
            txtTotalPagesProgressRatio.setText(totalPagesPercentage + " % ");
        else
            txtTotalPagesProgressRatio.setText(100 + " % ");

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat) nf;
        formatter.applyPattern("##.#");

        // 3rd progress bar [no. of read parts].
        totalPartsProgressBar.setProgress(totalProgress / 20);
        txtTotalPartsProgress.setText(formatter.format(totalPagesProgressBar.getProgress() / 20.0) + " جزء");
        txtTotalPartsProgressRatio.setText(formatter.format(((float) totalPartsProgressBar.getProgress()) / totalPartsProgressBar.getMax() * 100) + " % ");

        // Set no. of khatmah(s).
        khatmahCounterValue = preferences.getInt(Constant.KHATMAH_COUNTER, 0);

        khatmahCounter.setText(khatmahCounterValue+"");

        if(totalPagesProgressBar.getProgress() == totalPagesProgressBar.getMax()){
            resetTotalPagesProgress();
        }
    }

    private void resetTotalPagesProgress(){
        preferences.edit().putInt(Constant.TOTAL_PROGRESS, 0).apply();
        new MaterialAlertDialogBuilder(getContext(), R.style.Theme_MyApp_Dialog_Alert)
                .setMessage("لقد أتممت ختمتك، بارك الله فيك وجعلك ممن يُقَال لهم \"اقْرَأْ وَارْتَقِ وَرَتِّلْ ، كَمَا كُنْتَ تُرَتِّلُ فِي الدُّنْيَا ، فَإِنَّ مَنْزِلَكَ عِنْدَ آخِرِ آيَةٍ تَقْرَؤُهَا.\"")
                .setPositiveButton("إلى الختمة الجديدة", (dialog, which) -> {
                    totalPagesProgressBar.setProgress(totalProgress);
                    totalPartsProgressBar.setProgress(totalProgress / 20);
                    khatmahCounterValue++;
                    preferences.edit().putInt(Constant.KHATMAH_COUNTER, khatmahCounterValue).apply();
                }).show();
    }
}