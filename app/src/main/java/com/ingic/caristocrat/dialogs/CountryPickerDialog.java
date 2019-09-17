package com.ingic.caristocrat.dialogs;

import android.annotation.SuppressLint;

import com.mukesh.countrypicker.CountryPicker;

/**
 */
@SuppressLint("ValidFragment")
public class CountryPickerDialog extends CountryPicker {
    CountryPicker picker;
    OnDestroyListener listener;

    @SuppressLint("ValidFragment")
    public CountryPickerDialog(String title, OnDestroyListener listener){
        this.picker = super.newInstance(title);
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener.onDestroy();
    }

    public interface OnDestroyListener{
        void onDestroy();
    }

    public interface OnCountrySelectedListener{
        void onCountrySelected(String name, String code, String dialCode, int flagDrawableResID);
    }
}