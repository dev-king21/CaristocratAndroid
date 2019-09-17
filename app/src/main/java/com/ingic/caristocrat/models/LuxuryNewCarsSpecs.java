package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LuxuryNewCarsSpecs {
    @SerializedName("Dimensions_Weight")
    @Expose
    private List<LimitedEditionSpec> dimensionsWeight = null;
    @SerializedName("Seating_Capacity")
    @Expose
    private List<LimitedEditionSpec> seatingCapacity = null;
    @SerializedName("DRIVE_TRAIN")
    @Expose
    private List<LimitedEditionSpec> drivetrain = null;
    @SerializedName("Engine")
    @Expose
    private List<LimitedEditionSpec> engine = null;
    @SerializedName("Performance")
    @Expose
    private List<LimitedEditionSpec> performance = null;
    @SerializedName("Transmission ")
    @Expose
    private List<LimitedEditionSpec> transmission = null;
    @SerializedName("Brakes")
    @Expose
    private List<LimitedEditionSpec> brakes = null;
    @SerializedName("Suspension")
    @Expose
    private List<LimitedEditionSpec> suspension = null;
    @SerializedName("Wheels_Tyres")
    @Expose
    private List<LimitedEditionSpec> wheelsTyres = null;
    @SerializedName("Fuel")
    @Expose
    private List<LimitedEditionSpec> fuel = null;
    @SerializedName("Emission")
    @Expose
    private List<LimitedEditionSpec> emission = null;
    @SerializedName("Warranty_Maintenance")
    @Expose
    private List<LimitedEditionSpec> warrantyMaintenace = null;

    public List<LimitedEditionSpec> getDimensionsWeight() {
        return dimensionsWeight;
    }

    public void setDimensionsWeight(List<LimitedEditionSpec> dimensionsWeight) {
        this.dimensionsWeight = dimensionsWeight;
    }

    public List<LimitedEditionSpec> getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(List<LimitedEditionSpec> seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public List<LimitedEditionSpec> getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(List<LimitedEditionSpec> drivetrain) {
        this.drivetrain = drivetrain;
    }

    public List<LimitedEditionSpec> getEngine() {
        return engine;
    }

    public void setEngine(List<LimitedEditionSpec> engine) {
        this.engine = engine;
    }

    public List<LimitedEditionSpec> getPerformance() {
        return performance;
    }

    public void setPerformance(List<LimitedEditionSpec> performance) {
        this.performance = performance;
    }

    public List<LimitedEditionSpec> getTransmission() {
        return transmission;
    }

    public void setTransmission(List<LimitedEditionSpec> transmission) {
        this.transmission = transmission;
    }

    public List<LimitedEditionSpec> getBrakes() {
        return brakes;
    }

    public void setBrakes(List<LimitedEditionSpec> brakes) {
        this.brakes = brakes;
    }

    public List<LimitedEditionSpec> getSuspension() {
        return suspension;
    }

    public void setSuspension(List<LimitedEditionSpec> suspension) {
        this.suspension = suspension;
    }

    public List<LimitedEditionSpec> getWheelsTyres() {
        return wheelsTyres;
    }

    public void setWheelsTyres(List<LimitedEditionSpec> wheelsTyres) {
        this.wheelsTyres = wheelsTyres;
    }

    public List<LimitedEditionSpec> getFuel() {
        return fuel;
    }

    public void setFuel(List<LimitedEditionSpec> fuel) {
        this.fuel = fuel;
    }

    public List<LimitedEditionSpec> getEmission() {
        return emission;
    }

    public void setEmission(List<LimitedEditionSpec> emission) {
        this.emission = emission;
    }

    public List<LimitedEditionSpec> getWarrantyMaintenace() {
        return warrantyMaintenace;
    }

    public void setWarrantyMaintenace(List<LimitedEditionSpec> warrantyMaintenace) {
        this.warrantyMaintenace = warrantyMaintenace;
    }

    public class Brakes {
        @SerializedName("brakes")
        @Expose
        private String brakes;

        public String getBrakes() {
            return brakes;
        }

        public void setBrakes(String brakes) {
            this.brakes = brakes;
        }

    }

    public class DepreciationTrend {
        @SerializedName("depreciation_trend")
        @Expose
        private String depreciationTrend;

        public String getDepreciationTrend() {
            return depreciationTrend;
        }

        public void setDepreciationTrend(String depreciationTrend) {
            this.depreciationTrend = depreciationTrend;
        }

    }

    public class DimensionsWeight {
        @SerializedName("length")
        @Expose
        private String length;
        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("weight_dist")
        @Expose
        private String weightDist;
        @SerializedName("trunk")
        @Expose
        private String trunk;
        @SerializedName("weight")
        @Expose
        private String weight;

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeightDist() {
            return weightDist;
        }

        public void setWeightDist(String weightDist) {
            this.weightDist = weightDist;
        }

        public String getTrunk() {
            return trunk;
        }

        public void setTrunk(String trunk) {
            this.trunk = trunk;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

    }

    public class Drivetrain {
        @SerializedName("drivetrain")
        @Expose
        private String drivetrain;

        public String getDrivetrain() {
            return drivetrain;
        }

        public void setDrivetrain(String drivetrain) {
            this.drivetrain = drivetrain;
        }

    }

    public class Emission {
        @SerializedName("emission")
        @Expose
        private String emission;

        public String getEmission() {
            return emission;
        }

        public void setEmission(String emission) {
            this.emission = emission;
        }

    }

    public class Engine {
        @SerializedName("displacement")
        @Expose
        private String displacement;
        @SerializedName("clynders")
        @Expose
        private String clynders;

        public String getDisplacement() {
            return displacement;
        }

        public void setDisplacement(String displacement) {
            this.displacement = displacement;
        }

        public String getClynders() {
            return clynders;
        }

        public void setClynders(String clynders) {
            this.clynders = clynders;
        }

    }

    public class Fuel {
        @SerializedName("consumbsion")
        @Expose
        private String consumbsion;

        public String getConsumbsion() {
            return consumbsion;
        }

        public void setConsumbsion(String consumbsion) {
            this.consumbsion = consumbsion;
        }

    }

    public class Lifecycle {
        @SerializedName("lifecycle")
        @Expose
        private String lifecycle;

        public String getLifecycle() {
            return lifecycle;
        }

        public void setLifecycle(String lifecycle) {
            this.lifecycle = lifecycle;
        }

    }

    public class Performance {
        @SerializedName("max_speed")
        @Expose
        private String maxSpeed;
        @SerializedName("acceleration")
        @Expose
        private String acceleration;
        @SerializedName("hp_rpm")
        @Expose
        private String hpRpm;
        @SerializedName("torque")
        @Expose
        private String torque;

        public String getMaxSpeed() {
            return maxSpeed;
        }

        public void setMaxSpeed(String maxSpeed) {
            this.maxSpeed = maxSpeed;
        }

        public String getAcceleration() {
            return acceleration;
        }

        public void setAcceleration(String acceleration) {
            this.acceleration = acceleration;
        }

        public String getHpRpm() {
            return hpRpm;
        }

        public void setHpRpm(String hpRpm) {
            this.hpRpm = hpRpm;
        }

        public String getTorque() {
            return torque;
        }

        public void setTorque(String torque) {
            this.torque = torque;
        }

    }

    public class SeatingCapacity {
        @SerializedName("seats")
        @Expose
        private String seats;

        public String getSeats() {
            return seats;
        }

        public void setSeats(String seats) {
            this.seats = seats;
        }

    }

    public class Suspension {

        @SerializedName("suspension")
        @Expose
        private String suspension;

        public String getSuspension() {
            return suspension;
        }

        public void setSuspension(String suspension) {
            this.suspension = suspension;
        }

    }

    public class Transmission {
        @SerializedName("gearbox")
        @Expose
        private String gearbox;

        public String getGearbox() {
            return gearbox;
        }

        public void setGearbox(String gearbox) {
            this.gearbox = gearbox;
        }

    }

    public class WarrantyMaintenace {
        @SerializedName("warranty")
        @Expose
        private String warranty;
        @SerializedName("maintenance")
        @Expose
        private String maintenance;

        public String getWarranty() {
            return warranty;
        }

        public void setWarranty(String warranty) {
            this.warranty = warranty;
        }

        public String getMaintenance() {
            return maintenance;
        }

        public void setMaintenance(String maintenance) {
            this.maintenance = maintenance;
        }

    }

    public class WheelsTyres {
        @SerializedName("front_tyre")
        @Expose
        private String frontTyre;
        @SerializedName("back_tyre")
        @Expose
        private String backTyre;

        public String getFrontTyre() {
            return frontTyre;
        }

        public void setFrontTyre(String frontTyre) {
            this.frontTyre = frontTyre;
        }

        public String getBackTyre() {
            return backTyre;
        }

        public void setBackTyre(String backTyre) {
            this.backTyre = backTyre;
        }

    }
}
