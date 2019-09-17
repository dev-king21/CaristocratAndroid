package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.webhelpers.models.WebRequestData;

import java.io.Serializable;

/**
 */
public class ReportListing extends WebRequestData{

        @SerializedName("car_id")
        @Expose
        private Integer carId;
        @SerializedName("message")
        @Expose
        private String message;

        public int getCarId() {
            return carId;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


}
