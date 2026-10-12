package com.smartbus.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "traffic")
public class Traffic {

    @Id
    private String id;

    private String connectionId;

    private String trafficLevel;

    private double timeMultiplier;

    public Traffic() {
    }

    public Traffic(String id, String connectionId,
                   String trafficLevel, double timeMultiplier) {
        this.id = id;
        this.connectionId = connectionId;
        this.trafficLevel = trafficLevel;
        this.timeMultiplier = timeMultiplier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public double getTimeMultiplier() {
        return timeMultiplier;
    }

    public void setTimeMultiplier(double timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
    }
}