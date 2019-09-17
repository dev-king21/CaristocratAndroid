package com.ingic.caristocrat.models;

/**
 */
public class AutoLifeCategory {
    private int categoryBackgroundImage;
    private String categoryTitle;
    private String categoryDescription;
    private String categoryBackroundImagePath;

    public AutoLifeCategory(int categoryBackgroundImage, String categoryTitle, String categoryDescription) {
        this.categoryBackgroundImage = categoryBackgroundImage;
        this.categoryTitle = categoryTitle;
        this.categoryDescription = categoryDescription;
    }

    public int getCategoryBackgroundImage() {
        return categoryBackgroundImage;
    }

    public void setCategoryBackgroundImage(int categoryBackgroundImage) {
        this.categoryBackgroundImage = categoryBackgroundImage;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryBackroundImagePath() {
        return categoryBackroundImagePath;
    }

    public void setCategoryBackroundImagePath(String categoryBackroundImagePath) {
        this.categoryBackroundImagePath = categoryBackroundImagePath;
    }
}
