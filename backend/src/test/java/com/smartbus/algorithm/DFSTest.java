package com.smartbus.algorithm;

import java.util.List;

public class DFSTest {

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
        graph.addEdge("ST01", "ST02", 2.0, false);
        graph.addEdge("ST01", "ST06", 1.4, false);

        graph.addEdge("ST02", "ST03", 1.2, false);
        graph.addEdge("ST02", "ST04", 1.7, false);

        graph.addEdge("ST06", "ST03", 1.8, true);

        graph.addEdge("ST03", "ST04", 1.0, false);
        graph.addEdge("ST03", "ST05", 2.0, false);

        graph.addEdge("ST04", "ST05", 1.1, false);

        // Run DFS from SVECW
        DFS dfs = new DFS();

        List<Vertex> result =
                dfs.traverse(graph, "ST01");

        System.out.println();
        System.out.println("===== SMARTTRANSIT DFS TEST =====");

        System.out.println("DFS Traversal:");

        for (int i = 0; i < result.size(); i++) {

            System.out.print(result.get(i).getName());

            if (i < result.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
        System.out.println("=================================");
    }
}