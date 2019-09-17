package com.ingic.caristocrat.interfaces;

import android.view.View;

import com.ingic.caristocrat.models.TradeCar;

/**
 */
public interface AutoCompleteItemClickListener {
    void onItemClick(TradeCar entity,int pos,View view);
}
