package com.smartbus.dto;

import java.util.List;

public class RouteResponse {

    private boolean success;

    private String source;
    private String destination;

    private List<String> route;

    private double distance;
    private double travelTime;
    private double fare;

    private int stops;

    private String trafficLevel;

    private String preference;
    private String timePeriod;

    public RouteResponse() {
    }

    public RouteResponse(
            boolean success,
            String source,
            String destination,
            List<String> route,
            double distance,
            double travelTime,
            double fare,
            int stops,
            String trafficLevel,
            String preference,
            String timePeriod) {

        this.success = success;
        this.source = source;
        this.destination = destination;
        this.route = route;
        this.distance = distance;
        this.travelTime = travelTime;
        this.fare = fare;
        this.stops = stops;
        this.trafficLevel = trafficLevel;
        this.preference = preference;
        this.timePeriod = timePeriod;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
}