package com.ingic.caristocrat.models;

public class CompareCarPanel {
    private int id, selectedYear;
    private TradeCar tradeCar;
    private FilterBrand selectedBrand;
    private Model selectedModel;
    private Version version;
    private boolean moreCar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TradeCar getTradeCar() {
        return tradeCar;
    }

    public void setTradeCar(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }

    public FilterBrand getSelectedBrand() {
        return selectedBrand;
    }

    public void setSelectedBrand(FilterBrand selectedBrand) {
        this.selectedBrand = selectedBrand;
    }

    public Model getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(Model selectedModel) {
        this.selectedModel = selectedModel;
    }

    public boolean isMoreCar() {
        return moreCar;
    }

    public void setMoreCar(boolean moreCar) {
        this.moreCar = moreCar;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
