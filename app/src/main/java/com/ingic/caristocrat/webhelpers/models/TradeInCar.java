package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradeInCar extends WebRequestData {
    @SerializedName("owner_car_id")
    @Expose
    private Integer ownerCarId;
    @SerializedName("customer_car_id")
    @Expose
    private Integer customerCarId;
    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("type")
    @Expose
    private Integer type;

    public int getOwnerCarId() {
        return ownerCarId;
    }

    public void setOwnerCarId(int ownerCarId) {
        this.ownerCarId = ownerCarId;
    }

    public int getCustomerCarId() {
        return customerCarId;
    }

    public void setCustomerCarId(int customerCarId) {
        this.customerCarId = customerCarId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
