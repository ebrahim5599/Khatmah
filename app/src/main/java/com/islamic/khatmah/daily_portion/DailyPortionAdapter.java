package com.islamic.khatmah.daily_portion;

import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class DailyPortionAdapter extends FragmentStateAdapter {
    int currentPageNum;
    int pagesPerDay;
    private static boolean[] isChecked;
    private SharedPreferences preferences;
    public ViewPager2 viewPager2;

    public DailyPortionAdapter(@NonNull FragmentActivity fragmentActivity, int currentPageNum, boolean[] isChecked,int pagesPerDay, ViewPager2 viewPager2) {
        super(fragmentActivity);
        this.currentPageNum = currentPageNum;
        this.pagesPerDay = pagesPerDay;
        Log.d("pages_per_day", "" + pagesPerDay);
        DailyPortionAdapter.isChecked = isChecked;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DailyPortionViewPagerFragment.newInstance(position, currentPageNum, pagesPerDay, isChecked,viewPager2);
    }

    @Override
    public int getItemCount() {
        return pagesPerDay;
    }

}