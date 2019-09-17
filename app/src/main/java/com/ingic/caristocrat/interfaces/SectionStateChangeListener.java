package com.ingic.caristocrat.interfaces;

import com.ingic.caristocrat.models.Section;

public interface SectionStateChangeListener {
    void onSectionStateChanged(Section section, boolean isOpen);
}