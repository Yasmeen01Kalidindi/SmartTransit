package com.smartbus.dto;

import java.util.List;

public class RouteOptionResponse {

    private String type;

    private List<String> route;

    private double distance;
    private double travelTime;
    private double fare;

    private int stops;
    private int transfers;

    private String trafficLevel;

    public RouteOptionResponse() {
    }

    public RouteOptionResponse(
            String type,
            List<String> route,
            double distance,
            double travelTime,
            double fare,
            int stops,
            int transfers,
            String trafficLevel) {

        this.type = type;
        this.route = route;
        this.distance = distance;
        this.travelTime = travelTime;
        this.fare = fare;
        this.stops = stops;
        this.transfers = transfers;
        this.trafficLevel = trafficLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public int getTransfers() {
        return transfers;
    }

    public void setTransfers(int transfers) {
        this.transfers = transfers;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }
}