package com.islamic.khatmah.progress;

import static android.content.Context.MODE_PRIVATE;

import static com.islamic.khatmah.constants.Constant.PAGES_PER_DAY;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;

import java.util.Locale;

public class ProgressFragment extends Fragment {
    private TextView txtWeeklyProgressRatio, txtWeeklyProgressPages;
    private TextView txtTotalPagesProgress, txtTotalPagesProgressRatio;
    private TextView txtTotalPartsProgress, txtTotalPartsProgressRatio;
    private ProgressBar weeklyProgressBar, totalPagesProgressBar, totalPartsProgressBar;
    private int pagesPerDay , weeklyProgress, totalProgress;

    SharedPreferences.Editor editor;
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

        // load shared Preferences
        preferences = getActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        // Set progress bar Maximum value.
        totalPagesProgressBar.setMax(604);
        totalPartsProgressBar.setMax(30);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();

        weeklyProgress = preferences.getInt(Constant.WEEKLY_PROGRESS, 0);
        totalProgress  = preferences.getInt(Constant.TOTAL_PROGRESS, 0);

        pagesPerDay = preferences.getInt(PAGES_PER_DAY, 1);
        weeklyProgressBar.setMax(pagesPerDay * 7);
        // 1st progress bar [weekly target].
        weeklyProgressBar.setProgress(weeklyProgress);
        txtWeeklyProgressRatio.setText((int) (((float) weeklyProgress) / weeklyProgressBar.getMax() * 100) + " % ");
        txtWeeklyProgressPages.setText(weeklyProgressBar.getProgress() + " Pages");

        // 2nd progress bar [no. of read pages].
        totalPagesProgressBar.setProgress(totalProgress);
        txtTotalPagesProgressRatio.setText((int) (((float) totalPagesProgressBar.getProgress()) / totalPagesProgressBar.getMax() * 100) + " % ");
        txtTotalPagesProgress.setText(totalPagesProgressBar.getProgress()+" صفحة");

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat) nf;
        formatter.applyPattern("##.#");

        // 3rd progress bar [no. of read parts].
        totalPartsProgressBar.setProgress(totalProgress/20);
        txtTotalPartsProgressRatio.setText(formatter.format(((float) totalPartsProgressBar.getProgress()) / totalPartsProgressBar.getMax() * 100) + " % ");
        txtTotalPartsProgress.setText(formatter.format(totalPagesProgressBar.getProgress()/20.0)+" جزء");
    }

    @SuppressLint("SetTextI18n")
    private void updateBars(int pages) {
        weeklyProgressBar.incrementProgressBy(pages);
        weeklyProgress += pages;

        editor = preferences.edit();
        editor.putInt("weaklyProgress", weeklyProgressBar.getProgress());
        editor.commit();

        txtWeeklyProgressRatio.setText((int) (((float) weeklyProgressBar.getProgress()) / weeklyProgressBar.getMax() * 100) + " % ");
        txtWeeklyProgressPages.setText(weeklyProgressBar.getProgress() + " Pages");

        if (weeklyProgressBar.getProgress() == weeklyProgressBar.getMax()) {
            txtWeeklyProgressRatio.setText(100 + " %");
            txtWeeklyProgressPages.setText(weeklyProgressBar.getMax() + " Pages");

//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_MyApp_Dialog_Alert);
//            builder.setMessage("أحسنت لقد أتممت الورد الإسبوعي، تهانينا.")
//                    .setPositiveButton(
//                            "حسنا",
//                            (dialog, which) -> {
//                            });
//            AlertDialog dialog = builder.create();
//            dialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
//                    dialog.show();
                    weeklyProgressBar.setProgress(0);
                    txtWeeklyProgressRatio.setText("0 %");
                    txtWeeklyProgressPages.setText("0 Pages");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }, 500);
        }

        if (totalPagesProgressBar.getProgress() == totalPagesProgressBar.getMax()) {
            txtTotalPagesProgressRatio.setText(100 + " %");
            txtTotalPagesProgress.setText(604 + " Pages");
            txtTotalPartsProgressRatio.setText(100 + " %");
            txtTotalPartsProgress.setText(30 + " Parts");

//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setMessage("Congratulations, you have completed the your reading \n Do you want to read a prayer?")
//                    .setPositiveButton(
//                            "OK",
//                            (dialog, i) -> {
////                                getActivity().getSupportFragmentManager().beginTransaction()
////                                        .replace(R.id.frameLayout, new Prayer(), "findThisFragment")
////                                        .addToBackStack(null)
////                                        .commit();
//                                Intent intent = new Intent(getActivity(), PrayerActivity.class);
//                                startActivity(intent);
//
//                            })
//                    .setNegativeButton("No", (dialogInterface, i) -> {
//
//                    });
//            AlertDialog dialog = builder.create();
//            dialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
//                    dialog.show();
                    txtTotalPagesProgressRatio.setText(0 + " %");
                    txtTotalPagesProgress.setText(0 + " Pages");
                    txtTotalPartsProgressRatio.setText(0 + " %");
                    txtTotalPartsProgress.setText(0 + " Parts");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }, 500);
        }
    }
}