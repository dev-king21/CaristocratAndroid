package com.ingic.caristocrat.interfaces;

import com.ingic.caristocrat.models.Model;

import java.util.ArrayList;

public interface OnModelsSelectedListener {
    void onModelsSelected(boolean selected, int brandId, ArrayList<Model> selectedModels);
}
