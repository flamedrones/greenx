package com.flamecode.greenx;

public class TokenRewardInput {

    private double distance = 0;
    private double pricePerUnit = 0;

    public TokenRewardInput() {
    }

    public TokenRewardInput(double distance, double pricePerUnit) {
        this.distance = distance;
        this.pricePerUnit = pricePerUnit;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
