package com.findhouse.data;

import java.io.Serializable;

public class HouseDetail implements Serializable {
    private String houseId;
    private String houseApartment;
    private String houseArea;
    private String houseFloor;
    private String houseFix;
    private String houseOrientation;
    private String houseInstall;
    private String houseImg;
    private String houseDes;
    private String uid;
    private String userName;
    private String userTel;
    private int isStar;

    public int getIsStar() {
        return isStar;
    }

    public void setIsStar(int isStar) {
        this.isStar = isStar;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseApartment() {
        return houseApartment;
    }

    public void setHouseApartment(String houseApartment) {
        this.houseApartment = houseApartment;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getHouseFloor() {
        return houseFloor;
    }

    public void setHouseFloor(String houseFloor) {
        this.houseFloor = houseFloor;
    }

    public String getHouseFix() {
        return houseFix;
    }

    public void setHouseFix(String houseFix) {
        this.houseFix = houseFix;
    }

    public String getHouseOrientation() {
        return houseOrientation;
    }

    public void setHouseOrientation(String houseOrientation) {
        this.houseOrientation = houseOrientation;
    }

    public String getHouseInstall() {
        return houseInstall;
    }

    public void setHouseInstall(String houseInstall) {
        this.houseInstall = houseInstall;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
