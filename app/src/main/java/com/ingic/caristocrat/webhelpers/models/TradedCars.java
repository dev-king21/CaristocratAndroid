package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.models.TradeCar;

import java.util.ArrayList;

public class TradedCars {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("my_car")
    @Expose
    private
    TradeCar myCar;
    @SerializedName("trade_against")
    @Expose
    private
    TradeCar tradeAgainstCar;
    @SerializedName("amount")
    @Expose
    private
    double amount;
    @SerializedName("currency")
    @Expose
    private
    String currency;
    @SerializedName("dealer_info")
    @Expose
    private
    User dealerInfo;
    @SerializedName("is_expired")
    @Expose
    private
    boolean isExpired;
    @SerializedName("offer_details")
    @Expose
    private
    ArrayList<OfferDetail> offerDetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TradeCar getMyCar() {
        return myCar;
    }

    public void setMyCar(TradeCar myCar) {
        this.myCar = myCar;
    }

    public TradeCar getTradeAgainstCar() {
        return tradeAgainstCar;
    }

    public void setTradeAgainstCar(TradeCar tradeAgainstCar) {
        this.tradeAgainstCar = tradeAgainstCar;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public User getDealerInfo() {
        return dealerInfo;
    }

    public void setDealerInfo(User dealerInfo) {
        this.dealerInfo = dealerInfo;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public ArrayList<OfferDetail> getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(ArrayList<OfferDetail> offerDetails) {
        this.offerDetails = offerDetails;
    }
}
