package com.smartbus.dto;

public class RouteRequest {

    private String source;
    private String destination;

    // SHORTEST, FASTEST, CHEAPEST, TRAFFIC_AWARE
    private String preference;

    // LOW, MEDIUM, HIGH
    private String trafficLevel;

    // MORNING_PEAK, AFTERNOON, EVENING_PEAK, NIGHT
    private String timePeriod;

    public RouteRequest() {
    }

    public RouteRequest(
            String source,
            String destination,
            String preference,
            String trafficLevel,
            String timePeriod) {

        this.source = source;
        this.destination = destination;
        this.preference = preference;
        this.trafficLevel = trafficLevel;
        this.timePeriod = timePeriod;
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

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
}