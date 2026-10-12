package com.smartbus.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "connections")
public class Connection {

    @Id
    private String id;

    private String fromStop;

    private String toStop;

    private double distance;

    private double travelTime;

    private double fare;

    private String trafficLevel;

    private boolean blocked;

    public Connection() {
    }

    public Connection(String id, String fromStop, String toStop,
                      double distance, double travelTime,
                      double fare, String trafficLevel,
                      boolean blocked) {

        this.id = id;
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.distance = distance;
        this.travelTime = travelTime;
        this.fare = fare;
        this.trafficLevel = trafficLevel;
        this.blocked = blocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getToStop() {
        return toStop;
    }

    public void setToStop(String toStop) {
        this.toStop = toStop;
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

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}