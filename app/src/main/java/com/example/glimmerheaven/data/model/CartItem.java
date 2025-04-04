package com.example.glimmerheaven.data.model;

public class CartItem {
    private String productId;
    private int qty;

    public CartItem() {
    }

    public CartItem(String productId, int qty) {
        this.productId = productId;
        this.qty = qty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
