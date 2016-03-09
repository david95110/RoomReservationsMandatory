package com.example.sannewinther.mandatoryassignment;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomId;
    private String name;
    private String description;
    private int capacity;
    private String remarks;
    private int buildingId;

    public Room(int roomId, String name, String description, int capacity, String remarks, int buildingId) {
        this.roomId = roomId;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.remarks = remarks;
        this.buildingId = buildingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getBuildingId() {
        return buildingId;
    }

    @Override
    public String toString() {
        return name;
    }
}
