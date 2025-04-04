package com.example.glimmerheaven.data.model;

public class Card {
    private String cardNumber;
    private String cardHolderName;
    private int expireYear;
    private int expireMonth;
    private String cardType;

    public Card() {
    }

    public Card(String cardNumber, String cardHolderName, int expireYear, int expireMonth, String cardType) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expireYear = expireYear;
        this.expireMonth = expireMonth;
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    public int getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
