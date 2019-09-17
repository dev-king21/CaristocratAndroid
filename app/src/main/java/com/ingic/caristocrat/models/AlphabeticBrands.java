package com.ingic.caristocrat.models;

import java.util.ArrayList;

public class AlphabeticBrands {
    private int id;
    private String alphabet;
    private ArrayList<FilterBrand> brands;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public ArrayList<FilterBrand> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<FilterBrand> brands) {
        this.brands = brands;
    }
}
