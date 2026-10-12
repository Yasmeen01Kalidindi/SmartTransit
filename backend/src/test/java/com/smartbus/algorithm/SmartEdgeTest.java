package com.smartbus.algorithm;

public class SmartEdgeTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // Add bus stops
        graph.addVertex(
                new Vertex("ST01", "SVECW")
        );

        graph.addVertex(
                new Vertex("ST02", "Railway Station")
        );

        // Add a SmartTransit road
        graph.addSmartUndirectedEdge(
                "ST01",
                "ST02",
                2.0,
                6.0,
                10.0,
                "LOW",
                false
        );

        // Get the connection from SVECW
        Edge edge =
                graph.getEdges("ST01").get(0);

        System.out.println();
        System.out.println(
                "===== SMARTTRANSIT EDGE TEST ====="
        );

        System.out.println(
                "From: "
                + edge.getSource().getName()
        );

        System.out.println(
                "To: "
                + edge.getDestination().getName()
        );

        System.out.println(
                "Distance: "
                + edge.getWeight()
                + " km"
        );

        System.out.println(
                "Travel Time: "
                + edge.getTravelTime()
                + " minutes"
        );

        System.out.println(
                "Fare: ₹"
                + edge.getFare()
        );

        System.out.println(
                "Traffic: "
                + edge.getTrafficLevel()
        );

        System.out.println(
                "Blocked: "
                + edge.isBlocked()
        );

        System.out.println(
                "==================================="
        );
    }
}