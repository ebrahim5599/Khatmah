package com.islamic.khatmah.progress;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.islamic.khatmah.MainActivity;
import com.islamic.khatmah.R;
import com.islamic.khatmah.constants.Constant;

import java.util.Locale;

public class ProgressFragment extends Fragment {
    private TextView txtWeeklyProgressRatio, txtWeeklyProgressPages;
    private TextView txtTotalPagesProgress, txtTotalPagesProgressRatio;
    private TextView txtTotalPartsProgress, txtTotalPartsProgressRatio;
    private ProgressBar weaklyProgressBar, totalPagesProgressBar, totalPartsProgressBar;
    private int pagesPerDay = 0, weaklyProgress = 15, totalProgress = 40;
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

        // Define views of weekly progress bar.
        txtWeeklyProgressRatio = view.findViewById(R.id.txtWeeklyProgressRatio);
        txtWeeklyProgressPages = view.findViewById(R.id.txtWeeklyProgressPages);
        weaklyProgressBar = view.findViewById(R.id.weeklyProgressBar);

        // Define views of total progress bar.
        txtTotalPagesProgressRatio = view.findViewById(R.id.txtAllProgressRatio);
        txtTotalPartsProgressRatio = view.findViewById(R.id.txtAllProgressRatio2);
        txtTotalPagesProgress = view.findViewById(R.id.txtAllProgressPages);
        txtTotalPartsProgress = view.findViewById(R.id.txtAllProgressPages2);
        totalPagesProgressBar = view.findViewById(R.id.allProgressBar);
        totalPartsProgressBar = view.findViewById(R.id.allProgressBar2);

        // load shared Preferences
        preferences = getActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
        pagesPerDay = preferences.getInt(MainActivity.PAGES_PER_DAY, 1);

        // Set progress bar Maximum value.
        weaklyProgressBar.setMax(pagesPerDay * 7);
        totalPagesProgressBar.setMax(604);
        totalPartsProgressBar.setMax(30);

//        if (savedInstanceState != null) {
//            super.onViewStateRestored(savedInstanceState);
//            pagesPerDay = savedInstanceState.getInt("pages");
//            weaklyProgress = savedInstanceState.getInt("weaklyProgress");
//        }
        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();

        // 1st progress bar [weekly target].
        weaklyProgressBar.setProgress(weaklyProgress);
        txtWeeklyProgressRatio.setText((int) (((float) weaklyProgressBar.getProgress()) / weaklyProgressBar.getMax() * 100) + " % ");
        txtWeeklyProgressPages.setText(weaklyProgressBar.getProgress() + " Pages");

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

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("pages", pagesPerDay);
//        outState.putInt("weaklyProgress", weaklyProgress);
//    }

    @SuppressLint("SetTextI18n")
    private void updateBars(int pages) {
        weaklyProgressBar.incrementProgressBy(pages);
        weaklyProgress += pages;

        editor = preferences.edit();
        editor.putInt("weaklyProgress", weaklyProgressBar.getProgress());
        editor.commit();

        txtWeeklyProgressRatio.setText((int) (((float) weaklyProgressBar.getProgress()) / weaklyProgressBar.getMax() * 100) + " % ");
        txtWeeklyProgressPages.setText(weaklyProgressBar.getProgress() + " Pages");
        if (weaklyProgressBar.getProgress() == weaklyProgressBar.getMax()) {
            txtWeeklyProgressRatio.setText(100 + " %");
            txtWeeklyProgressPages.setText(weaklyProgressBar.getMax() + " Pages");
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Congratulations, you have completed the weekly reading")
                    .setPositiveButton(
                            "OK",
                            (dialog, which) -> {
                            });
            AlertDialog dialog = builder.create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.show();
                    weaklyProgressBar.setProgress(0);
                    txtWeeklyProgressRatio.setText("0 %");
                    txtWeeklyProgressPages.setText("0 Pages");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }, 500);


        }
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
}
/*

    private void setProgressBar(int value){
        weaklyProgressBar.setProgress(value);

        editor = preferences.edit();
        editor.putInt("weaklyProgress", weaklyProgressBar.getProgress());
        editor.commit();

        txtWeeklyProgressRatio.setText((int) (((float) weaklyProgressBar.getProgress()) / weaklyProgressBar.getMax() * 100) + " % ");
        txtWeeklyProgressPages.setText(weaklyProgressBar.getProgress() + " Pages");

        if (weaklyProgressBar.getProgress() == weaklyProgressBar.getMax()) {
            txtWeeklyProgressRatio.setText(100 + " %");
            txtWeeklyProgressPages.setText(weaklyProgressBar.getMax() + " Pages");
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Congratulations, you have completed the weekly reading")
                    .setPositiveButton(
                            "OK",
                            (dialog, which) -> {
                            });
            AlertDialog dialog = builder.create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.show();
                    weaklyProgressBar.setProgress(0);
                    txtWeeklyProgressRatio.setText("0 %");
                    txtWeeklyProgressPages.setText("0 Pages");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }, 500);


        }
    }

 */











//package com.islamic.khatmah.progress;
//
//        import static android.content.Context.MODE_PRIVATE;
//
//        import android.annotation.SuppressLint;
//        import android.app.AlertDialog;
//        import android.content.Intent;
//        import android.content.SharedPreferences;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.preference.PreferenceManager;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.view.WindowManager;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.ProgressBar;
//        import android.widget.TextView;
//
//        import androidx.annotation.NonNull;
//        import androidx.annotation.Nullable;
//        import androidx.fragment.app.Fragment;
//        import androidx.lifecycle.ViewModelProvider;
//        import androidx.viewpager2.widget.ViewPager2;
//
//        import com.islamic.khatmah.R;
//        import com.islamic.khatmah.constants.Constant;
//
//public class ProgressFragment extends Fragment {
//    private ProgressViewModel mViewModel;
//    private TextView txtAllProgressRatio, txtWeeklyProgressRatio, txtWeeklyProgressPages, txtAllProgressRatioParts, txtAllProgressPages1, txtAllProgressPages2;
//    private ProgressBar weaklyProgressBar, allProgressBar, allProgressBarParts;
//    private int pages = 0, weaklyProgress = 0, allProgress = 0, read_pages = 0;
//    Button btnSetCounter, btnResetCounter, btnFinishReading;
//    SharedPreferences.Editor editor;
//    SharedPreferences preferences;
//
//    public static ProgressFragment newInstance() {
//        return new ProgressFragment();
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.progress_fragment, container, false);
//
//        txtWeeklyProgressRatio = view.findViewById(R.id.txtWeeklyProgressRatio);
//        txtWeeklyProgressPages = view.findViewById(R.id.txtWeeklyProgressPages);
//        txtAllProgressRatio = view.findViewById(R.id.txtAllProgressRatio);
//        txtAllProgressRatioParts = view.findViewById(R.id.txtAllProgressRatio2);
//        txtAllProgressPages1 = view.findViewById(R.id.txtAllProgressPages);
//        txtAllProgressPages2 = view.findViewById(R.id.txtAllProgressPages2);
//        weaklyProgressBar = view.findViewById(R.id.weeklyProgressBar);
//        allProgressBar = view.findViewById(R.id.allProgressBar);
//        allProgressBarParts = view.findViewById(R.id.allProgressBar2);
////        txtCounter = view.findViewById(R.id.edtTextCounter);
//
//        ///// load shared Preferences
//        preferences = getActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
//        pages = preferences.getInt("PAGES_PER_DAY", 1);
//
//        setMaxBars(pages);
////        txtCounter.setText(String.valueOf(pages));
//        weaklyProgressBar.setProgress(preferences.getInt("weaklyProgress", 0));
//        allProgressBar.setProgress(preferences.getInt("allProgress", 0));
//        allProgressBarParts.setProgress(preferences.getInt("allProgress", 0) / 20);
//        txtWeeklyProgressRatio.setText((int) (((float) weaklyProgressBar.getProgress()) / weaklyProgressBar.getMax() * 100) + " % ");
//        txtWeeklyProgressPages.setText(weaklyProgressBar.getProgress() + " Pages");
//        txtAllProgressRatio.setText((int) (((float) allProgressBar.getProgress()) / allProgressBar.getMax() * 100) + " %");
//        txtAllProgressPages1.setText(allProgressBar.getProgress() + " Pages");
//        txtAllProgressRatioParts.setText((int) (((allProgressBar.getProgress() / 20) / 30.0) * 100) + " %");
//        txtAllProgressPages2.setText(allProgressBar.getProgress() / 20 + " Parts");
//
//        btnResetCounter = view.findViewById(R.id.btnResetConter);
//        btnResetCounter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pages = 0;
//                resetBars();
//                preferences = getActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, MODE_PRIVATE);
//                editor = preferences.edit();
//                editor.putInt("pages", pages);
//                editor.commit();
//            }
//        });
//        btnFinishReading = view.findViewById(R.id.btnFinishReding);
//        btnFinishReading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateBars(pages);
//            }
//        });
//        if (savedInstanceState != null) {
//            super.onViewStateRestored(savedInstanceState);
//            pages = savedInstanceState.getInt("pages");
//            weaklyProgress = savedInstanceState.getInt("weaklyProgress");
//            allProgress = savedInstanceState.getInt("allProgress");
//        }
//        return view;
//
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
//        // TODO: Use the ViewModel
//    }
//
//    private void setMaxBars(int pages) {
//        weaklyProgressBar.setMax(pages * 7);
//        allProgressBar.setMax(604);
//        allProgressBarParts.setMax(30);
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("pages", pages);
//        outState.putInt("weaklyProgress", weaklyProgress);
//        outState.putInt("allProgress", allProgress);
//    }
//
////    @Override
////    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
////        super.onViewStateRestored(savedInstanceState);
////        pages = savedInstanceState.getInt("pages");
////        weaklyProgress = savedInstanceState.getInt("weaklyProgress");
////        allProgress = savedInstanceState.getInt("allProgress");
////    }
//
//    private void resetBars() {
////        weaklyProgressBar.setProgress(weaklyPrograss+pages);
////        allProgressBar.setProgress(allPrograss+pages);
//        weaklyProgressBar.setProgress(0);
//        allProgressBar.setProgress(0);
//        allProgressBarParts.setProgress(0);
//        txtWeeklyProgressRatio.setText("0 %");
//        txtWeeklyProgressPages.setText("0 Pages");
//        txtAllProgressRatio.setText("0 %");
//        txtAllProgressRatioParts.setText("0 %");
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void updateBars(int pages) {
//        weaklyProgressBar.incrementProgressBy(pages);
//        allProgressBar.incrementProgressBy(pages);
//        allProgressBarParts.setProgress((preferences.getInt("allProgress", 0) + pages) / 20);
//        weaklyProgress += pages;
//        allProgress += pages;
//
//        editor = preferences.edit();
//        editor.putInt("weaklyProgress", weaklyProgressBar.getProgress());
//        editor.putInt("allProgress", allProgressBar.getProgress());
//        editor.commit();
//
//        txtWeeklyProgressRatio.setText((int) (((float) weaklyProgressBar.getProgress()) / weaklyProgressBar.getMax() * 100) + " % ");
//        txtWeeklyProgressPages.setText(weaklyProgressBar.getProgress() + " Pages");
//        txtAllProgressRatio.setText((int) (((float) allProgressBar.getProgress()) / allProgressBar.getMax() * 100) + " %");
//        txtAllProgressPages1.setText(allProgressBar.getProgress() + " Pages");
//        txtAllProgressRatioParts.setText((int) (((allProgressBar.getProgress() / 20) / 30.0) * 100) + " %");
//        txtAllProgressPages2.setText(allProgressBar.getProgress() / 20 + " Parts");
//        if (weaklyProgressBar.getProgress() == weaklyProgressBar.getMax()) {
//            txtWeeklyProgressRatio.setText(100 + " %");
//            txtWeeklyProgressPages.setText(weaklyProgressBar.getMax() + " Pages");
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setMessage("Congratulations, you have completed the weekly reading")
//                    .setPositiveButton(
//                            "OK",
//                            (dialog, which) -> {
//                            });
//            AlertDialog dialog = builder.create();
//            dialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    dialog.show();
//                    weaklyProgressBar.setProgress(0);
//                    txtWeeklyProgressRatio.setText("0 %");
//                    txtWeeklyProgressPages.setText("0 Pages");
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                }
//            }, 500);
//
//
//        }
//
//        if (allProgressBar.getProgress() == allProgressBar.getMax()) {
//            txtAllProgressRatio.setText(100 + " %");
//            txtAllProgressPages1.setText(604 + " Pages");
//            txtAllProgressRatioParts.setText(100 + " %");
//            txtAllProgressPages2.setText(30 + " Parts");
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
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    dialog.show();
//                    allProgressBar.setProgress(0);
//                    txtAllProgressRatio.setText("0 %");
//                    allProgressBarParts.setProgress(0);
//                    txtAllProgressRatioParts.setText("0 %");
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                }
//            }, 500);
//        }
//
//    }
//}