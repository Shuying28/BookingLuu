package com.example.bookingluu.Restaurant;

public class Table {
    private int TableNo,Pax;
    private String Date;

    public Table(int tableNo, int pax, String date) {
        this.TableNo = tableNo;
        Pax = pax;
        Date = date;
    }

    public Table() {
    }

    public int getTableNo() {
        return TableNo;
    }

    public void setTableNo(int tableNo) {
        TableNo = tableNo;
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
}
