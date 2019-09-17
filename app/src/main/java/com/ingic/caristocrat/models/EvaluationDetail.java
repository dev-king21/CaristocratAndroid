package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.webhelpers.models.User;

import java.io.Serializable;

/**
 */
public class EvaluationDetail implements Serializable {
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("currency")
    @Expose
    String currency;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}