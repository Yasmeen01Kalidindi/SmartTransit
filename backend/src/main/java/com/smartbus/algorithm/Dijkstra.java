package com.smartbus.algorithm;

import java.util.*;
import com.smartbus.enums.TimePeriod;

public class Dijkstra {

    public RouteResult findShortestPath(
            Graph graph,
            String sourceId,
            String destinationId) {

        Map<String, Double> distances = new HashMap<>();

        Map<String, String> previous = new HashMap<>();

        PriorityQueue<Node> priorityQueue =
                new PriorityQueue<>(
                        Comparator.comparingDouble(Node::getDistance)
                );

        // Initially, distance to every vertex is infinity
        for (Vertex vertex : graph.getVertices()) {

            distances.put(
                    vertex.getId(),
                    Double.POSITIVE_INFINITY
            );
        }

        // Distance from source to itself is 0
        distances.put(sourceId, 0.0);

        priorityQueue.add(
                new Node(sourceId, 0.0)
        );

        while (!priorityQueue.isEmpty()) {

            Node current = priorityQueue.poll();

            String currentId =
                    current.getVertexId();

            // Ignore an outdated PriorityQueue entry
            if (current.getDistance()
                    > distances.get(currentId)) {

                continue;
            }

            // Destination reached
            if (currentId.equals(destinationId)) {
                break;
            }

            // Check all connected edges
            for (Edge edge :
                    graph.getEdges(currentId)) {

                // Ignore blocked roads
                if (edge.isBlocked()) {
                    continue;
                }

                String neighborId =
                        edge.getDestination().getId();

                double newDistance =
                        distances.get(currentId)
                        + edge.getWeight();

                // Found a shorter route
                if (newDistance
                        < distances.get(neighborId)) {

                    distances.put(
                            neighborId,
                            newDistance
                    );

                    previous.put(
                            neighborId,
                            currentId
                    );

                    priorityQueue.add(
                            new Node(
                                    neighborId,
                                    newDistance
                            )
                    );
                }
            }
        }

        List<Vertex> path =
                buildPath(
                        graph,
                        previous,
                        sourceId,
                        destinationId
                );

        double totalDistance =
                distances.get(destinationId);

        // No route found
        if (Double.isInfinite(totalDistance)) {

            return new RouteResult(
                    new ArrayList<Vertex>(),
                    Double.POSITIVE_INFINITY
            );
        }

        return new RouteResult(
                path,
                totalDistance
        );
    }

    private List<Vertex> buildPath(
            Graph graph,
            Map<String, String> previous,
            String sourceId,
            String destinationId) {

        List<Vertex> path =
                new ArrayList<>();

        String currentId =
                destinationId;

        while (currentId != null) {

            Vertex vertex =
                    graph.getVertex(currentId);

            if (vertex == null) {

                return new ArrayList<>();
            }

            path.add(vertex);

            if (currentId.equals(sourceId)) {
                break;
            }

            currentId =
                    previous.get(currentId);
        }

        // Destination cannot be reached
        if (path.isEmpty()
                || !path.get(path.size() - 1)
                        .getId()
                        .equals(sourceId)) {

            return new ArrayList<>();
        }

        Collections.reverse(path);

        return path;
    }

    private static class Node {

        private String vertexId;
        private double distance;

        public Node(
                String vertexId,
                double distance) {

            this.vertexId = vertexId;
            this.distance = distance;
        }

        public String getVertexId() {
            return vertexId;
        }

        public double getDistance() {
            return distance;
        }
    }
public RouteResult findRoute(
        Graph graph,
        String sourceId,
        String destinationId,
        String preference) {
          return findRoute(
            graph,
            sourceId,
            destinationId,
            preference,
            null
        );
    }
public RouteResult findRoute(
        Graph graph,
        String sourceId,
        String destinationId,
        String preference,
        TimePeriod timePeriod) {

    Map<String, Double> costs = new HashMap<>();
    Map<String, String> previous = new HashMap<>();

    PriorityQueue<Node> priorityQueue =
            new PriorityQueue<>(
                    Comparator.comparingDouble(Node::getDistance)
            );

    for (Vertex vertex : graph.getVertices()) {
        costs.put(vertex.getId(), Double.POSITIVE_INFINITY);
    }

    costs.put(sourceId, 0.0);
    priorityQueue.add(new Node(sourceId, 0.0));

    while (!priorityQueue.isEmpty()) {

        Node current = priorityQueue.poll();
        String currentId = current.getVertexId();

        if (current.getDistance() > costs.get(currentId)) {
            continue;
        }

        if (currentId.equals(destinationId)) {
            break;
        }

        for (Edge edge : graph.getEdges(currentId)) {

            if (edge.isBlocked()) {
                continue;
            }

            String neighborId =
                    edge.getDestination().getId();

            double edgeCost =
                    getEdgeCost(
                            edge,
                            preference,
                            timePeriod
                    );

            double newCost =
                    costs.get(currentId) + edgeCost;

            if (newCost < costs.get(neighborId)) {

                costs.put(neighborId, newCost);

                previous.put(
                        neighborId,
                        currentId
                );

                priorityQueue.add(
                        new Node(
                                neighborId,
                                newCost
                        )
                );
            }
        }
    }

    List<Vertex> path =
            buildPath(
                    graph,
                    previous,
                    sourceId,
                    destinationId
            );

    if (path.isEmpty()) {
        return new RouteResult(
                new ArrayList<Vertex>(),
                Double.POSITIVE_INFINITY
        );
    }

    RouteMetrics metrics =
            RouteMetrics.calculate(
                    graph,
                    path
            );

    return new RouteResult(
            path,
            metrics.getDistance()
    );
}

    private double getEdgeCost(
        Edge edge,
        String preference,
        TimePeriod timePeriod) {

    if (preference == null) {
        return edge.getWeight();
    }

    switch (preference.toUpperCase()) {

        case "FASTEST":

            if (timePeriod != null) {
                return TimeBasedRouting.getEffectiveTravelTime(
                        edge.getTravelTime(),
                        edge.getTrafficLevel(),
                        timePeriod
                );
            }

            return edge.getTravelTime();

        case "CHEAPEST":

            return edge.getFare();

        case "TRAFFIC_AWARE":

            if (timePeriod != null) {
                return TimeBasedRouting.getEffectiveTravelTime(
                        edge.getTravelTime(),
                        edge.getTrafficLevel(),
                        timePeriod
                );
            }

            double trafficMultiplier = 1.0;

            if ("MEDIUM".equalsIgnoreCase(
                    edge.getTrafficLevel())) {

                trafficMultiplier = 1.5;

            } else if ("HIGH".equalsIgnoreCase(
                    edge.getTrafficLevel())) {

                trafficMultiplier = 2.0;
            }

            return edge.getTravelTime() * trafficMultiplier;

        case "SHORTEST":

        default:

            return edge.getWeight();
    }
}

}