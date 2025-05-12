package com.example.glimmerheaven.data.model;

import java.util.ArrayList;

public class Customer {
    private String fname,lname,email,cnt,imgUrl;
    private long birthDay;
    private long craetedDate,updatedDate;
    private boolean status;
    private ArrayList<CartItem> cart = new ArrayList<>();
    private ArrayList<String> wishList = new ArrayList<>();
    private String gender;
    private ArrayList<Address> address = new ArrayList<>();
    private ArrayList<Card> card = new ArrayList<>();
    private ArrayList<String> orders = new ArrayList<>();
    private String FCMToken;

    public Customer() {
    }

    public Customer(String fname, String lname, String email, String cnt, long birthDay, long craetedDate,
                    long updatedDate, boolean status, ArrayList<CartItem> cart, ArrayList<String> wishList,
                    String gender, ArrayList<Address> address, ArrayList<Card> card,ArrayList<String> orders,
                    String FCMToken,String imgUrl) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.cnt = cnt;
        this.birthDay = birthDay;
        this.craetedDate = craetedDate;
        this.updatedDate = updatedDate;
        this.status = status;
        this.cart = cart;
        this.wishList = wishList;
        this.gender = gender;
        this.address = address;
        this.card = card;
        this.orders = orders;
        this.FCMToken = FCMToken;
        this.imgUrl = imgUrl;
    }

    public void setOrders(ArrayList<String> orders) {
        this.orders = orders;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrder(ArrayList<String> orders) {
        this.orders = orders;
    }

    public ArrayList<Card> getCard() {
        return card;
    }

    public void setCard(ArrayList<Card> card) {
        this.card = card;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public long getCraetedDate() {
        return craetedDate;
    }

    public void setCraetedDate(long craetedDate) {
        this.craetedDate = craetedDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartItem> cart) {
        this.cart = cart;
    }

    public ArrayList<String> getWishList() {
        return wishList;
    }

    public void setWishList(ArrayList<String> wishList) {
        this.wishList = wishList;
    }
}
