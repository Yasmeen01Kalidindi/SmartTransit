package com.smartbus.algorithm;

import java.util.*;

public class Prim {

    public List<Edge> findMST(Graph graph) {

        List<Edge> mst = new ArrayList<>();

        Set<String> visited = new HashSet<>();

        PriorityQueue<Edge> priorityQueue =
                new PriorityQueue<>(
                        Comparator.comparingDouble(Edge::getWeight)
                );

        Collection<Vertex> vertices =
                graph.getVertices();

        if (vertices.isEmpty()) {
            return mst;
        }

        /*
         * SmartTransit starts the MST from ST01 (SVECW).
         * This avoids depending on HashMap iteration order.
         */
        Vertex start = graph.getVertex("ST01");

        /*
         * Safety fallback in case ST01 does not exist.
         */
        if (start == null) {
            start = vertices.iterator().next();
        }

        visited.add(start.getId());

        priorityQueue.addAll(
                getAvailableEdges(graph, start.getId())
        );

        while (!priorityQueue.isEmpty()
                && visited.size() < vertices.size()) {

            Edge edge = priorityQueue.poll();

            if (edge.isBlocked()) {
                continue;
            }

            String sourceId =
                    edge.getSource().getId();

            String destinationId =
                    edge.getDestination().getId();

            /*
             * Ignore edges where both ends are already
             * inside the MST.
             */
            if (visited.contains(sourceId)
                    && visited.contains(destinationId)) {

                continue;
            }

            /*
             * Prim needs an edge connecting the existing
             * tree to a new vertex.
             */
            String newVertexId;

            if (visited.contains(sourceId)) {
                newVertexId = destinationId;
            } else if (visited.contains(destinationId)) {
                newVertexId = sourceId;
            } else {
                continue;
            }

            if (visited.contains(newVertexId)) {
                continue;
            }

            mst.add(edge);

            visited.add(newVertexId);

            priorityQueue.addAll(
                    getAvailableEdges(
                            graph,
                            newVertexId
                    )
            );
        }

        return mst;
    }


    private List<Edge> getAvailableEdges(
            Graph graph,
            String vertexId) {

        List<Edge> availableEdges =
                new ArrayList<>();

        for (Edge edge : graph.getEdges(vertexId)) {

            if (!edge.isBlocked()) {

                availableEdges.add(edge);
            }
        }

        return availableEdges;
    }
}