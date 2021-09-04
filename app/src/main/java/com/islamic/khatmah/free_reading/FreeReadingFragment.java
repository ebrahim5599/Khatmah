package com.islamic.khatmah.free_reading;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.islamic.khatmah.R;
import com.islamic.khatmah.quran_activity.QuranActivity;

import java.util.ArrayList;

public class FreeReadingFragment extends Fragment {

    private FreeReadingViewModel mViewModel;
    private SurahAdapter surahAdapter;

    public static FreeReadingFragment newInstance() {
        return new FreeReadingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        mViewModel = new ViewModelProvider(this).get(FreeReadingViewModel.class);
        View view = inflater.inflate(R.layout.free_reading_fragment, container, false);

        int i = 1;
        ArrayList<SurahClass> surahArrayList = new ArrayList<SurahClass>();
        surahArrayList.add(new SurahClass(1  , ArbNum(i++),"الفاتحة","مكية",ArbNum(7)+" آيات"));
        surahArrayList.add(new SurahClass(2  , ArbNum(i++),"البقرة","مدنية",ArbNum(286)+" آية"));
        surahArrayList.add(new SurahClass(50 , ArbNum(i++),"آل عمران","مدنية",ArbNum(200)+" آية"));
        surahArrayList.add(new SurahClass(77 , ArbNum(i++),"النساء","مدنية",ArbNum(176)+" آية"));
        surahArrayList.add(new SurahClass(106, ArbNum(i++),"المائدة","مدنية",ArbNum(120)+" آية"));
        surahArrayList.add(new SurahClass(128, ArbNum(i++),"الأنعام","مكية",ArbNum(165)+" آية"));
        surahArrayList.add(new SurahClass(151, ArbNum(i++),"الأعراف","مكية",ArbNum(206)+" آية"));
        surahArrayList.add(new SurahClass(177, ArbNum(i++),"الأنفال","مدنية",ArbNum(75)+" آية"));
        surahArrayList.add(new SurahClass(187, ArbNum(i++),"التوبة","مدنية",ArbNum(129)+" آية"));
        surahArrayList.add(new SurahClass(208, ArbNum(i++),"يونس","مكية",ArbNum(109)+" آية"));
        surahArrayList.add(new SurahClass(221, ArbNum(i++),"هود","مكية",ArbNum(123)+" آية"));

        surahArrayList.add(new SurahClass(603, ArbNum(i++),"الكافرون","مكية",ArbNum(5)+" آية"));
        surahArrayList.add(new SurahClass(604, ArbNum(i++),"الاخلاص","مكية",ArbNum(5)+" آية"));


        // Find a reference to the {@link ListView} in the layout
        ListView listView = view.findViewById(R.id.listView);
        // Create a new adapter that takes an empty list of surah as input
        surahAdapter = new SurahAdapter(getContext(),surahArrayList);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(surahAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), surahArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                Intent quranActivityIntent = new Intent(getContext(), QuranActivity.class);
                quranActivityIntent.putExtra("PAGE_NUMBER", surahArrayList.get(i).getPage_number());
                startActivity(quranActivityIntent);
            }
        });
        return view;
    }

    private String ArbNum(int number) {

        String stNum = String.valueOf(number);
        String result ="";

        for (int i = 0; i < stNum.length(); i++) {
            char num = String.valueOf(stNum).charAt(i);
            int ArabicNum = num + 1584;
            result += (char) ArabicNum;
        }
        return result;
    }
}