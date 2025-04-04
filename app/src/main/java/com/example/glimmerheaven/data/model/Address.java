package com.example.glimmerheaven.data.model;

public class Address {
    private String addressLineOne;
    private String addressLineTwo;
    private String villageOrTown;
    private String district;
    private String province;

    public Address() {
    }

    public Address(String addressLineOne, String addressLineTwo, String villageOrTown, String district, String province) {
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.villageOrTown = villageOrTown;
        this.district = district;
        this.province = province;
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

    public String getVillageOrTown() {
        return villageOrTown;
    }

    public void setVillageOrTown(String city) {
        this.villageOrTown = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
