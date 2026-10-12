package com.smartbus.algorithm;

import java.util.*;

public class NetworkAnalyzer {

    /*
     * Counts the number of bus stops in the network.
     */
    public int getTotalStops(Graph graph) {

        return graph.getVertices().size();
    }

    /*
     * Counts unique roads.
     *
     * Because an undirected road is stored as two
     * directed edges, we count each pair only once.
     */
    public int getTotalRoads(Graph graph) {

        Set<String> roads = new HashSet<>();

        for (Vertex vertex : graph.getVertices()) {

            for (Edge edge : graph.getEdges(vertex.getId())) {

                String source =
                        edge.getSource().getId();

                String destination =
                        edge.getDestination().getId();

                String road;

                if (source.compareTo(destination) < 0) {
                    road = source + "-" + destination;
                } else {
                    road = destination + "-" + source;
                }

                roads.add(road);
            }
        }

        return roads.size();
    }

    /*
     * Finds the most connected bus stop.
     */
    public Vertex getMostConnectedStop(Graph graph) {

        Vertex mostConnected = null;
        int maximumConnections = -1;

        for (Vertex vertex : graph.getVertices()) {

            int connections = 0;

            for (Edge edge : graph.getEdges(vertex.getId())) {

                if (!edge.isBlocked()) {
                    connections++;
                }
            }

            if (connections > maximumConnections) {

                maximumConnections = connections;
                mostConnected = vertex;
            }
        }

        return mostConnected;
    }

    /*
     * Finds the least connected bus stop.
     */
    public Vertex getLeastConnectedStop(Graph graph) {

        Vertex leastConnected = null;
        int minimumConnections = Integer.MAX_VALUE;

        for (Vertex vertex : graph.getVertices()) {

            int connections = 0;

            for (Edge edge : graph.getEdges(vertex.getId())) {

                if (!edge.isBlocked()) {
                    connections++;
                }
            }

            if (connections < minimumConnections) {

                minimumConnections = connections;
                leastConnected = vertex;
            }
        }

        return leastConnected;
    }

    /*
     * Calculates total distance of unique active roads.
     */
    public double getTotalNetworkDistance(Graph graph) {

        Set<String> countedRoads = new HashSet<>();

        double totalDistance = 0;

        for (Vertex vertex : graph.getVertices()) {

            for (Edge edge : graph.getEdges(vertex.getId())) {

                if (edge.isBlocked()) {
                    continue;
                }

                String source =
                        edge.getSource().getId();

                String destination =
                        edge.getDestination().getId();

                String road;

                if (source.compareTo(destination) < 0) {
                    road = source + "-" + destination;
                } else {
                    road = destination + "-" + source;
                }

                if (!countedRoads.contains(road)) {

                    totalDistance += edge.getWeight();

                    countedRoads.add(road);
                }
            }
        }

        return totalDistance;
    }

    /*
     * Counts currently blocked roads.
     */
    public int getBlockedRoads(Graph graph) {

        Set<String> blockedRoads = new HashSet<>();

        for (Vertex vertex : graph.getVertices()) {

            for (Edge edge : graph.getEdges(vertex.getId())) {

                if (!edge.isBlocked()) {
                    continue;
                }

                String source =
                        edge.getSource().getId();

                String destination =
                        edge.getDestination().getId();

                String road;

                if (source.compareTo(destination) < 0) {
                    road = source + "-" + destination;
                } else {
                    road = destination + "-" + source;
                }

                blockedRoads.add(road);
            }
        }

        return blockedRoads.size();
    }
}