package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 */
public class ErrorModel {
    @SerializedName("email")
    @Expose
    private ArrayList<String> email = new ArrayList<>();
    @SerializedName("password")
    @Expose
    private ArrayList<String> password = new ArrayList<>();
    @SerializedName("error")
    @Expose
    private ArrayList<String> error = new ArrayList<>();
    @SerializedName("password_confirmation")
    @Expose
    private ArrayList<String> passwordConfirmation = new ArrayList<>();

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getPassword() {
        return password;
    }

    public void setPassword(ArrayList<String> password) {
        this.password = password;
    }

    public ArrayList<String> getError() {
        return error;
    }

    public void setError(ArrayList<String> error) {
        this.error = error;
    }

    public ArrayList<String> getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(ArrayList<String> passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
