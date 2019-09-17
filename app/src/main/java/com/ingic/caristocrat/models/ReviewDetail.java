package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewDetail {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("average_rating")
    @Expose
    private float averageRating;
    @SerializedName("review_message")
    @Expose
    private String reviewMessage;
    @SerializedName("user_details")
    @Expose
    private ReviewDetailUser userDetails;
    @SerializedName("details")
    @Expose
    private ArrayList<RatingDetail> details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public ArrayList<RatingDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<RatingDetail> details) {
        this.details = details;
    }

    public ReviewDetailUser getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(ReviewDetailUser userDetails) {
        this.userDetails = userDetails;
    }

    public class ReviewDetailUser{
        @SerializedName("user_id")
        @Expose
        private int user_id;
        @SerializedName("user_name")
        @Expose
        private String user_name;
        @SerializedName("image_url")
        @Expose
        private String image_url;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }

    public class RatingDetail{
        @SerializedName("aspect_id")
        @Expose
        private int id;
        @SerializedName("rating")
        @Expose
        private float rating;
        @SerializedName("aspect_title")
        @Expose
        private String aspectTitle;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getAspectTitle() {
            return aspectTitle;
        }

        public void setAspectTitle(String aspectTitle) {
            this.aspectTitle = aspectTitle;
        }
    }
}
