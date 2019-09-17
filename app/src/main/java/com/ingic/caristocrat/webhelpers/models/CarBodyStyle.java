package com.ingic.caristocrat.webhelpers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CarBodyStyle {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("selected_icon")
    @Expose
    private String selected_icon;
    @SerializedName("un_selected_icon")
    @Expose
    private String un_selected_icon;
    @SerializedName("child_types")
    @Expose
    private ArrayList<CarBodyStyle> childTypes;

    private String model, version;

    private boolean selected = false;

    boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSelected_icon() {
        return selected_icon;
    }

    public void setSelected_icon(String selected_icon) {
        this.selected_icon = selected_icon;
    }

    public String getUn_selected_icon() {
        return un_selected_icon;
    }

    public void setUn_selected_icon(String un_selected_icon) {
        this.un_selected_icon = un_selected_icon;
    }

    public ArrayList<CarBodyStyle> getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(ArrayList<CarBodyStyle> childTypes) {
        this.childTypes = childTypes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
