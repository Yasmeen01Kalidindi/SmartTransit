package com.smartbus.algorithm;

public class DijkstraPreferenceTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        graph.addVertex(new Vertex("ST01", "SVECW"));
        graph.addVertex(new Vertex("ST02", "Railway Station"));
        graph.addVertex(new Vertex("ST03", "Market"));
        graph.addVertex(new Vertex("ST04", "Bhimavaram Bus Stand"));

        // Route 1
        graph.addSmartUndirectedEdge(
                "ST01", "ST02",
                2.0, 10.0, 10.0,
                "LOW", false
        );

        graph.addSmartUndirectedEdge(
                "ST02", "ST04",
                2.0, 10.0, 10.0,
                "LOW", false
        );

        // Route 2
        graph.addSmartUndirectedEdge(
                "ST01", "ST03",
                3.0, 5.0, 20.0,
                "HIGH", false
        );

        graph.addSmartUndirectedEdge(
                "ST03", "ST04",
                3.0, 5.0, 20.0,
                "HIGH", false
        );

        Dijkstra dijkstra = new Dijkstra();

        System.out.println();
        System.out.println("===== DIJKSTRA PREFERENCE TEST =====");

        RouteResult shortest =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST04",
                        "SHORTEST"
                );

        System.out.println(
                "Shortest: " + shortest.getPath()
        );

        RouteResult fastest =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST04",
                        "FASTEST"
                );

        System.out.println(
                "Fastest: " + fastest.getPath()
        );

        RouteResult cheapest =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST04",
                        "CHEAPEST"
                );

        System.out.println(
                "Cheapest: " + cheapest.getPath()
        );

        RouteResult trafficAware =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST04",
                        "TRAFFIC_AWARE"
                );

        System.out.println(
                "Traffic-Aware: "
                + trafficAware.getPath()
        );

        System.out.println(
                "===================================="
        );
    }
}