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
    private String housImg;
    private String houseDes;

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

    public String getHousImg() {
        return housImg;
    }

    public void setHousImg(String housImg) {
        this.housImg = housImg;
    }

    public String getHouseDes() {
        return houseDes;
    }

    public void setHouseDes(String houseDes) {
        this.houseDes = houseDes;
    }
}