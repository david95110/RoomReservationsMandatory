package com.example.danielwinther.androidroomreservations;

import java.io.Serializable;

public class City implements Serializable {
    private int cityId;
    private String name;

    public City(int cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
