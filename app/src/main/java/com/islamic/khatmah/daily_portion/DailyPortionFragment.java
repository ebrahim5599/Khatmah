package com.islamic.khatmah.daily_portion;

import static com.islamic.khatmah.MainActivity.sharedPreferences;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.islamic.khatmah.R;
import com.islamic.khatmah.quran_activity.QuranActivity;

public class DailyPortionFragment extends Fragment {

//    private DailyPortionViewModel mViewModel;

    private TextView page_number;
    private int current_page;

    public static DailyPortionFragment newInstance() {
        return new DailyPortionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        mViewModel = new ViewModelProvider(this).get(DailyPortionViewModel.class);
        View view = inflater.inflate(R.layout.daily_portion_fragment, container, false);

        page_number = view.findViewById(R.id.page_number);

        Button start_btn = view.findViewById(R.id.start);
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

    @Override
    public void onResume() {
        super.onResume();
        current_page = sharedPreferences.getInt("CURRENT_PAGE",1);
        page_number.setText(current_page+"");
    }
}