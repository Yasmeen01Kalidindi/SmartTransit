package com.smartbus.algorithm;

public class DynamicGraphTest {

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

        Dijkstra dijkstra = new Dijkstra();

        System.out.println(
                "===== DYNAMIC GRAPH TEST ====="
        );

        // 1. Normal route
        RouteResult normalRoute =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST05",
                        "SHORTEST"
                );

        System.out.println("Normal Route:");
        System.out.println(normalRoute.getPath());
        System.out.println(
                "Distance: "
                + normalRoute.getDistance()
                + " km"
        );

        // 2. Block a road
        System.out.println();
        System.out.println(
                "Blocking road: SVECW -> Market"
        );

        graph.blockEdge("ST01", "ST04");

        // 3. Find route again
        RouteResult rerouted =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST05",
                        "SHORTEST"
                );

        System.out.println();
        System.out.println("Rerouted Path:");
        System.out.println(rerouted.getPath());
        System.out.println(
                "Distance: "
                + rerouted.getDistance()
                + " km"
        );

        // 4. Restore the road
        System.out.println();
        System.out.println(
                "Restoring road: SVECW -> Market"
        );

        graph.restoreEdge("ST01", "ST04");

        // 5. Find route again
        RouteResult restoredRoute =
                dijkstra.findRoute(
                        graph,
                        "ST01",
                        "ST05",
                        "SHORTEST"
                );

        System.out.println();
        System.out.println("After Restoration:");
        System.out.println(
                restoredRoute.getPath()
        );
        System.out.println(
                "Distance: "
                + restoredRoute.getDistance()
                + " km"
        );

        System.out.println(
                "================================"
        );
    }
}