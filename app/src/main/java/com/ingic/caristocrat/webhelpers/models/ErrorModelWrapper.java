package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 */
public class ErrorModelWrapper {
    @SerializedName("errors")
    @Expose
    private ErrorModel errorModel;

    public ErrorModel getErrorModel() {
        return errorModel;
    }
}
