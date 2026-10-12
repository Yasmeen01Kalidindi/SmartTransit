package com.smartbus.service;

import com.smartbus.algorithm.BFS;
import com.smartbus.algorithm.DFS;
import com.smartbus.algorithm.Graph;
import com.smartbus.algorithm.Prim;
import com.smartbus.algorithm.Kruskal;
import com.smartbus.algorithm.Vertex;
import com.smartbus.algorithm.Edge;

import com.smartbus.entity.Bus;
import com.smartbus.entity.BusStop;
import com.smartbus.entity.Connection;

import com.smartbus.repository.BusRepository;
import com.smartbus.repository.BusStopRepository;
import com.smartbus.repository.ConnectionRepository;

import com.smartbus.dto.NetworkResponse;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NetworkService {

    private final BusStopRepository busStopRepository;
    private final ConnectionRepository connectionRepository;
    private final BusRepository busRepository;

    public NetworkService(
            BusStopRepository busStopRepository,
            ConnectionRepository connectionRepository,
            BusRepository busRepository) {

        this.busStopRepository = busStopRepository;
        this.connectionRepository = connectionRepository;
        this.busRepository = busRepository;
    }

    /*
     * Builds the graph using bus stops and
     * connections stored in PostgreSQL.
     */
    private Graph buildGraph() {

        Graph graph = new Graph();

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

        List<Connection> connections =
                connectionRepository.findAll();

        for (Connection connection : connections) {

            graph.addSmartUndirectedEdge(
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
     * Performs BFS traversal.
     */
    public List<String> runBFS(String startStopId) {

        Graph graph = buildGraph();

        BFS bfs = new BFS();

        List<Vertex> vertices =
                bfs.traverse(graph, startStopId);

        List<String> result =
                new ArrayList<String>();

        for (Vertex vertex : vertices) {

            result.add(vertex.getName());
        }

        return result;
    }

    /*
     * Performs DFS traversal.
     */
    public List<String> runDFS(String startStopId) {

        Graph graph = buildGraph();

        DFS dfs = new DFS();

        List<Vertex> vertices =
                dfs.traverse(graph, startStopId);

        List<String> result =
                new ArrayList<String>();

        for (Vertex vertex : vertices) {

            result.add(vertex.getName());
        }

        return result;
    }

    /*
     * Returns network statistics.
     */
    public NetworkResponse getNetworkStatistics() {

        List<BusStop> stops =
                busStopRepository.findAll();

        List<Connection> connections =
                connectionRepository.findAll();

        List<Bus> buses =
                busRepository.findAll();

        int totalStops = stops.size();

        int totalRoads = connections.size();

        int blockedRoads = 0;

        double totalNetworkDistance = 0;

        for (Connection connection : connections) {

            if (connection.isBlocked()) {

                blockedRoads++;
            }

            totalNetworkDistance +=
                    connection.getDistance();
        }

        int activeBuses = 0;

        for (Bus bus : buses) {

            if (bus.getOccupied() < bus.getCapacity()) {

                activeBuses++;
            }
        }

        String mostConnectedStop = "N/A";

        String leastConnectedStop = "N/A";

        if (!stops.isEmpty()) {

            BusStop most = stops.get(0);

            BusStop least = stops.get(0);

            for (BusStop stop : stops) {

                if (stop.getConnectedRoads()
                        > most.getConnectedRoads()) {

                    most = stop;
                }

                if (stop.getConnectedRoads()
                        < least.getConnectedRoads()) {

                    least = stop;
                }
            }

            mostConnectedStop =
                    most.getName();

            leastConnectedStop =
                    least.getName();
        }

        List<String> reachableStopNames =
                new ArrayList<String>();

        if (!stops.isEmpty()) {

            String startStopId =
                    stops.get(0).getId();

            try {

                reachableStopNames =
                        runBFS(startStopId);

            } catch (Exception e) {

                reachableStopNames =
                        new ArrayList<String>();
            }
        }

        int reachableStops =
                reachableStopNames.size();

        boolean connected =
                totalStops > 0
                && reachableStops == totalStops;

        /*
         * IMPORTANT:
         * The argument order here exactly matches
         * the NetworkResponse constructor.
         */
        return new NetworkResponse(
                totalStops,
                totalRoads,
                mostConnectedStop,
                leastConnectedStop,
                totalNetworkDistance,
                blockedRoads,
                activeBuses,
                connected,
                reachableStops,
                reachableStopNames
        );
    }

    /*
     * Generates Minimum Spanning Tree
     * using Prim's algorithm.
     */
    public List<Edge> generatePrimMST() {

        Graph graph = buildGraph();

        Prim prim = new Prim();

        return prim.findMST(graph);
    }

    /*
     * Generates Minimum Spanning Tree
     * using Kruskal's algorithm.
     */
    public List<Edge> generateKruskalMST() {

        Graph graph = buildGraph();

        Kruskal kruskal = new Kruskal();

        return kruskal.findMST(graph);
    }
}