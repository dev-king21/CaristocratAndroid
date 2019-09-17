package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.databinding.FragmentAddCarBinding;
import com.ingic.caristocrat.helpers.Titlebar;

/**
 */
public class AddCarFragment extends BaseFragment implements View.OnClickListener{
    FragmentAddCarBinding binding;

    public AddCarFragment(){}

    public static AddCarFragment Instance(){
        return new AddCarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_car, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        binding.btnAddCar.setOnClickListener(this);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.add_car));
        titlebar.showBackButton(mainActivityContext, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddCar:
                mainActivityContext.onBackPressed();
                break;
        }
    }
}
