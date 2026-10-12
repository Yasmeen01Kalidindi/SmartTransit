package com.smartbus.algorithm;

import java.util.*;

public class DFS {

    public List<Vertex> traverse(Graph graph, String startId) {

        List<Vertex> result = new ArrayList<>();

        Set<String> visited = new HashSet<>();

        dfsRecursive(
                graph,
                startId,
                visited,
                result
        );

        return result;
    }

    private void dfsRecursive(
            Graph graph,
            String currentId,
            Set<String> visited,
            List<Vertex> result) {

        if (visited.contains(currentId)) {
            return;
        }

        Vertex currentVertex =
                graph.getVertex(currentId);

        if (currentVertex == null) {
            return;
        }

        visited.add(currentId);

        result.add(currentVertex);

        for (Edge edge : graph.getEdges(currentId)) {

            if (edge.isBlocked()) {
                continue;
            }

            String neighborId =
                    edge.getDestination().getId();

            if (!visited.contains(neighborId)) {

                dfsRecursive(
                        graph,
                        neighborId,
                        visited,
                        result
                );
            }
        }
    }
}