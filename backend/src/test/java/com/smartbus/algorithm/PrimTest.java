package com.smartbus.algorithm;

import java.util.List;

public class PrimTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // Add bus stops
        graph.addVertex(new Vertex("ST01", "SVECW"));
        graph.addVertex(new Vertex("ST02", "Railway Station"));
        graph.addVertex(new Vertex("ST03", "Town Hall"));
        graph.addVertex(new Vertex("ST04", "Market"));
        graph.addVertex(new Vertex("ST05", "Bhimavaram Bus Stand"));
        graph.addVertex(new Vertex("ST06", "College Road"));

        // Add roads
        graph.addUndirectedEdge("ST01", "ST02", 2.0, false);
        graph.addUndirectedEdge("ST01", "ST06", 1.4, false);

        graph.addUndirectedEdge("ST02", "ST03", 1.2, false);
        graph.addUndirectedEdge("ST02", "ST04", 1.7, false);

        graph.addUndirectedEdge("ST06", "ST03", 1.8, true);

        graph.addUndirectedEdge("ST03", "ST04", 1.0, false);
        graph.addUndirectedEdge("ST03", "ST05", 2.0, false);

        graph.addUndirectedEdge("ST04", "ST05", 1.1, false);

        // Run Prim's algorithm
        Prim prim = new Prim();

        List<Edge> mst = prim.findMST(graph);

        System.out.println();
        System.out.println("===== SMARTTRANSIT PRIM MST TEST =====");

        double totalDistance = 0;

        for (Edge edge : mst) {

            System.out.println(
                    edge.getSource().getName()
                    + " -> "
                    + edge.getDestination().getName()
                    + " : "
                    + edge.getWeight()
                    + " km"
            );

            totalDistance += edge.getWeight();
        }

        System.out.println("--------------------------------------");
        System.out.println(
                "Total MST Distance: "
                + totalDistance
                + " km"
        );

        System.out.println("======================================");
    }
}