package com.smartbus.service;

import com.smartbus.dto.BusResponse;
import com.smartbus.entity.Bus;
import com.smartbus.repository.BusRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    private final BusRepository busRepository;

    public BusService(
            BusRepository busRepository) {

        this.busRepository = busRepository;
    }

    // Get all buses
    public List<BusResponse> getAllBuses() {

        List<Bus> buses =
                busRepository.findAll();

        List<BusResponse> responses =
                new ArrayList<>();

        for (Bus bus : buses) {

            responses.add(
                    convertToResponse(bus)
            );
        }

        return responses;
    }

    // Get one bus by number
    public BusResponse getBusByNumber(
            String number) {

        Optional<Bus> optionalBus =
                busRepository.findById(number);

        if (!optionalBus.isPresent()) {
            return null;
        }

        return convertToResponse(
                optionalBus.get()
        );
    }

    // Add a new bus
    public Bus addBus(Bus bus) {

        validateBus(bus);

        if (busRepository.existsById(bus.getNumber())) {
            throw new IllegalArgumentException("Bus already exists: " + bus.getNumber());
        }

        return busRepository.save(bus);
    }

    // Update bus details
    public Bus updateBus(
            String number,
            Bus updatedBus) {

        Optional<Bus> existingBus =
                busRepository.findById(number);

        if (!existingBus.isPresent()) {
            return null;
        }

        validateBus(updatedBus);

        Bus bus =
                existingBus.get();

        bus.setRoute(
                updatedBus.getRoute()
        );

        bus.setCapacity(
                updatedBus.getCapacity()
        );

        bus.setOccupied(
                updatedBus.getOccupied()
        );

        return busRepository.save(bus);
    }

    // Update occupied seats
    public BusResponse updateCapacity(
            String number,
            int occupiedSeats) {

        Optional<Bus> optionalBus =
                busRepository.findById(number);

        if (!optionalBus.isPresent()) {
            return null;
        }

        Bus bus =
                optionalBus.get();

        if (occupiedSeats < 0 || occupiedSeats > bus.getCapacity()) {
            throw new IllegalArgumentException(
                    "Occupied seats must be between 0 and the bus capacity."
            );
        }

        bus.setOccupied(
                occupiedSeats
        );

        Bus savedBus =
                busRepository.save(bus);

        return convertToResponse(
                savedBus
        );
    }

    // Delete a bus
    public boolean deleteBus(
            String number) {

        if (!busRepository.existsById(number)) {
            return false;
        }

        busRepository.deleteById(number);

        return true;
    }

    private void validateBus(Bus bus) {
        if (bus == null || bus.getNumber() == null || bus.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Bus number is required.");
        }
        if (bus.getCapacity() <= 0) {
            throw new IllegalArgumentException("Bus capacity must be greater than zero.");
        }
        if (bus.getOccupied() < 0 || bus.getOccupied() > bus.getCapacity()) {
            throw new IllegalArgumentException("Occupied seats must be between 0 and the bus capacity.");
        }
    }

    // Convert Bus entity to BusResponse
    private BusResponse convertToResponse(
            Bus bus) {

        int capacity =
                bus.getCapacity();

        int occupied =
                bus.getOccupied();

        int available =
                capacity - occupied;

        String status =
                calculateStatus(
                        capacity,
                        occupied
                );

        return new BusResponse(
                bus.getNumber(),
                bus.getRoute(),
                capacity,
                occupied,
                available,
                status
        );
    }

    // Calculate bus occupancy status
    private String calculateStatus(
            int capacity,
            int occupied) {

        if (capacity <= 0) {
            return "AVAILABLE";
        }

        double occupancy =
                ((double) occupied / capacity) * 100;

        if (occupancy >= 100) {
            return "FULL";

        } else if (occupancy >= 75) {
            return "CROWDED";

        } else if (occupancy >= 50) {
            return "FILLING";

        } else {
            return "AVAILABLE";
        }
    }
}