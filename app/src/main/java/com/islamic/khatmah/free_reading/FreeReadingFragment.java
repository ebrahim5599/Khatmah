package com.islamic.khatmah.free_reading;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islamic.khatmah.R;

public class FreeReadingFragment extends Fragment {

    private FreeReadingViewModel mViewModel;

    public static FreeReadingFragment newInstance() {
        return new FreeReadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.free_reading_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FreeReadingViewModel.class);
        // TODO: Use the ViewModel
    }

}