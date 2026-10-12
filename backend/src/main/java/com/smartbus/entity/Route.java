package com.smartbus.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    private String id;

    private String busNumber;

    private String startStop;

    private String destinationStop;

    private double distance;

    private double travelTime;

    private double fare;

    private String status;

    public Route() {
    }

    public Route(String id, String busNumber, String startStop,
                 String destinationStop, double distance,
                 double travelTime, double fare, String status) {

        this.id = id;
        this.busNumber = busNumber;
        this.startStop = startStop;
        this.destinationStop = destinationStop;
        this.distance = distance;
        this.travelTime = travelTime;
        this.fare = fare;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStartStop() {
        return startStop;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public String getDestinationStop() {
        return destinationStop;
    }

    public void setDestinationStop(String destinationStop) {
        this.destinationStop = destinationStop;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}