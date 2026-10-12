package com.smartbus.algorithm;

public class NetworkAnalyzerTest {

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

        graph.addSmartUndirectedEdge(
                "ST02", "ST04",
                1.7, 7, 8,
                "LOW", false
        );

        NetworkAnalyzer analyzer =
                new NetworkAnalyzer();

        System.out.println(
                "===== NETWORK ANALYZER TEST ====="
        );

        System.out.println();

        System.out.println(
                "Total Bus Stops: "
                + analyzer.getTotalStops(graph)
        );

        System.out.println(
                "Total Roads: "
                + analyzer.getTotalRoads(graph)
        );

        Vertex mostConnected =
                analyzer.getMostConnectedStop(graph);

        System.out.println(
                "Most Connected Stop: "
                + mostConnected.getName()
        );

        Vertex leastConnected =
                analyzer.getLeastConnectedStop(graph);

        System.out.println(
                "Least Connected Stop: "
                + leastConnected.getName()
        );

        System.out.println(
                "Total Active Network Distance: "
                + analyzer.getTotalNetworkDistance(graph)
                + " km"
        );

        System.out.println(
                "Blocked Roads: "
                + analyzer.getBlockedRoads(graph)
        );

        // Block one road
        System.out.println();
        System.out.println(
                "Blocking road: Railway Station -> Market"
        );

        graph.blockEdge("ST02", "ST04");
        graph.blockEdge("ST04", "ST02");

        System.out.println(
                "Blocked Roads After Update: "
                + analyzer.getBlockedRoads(graph)
        );

        System.out.println(
                "Active Network Distance After Update: "
                + analyzer.getTotalNetworkDistance(graph)
                + " km"
        );

        System.out.println(
                "================================="
        );
    }
}