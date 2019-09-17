package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 */
public class PostRegion extends WebRequestData{
    @SerializedName("region_id")
    @Expose
    private ArrayList<Integer> region_id;
    @SerializedName("region_reminder")
    @Expose
    private int region_reminder;

    public ArrayList<Integer> getRegion_id() {
        return region_id;
    }

    public void setRegion_id(ArrayList<Integer> region_id) {
        this.region_id = region_id;
    }

    public int getRegion_reminder() {
        return region_reminder;
    }

    public void setRegion_reminder(int region_reminder) {
        this.region_reminder = region_reminder;
    }
}
