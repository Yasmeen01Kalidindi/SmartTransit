package com.smartbus.algorithm;

import com.smartbus.enums.TimePeriod;

public class TimeBasedDijkstraTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        graph.addVertex(new Vertex("ST01", "SVECW"));
        graph.addVertex(new Vertex("ST02", "Railway Station"));
        graph.addVertex(new Vertex("ST03", "Market"));
        graph.addVertex(new Vertex("ST04", "Bus Stand"));

        // Route 1: shorter normally
        graph.addSmartUndirectedEdge(
                "ST01", "ST02",
                2.0, 10, 10,
                "HIGH", false
        );

        graph.addSmartUndirectedEdge(
                "ST02", "ST04",
                2.0, 10, 10,
                "HIGH", false
        );

        // Route 2: slightly longer but low traffic
        graph.addSmartUndirectedEdge(
                "ST01", "ST03",
                2.5, 12, 12,
                "LOW", false
        );

        graph.addSmartUndirectedEdge(
                "ST03", "ST04",
                2.5, 12, 12,
                "LOW", false
        );

        Dijkstra dijkstra = new Dijkstra();

        RouteResult normalRoute =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST04",
                        "FASTEST"
                );

        RouteResult peakRoute =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST04",
                        "FASTEST",
                        TimePeriod.MORNING_PEAK
                );

        System.out.println(
                "Normal Route: "
                + normalRoute.getPath()
        );

        System.out.println(
                "Morning Peak Route: "
                + peakRoute.getPath()
        );

        System.out.println(
                "Normal Distance: "
                + normalRoute.getDistance()
                + " km"
        );

        System.out.println(
                "Peak Distance: "
                + peakRoute.getDistance()
                + " km"
        );
    }
}