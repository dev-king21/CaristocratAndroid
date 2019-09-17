package com.ingic.caristocrat.interfaces;
import com.ingic.caristocrat.models.Item;
import com.ingic.caristocrat.models.MyCarAttributes;
import com.ingic.caristocrat.models.Section;


public interface ItemClickListener {
    void itemClicked(MyCarAttributes item);
    void itemClicked(Section section);
}
