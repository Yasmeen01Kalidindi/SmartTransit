package com.smartbus.service;

import com.smartbus.entity.BusStop;
import com.smartbus.repository.BusStopRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusStopService {

    private final BusStopRepository busStopRepository;

    public BusStopService(
            BusStopRepository busStopRepository) {

        this.busStopRepository = busStopRepository;
    }

    // Get all bus stops
    public List<BusStop> getAllStops() {

        return busStopRepository.findAll();
    }

    // Get one bus stop by ID
    public Optional<BusStop> getStopById(
            String id) {

        return busStopRepository.findById(id);
    }

    // Add a new bus stop
    public BusStop addStop(
            BusStop busStop) {

        return busStopRepository.save(busStop);
    }

    // Update an existing bus stop
    public BusStop updateStop(
            String id,
            BusStop updatedStop) {

        Optional<BusStop> existingStop =
                busStopRepository.findById(id);

        if (!existingStop.isPresent()) {
            return null;
        }

        BusStop stop =
                existingStop.get();

        stop.setName(
                updatedStop.getName()
        );

        stop.setLocation(
                updatedStop.getLocation()
        );

        stop.setConnectedRoads(
                updatedStop.getConnectedRoads()
        );

        stop.setStatus(
                updatedStop.getStatus()
        );

        stop.setLatitude(
                updatedStop.getLatitude()
        );

        stop.setLongitude(
                updatedStop.getLongitude()
        );

        return busStopRepository.save(stop);
    }

    // Delete a bus stop
    public boolean deleteStop(
            String id) {

        if (!busStopRepository.existsById(id)) {
            return false;
        }

        busStopRepository.deleteById(id);

        return true;
    }
}