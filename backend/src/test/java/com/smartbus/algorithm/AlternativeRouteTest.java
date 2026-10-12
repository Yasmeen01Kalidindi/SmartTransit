package com.smartbus.algorithm;

import java.util.List;

public class AlternativeRouteTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // Add bus stops
        graph.addVertex(new Vertex("ST01", "SVECW"));
        graph.addVertex(new Vertex("ST02", "Railway Station"));
        graph.addVertex(new Vertex("ST03", "Town Hall"));
        graph.addVertex(new Vertex("ST04", "Market"));
        graph.addVertex(new Vertex("ST05", "Bus Stand"));

        // Main route
        graph.addSmartUndirectedEdge(
                "ST01", "ST02",
                2.0, 8, 10,
                "LOW", false
        );

        graph.addSmartUndirectedEdge(
                "ST02", "ST03",
                1.2, 5, 5,
                "LOW", false
        );

        graph.addSmartUndirectedEdge(
                "ST03", "ST05",
                2.0, 8, 10,
                "LOW", false
        );

        // Alternative route
        graph.addSmartUndirectedEdge(
                "ST01", "ST04",
                2.5, 9, 12,
                "LOW", false
        );

        graph.addSmartUndirectedEdge(
                "ST04", "ST05",
                1.5, 6, 8,
                "LOW", false
        );

        // Additional connection
        graph.addSmartUndirectedEdge(
                "ST02", "ST04",
                1.7, 7, 8,
                "LOW", false
        );

        AlternativeRoute alternativeRoute =
                new AlternativeRoute();

        List<RouteResult> routes =
                alternativeRoute.findAlternatives(
                        graph,
                        "ST01",
                        "ST05"
                );

        System.out.println(
                "===== ALTERNATIVE ROUTE TEST ====="
        );

        for (int i = 0; i < routes.size(); i++) {

            RouteResult route = routes.get(i);

            System.out.println(
                    "Route " + (i + 1) + ": "
                    + route.getPath()
            );

            System.out.println(
                    "Distance: "
                    + route.getDistance()
                    + " km"
            );

            System.out.println();
        }

        System.out.println(
                "Total Routes Found: "
                + routes.size()
        );

        System.out.println(
                "=================================="
        );
    }
}