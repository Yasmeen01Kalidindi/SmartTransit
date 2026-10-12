package com.smartbus.service;

import com.smartbus.algorithm.AlternativeRoute;
import com.smartbus.algorithm.Dijkstra;
import com.smartbus.algorithm.Edge;
import com.smartbus.algorithm.Graph;
import com.smartbus.algorithm.RouteMetrics;
import com.smartbus.algorithm.RouteResult;
import com.smartbus.algorithm.Vertex;
import com.smartbus.dto.RouteOptionResponse;
import com.smartbus.entity.BusStop;
import com.smartbus.entity.Connection;
import com.smartbus.enums.TimePeriod;
import com.smartbus.repository.BusStopRepository;
import com.smartbus.repository.ConnectionRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private final BusStopRepository busStopRepository;
    private final ConnectionRepository connectionRepository;

    private final Dijkstra dijkstra;
    private final AlternativeRoute alternativeRoute;

    public RouteService(
            BusStopRepository busStopRepository,
            ConnectionRepository connectionRepository) {

        this.busStopRepository = busStopRepository;
        this.connectionRepository = connectionRepository;

        this.dijkstra = new Dijkstra();
        this.alternativeRoute = new AlternativeRoute();
    }

    /*
     * Builds the graph using bus stops and connections
     * stored in PostgreSQL.
     */
    private Graph buildGraph() {

        Graph graph = new Graph();

        // Add all bus stops as graph vertices
        List<BusStop> busStops =
                busStopRepository.findAll();

        for (BusStop stop : busStops) {

            Vertex vertex =
                    new Vertex(
                            stop.getId(),
                            stop.getName()
                    );

            graph.addVertex(vertex);
        }

        // Add all roads/connections as graph edges
        List<Connection> connections =
                connectionRepository.findAll();

        for (Connection connection : connections) {

            graph.addSmartEdge(
                    connection.getFromStop(),
                    connection.getToStop(),
                    connection.getDistance(),
                    connection.getTravelTime(),
                    connection.getFare(),
                    connection.getTrafficLevel(),
                    connection.isBlocked()
            );
        }

        return graph;
    }

    /*
     * Finds a route according to the selected preference.
     *
     * SHORTEST       -> minimum distance
     * FASTEST        -> minimum travel time
     * CHEAPEST       -> minimum fare
     * TRAFFIC_AWARE  -> traffic/time-aware route
     */
    public RouteResult findRoute(
            String sourceId,
            String destinationId,
            String preference,
            TimePeriod timePeriod) {

        validateRouteRequest(sourceId, destinationId);

        Graph graph = buildGraph();

        return dijkstra.findRoute(
                graph,
                sourceId,
                destinationId,
                preference,
                timePeriod
        );
    }

    /*
     * Calculates complete route metrics:
     * distance, travel time, fare and traffic level.
     */
    public RouteMetrics getRouteMetrics(
            String sourceId,
            String destinationId,
            String preference,
            TimePeriod timePeriod) {

        validateRouteRequest(sourceId, destinationId);

        Graph graph = buildGraph();

        RouteResult route =
                dijkstra.findRoute(
                        graph,
                        sourceId,
                        destinationId,
                        preference,
                        timePeriod
                );

        if (route.getPath().isEmpty()) {

            return new RouteMetrics(
                    0,
                    0,
                    0,
                    "UNKNOWN"
            );
        }

        return RouteMetrics.calculate(
                graph,
                route.getPath(),
                timePeriod
        );
    }

    /*
     * Finds up to 3 alternative routes.
     */
    public List<RouteOptionResponse> findAlternativeRoutes(
            String sourceId,
            String destinationId,
            TimePeriod timePeriod) {

        validateRouteRequest(sourceId, destinationId);

        Graph graph = buildGraph();

        List<RouteResult> routes =
                alternativeRoute.findAlternatives(
                        graph,
                        sourceId,
                        destinationId
                );

        List<RouteOptionResponse> options =
                new ArrayList<>();

        for (int i = 0; i < routes.size(); i++) {

            RouteResult route =
                    routes.get(i);

            if (route.getPath().isEmpty()) {
                continue;
            }

            RouteMetrics metrics =
                    RouteMetrics.calculate(
                            graph,
                            route.getPath(),
                            timePeriod
                    );

            List<String> routeNames =
                    getRouteStopNames(route);

            RouteOptionResponse option =
                    new RouteOptionResponse();

            if (i == 0) {
                option.setType("SHORTEST");
            } else {
                option.setType("ALTERNATIVE " + i);
            }

            option.setRoute(routeNames);

            option.setDistance(
                    metrics.getDistance()
            );

            option.setTravelTime(
                    metrics.getTravelTime()
            );

            option.setFare(
                    metrics.getFare()
            );

            option.setStops(
                    routeNames.size()
            );

            option.setTransfers(0);

            option.setTrafficLevel(
                    metrics.getTrafficLevel()
            );

            options.add(option);
        }

        return options;
    }

    private void validateRouteRequest(
            String sourceId,
            String destinationId) {

        if (sourceId == null || sourceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Source stop is required.");
        }

        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination stop is required.");
        }

        if (sourceId.equals(destinationId)) {
            throw new IllegalArgumentException("Source and destination cannot be the same.");
        }

        if (!busStopRepository.existsById(sourceId)) {
            throw new IllegalArgumentException("Source stop not found: " + sourceId);
        }

        if (!busStopRepository.existsById(destinationId)) {
            throw new IllegalArgumentException("Destination stop not found: " + destinationId);
        }
    }

    /*
     * Converts a route's vertices into a list
     * of bus-stop names.
     */
    public List<String> getRouteStopNames(
            RouteResult route) {

        List<String> stopNames =
                new ArrayList<>();

        for (Vertex vertex : route.getPath()) {

            stopNames.add(
                    vertex.getName()
            );
        }

        return stopNames;
    }

    /*
     * Returns all bus stops.
     */
    public List<BusStop> getAllBusStops() {

        return busStopRepository.findAll();
    }

    /*
     * Returns all connections.
     */
    public List<Connection> getAllConnections() {

        return connectionRepository.findAll();
    }
}