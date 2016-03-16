package com.example.danielwinther.androidroomreservations;

import java.io.Serializable;

public class Reservation implements Serializable {
    private int reservationId;
    private String deviceId;
    private String purpose;
    private String FromTimeString;
    private String ToTimeString;
    private int roomId;
    private int userId;

    public Reservation(int reservationId, String deviceId, String purpose, String fromTimeString, String toTimeString, int roomId, int userId) {
        this.reservationId = reservationId;
        this.deviceId = deviceId;
        this.purpose = purpose;
        FromTimeString = fromTimeString;
        ToTimeString = toTimeString;
        this.roomId = roomId;
        this.userId = userId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getFromTimeString() {
        return FromTimeString;
    }

    public String getToTimeString() {
        return ToTimeString;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "From: " + FromTimeString + "\nTo: " + ToTimeString;
    }
}
