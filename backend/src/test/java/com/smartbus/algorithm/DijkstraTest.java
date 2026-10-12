package com.smartbus.algorithm;

public class DijkstraTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // Add vertices
        graph.addVertex(new Vertex("ST01", "SVECW"));
        graph.addVertex(new Vertex("ST02", "Railway Station"));
        graph.addVertex(new Vertex("ST03", "Town Hall"));
        graph.addVertex(new Vertex("ST04", "Market"));
        graph.addVertex(new Vertex("ST05", "Bhimavaram Bus Stand"));
        graph.addVertex(new Vertex("ST06", "College Road"));

        // Add undirected roads
        graph.addUndirectedEdge(
                "ST01", "ST02", 2.0, false);

        graph.addUndirectedEdge(
                "ST01", "ST06", 1.4, false);

        graph.addUndirectedEdge(
                "ST02", "ST03", 1.2, false);

        graph.addUndirectedEdge(
                "ST02", "ST04", 1.7, false);

        graph.addUndirectedEdge(
                "ST06", "ST03", 1.8, true);

        graph.addUndirectedEdge(
                "ST03", "ST04", 1.0, false);

        graph.addUndirectedEdge(
                "ST03", "ST05", 2.0, false);

        graph.addUndirectedEdge(
                "ST04", "ST05", 1.1, false);

        // Run Dijkstra
        Dijkstra dijkstra = new Dijkstra();

        RouteResult result =
                dijkstra.findShortestPath(
                        graph,
                        "ST01",
                        "ST05"
                );

        System.out.println();
        System.out.println(
                "===== SMARTTRANSIT DIJKSTRA TEST ====="
        );

        System.out.println("Shortest Route:");

        for (Vertex vertex : result.getPath()) {

            System.out.print(
                    vertex.getName()
            );

            if (vertex !=
                    result.getPath()
                            .get(result.getPath().size() - 1)) {

                System.out.print(" -> ");
            }
        }

        System.out.println();

        System.out.println(
                "Total Distance: "
                + result.getDistance()
                + " km"
        );

        System.out.println(
                "======================================"
        );
    }
}