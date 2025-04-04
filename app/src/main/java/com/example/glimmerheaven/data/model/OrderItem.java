package com.example.glimmerheaven.data.model;

import java.util.Map;

public class OrderItem {
    private String productId;
    private String discountId;
    private double unitPrice;
    private int qty;

    public OrderItem() {
    }

    public OrderItem(String productId, String discountId, double unitPrice, int qty) {
        this.productId = productId;
        this.discountId = discountId;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
