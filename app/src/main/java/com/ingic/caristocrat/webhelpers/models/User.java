package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.models.ShowroomDetails;

/**
 */
public class User {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("details")
    @Expose
    private UserDetails details;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private int expiresIn;
    @SerializedName("showroom_details")
    @Expose
    private ShowroomDetails showroom_details;
    @SerializedName("push_notification")
    @Expose
    private int push_notification;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserDetails getDetails() {
        return details;
    }

    public void setDetails(UserDetails details) {
        this.details = details;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public ShowroomDetails getShowroom_details() {
        return showroom_details;
    }

    public void setShowroom_details(ShowroomDetails showroom_details) {
        this.showroom_details = showroom_details;
    }

    public int getPush_notification() {
        return push_notification;
    }

    public void setPush_notification(int push_notification) {
        this.push_notification = push_notification;
    }
}
