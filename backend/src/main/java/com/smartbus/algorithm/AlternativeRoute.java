package com.smartbus.algorithm;

import java.util.*;

public class AlternativeRoute {

    private Dijkstra dijkstra;

    public AlternativeRoute() {
        dijkstra = new Dijkstra();
    }

    public List<RouteResult> findAlternatives(
            Graph graph,
            String sourceId,
            String destinationId) {

        List<RouteResult> alternatives =
                new ArrayList<>();

        // Find the original shortest route
        RouteResult mainRoute =
                dijkstra.findRoute(
                        graph,
                        sourceId,
                        destinationId,
                        "SHORTEST"
                );

        if (mainRoute.getPath().isEmpty()) {
            return alternatives;
        }

        alternatives.add(mainRoute);

        // Store original blocked status
        List<Edge> changedEdges =
                new ArrayList<>();

        /*
         * Temporarily block each edge of the
         * main route and find another route.
         */
        List<Vertex> mainPath =
                mainRoute.getPath();

        for (int i = 0; i < mainPath.size() - 1; i++) {

            String from =
                    mainPath.get(i).getId();

            String to =
                    mainPath.get(i + 1).getId();

            for (Edge edge : graph.getEdges(from)) {

                if (edge.getDestination()
                        .getId()
                        .equals(to)) {

                    if (!edge.isBlocked()) {

                        edge.setBlocked(true);
                        changedEdges.add(edge);

                        RouteResult alternative =
                                dijkstra.findRoute(
                                        graph,
                                        sourceId,
                                        destinationId,
                                        "SHORTEST"
                                );

                        if (!alternative
                                .getPath()
                                .isEmpty()) {

                            if (!containsSamePath(
                                    alternatives,
                                    alternative)) {

                                alternatives.add(
                                        alternative
                                );
                            }
                        }

                        edge.setBlocked(false);
                    }

                    break;
                }
            }

            // Stop after finding 3 routes
            if (alternatives.size() >= 3) {
                break;
            }
        }

        return alternatives;
    }

    private boolean containsSamePath(
            List<RouteResult> routes,
            RouteResult newRoute) {

        for (RouteResult route : routes) {

            if (route.getPath()
                    .equals(newRoute.getPath())) {

                return true;
            }
        }

        return false;
    }
}