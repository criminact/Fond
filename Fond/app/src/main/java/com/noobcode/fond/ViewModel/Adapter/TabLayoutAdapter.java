package com.noobcode.fond.ViewModel.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.noobcode.fond.View.Fragments.ChatFragment;
import com.noobcode.fond.View.Fragments.HomeFragment;
import com.noobcode.fond.View.Fragments.ProfileFragment;
import com.noobcode.fond.View.HomeActivity;

public class TabLayoutAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private int totalTabs;



    public TabLayoutAdapter(@NonNull FragmentManager fm, int behavior, Context mContext, int totalTabs) {
        super(fm, behavior);
        this.mContext = mContext;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                ChatFragment sportFragment = new ChatFragment();
                return sportFragment;
            case 2:
                ProfileFragment movieFragment = new ProfileFragment();
                return movieFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
