package com.islamic.khatmah.progress;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.islamic.khatmah.R;

public class ProgressFragment extends Fragment {
    private ProgressViewModel mViewModel;
    private TextView txtAllProgressRatio, txtWeeklyProgressRatio, txtAllProgressRatioParts;
    private ProgressBar weaklyProgressBar, allProgressBar, allProgressBarParts;
    private int pages = 0, weaklyProgress = 0, allProgress = 0;
    Button btnSetCounter, btnResetCounter, btnFinishReading;
    TextView txtCounter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.progress_fragment, container, false);
        txtWeeklyProgressRatio = view.findViewById(R.id.txtWeeklyProgressRatio);
        txtAllProgressRatio = view.findViewById(R.id.txtAllProgressRatio);
        txtAllProgressRatioParts = view.findViewById(R.id.txtAllProgressRatio2);
        weaklyProgressBar = view.findViewById(R.id.weeklyProgressBar);
        allProgressBar = view.findViewById(R.id.allProgressBar);
        allProgressBarParts = view.findViewById(R.id.allProgressBar2);
        txtCounter = view.findViewById(R.id.edtTextCounter);

        ///// load shared Preferences
        preferences = getActivity().getSharedPreferences("preferences_file", MODE_PRIVATE);
//        editor = preferences.edit();
//        pref = getActivity().getPreferences(MODE_PRIVATE);
//
//        pages = pref.getInt("pages",0);
//        SharedPreferences preferences = getContext().getSharedPreferences("pref",getContext().MODE_PRIVATE);
//        pages = preferences.getInt("pages", 0);
        pages = preferences.getInt("PAGES_PER_DAY",1);

        setMaxBars(pages);
        txtCounter.setText(String.valueOf(pages));
        weaklyProgressBar.setProgress(pref.getInt("weaklyProgress", 0));
        allProgressBar.setProgress(pref.getInt("allProgress", 0));
        allProgressBarParts.setProgress(pref.getInt("allProgress", 0) / 20);
        txtWeeklyProgressRatio.setText(String.valueOf(weaklyProgressBar.getProgress()) + " pages \n \n" + String.valueOf((int) (((float) weaklyProgressBar.getProgress()) / weaklyProgressBar.getMax() * 100)) + " %");
        txtAllProgressRatio.setText(String.valueOf(allProgressBar.getProgress()) + " pages \n \n" + String.valueOf((int) (((float) allProgressBar.getProgress()) / allProgressBar.getMax() * 100)) + " %");
        txtAllProgressRatioParts.setText(String.valueOf((allProgressBar.getProgress() / 20)) + " parts \n \n" + String.valueOf((int) (((allProgressBar.getProgress() / 20) / 30.0) * 100)) + " %");

        /////
//        btnSetCounter = view.findViewById(R.id.btnSetCounter);
//        btnSetCounter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pages = Integer.parseInt(String.valueOf(edtTextCounter.getText()));
//                setMaxBars(pages);
//                pref = getActivity().getPreferences(MODE_PRIVATE);
//                editor = pref.edit();
//                editor.putInt("pages", pages);
//                editor.commit();
//            }
//        });
        btnResetCounter = view.findViewById(R.id.btnResetConter);
        btnResetCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pages = 0;
                txtCounter.setText("1");
                resetBars();
                preferences = getActivity().getSharedPreferences("preferences_file",MODE_PRIVATE);
                editor = preferences.edit();
                editor.putInt("pages", pages);
                editor.commit();
            }
        });
        btnFinishReading = view.findViewById(R.id.btnFinishReding);
        btnFinishReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBars(pages);
            }
        });
        if (savedInstanceState != null) {
            super.onViewStateRestored(savedInstanceState);
            pages = savedInstanceState.getInt("pages");
            weaklyProgress = savedInstanceState.getInt("weaklyProgress");
            allProgress = savedInstanceState.getInt("allProgress");
        }
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
        // TODO: Use the ViewModel
    }

    private void setMaxBars(int pages) {
        weaklyProgressBar.setMax(pages * 7);
        allProgressBar.setMax(604);
        allProgressBarParts.setMax(30);
    }

    private void updateBars(int pages) {
//        weaklyProgressBar.setProgress(weaklyPrograss+pages);
//        allProgressBar.setProgress(allPrograss+pages);
        weaklyProgressBar.incrementProgressBy(pages);
        allProgressBar.incrementProgressBy(pages);
        allProgressBarParts.setProgress((pref.getInt("allProgress", 0) + pages) / 20);
        weaklyProgress += pages;
        allProgress += pages;
        pref = getActivity().getPreferences(MODE_PRIVATE);
        editor = pref.edit();
        editor.putInt("weaklyProgress", weaklyProgressBar.getProgress());
        editor.putInt("allProgress", allProgressBar.getProgress());
        editor.commit();

        txtWeeklyProgressRatio.setText(String.valueOf(weaklyProgressBar.getProgress())+" pages \n \n"+String.valueOf((int)(((float) weaklyProgressBar.getProgress())/weaklyProgressBar.getMax()*100))+ " %");
        txtAllProgressRatio.setText(String.valueOf(allProgressBar.getProgress())+" pages \n \n"+String.valueOf((int) (((float) allProgressBar.getProgress())/allProgressBar.getMax()*100))+ " %");
        txtAllProgressRatioParts.setText(String.valueOf((allProgressBar.getProgress()/20))+" parts \n \n"+String.valueOf((int) (((allProgressBar.getProgress()/20)/30.0)*100))+ " %");
        if (weaklyProgressBar.getProgress()==weaklyProgressBar.getMax()){
            txtWeeklyProgressRatio.setText(String.valueOf(weaklyProgressBar.getMax())+" pages \n \n"+String.valueOf(100)+ " %");
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
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }, 500);


        }

        if (allProgressBar.getProgress()==allProgressBar.getMax()){
            txtAllProgressRatio.setText(String.valueOf(604)+" pages \n \n"+String.valueOf(100)+ " %");
            txtAllProgressRatioParts.setText(String.valueOf(30)+" parts \n \n"+String.valueOf(100)+ " %");
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Congratulations, you have completed the your reading \n Do you want to read a prayer?")
                    .setPositiveButton(
                            "OK",
                            (dialog, i) -> {
//                                getActivity().getSupportFragmentManager().beginTransaction()
//                                        .replace(R.id.frameLayout, new Prayer(), "findThisFragment")
//                                        .addToBackStack(null)
//                                        .commit();
                                Intent intent = new Intent(getActivity(), PrayerActivity.class);
                                startActivity(intent);

                            })
                    .setNegativeButton("No", (dialogInterface, i) -> {

                    });
            AlertDialog dialog = builder.create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogAnimation;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.show();
                    allProgressBar.setProgress(0);
                    txtAllProgressRatio.setText("0 %");
                    allProgressBarParts.setProgress(0);
                    txtAllProgressRatioParts.setText("0 %");
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }, 500);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pages", pages);
        outState.putInt("weaklyProgress", weaklyProgress);
        outState.putInt("allProgress", allProgress);
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        pages = savedInstanceState.getInt("pages");
//        weaklyProgress = savedInstanceState.getInt("weaklyProgress");
//        allProgress = savedInstanceState.getInt("allProgress");
//    }

    private void resetBars() {
//        weaklyProgressBar.setProgress(weaklyPrograss+pages);
//        allProgressBar.setProgress(allPrograss+pages);
        weaklyProgressBar.setProgress(0);
        allProgressBar.setProgress(0);
        allProgressBarParts.setProgress(0);
        txtWeeklyProgressRatio.setText("0 %");
        txtAllProgressRatio.setText("0 %");
        txtAllProgressRatioParts.setText("0 %");
    }
}