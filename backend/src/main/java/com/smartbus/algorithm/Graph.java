package com.smartbus.algorithm;

import java.util.*;

public class Graph {

    private Map<String, Vertex> vertices;
    private Map<String, List<Edge>> adjacencyList;

    public Graph() {

        vertices = new HashMap<>();
        adjacencyList = new HashMap<>();
    }


    public void addVertex(Vertex vertex) {

        vertices.put(vertex.getId(), vertex);

        if (!adjacencyList.containsKey(vertex.getId())) {

            adjacencyList.put(
                    vertex.getId(),
                    new ArrayList<Edge>()
            );
        }
    }


    // Existing method
    // Adds a simple directed edge.
    public void addEdge(
            String sourceId,
            String destinationId,
            double weight,
            boolean blocked) {

        Vertex source =
                vertices.get(sourceId);

        Vertex destination =
                vertices.get(destinationId);

        if (source == null ||
                destination == null) {

            return;
        }

        Edge edge =
                new Edge(
                        source,
                        destination,
                        weight,
                        blocked
                );

        adjacencyList
                .get(sourceId)
                .add(edge);
    }


    // Existing method
    // Adds a simple undirected road.
    public void addUndirectedEdge(
            String sourceId,
            String destinationId,
            double weight,
            boolean blocked) {

        addEdge(
                sourceId,
                destinationId,
                weight,
                blocked
        );

        addEdge(
                destinationId,
                sourceId,
                weight,
                blocked
        );
    }


    // NEW METHOD
    // Adds a SmartTransit directed connection
    // with distance, time, fare and traffic.
    public void addSmartEdge(
            String sourceId,
            String destinationId,
            double distance,
            double travelTime,
            double fare,
            String trafficLevel,
            boolean blocked) {

        Vertex source =
                vertices.get(sourceId);

        Vertex destination =
                vertices.get(destinationId);

        if (source == null ||
                destination == null) {

            return;
        }

        Edge edge =
                new Edge(
                        source,
                        destination,
                        distance,
                        travelTime,
                        fare,
                        trafficLevel,
                        blocked
                );

        adjacencyList
                .get(sourceId)
                .add(edge);
    }


    // NEW METHOD
    // Adds a SmartTransit undirected road.
    public void addSmartUndirectedEdge(
            String sourceId,
            String destinationId,
            double distance,
            double travelTime,
            double fare,
            String trafficLevel,
            boolean blocked) {

        addSmartEdge(
                sourceId,
                destinationId,
                distance,
                travelTime,
                fare,
                trafficLevel,
                blocked
        );

        addSmartEdge(
                destinationId,
                sourceId,
                distance,
                travelTime,
                fare,
                trafficLevel,
                blocked
        );
    }


    public Vertex getVertex(String id) {

        return vertices.get(id);
    }


    public List<Edge> getEdges(String vertexId) {

        if (!adjacencyList
                .containsKey(vertexId)) {

            return new ArrayList<Edge>();
        }

        return adjacencyList.get(vertexId);
    }


    public Collection<Vertex> getVertices() {

        return vertices.values();
    }


    public void blockEdge(
            String sourceId,
            String destinationId) {

        List<Edge> edges =
                adjacencyList.get(sourceId);

        if (edges == null) {
            return;
        }

        for (Edge edge : edges) {

            if (edge.getDestination()
                    .getId()
                    .equals(destinationId)) {

                edge.setBlocked(true);
            }
        }
    }


    public void restoreEdge(
            String sourceId,
            String destinationId) {

        List<Edge> edges =
                adjacencyList.get(sourceId);

        if (edges == null) {
            return;
        }

        for (Edge edge : edges) {

            if (edge.getDestination()
                    .getId()
                    .equals(destinationId)) {

                edge.setBlocked(false);
            }
        }
    }


    public void printGraph() {

        for (String vertexId :
                adjacencyList.keySet()) {

            System.out.print(
                    vertices.get(vertexId)
                            .getName()
                    + " -> "
            );

            for (Edge edge :
                    adjacencyList.get(vertexId)) {

                System.out.print(
                        edge.getDestination()
                                .getName()
                        + " ("
                        + edge.getWeight()
                        + " km) "
                );
            }

            System.out.println();
        }
    }
}