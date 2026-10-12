package com.smartbus.algorithm;

import java.util.List;

public class RouteMetricsTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        graph.addVertex(new Vertex("ST01", "SVECW"));
        graph.addVertex(new Vertex("ST02", "Railway Station"));
        graph.addVertex(new Vertex("ST03", "Market"));
        graph.addVertex(new Vertex("ST04", "Bhimavaram Bus Stand"));

        graph.addSmartUndirectedEdge(
                "ST01", "ST02",
                2.0, 6.0, 10.0,
                "LOW", false
        );

        graph.addSmartUndirectedEdge(
                "ST02", "ST03",
                1.7, 5.0, 8.0,
                "MEDIUM", false
        );

        graph.addSmartUndirectedEdge(
                "ST03", "ST04",
                1.1, 4.0, 10.0,
                "LOW", false
        );

        Dijkstra dijkstra = new Dijkstra();

        RouteResult result =
                dijkstra.findShortestPath(
                        graph,
                        "ST01",
                        "ST04"
                );

        List<Vertex> path = result.getPath();

        RouteMetrics metrics =
                RouteMetrics.calculate(graph, path);

        System.out.println();
        System.out.println("===== ROUTE METRICS TEST =====");

        System.out.println(
                "Route: " + path
        );

        System.out.println(
                "Distance: "
                + metrics.getDistance()
                + " km"
        );

        System.out.println(
                "Travel Time: "
                + metrics.getTravelTime()
                + " minutes"
        );

        System.out.println(
                "Fare: ₹"
                + metrics.getFare()
        );

        System.out.println(
                "Traffic: "
                + metrics.getTrafficLevel()
        );

        System.out.println(
                "=============================="
        );
    }
}