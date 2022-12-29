package com.example.bookingluu.Restaurant;

public class Reservation {
    private int bookingNo,Pax,TableNo;
    private String Date,Time,Food,customerID,customerName, customerPhoneNo,customerEmail, customerNotes,status, restaurantName;

    public Reservation(int bookingNo, int pax,int tableNo, String date, String time, String food, String customerID, String customerName, String customerPhoneNo, String customerEmail, String customerNotes, String restaurantName) {
        this.bookingNo = bookingNo;
        Pax = pax;
        Date = date;
        Time = time;
        Food = food;
        this.TableNo=tableNo;
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.customerEmail = customerEmail;
        this.customerNotes = customerNotes;
        this.restaurantName=restaurantName;
        this.status = "Pending";
    }

    public int getTableNo() {
        return TableNo;
    }

    public void setTableNo(int tableNo) {
        TableNo = tableNo;
    }

    public int getBookingNo() {
        return bookingNo;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setBookingNo(int bookingNo) {
        this.bookingNo = bookingNo;
    }

    public int getPax() {
        return Pax;
    }

    public void setPax(int pax) {
        Pax = pax;
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
