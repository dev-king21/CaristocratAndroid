package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 */
public class NotificationWrapper implements Serializable {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("action_type")
        @Expose
        private int actionType;
        @SerializedName("ref_id")
        @Expose
        private int refId;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("car_name")
        @Expose
        private String carName;
        @SerializedName("model_year")
        @Expose
        private String modelYear;
        @SerializedName("chassis")
        @Expose
        private String chassis;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }
}
