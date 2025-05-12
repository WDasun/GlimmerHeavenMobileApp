package com.example.glimmerheaven.data.model;

public class Shop {
    private String shopName;
    private String email;
    private String contactNumber;
    private String addressLineOne;
    private String addressLineTwo;
    private double latitude;
    private double longitude;

    public Shop() {
    }

    public Shop(String shopName, String email, String contactNumber, String addressLineOne, String addressLineTwo, double latitude, double longitude) {
        this.shopName = shopName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
