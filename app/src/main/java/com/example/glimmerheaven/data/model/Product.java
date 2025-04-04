package com.example.glimmerheaven.data.model;

import java.util.Map;

public class Product {
    private String categoryId;
    private String name;
    private String image;
    private String sku;
    private String description;
    private double price;
    private int qty;
    private boolean status;
    private Map<String, String> variationsOptions;
    private Map<String,Discount> discount;

    public Product() {
    }

    public Product(String categoryId, String name, String image, String sku, String description, double price, int qty, boolean status, Map<String, String> variationsOptions,Map<String,Discount>discount) {
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.qty = qty;
        this.status = status;
        this.variationsOptions = variationsOptions;
        this.discount = discount;
    }

    public Map<String, Discount> getDiscount() {
        return discount;
    }

    public void setDiscount(Map<String, Discount> discount) {
        this.discount = discount;
    }

    public Map<String, String> getVariationsOptions() {
        return variationsOptions;
    }

    public void setVariationsOptions(Map<String, String> variationsOptions) {
        this.variationsOptions = variationsOptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


}
