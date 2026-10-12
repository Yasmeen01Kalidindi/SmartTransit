package com.smartbus.algorithm;

import java.util.*;

public class BFS {

    public List<Vertex> traverse(Graph graph, String startId) {

        List<Vertex> result = new ArrayList<>();

        Set<String> visited = new HashSet<>();

        Queue<String> queue = new LinkedList<>();

        queue.add(startId);
        visited.add(startId);

        while (!queue.isEmpty()) {

            String currentId = queue.poll();

            Vertex currentVertex =
                    graph.getVertex(currentId);

            if (currentVertex == null) {
                continue;
            }

            result.add(currentVertex);

            for (Edge edge : graph.getEdges(currentId)) {

                if (edge.isBlocked()) {
                    continue;
                }

                String neighborId =
                        edge.getDestination().getId();

                if (!visited.contains(neighborId)) {

                    visited.add(neighborId);

                    queue.add(neighborId);
                }
            }
        }

        return result;
    }
}