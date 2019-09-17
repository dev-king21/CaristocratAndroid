package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.webhelpers.models.WebRequestData;

import org.json.JSONArray;

public class RatingPost extends WebRequestData {
    @SerializedName("car_id")
    @Expose
    private int car_id;

    @SerializedName("review_message")
    @Expose
    private String review_message;

    @SerializedName("rating")
    @Expose
    private String rating;

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getReview_message() {
        return review_message;
    }

    public void setReview_message(String review_message) {
        this.review_message = review_message;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
