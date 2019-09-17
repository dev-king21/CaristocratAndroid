package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentCompareBinding;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;

import java.util.ArrayList;
import java.util.List;

public class CompareFragment extends BaseFragment implements View.OnClickListener {
    private FragmentCompareBinding binding;
    LuxuryMarketSearchFilter filter;

    public CompareFragment() {
    }

    public static CompareFragment Instance() {
        return new CompareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare, container, false);
        filter = LuxuryMarketSearchFilter.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.compare));
        titlebar.showBackButton(mainActivityContext, false);
//        titlebar.showFilter().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibFilter:
                mainActivityContext.replaceFragment(LuxuryMarketFilterFragment.Instance(AppConstants.MainCategoriesType.LUXURY_NEW_CARS), LuxuryMarketFilterFragment.class.getSimpleName(), true, false);
                break;
        }
    }

    private void init() {
        ComparePagerAdapter adapter = new ComparePagerAdapter(getChildFragmentManager());
//        adapter.add(mainActivityContext.getResources().getString(R.string.compare_any_car), new CompareAnyCarFragment(filter));
        adapter.add(mainActivityContext.getResources().getString(R.string.compare_any_car), new CompareCarSearchPanelFragment(filter));
        adapter.add(mainActivityContext.getResources().getString(R.string.professional_comparison), new CompareSegmentMainWiseFragment(filter));
        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.VISIBLE);
        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.compare_cars));
        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.VISIBLE);
        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.VISIBLE);
                        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.compare_cars));
                        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.GONE);
                        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.apply));
                        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class ComparePagerAdapter extends FragmentStatePagerAdapter {
        private final List<BaseFragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        ComparePagerAdapter(FragmentManager manager) {
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

        void add(String title, BaseFragment fragment) {
            titles.add(title);
            fragments.add(fragment);
        }
    }
}
