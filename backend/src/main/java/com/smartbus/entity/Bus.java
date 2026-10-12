package com.smartbus.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    private String number;

    private String route;

    private int capacity;

    private int occupied;

    public Bus() {
    }

    public Bus(String number, String route, int capacity, int occupied) {
        this.number = number;
        this.route = route;
        this.capacity = capacity;
        this.occupied = occupied;
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
}