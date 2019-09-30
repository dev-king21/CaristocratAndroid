package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.models.TradeCar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 */

public class News {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("views_count")
    @Expose
    private int viewsCount;
    @SerializedName("favorite_count")
    @Expose
    private int favoriteCount;
    @SerializedName("comments_count")
    @Expose
    private int commentsCount;
    @SerializedName("is_featured")
    @Expose
    private int isFeatured;
    @SerializedName("like_count")
    @Expose
    private int likeCount;
    @SerializedName("is_liked")
    @Expose
    private boolean isLiked;
    @SerializedName("is_favorite")
    @Expose
    private boolean isFavorite;
    @SerializedName("is_viewed")
    @Expose
    private boolean is_viewed;
    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("media")
    @Expose
    private ArrayList<Media> media = new ArrayList<>();
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("tradeCar")
    @Expose
    private TradeCar tradeCar;
    @SerializedName("source_image_url")
    @Expose
    private String sourceImage;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("related_car")
    @Expose
    private String related_car;
    @SerializedName("link2")
    @Expose
    private String deepLink;

    @SerializedName("subscription_amount")
    @Expose
    private float subscriptionPrice;

    @SerializedName("allReportSubcriptionAmount")
    @Expose
    private float allReportSubcriptionAmount;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    public boolean isIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(boolean is_viewed) {
        this.is_viewed = is_viewed;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public TradeCar getTradeCar() {
        return tradeCar;
    }

    public void setTradeCar(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }

    public String getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(String sourceImage) {
        this.sourceImage = sourceImage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRelated_car() {
        return related_car;
    }

    public void setRelated_car(String related_car) {
        this.related_car = related_car;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public float getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(float subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public float getAllReportSubcriptionAmount() {
        return allReportSubcriptionAmount;
    }

    public void setAllReportSubcriptionAmount(float allReportSubcriptionAmount) {
        this.allReportSubcriptionAmount = allReportSubcriptionAmount;
    }
}