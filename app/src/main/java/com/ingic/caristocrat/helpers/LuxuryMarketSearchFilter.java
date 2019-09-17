package com.ingic.caristocrat.helpers;

import com.ingic.caristocrat.models.FilterBrand;
import com.ingic.caristocrat.models.Model;
import com.ingic.caristocrat.models.Region;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;

import java.util.ArrayList;

public class LuxuryMarketSearchFilter {
    private static LuxuryMarketSearchFilter Instance;

    private ArrayList<FilterBrand> brandsList = new ArrayList<>();
    private ArrayList<CarBodyStyle> carBodyStyles = new ArrayList<>();
    private ArrayList<Region> selectedRegions = new ArrayList<>();
    private String brandIds, versionName;
    private Integer transmissionType, carType, dealerType;
    private Long minyear, maxYear, minPrice, maxPrice, minMileage, maxMileage;
    private boolean filter, filterApply, isAutomatic, isManual, isLuxuryNewCars;
    private float rating = -1;
    private FilterBrand filterBrand;

    private LuxuryMarketSearchFilter() {
    }

    public static LuxuryMarketSearchFilter getInstance() {
        if (Instance == null) {
            Instance = new LuxuryMarketSearchFilter();
        }
        return Instance;
    }

    public void resetFilter(boolean clearRegions) {
        this.dealerType = null;
        this.brandIds = null;
        this.versionName = null;
        this.transmissionType = null;
        this.minyear = null;
        this.maxYear = null;
        this.carType = null;
        this.minPrice = null;
        this.maxPrice = null;
        this.minMileage = null;
        this.maxMileage = null;
        this.filter = false;
        this.filterApply = false;
        this.isAutomatic = false;
        this.isManual = false;
        this.brandsList.clear();
        this.rating = -1;
        this.filterBrand = null;
        if (clearRegions) {
            this.selectedRegions.clear();
        }
        resetCarBodyStyles();
    }

    public ArrayList<Region> getSelectedRegions() {
        return selectedRegions;
    }

    public void setSelectedRegions(ArrayList<Region> selectedRegions) {
        this.selectedRegions = selectedRegions;
    }

    public String getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String brandIds) {
        this.filter = true;
        this.brandIds = brandIds;
    }

    public Integer getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(Integer transmissionType) {
        this.filter = true;
        this.transmissionType = transmissionType;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.filter = true;
        this.carType = carType;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = true;
        this.filter = filter;
    }

    public Long getMinyear() {
        return minyear;
    }

    public void setMinyear(Long minyear) {
        this.filter = true;
        this.minyear = minyear;
    }

    public Long getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Long maxYear) {
        this.filter = true;
        this.maxYear = maxYear;
    }

    public ArrayList<FilterBrand> getBrandsList() {
        return brandsList;
    }

    public void setBrandsList(ArrayList<FilterBrand> brandsList) {
        this.filter = true;
        this.brandsList = brandsList;
    }

    public void addBrand(FilterBrand filterBrand) {
        this.setFilter(true);
        this.brandsList.add(filterBrand);
    }

    public void removeBrand(int position) {
        this.brandsList.remove(position);
    }

    public void resetBrandsList() {
        this.brandsList.clear();
    }

    public ArrayList<CarBodyStyle> getCarBodyStyles() {
        return carBodyStyles;
    }

    public void setCarBodyStyles(ArrayList<CarBodyStyle> carBodyStyles) {
        this.filter = true;
        this.carBodyStyles = carBodyStyles;
    }

    public void addCarBodyStyle(CarBodyStyle carBodyStyle) {
        this.setFilter(true);
        this.carBodyStyles.add(carBodyStyle);
    }

    public void removeCarBodyStyle(int position) {
        this.carBodyStyles.remove(position);
    }

    public void resetCarBodyStyles() {
        for (int pos = 0; pos < carBodyStyles.size(); pos++) {
            carBodyStyles.get(pos).setSelected(false);
        }
    }

    public boolean isFilterApply() {
        return filterApply;
    }

    public void setFilterApply(boolean filterApply) {
        this.filterApply = filterApply;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.filter = true;
        this.minPrice = minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.filter = true;
        this.maxPrice = maxPrice;
    }

    public boolean isAutomatic() {
        return isAutomatic;
    }

    public void setAutomatic(boolean automatic) {
        if (automatic) {
            setFilter(true);
        }
        isAutomatic = automatic;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        if (manual) {
            setFilter(true);
        }
        isManual = manual;
    }

    public Long getMinMileage() {
        return minMileage;
    }

    public void setMinMileage(Long minMileage) {
        this.minMileage = minMileage;
    }

    public Long getMaxMileage() {
        return maxMileage;
    }

    public void setMaxMileage(Long maxMileage) {
        this.maxMileage = maxMileage;
    }

    public Integer getDealerType() {
        return dealerType;
    }

    public void setDealerType(Integer dealerType) {
        this.dealerType = dealerType;
        setFilter(true);
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isLuxuryNewCars() {
        return isLuxuryNewCars;
    }

    public void setLuxuryNewCars(boolean luxuryNewCars) {
        isLuxuryNewCars = luxuryNewCars;
    }

    public FilterBrand getFilterBrand() {
        return filterBrand;
    }

    public void setFilterBrand(FilterBrand filterBrand) {
        this.filterBrand = filterBrand;
    }
}
