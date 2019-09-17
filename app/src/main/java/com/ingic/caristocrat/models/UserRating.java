package com.ingic.caristocrat.models;

import java.util.ArrayList;

public class UserRating {
    private int id;
    private float overAllRating;
    private ReviewDetail.ReviewDetailUser user;
    private ArrayList<RatingAttribute> ratingAttributes;
    private String reviewDetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<RatingAttribute> getRatingAttributes() {
        return ratingAttributes;
    }

    public void setRatingAttributes(ArrayList<RatingAttribute> ratingAttributes) {
        this.ratingAttributes = ratingAttributes;
    }

    public String getReviewDetails() {
        return reviewDetails;
    }

    public void setReviewDetails(String reviewDetails) {
        this.reviewDetails = reviewDetails;
    }

    public float getOverAllRating() {
        return overAllRating;
    }

    public void setOverAllRating(float overAllRating) {
        this.overAllRating = overAllRating;
    }

    public ReviewDetail.ReviewDetailUser getUser() {
        return user;
    }

    public void setUser(ReviewDetail.ReviewDetailUser user) {
        this.user = user;
    }
}
