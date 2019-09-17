package com.ingic.caristocrat.models;

public class DepricationAmount {
    int year;
    Double amount;
    String year_title;
    int percentage;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getYear_title() {
        return year_title;
    }

    public void setYear_title(String year_title) {
        this.year_title = year_title;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
