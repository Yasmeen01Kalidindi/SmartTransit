package com.smartbus.algorithm;

import com.smartbus.enums.TimePeriod;

public class RouteMetrics {

    private double distance;
    private double travelTime;
    private double fare;
    private String trafficLevel;

    public RouteMetrics(double distance,
                        double travelTime,
                        double fare,
                        String trafficLevel) {

        this.distance = distance;
        this.travelTime = travelTime;
        this.fare = fare;
        this.trafficLevel = trafficLevel;
    }

    public static RouteMetrics calculate(
            Graph graph,
            java.util.List<Vertex> path) {

        return calculate(
                graph,
                path,
                null
        );
    }

    public static RouteMetrics calculate(
            Graph graph,
            java.util.List<Vertex> path,
            TimePeriod timePeriod) {

        double totalDistance = 0.0;
        double totalTravelTime = 0.0;
        double totalFare = 0.0;

        String highestTraffic = "LOW";

        for (int i = 0; i < path.size() - 1; i++) {

            String fromId = path.get(i).getId();
            String toId = path.get(i + 1).getId();

            Edge selectedEdge = null;

            for (Edge edge : graph.getEdges(fromId)) {

                if (edge.getDestination()
                        .getId()
                        .equals(toId)) {

                    selectedEdge = edge;
                    break;
                }
            }

            if (selectedEdge == null) {
                continue;
            }

            // Distance
            totalDistance += selectedEdge.getWeight();

            // Travel time
            if (timePeriod != null) {

                totalTravelTime +=
                        TimeBasedRouting.getEffectiveTravelTime(
                                selectedEdge.getTravelTime(),
                                selectedEdge.getTrafficLevel(),
                                timePeriod
                        );

            } else {

                totalTravelTime +=
                        selectedEdge.getTravelTime();
            }

            // Fare
            totalFare += selectedEdge.getFare();

            // Highest traffic level
            String traffic =
                    selectedEdge.getTrafficLevel();

            if ("HIGH".equalsIgnoreCase(traffic)) {

                highestTraffic = "HIGH";

            } else if ("MEDIUM".equalsIgnoreCase(traffic)
                    && !"HIGH".equalsIgnoreCase(highestTraffic)) {

                highestTraffic = "MEDIUM";
            }
        }

        return new RouteMetrics(
                totalDistance,
                totalTravelTime,
                totalFare,
                highestTraffic
        );
    }

    public double getDistance() {
        return distance;
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
}