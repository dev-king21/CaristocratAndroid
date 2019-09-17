package com.ingic.caristocrat.models;

import android.support.annotation.NonNull;

import java.io.File;
import java.net.URI;


public class MyImageFile extends File {
    private int imageType;

    public MyImageFile(@NonNull String pathname) {
        super(pathname);
    }

    public MyImageFile(String parent, @NonNull String child) {
        super(parent, child);
    }

    public MyImageFile(File parent, @NonNull String child) {
        super(parent, child);
    }

    public MyImageFile(@NonNull URI uri) {
        super(uri);
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }
}
