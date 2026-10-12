package com.smartbus.algorithm;

import java.util.List;

public class RouteResult {

    private List<Vertex> path;
    private double distance;

    public RouteResult(List<Vertex> path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<Vertex> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }
}