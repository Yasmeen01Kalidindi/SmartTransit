package com.smartbus.controller;

import com.smartbus.entity.Bus;
import com.smartbus.entity.BusStop;
import com.smartbus.entity.Connection;
import com.smartbus.service.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // =========================
    // BUS STOP OPERATIONS
    // =========================

    // Get all bus stops
    @GetMapping("/stops")
    public ResponseEntity<List<BusStop>> getAllStops() {

        return ResponseEntity.ok(
                adminService.getAllStops()
        );
    }

    // Add a new bus stop
    @PostMapping("/stops")
    public ResponseEntity<BusStop> addStop(
            @RequestBody BusStop busStop) {

        return ResponseEntity.ok(
                adminService.addStop(busStop)
        );
    }

    // Update an existing bus stop
    @PutMapping("/stops/{id}")
    public ResponseEntity<BusStop> updateStop(
            @PathVariable String id,
            @RequestBody BusStop updatedStop) {

        BusStop stop =
                adminService.updateStop(
                        id,
                        updatedStop
                );

        if (stop == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(stop);
    }

    // Delete a bus stop
    @DeleteMapping("/stops/{id}")
    public ResponseEntity<String> deleteStop(
            @PathVariable String id) {

        boolean deleted =
                adminService.deleteStop(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Bus stop deleted successfully"
        );
    }

    // =========================
    // ROAD / CONNECTION OPERATIONS
    // =========================

    // Get all roads/connections
    @GetMapping("/roads")
    public ResponseEntity<List<Connection>> getAllConnections() {

        return ResponseEntity.ok(
                adminService.getAllConnections()
        );
    }

    // Add a new road/connection
    @PostMapping("/roads")
    public ResponseEntity<Connection> addConnection(
            @RequestBody Connection connection) {

        return ResponseEntity.ok(
                adminService.addConnection(connection)
        );
    }

    // Delete a road/connection
    @DeleteMapping("/roads/{id}")
    public ResponseEntity<String> deleteConnection(
            @PathVariable String id) {

        boolean deleted =
                adminService.deleteConnection(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Road deleted successfully"
        );
    }

    // Block a road
    @PutMapping("/roads/{id}/block")
    public ResponseEntity<Connection> blockRoad(
            @PathVariable String id) {

        Connection connection =
                adminService.blockRoad(id);

        if (connection == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(connection);
    }

    // Restore a blocked road
    @PutMapping("/roads/{id}/restore")
    public ResponseEntity<Connection> restoreRoad(
            @PathVariable String id) {

        Connection connection =
                adminService.restoreRoad(id);

        if (connection == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(connection);
    }

    // Update traffic level
    @PutMapping("/roads/{id}/traffic")
    public ResponseEntity<Connection> updateTraffic(
            @PathVariable String id,
            @RequestBody Connection connection) {

        Connection updatedConnection =
                adminService.updateTraffic(
                        id,
                        connection.getTrafficLevel()
                );

        if (updatedConnection == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedConnection);
    }

    // =========================
    // BUS CAPACITY OPERATIONS
    // =========================

    // Get all buses
    @GetMapping("/buses")
    public ResponseEntity<List<Bus>> getAllBuses() {

        return ResponseEntity.ok(
                adminService.getAllBuses()
        );
    }

    // Update occupied seats
    @PutMapping("/buses/{busNumber}/capacity")
    public ResponseEntity<Bus> updateBusCapacity(
            @PathVariable String busNumber,
            @RequestBody Bus bus) {

        Bus updatedBus =
                adminService.updateBusCapacity(
                        busNumber,
                        bus.getOccupied()
                );

        if (updatedBus == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedBus);
    }
}