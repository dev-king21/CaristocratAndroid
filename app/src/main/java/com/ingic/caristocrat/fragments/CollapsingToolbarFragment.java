package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.databinding.LayoutCollapsingTitlebarBinding;
import com.ingic.caristocrat.helpers.Titlebar;

/**
 */
public class CollapsingToolbarFragment extends BaseFragment {
    LayoutCollapsingTitlebarBinding binding;

    public static CollapsingToolbarFragment Instance(){
        return new CollapsingToolbarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_collapsing_titlebar, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.hideTitlebar();
        binding.titlebar.resetTitlebar(mainActivityContext);
        binding.titlebar.showTransparentTitlebar(mainActivityContext);
        binding.titlebar.showHomeButton(mainActivityContext);

//        mainActivityContext.setSupportActionBar(binding.toolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.appbar.setVisibility(View.GONE);
        binding.titlebar.hideTitlebar();
    }
}
