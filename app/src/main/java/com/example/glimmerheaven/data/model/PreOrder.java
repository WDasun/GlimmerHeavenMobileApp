package com.example.glimmerheaven.data.model;

import java.util.ArrayList;
import java.util.List;

public class PreOrder {
    private Address address;
    private String cnt;
    private String paymentType;
    private String additionalNote;
    private EnteredCard enteredCard = new EnteredCard();
    private ArrayList<CartItem> selectedCartItemList = new ArrayList<>();
    private List<OrderItem> orderItemList;
    private String shippingType;
    private String onlinePaymentDetails;
    private double total;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getOnlinePaymentDetails() {
        return onlinePaymentDetails;
    }

    public void setOnlinePaymentDetails(String onlinePaymentDetails) {
        this.onlinePaymentDetails = onlinePaymentDetails;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public ArrayList<CartItem> getSelectedCartItemList() {
        return selectedCartItemList;
    }

    public void setSelectedCartItemList(ArrayList<CartItem> selectedCartItemList) {
        this.selectedCartItemList = selectedCartItemList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public class EnteredCard{
        private Card card;
        private int cvv;

        public EnteredCard() {
        }

        public EnteredCard(Card card, int cvv) {
            this.card = card;
            this.cvv = cvv;
        }

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public int getCvv() {
            return cvv;
        }

        public void setCvv(int cvv) {
            this.cvv = cvv;
        }
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public EnteredCard getEnteredCard() {
        return enteredCard;
    }

    public void setEnteredCard(EnteredCard enteredCard) {
        this.enteredCard = enteredCard;
    }
}
