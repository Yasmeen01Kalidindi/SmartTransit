package com.smartbus.algorithm;

import java.util.*;

public class ConnectivityAnalyzer {

    private BFS bfs;

    public ConnectivityAnalyzer() {
        bfs = new BFS();
    }

    /*
     * Checks whether all bus stops in the graph
     * are reachable from the given starting stop.
     */
    public boolean isConnected(
            Graph graph,
            String startId) {

        List<Vertex> visited =
                bfs.traverse(graph, startId);

        int totalVertices =
                graph.getVertices().size();

        return visited.size() == totalVertices;
    }

    /*
     * Returns the number of bus stops that can
     * currently be reached from the starting stop.
     */
    public int reachableStops(
            Graph graph,
            String startId) {

        List<Vertex> visited =
                bfs.traverse(graph, startId);

        return visited.size();
    }

    /*
     * Returns the names of bus stops that can
     * currently be reached.
     */
    public List<String> getReachableStopNames(
            Graph graph,
            String startId) {

        List<Vertex> visited =
                bfs.traverse(graph, startId);

        List<String> names =
                new ArrayList<>();

        for (Vertex vertex : visited) {
            names.add(vertex.getName());
        }

        return names;
    }
}