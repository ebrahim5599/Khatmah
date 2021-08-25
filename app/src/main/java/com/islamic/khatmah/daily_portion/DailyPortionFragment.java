package com.islamic.khatmah.daily_portion;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islamic.khatmah.R;

public class DailyPortionFragment extends Fragment {

    private DailyPortionViewModel mViewModel;

    public static DailyPortionFragment newInstance() {
        return new DailyPortionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(DailyPortionViewModel.class);
        return inflater.inflate(R.layout.daily_portion_fragment, container, false);
    }
}