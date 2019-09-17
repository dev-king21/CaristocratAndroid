package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 */
public class UserDetails {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("email_updates")
    @Expose
    private String emailUpdates;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("social_login")
    @Expose
    private boolean socialLogin;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("bn")
    @Expose
    private int region_reminder;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("gender_label")
    @Expose
    private String genderLabel;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("region_id")
    @Expose
    private int region_id;
    @SerializedName("region_detail")
    @Expose
    private UserRegionDetail userRegionDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmailUpdates() {
        return emailUpdates;
    }

    public void setEmailUpdates(String emailUpdates) {
        this.emailUpdates = emailUpdates;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getSocialLogin() {
        return socialLogin;
    }

    public void setSocialLogin(boolean socialLogin) {
        this.socialLogin = socialLogin;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getRegion_reminder() {
        return region_reminder;
    }

    public void setRegion_reminder(int region_reminder) {
        this.region_reminder = region_reminder;
    }

    public boolean isSocialLogin() {
        return socialLogin;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getGenderLabel() {
        return genderLabel;
    }

    public void setGenderLabel(String genderLabel) {
        this.genderLabel = genderLabel;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }

    public UserRegionDetail getUserRegionDetail() {
        return userRegionDetail;
    }

    public void setUserRegionDetail(UserRegionDetail userRegionDetail) {
        this.userRegionDetail = userRegionDetail;
    }
}
