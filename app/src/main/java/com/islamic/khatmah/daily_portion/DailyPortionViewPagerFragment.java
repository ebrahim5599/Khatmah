package com.islamic.khatmah.daily_portion;

import static com.islamic.khatmah.constants.Constant.CURRENT_JUZ;
import static com.islamic.khatmah.constants.Constant.CURRENT_PAGE;
import static com.islamic.khatmah.constants.Constant.CURRENT_SURAH;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islamic.khatmah.ui.main.MainActivity;
import com.islamic.khatmah.constants.Constant;
import com.islamic.khatmah.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class DailyPortionViewPagerFragment extends Fragment {

    private static final String ARG_POSITION = "param1";
    private static final String ARG_CURRENT_PAGE_NUM = "param2";
    private static final String ARG_IS_CHECKED_ARR = "param3";
    private static final String ARG_PAGE_PER_DAY = "param4";
    private LinearLayout layout;
    private ImageButton checkButton;
    private TextView counter_text, juz_number, surah_name, page_number;
    private int juz;

    private static int counter = 0;
    private static boolean[] isChecked;
    private int position;
    private int currentPageNum;
    private int pagesPerDay;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private static ViewPager2 viewPager;
    private int weeklyProgress, totalProgress;


    public static DailyPortionViewPagerFragment newInstance(int position, int currentPageNum, int pagesPerDay, boolean[] isChecked, ViewPager2 viewPager2) {
        DailyPortionViewPagerFragment fragment = new DailyPortionViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_CURRENT_PAGE_NUM, currentPageNum);
        args.putBooleanArray(ARG_IS_CHECKED_ARR, isChecked);
        args.putInt(ARG_PAGE_PER_DAY, pagesPerDay);
        fragment.setArguments(args);
        viewPager = viewPager2;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            currentPageNum = getArguments().getInt(ARG_CURRENT_PAGE_NUM);
            isChecked = getArguments().getBooleanArray(ARG_IS_CHECKED_ARR);
            pagesPerDay = getArguments().getInt(ARG_PAGE_PER_DAY);

            counter = sharedPreferences.getInt(Constant.DAILY_PROGRESS, 0);

            weeklyProgress = sharedPreferences.getInt(Constant.WEEKLY_PROGRESS, 0);
            totalProgress = sharedPreferences.getInt(Constant.TOTAL_PROGRESS, 0);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setProgress(counter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_portion_view_pager_fragment, container, false);

        // Create references for views.
        juz_number = view.findViewById(R.id.juz_number_daily_portion);
        surah_name = view.findViewById(R.id.surah_name_daily_portion);
        page_number = view.findViewById(R.id.page_number_daily_portion);


        ImageView img = view.findViewById(R.id.img);
        layout = view.findViewById(R.id.read_linear);
        counter_text = view.findViewById(R.id.counter_text);
        checkButton = view.findViewById(R.id.read);
        int resources = isChecked[position] ? R.drawable.checked : R.drawable.unchecked;
        progressBar = view.findViewById(R.id.progress);
        checkButton.setImageResource(resources);
//        counter_text.setText(String.valueOf(counter));
        progressBar.setMax(pagesPerDay);
        progressBar.setProgress(counter);
        setImage(position + currentPageNum, img);


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        weeklyProgress = sharedPreferences.getInt(Constant.WEEKLY_PROGRESS, 0);
        totalProgress = sharedPreferences.getInt(Constant.TOTAL_PROGRESS, 0);

        checkButton.setOnClickListener(view12 -> {
            totalProgress = sharedPreferences.getInt(Constant.TOTAL_PROGRESS, 0);
            weeklyProgress = sharedPreferences.getInt(Constant.WEEKLY_PROGRESS, 0);
            if (isChecked[position]) {
                counter--;
                isChecked[position] = false;
                weeklyProgress--;
                totalProgress--;
                checkButton.setImageResource(R.drawable.unchecked);
            } else {
                counter++;
                isChecked[position] = true;
                weeklyProgress++;
                totalProgress++;
                checkButton.setImageResource(R.drawable.checked);
                viewPager.setCurrentItem(position + 1);
                if (counter >= pagesPerDay) {
                    new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
                            .setMessage(R.string.daily_portion_completion_message)
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                // Save the last page, Surah and Juz in SharedPreference.
                                // [CURRENT_PAGE + number of PAGES_PER_DAY].
                                if (pagesPerDay + currentPageNum > 604)
                                    editor.putInt(CURRENT_PAGE, pagesPerDay + currentPageNum - 604);
                                else
                                    editor.putInt(CURRENT_PAGE, pagesPerDay + currentPageNum);
                                editor.putInt(Constant.DAILY_PROGRESS, 0);
                                editor.putBoolean(Constant.FINISH_DAILY_PROGRESS,true);
                                editor.apply();
                                resetValues();
                                requireActivity().finish();
                            }).show();
                }
            }
            editor.putInt(Constant.WEEKLY_PROGRESS, weeklyProgress);
            editor.putInt(Constant.DAILY_PROGRESS, counter);
            editor.putInt(Constant.TOTAL_PROGRESS, totalProgress);
            editor.apply();

            storeArray(isChecked, Constant.ARRAY_NAME, requireContext());
//            counter_text.setText(String.valueOf(counter));
            progressBar.setProgress(counter);
//            Toast.makeText(getContext(), String.valueOf(weeklyProgress), Toast.LENGTH_SHORT).show();
        });


        view.setOnClickListener(view1 -> {
            if (layout.getVisibility() == View.GONE) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void setImage(int picNum, ImageView img) {


        InputStream is;
        int currentPic = picNum;
        if (picNum > 604) {
            currentPic = picNum - 604;
        }



        juz = Math.min(((currentPic - 2) / 20) + 1, 30);
        juz_number.setText(String.format("الجزء %s", convertToArbNum(juz)));
        surah_name.setText(MainActivity.surahName.get(currentPic - 1));
        page_number.setText(String.format("صفحة  %s", convertToArbNum(currentPic)));
        sharedPreferences.edit().putString(CURRENT_JUZ, String.format("الجزء %s", convertToArbNum(juz))).apply();
        sharedPreferences.edit().putString(CURRENT_SURAH, MainActivity.surahName.get(currentPic - 1)).apply();



        try {
            if (getActivity() != null) {
                is = getActivity().openFileInput(String.valueOf(currentPic));
                Bitmap bit = BitmapFactory.decodeStream(is);
                img.setImageBitmap(bit);
            }
        } catch (FileNotFoundException e) {
            Picasso.get().load("https://quran-images-api.herokuapp.com/show/page/" + (currentPic)).into(img);
        }

    }

    public void storeArray(boolean[] array, String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(Constant.MAIN_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);

        editor.apply();
    }

    // this method converts English numbers to Indian number [Arabic].
    private String convertToArbNum(int number) {

        String stNum = String.valueOf(number);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < stNum.length(); i++) {
            char num = stNum.charAt(i);
            int ArabicNum = num + 1584;
            result.append((char) ArabicNum);
        }
        return result.toString();
    }

    private void resetValues() {
        boolean[] arr = new boolean[pagesPerDay];
        storeArray(arr, Constant.ARRAY_NAME, requireContext());
    }


}