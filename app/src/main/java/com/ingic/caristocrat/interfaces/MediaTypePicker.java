package com.ingic.caristocrat.interfaces;

import com.ingic.caristocrat.models.MyImageFile;

import java.io.File;
import java.util.ArrayList;



public interface MediaTypePicker {
    void onPhotoClicked(ArrayList<MyImageFile> file);

}
