package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * on 1/15/2019.
 */
public class Translation implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("setting_id")
    @Expose
    private Integer settingId;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("title")
    @Expose
    private Object title;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("about")
    @Expose
    private Object about;
    @SerializedName("ask_for_consultancy")
    @Expose
    private String askForConsultancy;
    @SerializedName("personal_shopper")
    @Expose
    private String personalShopper;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getAbout() {
        return about;
    }

    public void setAbout(Object about) {
        this.about = about;
    }

    public String getAskForConsultancy() {
        return askForConsultancy;
    }

    public void setAskForConsultancy(String askForConsultancy) {
        this.askForConsultancy = askForConsultancy;
    }

    public String getPersonalShopper() {
        return personalShopper;
    }

    public void setPersonalShopper(String personalShopper) {
        this.personalShopper = personalShopper;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

}
