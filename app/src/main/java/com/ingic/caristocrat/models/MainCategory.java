package com.ingic.caristocrat.models;

/**
 */
public class MainCategory {
    private int id, newArticleCount, backgroundImage;
    private String title, shortDescription, backgroundImageLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewArticleCount() {
        return newArticleCount;
    }

    public void setNewArticleCount(int newArticleCount) {
        this.newArticleCount = newArticleCount;
    }

    public int getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(int backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getBackgroundImageLink() {
        return backgroundImageLink;
    }

    public void setBackgroundImageLink(String backgroundImageLink) {
        this.backgroundImageLink = backgroundImageLink;
    }
}
