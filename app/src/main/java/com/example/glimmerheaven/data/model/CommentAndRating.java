package com.example.glimmerheaven.data.model;

public class CommentAndRating {
    private String userName;
    private String comment;
    private float rate;
    private Long changedDate;

    public CommentAndRating() {
    }

    public CommentAndRating(String userName, String comment, float rate, Long changedDate) {
        this.userName = userName;
        this.comment = comment;
        this.rate = rate;
        this.changedDate = changedDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Long changedDate) {
        this.changedDate = changedDate;
    }
}
