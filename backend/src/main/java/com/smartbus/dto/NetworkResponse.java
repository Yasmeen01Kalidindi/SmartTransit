package com.smartbus.dto;

import java.util.List;

public class NetworkResponse {

    private int totalStops;
    private int totalRoads;

    private String mostConnectedStop;
    private String leastConnectedStop;

    private double totalNetworkDistance;

    private int blockedRoads;
    private int activeBuses;

    private boolean connected;

    private int reachableStops;
    private List<String> reachableStopNames;

    public NetworkResponse() {
    }

    public NetworkResponse(
            int totalStops,
            int totalRoads,
            String mostConnectedStop,
            String leastConnectedStop,
            double totalNetworkDistance,
            int blockedRoads,
            int activeBuses,
            boolean connected,
            int reachableStops,
            List<String> reachableStopNames) {

        this.totalStops = totalStops;
        this.totalRoads = totalRoads;
        this.mostConnectedStop = mostConnectedStop;
        this.leastConnectedStop = leastConnectedStop;
        this.totalNetworkDistance = totalNetworkDistance;
        this.blockedRoads = blockedRoads;
        this.activeBuses = activeBuses;
        this.connected = connected;
        this.reachableStops = reachableStops;
        this.reachableStopNames = reachableStopNames;
    }

    public int getTotalStops() {
        return totalStops;
    }

    public void setTotalStops(int totalStops) {
        this.totalStops = totalStops;
    }

    public int getTotalRoads() {
        return totalRoads;
    }

    public void setTotalRoads(int totalRoads) {
        this.totalRoads = totalRoads;
    }

    public String getMostConnectedStop() {
        return mostConnectedStop;
    }

    public void setMostConnectedStop(String mostConnectedStop) {
        this.mostConnectedStop = mostConnectedStop;
    }

    public String getLeastConnectedStop() {
        return leastConnectedStop;
    }

    public void setLeastConnectedStop(String leastConnectedStop) {
        this.leastConnectedStop = leastConnectedStop;
    }

    public double getTotalNetworkDistance() {
        return totalNetworkDistance;
    }

    public void setTotalNetworkDistance(double totalNetworkDistance) {
        this.totalNetworkDistance = totalNetworkDistance;
    }

    public int getBlockedRoads() {
        return blockedRoads;
    }

    public void setBlockedRoads(int blockedRoads) {
        this.blockedRoads = blockedRoads;
    }

    public int getActiveBuses() {
        return activeBuses;
    }

    public void setActiveBuses(int activeBuses) {
        this.activeBuses = activeBuses;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getReachableStops() {
        return reachableStops;
    }

    public void setReachableStops(int reachableStops) {
        this.reachableStops = reachableStops;
    }

    public List<String> getReachableStopNames() {
        return reachableStopNames;
    }

    public void setReachableStopNames(
            List<String> reachableStopNames) {

        this.reachableStopNames = reachableStopNames;
    }
}