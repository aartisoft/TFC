package com.dev.tfc.model;

/**
 * Created by amit on 1/20/2018.
 */

public class OrderHistoryModel {
    String proname;
    String quantity;
    String price;
    String username;
    String date;
    String logo;

    public int getDelimg() {
        return delimg;
    }

    public void setDelimg(int delimg) {
        this.delimg = delimg;
    }

    int delimg;

    public String getDel() {
        return Del;
    }

    public void setDel(String del) {
        Del = del;
    }

    String Del;

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    String deliver;

    public OrderHistoryModel() {
        this.proname = proname;

        this.quantity = quantity;
        this.price = price;
        this.username = username;
        this.date = date;
        this.logo = logo;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }



    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
