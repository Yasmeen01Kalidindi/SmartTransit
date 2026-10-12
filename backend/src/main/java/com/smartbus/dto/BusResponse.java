package com.smartbus.dto;

public class BusResponse {

    private String number;
    private String route;

    private int capacity;
    private int occupied;
    private int available;

    private String status;

    public BusResponse() {
    }

    public BusResponse(
            String number,
            String route,
            int capacity,
            int occupied,
            int available,
            String status) {

        this.number = number;
        this.route = route;
        this.capacity = capacity;
        this.occupied = occupied;
        this.available = available;
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}