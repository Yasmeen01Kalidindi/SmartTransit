package com.smartbus.service;

import com.smartbus.algorithm.Dijkstra;
import com.smartbus.algorithm.Graph;
import com.smartbus.algorithm.RouteMetrics;
import com.smartbus.algorithm.RouteResult;
import com.smartbus.algorithm.Vertex;
import com.smartbus.entity.Bus;
import com.smartbus.entity.BusStop;
import com.smartbus.entity.Connection;
import com.smartbus.entity.Simulation;
import com.smartbus.enums.TimePeriod;
import com.smartbus.repository.BusRepository;
import com.smartbus.repository.BusStopRepository;
import com.smartbus.repository.ConnectionRepository;
import com.smartbus.repository.SimulationRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final BusStopRepository busStopRepository;
    private final ConnectionRepository connectionRepository;
    private final BusRepository busRepository;

    private final Dijkstra dijkstra;

    /*
     * Default What-If journey used by the simulation page.
     *
     * ST01 = SVECW
     * ST05 = Bhimavaram Bus Stand
     */
    private static final String DEFAULT_SOURCE = "ST01";
    private static final String DEFAULT_DESTINATION = "ST05";

    public SimulationService(
            SimulationRepository simulationRepository,
            BusStopRepository busStopRepository,
            ConnectionRepository connectionRepository,
            BusRepository busRepository) {

        this.simulationRepository = simulationRepository;
        this.busStopRepository = busStopRepository;
        this.connectionRepository = connectionRepository;
        this.busRepository = busRepository;

        this.dijkstra = new Dijkstra();
    }

    public List<Simulation> getAllSimulations() {

        return simulationRepository.findAll();
    }

    public Optional<Simulation> getSimulationById(
            String id) {

        return simulationRepository.findById(id);
    }

    /*
     * Main What-If simulation method.
     *
     * The real PostgreSQL network is NOT modified.
     * A temporary Graph is created in memory.
     */
    public Simulation runSimulation(
            String scenario,
            String value,
            String beforeResult,
            String afterResult) {

        validateScenario(scenario, value);

        String before;
        String after;

        /*
         * Calculate the normal network first.
         */
        before =
                calculateNormalResult(scenario, value);

        /*
         * Calculate the hypothetical network.
         */
        after =
                calculateSimulationResult(scenario, value);

        Simulation simulation =
                new Simulation();

        simulation.setId(
                UUID.randomUUID().toString()
        );

        simulation.setScenario(scenario);
        simulation.setValue(value);

        simulation.setBeforeResult(
                before
        );

        simulation.setAfterResult(
                after
        );

        return simulationRepository.save(
                simulation
        );
    }

    private void validateScenario(String scenario, String value) {
        if (scenario == null || scenario.trim().isEmpty()) {
            throw new IllegalArgumentException("Simulation scenario is required.");
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Simulation value is required.");
        }

        String normalized = scenario.trim();
        if (!("roadBlocked".equalsIgnoreCase(normalized)
                || "stopClosed".equalsIgnoreCase(normalized)
                || "trafficIncreased".equalsIgnoreCase(normalized)
                || "busUnavailable".equalsIgnoreCase(normalized)
                || "newRoad".equalsIgnoreCase(normalized))) {
            throw new IllegalArgumentException("Unsupported simulation scenario: " + scenario);
        }

        if ("trafficIncreased".equalsIgnoreCase(normalized)) {
            try {
                com.smartbus.enums.TrafficLevel.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Traffic level must be LOW, MEDIUM or HIGH.");
            }
        }

        if ("roadBlocked".equalsIgnoreCase(normalized)
                && !connectionRepository.existsById(value)) {
            throw new IllegalArgumentException("Road not found: " + value);
        }

        if ("stopClosed".equalsIgnoreCase(normalized)
                && !busStopRepository.existsById(value)) {
            throw new IllegalArgumentException("Bus stop not found: " + value);
        }

        if ("busUnavailable".equalsIgnoreCase(normalized)
                && !busRepository.existsById(value)) {
            throw new IllegalArgumentException("Bus not found: " + value);
        }

        if ("newRoad".equalsIgnoreCase(normalized)) {
            String[] parts = value.split("->");
            if (parts.length != 2) {
                throw new IllegalArgumentException("New road must use From -> To format.");
            }
            String from = parts[0].trim();
            String to = parts[1].trim();
            if (from.isEmpty() || to.isEmpty() || from.equalsIgnoreCase(to)) {
                throw new IllegalArgumentException("New road From and To stops must be different.");
            }
            if (findStopIdByName(from) == null || findStopIdByName(to) == null) {
                throw new IllegalArgumentException("New road endpoints must be valid bus stops.");
            }
        }
    }

    /*
     * Normal network result.
     */
    private String calculateNormalResult(
            String scenario,
            String value) {

        /*
         * Bus unavailable does not change the road graph.
         * Therefore show the current route plus bus information.
         */
        if ("busUnavailable".equalsIgnoreCase(scenario)) {

            return calculateBusResult(value, false);
        }

        Graph graph =
                buildSimulationGraph(
                        "",
                        "",
                        false
                );

        String preference =
                "SHORTEST";

        if ("trafficIncreased".equalsIgnoreCase(
                scenario)) {

            preference = "TRAFFIC_AWARE";
        }

        RouteResult route =
                dijkstra.findRoute(
                        graph,
                        DEFAULT_SOURCE,
                        DEFAULT_DESTINATION,
                        preference,
                        TimePeriod.AFTERNOON
                );

        return formatRouteResult(
                graph,
                route,
                "Normal network"
        );
    }

    /*
     * Hypothetical network result.
     */
    private String calculateSimulationResult(
            String scenario,
            String value) {

        /*
         * BUS UNAVAILABLE
         */
        if ("busUnavailable".equalsIgnoreCase(
                scenario)) {

            return calculateBusResult(
                    value,
                    true
            );
        }

        /*
         * Build a temporary graph.
         */
        Graph graph =
                buildSimulationGraph(
                        scenario,
                        value,
                        true
                );

        String preference =
                "SHORTEST";

        /*
         * Traffic simulations use the traffic-aware
         * Dijkstra mode.
         */
        if ("trafficIncreased".equalsIgnoreCase(
                scenario)) {

            preference = "TRAFFIC_AWARE";
        }

        RouteResult route =
                dijkstra.findRoute(
                        graph,
                        DEFAULT_SOURCE,
                        DEFAULT_DESTINATION,
                        preference,
                        TimePeriod.AFTERNOON
                );

        return formatRouteResult(
                graph,
                route,
                "After simulation"
        );
    }

    /*
     * Builds a temporary graph.
     *
     * IMPORTANT:
     * No database record is changed here.
     */
    private Graph buildSimulationGraph(
            String scenario,
            String value,
            boolean applySimulation) {

        Graph graph =
                new Graph();

        /*
         * Add vertices.
         *
         * For stopClosed, the selected stop is omitted
         * from the temporary graph.
         */
        List<BusStop> busStops =
                busStopRepository.findAll();

        for (BusStop stop : busStops) {

            if (applySimulation
                    && "stopClosed".equalsIgnoreCase(scenario)
                    && stop.getId().equals(value)) {

                continue;
            }

            Vertex vertex =
                    new Vertex(
                            stop.getId(),
                            stop.getName()
                    );

            graph.addVertex(vertex);
        }

        /*
         * Add normal connections.
         */
        List<Connection> connections =
                connectionRepository.findAll();

        for (Connection connection :
                connections) {

            /*
             * ROAD BLOCK SIMULATION
             *
             * Skip the selected road completely.
             */
            if (applySimulation
                    && "roadBlocked".equalsIgnoreCase(scenario)
                    && connection.getId().equals(value)) {

                continue;
            }

            /*
             * STOP CLOSURE SIMULATION
             *
             * Skip all roads connected to the closed stop.
             */
            if (applySimulation
                    && "stopClosed".equalsIgnoreCase(scenario)
                    && (connection.getFromStop().equals(value)
                    || connection.getToStop().equals(value))) {

                continue;
            }

            /*
             * Respect permanently blocked roads
             * stored in the database.
             */
            boolean blocked =
                    connection.isBlocked();

            /*
             * TRAFFIC SIMULATION
             *
             * Temporarily change traffic level
             * without modifying PostgreSQL.
             */
            String trafficLevel =
                    connection.getTrafficLevel();

            if (applySimulation
                    && "trafficIncreased".equalsIgnoreCase(scenario)) {

                trafficLevel = value;
            }

            graph.addSmartEdge(
                    connection.getFromStop(),
                    connection.getToStop(),
                    connection.getDistance(),
                    connection.getTravelTime(),
                    connection.getFare(),
                    trafficLevel,
                    blocked
            );
        }

        /*
         * NEW ROAD SIMULATION
         *
         * The selected new road is added only to
         * this temporary graph.
         */
        if (applySimulation
                && "newRoad".equalsIgnoreCase(scenario)) {

            addSimulatedNewRoad(
                    graph,
                    value
            );
        }

        return graph;
    }

    /*
     * Adds a hypothetical new road.
     *
     * The values are simulation values only.
     * They are NOT inserted into PostgreSQL.
     */
    private void addSimulatedNewRoad(
            Graph graph,
            String value) {

        if (value == null) {
            return;
        }

        String[] parts =
                value.split("->");

        if (parts.length != 2) {
            return;
        }

        String fromName =
                parts[0].trim();

        String toName =
                parts[1].trim();

        String fromId =
                findStopIdByName(fromName);

        String toId =
                findStopIdByName(toName);

        if (fromId == null
                || toId == null) {

            return;
        }

        /*
         * Simulated road characteristics:
         *
         * Distance  = 3.0 km
         * Time      = 8 minutes
         * Fare      = ₹10
         *
         * These are hypothetical What-If values.
         */
        graph.addSmartUndirectedEdge(
                fromId,
                toId,
                3.0,
                8.0,
                10.0,
                "LOW",
                false
        );
    }

    /*
     * Finds a stop ID using its name.
     */
    private String findStopIdByName(
            String name) {

        List<BusStop> stops =
                busStopRepository.findAll();

        for (BusStop stop : stops) {

            if (stop.getName()
                    .equalsIgnoreCase(name)) {

                return stop.getId();
            }
        }

        return null;
    }

    /*
     * Handles bus-unavailable simulation.
     *
     * The current graph does not associate individual
     * buses with individual graph edges, so removing
     * a bus must not falsely change the road graph.
     *
     * Instead we report the bus availability change
     * and show the network route separately.
     */
    private String calculateBusResult(
            String busNumber,
            boolean unavailable) {

        Bus selectedBus = null;

        List<Bus> buses =
                busRepository.findAll();

        for (Bus bus : buses) {

            if (bus.getNumber()
                    .equals(busNumber)) {

                selectedBus = bus;
                break;
            }
        }

        if (selectedBus == null) {

            return "Bus "
                    + busNumber
                    + " was not found.";
        }

        Graph graph =
                buildSimulationGraph(
                        "",
                        "",
                        false
                );

        RouteResult route =
                dijkstra.findRoute(
                        graph,
                        DEFAULT_SOURCE,
                        DEFAULT_DESTINATION,
                        "SHORTEST",
                        TimePeriod.AFTERNOON
                );

        String routeResult =
                formatRouteResult(
                        graph,
                        route,
                        "Network route"
                );

        if (!unavailable) {

            return "Bus "
                    + selectedBus.getNumber()
                    + " status: AVAILABLE FOR SIMULATION"
                    + "\n"
                    + "Capacity: "
                    + selectedBus.getCapacity()
                    + "\n"
                    + "Occupied: "
                    + selectedBus.getOccupied()
                    + "\n\n"
                    + routeResult;
        }

        Bus replacementBus = null;

        for (Bus bus : buses) {
            if (!bus.getNumber().equals(selectedBus.getNumber())
                    && bus.getCapacity() > bus.getOccupied()) {
                replacementBus = bus;
                break;
            }
        }

        String replacementText;

        if (replacementBus == null) {
            replacementText = "No other bus currently has available capacity.";
        } else {
            replacementText =
                    "Replacement bus: " + replacementBus.getNumber()
                    + " (available seats: "
                    + (replacementBus.getCapacity() - replacementBus.getOccupied())
                    + ")";
        }

        return "Bus "
                + selectedBus.getNumber()
                + " is simulated as UNAVAILABLE."
                + "\n"
                + "Capacity: "
                + selectedBus.getCapacity()
                + "\n"
                + "Occupied before simulation: "
                + selectedBus.getOccupied()
                + "\n\n"
                + "The road graph remains unchanged because buses are not graph edges."
                + "\n\n"
                + replacementText
                + "\n\n"
                + routeResult;
    }

    /*
     * Converts a Dijkstra result into readable text.
     */
    private String formatRouteResult(
            Graph graph,
            RouteResult route,
            String title) {

        if (route == null
                || route.getPath() == null
                || route.getPath().isEmpty()) {

            return title
                    + "\n"
                    + "No route is available between "
                    + "SVECW and Bhimavaram Bus Stand.";
        }

        RouteMetrics metrics =
                RouteMetrics.calculate(
                        graph,
                        route.getPath(),
                        TimePeriod.AFTERNOON
                );

        StringBuilder routeText =
                new StringBuilder();

        routeText.append(title);
        routeText.append("\n");

        routeText.append("Route: ");

        for (int i = 0;
             i < route.getPath().size();
             i++) {

            routeText.append(
                    route.getPath()
                            .get(i)
                            .getName()
            );

            if (i <
                    route.getPath().size() - 1) {

                routeText.append(
                        " -> "
                );
            }
        }

        routeText.append("\n");

        routeText.append(
                "Distance: "
        );

        routeText.append(
                String.format(
                        "%.2f",
                        metrics.getDistance()
                )
        );

        routeText.append(" km\n");

        routeText.append(
                "Travel Time: "
        );

        routeText.append(
                String.format(
                        "%.2f",
                        metrics.getTravelTime()
                )
        );

        routeText.append(" min\n");

        routeText.append(
                "Fare: ₹"
        );

        routeText.append(
                String.format(
                        "%.2f",
                        metrics.getFare()
                )
        );

        routeText.append("\n");

        routeText.append(
                "Stops: "
        );

        routeText.append(
                route.getPath().size()
        );

        routeText.append("\n");

        routeText.append(
                "Traffic: "
        );

        routeText.append(
                metrics.getTrafficLevel()
        );

        return routeText.toString();
    }

    /*
     * Saves an already-created simulation.
     */
    public Simulation saveSimulation(
            Simulation simulation) {

        return simulationRepository.save(
                simulation
        );
    }

    /*
     * Deletes a simulation.
     */
    public boolean deleteSimulation(
            String id) {

        if (!simulationRepository.existsById(id)) {

            return false;
        }

        simulationRepository.deleteById(id);

        return true;
    }
}