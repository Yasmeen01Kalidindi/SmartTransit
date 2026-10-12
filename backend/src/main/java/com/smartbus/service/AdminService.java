package com.smartbus.service;

import com.smartbus.entity.Bus;
import com.smartbus.entity.BusStop;
import com.smartbus.entity.Connection;
import com.smartbus.enums.TrafficLevel;
import com.smartbus.repository.BusRepository;
import com.smartbus.repository.BusStopRepository;
import com.smartbus.repository.ConnectionRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final BusStopRepository busStopRepository;
    private final ConnectionRepository connectionRepository;
    private final BusRepository busRepository;

    public AdminService(
            BusStopRepository busStopRepository,
            ConnectionRepository connectionRepository,
            BusRepository busRepository) {

        this.busStopRepository = busStopRepository;
        this.connectionRepository = connectionRepository;
        this.busRepository = busRepository;
    }

    // =========================
    // BUS STOP OPERATIONS
    // =========================

    public List<BusStop> getAllStops() {

        return busStopRepository.findAll();
    }

    public BusStop addStop(BusStop busStop) {

        validateStop(busStop);

        if (busStopRepository.existsById(busStop.getId())) {
            throw new IllegalArgumentException("Bus stop already exists: " + busStop.getId());
        }

        return busStopRepository.save(busStop);
    }

    public BusStop updateStop(
            String id,
            BusStop updatedStop) {

        Optional<BusStop> existing =
                busStopRepository.findById(id);

        if (!existing.isPresent()) {
            return null;
        }

        validateStop(updatedStop);

        BusStop stop = existing.get();

        stop.setName(updatedStop.getName());
        stop.setLocation(updatedStop.getLocation());
        stop.setConnectedRoads(
                updatedStop.getConnectedRoads()
        );
        stop.setStatus(updatedStop.getStatus());
        stop.setLatitude(updatedStop.getLatitude());
        stop.setLongitude(updatedStop.getLongitude());

        return busStopRepository.save(stop);
    }

    public boolean deleteStop(String id) {

        if (!busStopRepository.existsById(id)) {
            return false;
        }

        busStopRepository.deleteById(id);

        return true;
    }

    // =========================
    // ROAD / CONNECTION OPERATIONS
    // =========================

    public List<Connection> getAllConnections() {

        return connectionRepository.findAll();
    }

    public Connection addConnection(
            Connection connection) {

        validateConnection(connection);

        if (connectionRepository.existsById(connection.getId())) {
            throw new IllegalArgumentException("Road already exists: " + connection.getId());
        }

        if (!busStopRepository.existsById(connection.getFromStop())
                || !busStopRepository.existsById(connection.getToStop())) {
            throw new IllegalArgumentException("Both road endpoints must be valid bus stops.");
        }

        if (connection.getFromStop().equals(connection.getToStop())) {
            throw new IllegalArgumentException("Road From and To stops cannot be the same.");
        }

        return connectionRepository.save(connection);
    }

    public boolean deleteConnection(String id) {

        if (!connectionRepository.existsById(id)) {
            return false;
        }

        connectionRepository.deleteById(id);

        return true;
    }

    public Connection blockRoad(String id) {

        Optional<Connection> existing =
                connectionRepository.findById(id);

        if (!existing.isPresent()) {
            return null;
        }

        Connection connection = existing.get();

        connection.setBlocked(true);

        return connectionRepository.save(connection);
    }

    public Connection restoreRoad(String id) {

        Optional<Connection> existing =
                connectionRepository.findById(id);

        if (!existing.isPresent()) {
            return null;
        }

        Connection connection = existing.get();

        connection.setBlocked(false);

        return connectionRepository.save(connection);
    }

    public Connection updateTraffic(
            String id,
            String trafficLevel) {

        Optional<Connection> existing =
                connectionRepository.findById(id);

        if (!existing.isPresent()) {
            return null;
        }

        validateTrafficLevel(trafficLevel);

        Connection connection = existing.get();

        connection.setTrafficLevel(trafficLevel.toUpperCase());

        return connectionRepository.save(connection);
    }

    // =========================
    // BUS CAPACITY OPERATIONS
    // =========================

    public List<Bus> getAllBuses() {

        return busRepository.findAll();
    }

    public Bus updateBusCapacity(
            String busNumber,
            int occupiedSeats) {

        Optional<Bus> existing =
                busRepository.findById(busNumber);

        if (!existing.isPresent()) {
            return null;
        }

        Bus bus = existing.get();

        if (occupiedSeats < 0 || occupiedSeats > bus.getCapacity()) {
            throw new IllegalArgumentException(
                    "Occupied seats must be between 0 and the bus capacity."
            );
        }

        bus.setOccupied(occupiedSeats);

        return busRepository.save(bus);
    }
    private void validateStop(BusStop stop) {
        if (stop == null || stop.getId() == null || stop.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Bus stop ID is required.");
        }
        if (stop.getName() == null || stop.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Bus stop name is required.");
        }
    }

    private void validateConnection(Connection connection) {
        if (connection == null || connection.getId() == null || connection.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Road ID is required.");
        }
        if (connection.getFromStop() == null || connection.getFromStop().trim().isEmpty()
                || connection.getToStop() == null || connection.getToStop().trim().isEmpty()) {
            throw new IllegalArgumentException("Road From and To stops are required.");
        }
        if (connection.getDistance() < 0 || connection.getTravelTime() < 0 || connection.getFare() < 0) {
            throw new IllegalArgumentException("Road distance, travel time and fare cannot be negative.");
        }
        validateTrafficLevel(connection.getTrafficLevel());
    }

    private void validateTrafficLevel(String trafficLevel) {
        if (trafficLevel == null || trafficLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Traffic level is required.");
        }
        try {
            TrafficLevel.valueOf(trafficLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Traffic level must be LOW, MEDIUM or HIGH.");
        }
    }

}
