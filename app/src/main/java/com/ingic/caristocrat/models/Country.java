package com.ingic.caristocrat.models;

/**
 */
public class Country {
    String name;
    int selectedDrawable;
    int unselectedDrawable;

    public Country(String name, int selectedDrawable, int unselectedDrawable) {
        this.name = name;
        this.selectedDrawable = selectedDrawable;
        this.unselectedDrawable = unselectedDrawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelectedDrawable() {
        return selectedDrawable;
    }

    public void setSelectedDrawable(int selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
    }

    public int getUnselectedDrawable() {
        return unselectedDrawable;
    }

    public void setUnselectedDrawable(int unselectedDrawable) {
        this.unselectedDrawable = unselectedDrawable;
    }
}
