package com.example.bookingluu.Customer;

public class Customer {
    private String fullName, email, phoneNumber,image;

    public Customer(String fullName, String email, String phoneNumber, String image) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
