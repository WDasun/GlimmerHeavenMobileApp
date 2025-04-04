package com.example.glimmerheaven.data.model;

import java.util.ArrayList;
import java.util.List;

public class Discount {
    private String discountName;
    private String description;
    private double rate;
    private long startDate;
    private long endDate;
    private List<String> includedProductIdList = new ArrayList<>();
    private boolean status;

    public Discount() {
    }

    public Discount(String discountName, String description, double rate, long startDate, long endDate, List<String> includedProductIdList, boolean status) {
        this.discountName = discountName;
        this.description = description;
        this.rate = rate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.includedProductIdList = includedProductIdList;
        this.status = status;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public List<String> getIncludedProductIdList() {
        return includedProductIdList;
    }

    public void setIncludedProductIdList(List<String> includedProductIdList) {
        this.includedProductIdList = includedProductIdList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
