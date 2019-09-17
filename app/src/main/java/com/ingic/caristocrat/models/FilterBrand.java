package com.ingic.caristocrat.models;

import com.ingic.caristocrat.webhelpers.models.Media;

import java.util.ArrayList;

public class FilterBrand {
    private int id, logoResource;
    private String name;
    private boolean selected;
    private ArrayList<Media> media;
    private ArrayList<Model> models;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogoResource() {
        return logoResource;
    }

    public void setLogoResource(int logoResource) {
        this.logoResource = logoResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    public FilterBrand(String name) {
        this.name = name;
    }

    public ArrayList<Model> getModels() {
        return models;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

}
