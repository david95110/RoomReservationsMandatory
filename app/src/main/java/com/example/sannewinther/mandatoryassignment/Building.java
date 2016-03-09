package com.example.sannewinther.mandatoryassignment;

import java.io.Serializable;

public class Building implements Serializable {
    private int buildingId;
    private String name;
    private String address;
    private int cityId;

    public Building(int buildingId, String name, String address, int cityId) {
        this.buildingId = buildingId;
        this.name = name;
        this.address = address;
        this.cityId = cityId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCityId() {
        return cityId;
    }

    @Override
    public String toString() {
        return name;
    }
}
