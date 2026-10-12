package com.smartbus.controller;

import com.smartbus.entity.BusStop;
import com.smartbus.service.BusStopService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stops")
public class BusStopController {

    private final BusStopService busStopService;

    public BusStopController(
            BusStopService busStopService) {

        this.busStopService = busStopService;
    }

    /*
     * Get all bus stops.
     */
    @GetMapping
    public ResponseEntity<List<BusStop>> getAllStops() {

        return ResponseEntity.ok(
                busStopService.getAllStops()
        );
    }

    /*
     * Get one bus stop by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BusStop> getStopById(
            @PathVariable String id) {

        Optional<BusStop> stop =
                busStopService.getStopById(id);

        if (!stop.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(stop.get());
    }

    /*
     * Add a new bus stop.
     */
    @PostMapping
    public ResponseEntity<BusStop> addStop(
            @RequestBody BusStop busStop) {

        BusStop savedStop =
                busStopService.addStop(busStop);

        return ResponseEntity.ok(savedStop);
    }

    /*
     * Update an existing bus stop.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BusStop> updateStop(
            @PathVariable String id,
            @RequestBody BusStop updatedStop) {

        BusStop stop =
                busStopService.updateStop(
                        id,
                        updatedStop
                );

        if (stop == null) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(stop);
    }

    /*
     * Delete a bus stop.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStop(
            @PathVariable String id) {

        boolean deleted =
                busStopService.deleteStop(id);

        if (!deleted) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                "Bus stop deleted successfully"
        );
    }
}