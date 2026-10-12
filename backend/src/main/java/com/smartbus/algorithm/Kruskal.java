package com.smartbus.algorithm;

import java.util.*;

public class Kruskal {

    public List<Edge> findMST(Graph graph) {

        List<Edge> allEdges = new ArrayList<>();

        // Collect all edges
        for (Vertex vertex : graph.getVertices()) {

            for (Edge edge : graph.getEdges(vertex.getId())) {

                if (!edge.isBlocked()) {
                    allEdges.add(edge);
                }
            }
        }

        // Sort edges by weight
        allEdges.sort(
            Comparator.comparingDouble(Edge::getWeight)
        );

        List<Edge> mst = new ArrayList<>();

        Map<String, String> parent = new HashMap<>();

        // Initially every vertex is its own parent
        for (Vertex vertex : graph.getVertices()) {
            parent.put(vertex.getId(), vertex.getId());
        }

        // Process edges from smallest to largest
        for (Edge edge : allEdges) {

            String source =
                    edge.getSource().getId();

            String destination =
                    edge.getDestination().getId();

            String sourceParent =
                    find(parent, source);

            String destinationParent =
                    find(parent, destination);

            // If different, adding this edge won't form a cycle
            if (!sourceParent.equals(destinationParent)) {

                mst.add(edge);

                parent.put(
                    sourceParent,
                    destinationParent
                );
            }

            // MST needs V - 1 edges
            if (mst.size() ==
                    graph.getVertices().size() - 1) {
                break;
            }
        }

        return mst;
    }

    private String find(
            Map<String, String> parent,
            String vertex) {

        if (!parent.get(vertex).equals(vertex)) {

            parent.put(
                vertex,
                find(parent, parent.get(vertex))
            );
        }

        return parent.get(vertex);
    }
}