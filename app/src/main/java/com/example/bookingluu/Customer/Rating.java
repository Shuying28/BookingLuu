package com.example.bookingluu.Customer;

public class Rating {
    private String customerName, commentText, imgURI, rate;

    public Rating() {
    }

    public Rating(String customerName, String commentText, String imgURI, String rate) {
        this.customerName = customerName;
        this.commentText = commentText;
        this.imgURI = imgURI;
        this.rate = rate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getImgURI() {
        return imgURI;
    }

    public void setImgURI(String imgURI) {
        this.imgURI = imgURI;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
