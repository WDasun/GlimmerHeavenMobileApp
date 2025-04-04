package com.example.glimmerheaven.data.model;

import java.util.List;
import java.util.Map;

public class Order {
    private long orderDate;
    private String customerId;
    private String cnt;
    private String email;
    private Address address;
    private String paymentType;
    private String shippingType;
    private String additionalNote;
    private String onlinePaymentDetails;
    private List<OrderItem> orderItemList;
    private String orderStatus;
    private double total;

    public Order() {
    }

    public Order(long orderDate, String customerId, String cnt, String email, Address address, String paymentType, String shippingType, String additionalNote, String onlinePaymentDetails, List<OrderItem> orderItemList, String orderStatus,double total) {
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.cnt = cnt;
        this.email = email;
        this.address = address;
        this.paymentType = paymentType;
        this.shippingType = shippingType;
        this.additionalNote = additionalNote;
        this.onlinePaymentDetails = onlinePaymentDetails;
        this.orderItemList = orderItemList;
        this.orderStatus = orderStatus;
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public String getOnlinePaymentDetails() {
        return onlinePaymentDetails;
    }

    public void setOnlinePaymentDetails(String onlinePaymentDetails) {
        this.onlinePaymentDetails = onlinePaymentDetails;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
