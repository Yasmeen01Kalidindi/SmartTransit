package com.smartbus.algorithm;

public class Edge {

    private Vertex source;
    private Vertex destination;

    // Distance between two bus stops
    private double weight;

    // Travel time in minutes
    private double travelTime;

    // Fare for this connection
    private double fare;

    // LOW, MEDIUM, HIGH
    private String trafficLevel;

    // Whether the road is blocked
    private boolean blocked;


    // Old constructor
    // Kept so your existing Dijkstra, Prim,
    // Kruskal and test files continue working.
    public Edge(
            Vertex source,
            Vertex destination,
            double weight,
            boolean blocked) {

        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.blocked = blocked;

        // Default values
        this.travelTime = 0;
        this.fare = 0;
        this.trafficLevel = "LOW";
    }


    // New constructor for SmartTransit
    public Edge(
            Vertex source,
            Vertex destination,
            double weight,
            double travelTime,
            double fare,
            String trafficLevel,
            boolean blocked) {

        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.travelTime = travelTime;
        this.fare = fare;
        this.trafficLevel = trafficLevel;
        this.blocked = blocked;
    }


    public Vertex getSource() {
        return source;
    }


    public Vertex getDestination() {
        return destination;
    }


    public double getWeight() {
        return weight;
    }


    public double getTravelTime() {
        return travelTime;
    }


    public double getFare() {
        return fare;
    }


    public String getTrafficLevel() {
        return trafficLevel;
    }


    public boolean isBlocked() {
        return blocked;
    }


    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }


    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }


    public void setFare(double fare) {
        this.fare = fare;
    }


    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }


    @Override
    public String toString() {

        return source.getName()
                + " -> "
                + destination.getName()
                + " ("
                + weight
                + " km)";
    }
}