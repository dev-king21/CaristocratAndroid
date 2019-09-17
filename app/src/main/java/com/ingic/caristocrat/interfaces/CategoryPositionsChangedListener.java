package com.ingic.caristocrat.interfaces;

import com.ingic.caristocrat.webhelpers.models.Category;

import java.util.ArrayList;

public interface CategoryPositionsChangedListener {
    void onCategoryPositionChanged(int parentPosition, ArrayList<Category> categories);
}
