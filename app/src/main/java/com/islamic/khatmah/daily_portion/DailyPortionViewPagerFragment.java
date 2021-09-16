package com.islamic.khatmah.daily_portion;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private static final String ARG_COUNTER = "param5";
    private LinearLayout layout;
    private ImageButton checkButton;
    private TextView counter_text;

    private static int counter = 0;
    private static boolean[] isChecked;
    private int position;
    private int currentPageNum;
    private int pagesPerDay;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;


    public static DailyPortionViewPagerFragment newInstance(int position, int currentPageNum, int pagesPerDay, boolean[] isChecked) {
        DailyPortionViewPagerFragment fragment = new DailyPortionViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_CURRENT_PAGE_NUM, currentPageNum);
        args.putBooleanArray(ARG_IS_CHECKED_ARR, isChecked);
        args.putInt(ARG_PAGE_PER_DAY, pagesPerDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Constant.PROGRESS_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            currentPageNum = getArguments().getInt(ARG_CURRENT_PAGE_NUM);
            isChecked = getArguments().getBooleanArray(ARG_IS_CHECKED_ARR);
            pagesPerDay = getArguments().getInt(ARG_PAGE_PER_DAY);
            counter = sharedPreferences.getInt(Constant.PROGRESS_COUNT, 0);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        counter_text.setText(String.valueOf(counter));
        progressBar.setProgress(counter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_portion_view_pager_fragment, container, false);
        InputStream is;
        ImageView img = view.findViewById(R.id.img);
        layout = view.findViewById(R.id.read_linear);
        counter_text = view.findViewById(R.id.counter_text);
        checkButton = view.findViewById(R.id.read);
        int resources = isChecked[position] ? R.drawable.checked : R.drawable.unchecked;
        progressBar = view.findViewById(R.id.progress);
        checkButton.setImageResource(resources);
        counter_text.setText(String.valueOf(counter));
        progressBar.setMax(pagesPerDay);

        progressBar.setProgress(counter);
        try {
            if (getActivity() != null) {
                is = getActivity().openFileInput(String.valueOf(position + currentPageNum));
                Bitmap bit = BitmapFactory.decodeStream(is);
                img.setImageBitmap(bit);
            }
        } catch (FileNotFoundException e) {
            Picasso.get().load("https://quran-images-api.herokuapp.com/show/page/" + (position + currentPageNum)).into(img);
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PROGRESS_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        checkButton.setOnClickListener(view12 -> {
            if (isChecked[position]) {
                counter--;
                isChecked[position] = false;
                checkButton.setImageResource(R.drawable.unchecked);
            } else {
                counter++;
                isChecked[position] = true;
                checkButton.setImageResource(R.drawable.checked);
            }
            editor.putInt(Constant.PROGRESS_COUNT, counter).apply();
            storeArray(isChecked, Constant.ARRAY_NAME, getContext());
            counter_text.setText(String.valueOf(counter));
            progressBar.setProgress(counter);
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

    public boolean storeArray(boolean[] array, String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(Constant.PROGRESS_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);

        return editor.commit();
    }


}