package com.ingic.caristocrat.models;

/**
 */
public class Car {
    private boolean addNewCar;
    private int id, imageResource;
    private long travelledDistance;
    private double price, averateMarketPrice;
    private String carName, carSubtitle, carCompany, imagePath, year;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarSubtitle() {
        return carSubtitle;
    }

    public void setCarSubtitle(String carSubtitle) {
        this.carSubtitle = carSubtitle;
    }

    public String getCarCompany() {
        return carCompany;
    }

    public void setCarCompany(String carCompany) {
        this.carCompany = carCompany;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isAddNewCar() {
        return addNewCar;
    }

    public void setAddNewCar(boolean addNewCar) {
        this.addNewCar = addNewCar;
    }

    public long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAverateMarketPrice() {
        return averateMarketPrice;
    }

    public void setAverateMarketPrice(double averateMarketPrice) {
        this.averateMarketPrice = averateMarketPrice;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Car(String carName) {
        this.carName = carName;
    }
}
