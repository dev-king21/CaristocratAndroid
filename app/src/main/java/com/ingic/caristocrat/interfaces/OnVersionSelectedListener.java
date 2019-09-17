package com.ingic.caristocrat.interfaces;

import com.ingic.caristocrat.models.Version;

import java.util.ArrayList;

public interface OnVersionSelectedListener {
    void onVersionsSelected(boolean selected, int modelId, ArrayList<Version> selectedVersions);
}
