package com.smartbus.algorithm;

public class ConnectivityAnalyzerTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // Add bus stops
        graph.addVertex(new Vertex("ST01", "SVECW"));
        graph.addVertex(new Vertex("ST02", "Railway Station"));
        graph.addVertex(new Vertex("ST03", "Town Hall"));
        graph.addVertex(new Vertex("ST04", "Market"));
        graph.addVertex(new Vertex("ST05", "Bus Stand"));

        // Add roads
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
                "ST03", "ST04",
                1.0, 4, 5,
                "LOW", false
        );

        graph.addSmartUndirectedEdge(
                "ST04", "ST05",
                1.1, 5, 10,
                "LOW", false
        );

        ConnectivityAnalyzer analyzer =
                new ConnectivityAnalyzer();

        System.out.println(
                "===== CONNECTIVITY ANALYZER TEST ====="
        );

        // Test 1: Complete connected network
        System.out.println();
        System.out.println("Test 1: Normal Network");

        boolean connected =
                analyzer.isConnected(
                        graph,
                        "ST01"
                );

        System.out.println(
                "Connected: " + connected
        );

        System.out.println(
                "Reachable Stops: "
                + analyzer.reachableStops(
                        graph,
                        "ST01"
                )
        );

        System.out.println(
                "Stops: "
                + analyzer.getReachableStopNames(
                        graph,
                        "ST01"
                )
        );

        // Test 2: Block a critical road
        System.out.println();
        System.out.println(
                "Test 2: After Blocking Railway Station -> Town Hall"
        );

        graph.blockEdge("ST02", "ST03");
        graph.blockEdge("ST03", "ST02");

        connected =
                analyzer.isConnected(
                        graph,
                        "ST01"
                );

        System.out.println(
                "Connected: " + connected
        );

        System.out.println(
                "Reachable Stops: "
                + analyzer.reachableStops(
                        graph,
                        "ST01"
                )
        );

        System.out.println(
                "Stops: "
                + analyzer.getReachableStopNames(
                        graph,
                        "ST01"
                )
        );

        System.out.println(
                "======================================="
        );
    }
}