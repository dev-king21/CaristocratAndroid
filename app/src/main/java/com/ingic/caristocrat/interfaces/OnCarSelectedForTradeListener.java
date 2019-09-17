package com.ingic.caristocrat.interfaces;

import com.ingic.caristocrat.models.TradeCar;

public interface OnCarSelectedForTradeListener {
    void onNewCarAdd();
    void onCarSelected(TradeCar tradeCar);
    void onCarAdded(TradeCar tradeCar);
}
