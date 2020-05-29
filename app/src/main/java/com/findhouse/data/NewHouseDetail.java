package com.findhouse.data;

import java.io.Serializable;

public class NewHouseDetail implements Serializable {
    private String houseId;
    private String houseOpen;
    private String houseArea;
    private String houseType;
    private String houseApartment;
    private String houseImg;
    private String houseDes;
    private String apartmentArea;
    private String apartmentPrice;
    private String apartmentImg;
    private String uid;
    private String userName;
    private String userTel;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseOpen() {
        return houseOpen;
    }

    public void setHouseOpen(String houseOpen) {
        this.houseOpen = houseOpen;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getHouseApartment() {
        return houseApartment;
    }

    public void setHouseApartment(String houseApartment) {
        this.houseApartment = houseApartment;
    }

    public String getHouseImg() {
        return houseImg;
    }

    public void setHouseImg(String houseImg) {
        this.houseImg = houseImg;
    }

    public String getHouseDes() {
        return houseDes;
    }

    public void setHouseDes(String houseDes) {
        this.houseDes = houseDes;
    }

    public String getApartmentArea() {
        return apartmentArea;
    }

    public void setApartmentArea(String apartmentArea) {
        this.apartmentArea = apartmentArea;
    }

    public String getApartmentPrice() {
        return apartmentPrice;
    }

    public void setApartmentPrice(String apartmentPrice) {
        this.apartmentPrice = apartmentPrice;
    }

    public String getApartmentImg() {
        return apartmentImg;
    }

    public void setApartmentImg(String apartmentImg) {
        this.apartmentImg = apartmentImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
