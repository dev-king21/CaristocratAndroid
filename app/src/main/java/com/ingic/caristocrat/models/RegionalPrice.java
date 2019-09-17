package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegionalPrice {
    @SerializedName("price")
    @Expose
    double price;
    @SerializedName("region")
    @Expose
    Region region;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
