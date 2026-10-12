package com.smartbus.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "route_stops")
public class RouteStop {

    @Id
    private String id;

    private String routeId;

    private String stopId;

    private int stopOrder;

    public RouteStop() {
    }

    public RouteStop(String id, String routeId, String stopId, int stopOrder) {
        this.id = id;
        this.routeId = routeId;
        this.stopId = stopId;
        this.stopOrder = stopOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }
}