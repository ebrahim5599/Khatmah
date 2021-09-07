package com.islamic.khatmah.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.islamic.khatmah.daily_portion.DailyPortionFragment;
import com.islamic.khatmah.free_reading.FreeReadingFragment;
import com.islamic.khatmah.progress.ProgressFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {


    public SectionsPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return FreeReadingFragment.newInstance();
            case 1:
                return DailyPortionFragment.newInstance();
            default:
                return ProgressFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}