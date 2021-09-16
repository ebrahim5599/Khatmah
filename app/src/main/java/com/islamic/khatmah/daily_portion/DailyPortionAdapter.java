package com.islamic.khatmah.daily_portion;

import static com.islamic.khatmah.MainActivity.PAGES_PER_DAY;
import static com.islamic.khatmah.MainActivity.sharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DailyPortionAdapter extends FragmentStateAdapter {
    int currentPageNum;
    int pagesPerDay;
    private static boolean[] isChecked;

    public DailyPortionAdapter(@NonNull FragmentActivity fragmentActivity, int currentPageNum, boolean[] isChecked) {
        super(fragmentActivity);
        this.currentPageNum = currentPageNum;
        pagesPerDay = sharedPreferences.getInt(PAGES_PER_DAY, 1);
        Log.d("pages_per_day", "" + pagesPerDay);
        DailyPortionAdapter.isChecked = isChecked;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DailyPortionViewPagerFragment.newInstance(position, currentPageNum, pagesPerDay, isChecked);
    }

    @Override
    public int getItemCount() {
        return pagesPerDay;
    }
}