package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InteractionCar extends WebRequestData {
    @SerializedName("car_id")
    @Expose
    private int car_id;
    @SerializedName("type")
    @Expose
    private int type;

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
