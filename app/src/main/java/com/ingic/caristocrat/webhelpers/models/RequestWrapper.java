package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 */
public class RequestWrapper implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("default_language")
    @Expose
    private String defaultLanguage;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("depreciation_trend")
    @Expose
    private Integer depreciationTrend;
    @SerializedName("limit_for_cars")
    @Expose
    private Integer limitForCars;
    @SerializedName("limit_for_featured_cars")
    @Expose
    private Integer limitForFeaturedCars;
    @SerializedName("playstore")
    @Expose
    private Object playstore;
    @SerializedName("appstore")
    @Expose
    private Object appstore;
    @SerializedName("social_links")
    @Expose
    private Object socialLinks;
    @SerializedName("app_version")
    @Expose
    private Integer appVersion;
    @SerializedName("force_update")
    @Expose
    private String forceUpdate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("title")
    @Expose
    private Object title;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("about")
    @Expose
    private Object about;
    @SerializedName("personal_shopper")
    @Expose
    private String personalShopper;
    @SerializedName("ask_for_consultancy")
    @Expose
    private String askForConsultancy;
    @SerializedName("translations")
    @Expose
    private List<Translation> translations = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Integer getDepreciationTrend() {
        return depreciationTrend;
    }

    public void setDepreciationTrend(Integer depreciationTrend) {
        this.depreciationTrend = depreciationTrend;
    }

    public Integer getLimitForCars() {
        return limitForCars;
    }

    public void setLimitForCars(Integer limitForCars) {
        this.limitForCars = limitForCars;
    }

    public Integer getLimitForFeaturedCars() {
        return limitForFeaturedCars;
    }

    public void setLimitForFeaturedCars(Integer limitForFeaturedCars) {
        this.limitForFeaturedCars = limitForFeaturedCars;
    }

    public Object getPlaystore() {
        return playstore;
    }

    public void setPlaystore(Object playstore) {
        this.playstore = playstore;
    }

    public Object getAppstore() {
        return appstore;
    }

    public void setAppstore(Object appstore) {
        this.appstore = appstore;
    }

    public Object getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(Object socialLinks) {
        this.socialLinks = socialLinks;
    }

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
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

    public String getPersonalShopper() {
        return personalShopper;
    }

    public void setPersonalShopper(String personalShopper) {
        this.personalShopper = personalShopper;
    }

    public String getAskForConsultancy() {
        return askForConsultancy;
    }

    public void setAskForConsultancy(String askForConsultancy) {
        this.askForConsultancy = askForConsultancy;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

}