package com.example.bookingluu.Restaurant;

public class Reservation {
    private int bookingNo;
    private String Date,Time,Pax,Food,customerID,customerName, customerPhoneNo,customerEmail, customerNotes,status;

    public Reservation(int bookingNo, String date, String time, String pax, String food, String customerID, String customerName, String customerPhoneNo, String customerEmail, String customerNotes, String status) {
        this.bookingNo = bookingNo;
        Date = date;
        Time = time;
        Pax = pax;
        Food = food;
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.customerEmail = customerEmail;
        this.customerNotes = customerNotes;
        this.status="Pending";
    }

    public Reservation() {
    }

    public int getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(int bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPax() {
        return Pax;
    }

    public void setPax(String pax) {
        Pax = pax;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String food) {
        Food = food;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
