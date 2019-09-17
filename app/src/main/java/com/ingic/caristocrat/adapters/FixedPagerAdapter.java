package com.ingic.caristocrat.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ingic.caristocrat.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class FixedPagerAdapter extends FragmentStatePagerAdapter {
    private final List<BaseFragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public FixedPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void add(String title, BaseFragment fragment) {
        titles.add(title);
        fragments.add(fragment);
    }
}
