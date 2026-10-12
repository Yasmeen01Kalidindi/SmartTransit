package com.smartbus.dto;

public class BusStopResponse {

    private String id;
    private String name;
    private String location;

    private int connectedRoads;

    private String status;

    private double latitude;
    private double longitude;

    public BusStopResponse() {
    }

    public BusStopResponse(
            String id,
            String name,
            String location,
            int connectedRoads,
            String status,
            double latitude,
            double longitude) {

        this.id = id;
        this.name = name;
        this.location = location;
        this.connectedRoads = connectedRoads;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getConnectedRoads() {
        return connectedRoads;
    }

    public void setConnectedRoads(int connectedRoads) {
        this.connectedRoads = connectedRoads;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}