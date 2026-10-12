package com.smartbus.controller;

import com.smartbus.dto.BusResponse;
import com.smartbus.entity.Bus;
import com.smartbus.service.BusService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    // Get all buses
    @GetMapping
    public ResponseEntity<List<BusResponse>> getAllBuses() {

        return ResponseEntity.ok(
                busService.getAllBuses()
        );
    }

    // Get one bus by number
    @GetMapping("/{number}")
    public ResponseEntity<BusResponse> getBusByNumber(
            @PathVariable String number) {

        BusResponse bus =
                busService.getBusByNumber(number);

        if (bus == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bus);
    }

    // Add a new bus
    @PostMapping
    public ResponseEntity<Bus> addBus(
            @RequestBody Bus bus) {

        return ResponseEntity.ok(
                busService.addBus(bus)
        );
    }

    // Update bus details
    @PutMapping("/{number}")
    public ResponseEntity<Bus> updateBus(
            @PathVariable String number,
            @RequestBody Bus updatedBus) {

        Bus bus =
                busService.updateBus(
                        number,
                        updatedBus
                );

        if (bus == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bus);
    }

    // Update occupied seats
    @PutMapping("/{number}/capacity")
    public ResponseEntity<BusResponse> updateCapacity(
            @PathVariable String number,
            @RequestBody Bus bus) {

        BusResponse updatedBus =
                busService.updateCapacity(
                        number,
                        bus.getOccupied()
                );

        if (updatedBus == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedBus);
    }

    // Delete a bus
    @DeleteMapping("/{number}")
    public ResponseEntity<String> deleteBus(
            @PathVariable String number) {

        boolean deleted =
                busService.deleteBus(number);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Bus deleted successfully"
        );
    }
}