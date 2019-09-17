package com.ingic.caristocrat.models;

/**
 */
public class SubcategoriesFeaturedArticle {
    private int id, backgroundImage;
    private String shortDescription, backgroundImageLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(int backgroundImage) {
        this.backgroundImage = backgroundImage;
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
