package com.smartbus.controller;

import com.smartbus.dto.SimulationRequest;
import com.smartbus.entity.Simulation;
import com.smartbus.service.SimulationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(
            SimulationService simulationService) {

        this.simulationService =
                simulationService;
    }

    /*
     * Get all saved simulations.
     */
    @GetMapping
    public ResponseEntity<List<Simulation>>
    getAllSimulations() {

        return ResponseEntity.ok(
                simulationService
                        .getAllSimulations()
        );
    }

    /*
     * Get one simulation by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Simulation>
    getSimulationById(
            @PathVariable String id) {

        Optional<Simulation> simulation =
                simulationService
                        .getSimulationById(id);

        if (!simulation.isPresent()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                simulation.get()
        );
    }

    /*
     * Run a What-If simulation.
     */
    @PostMapping
    public ResponseEntity<Simulation>
    runSimulation(
            @RequestBody SimulationRequest request) {

        if (request == null
                || request.getScenario() == null
                || request.getScenario().trim().isEmpty()) {
            throw new IllegalArgumentException("Simulation scenario is required.");
        }

        if (request.getValue() == null
                || request.getValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Simulation value is required.");
        }

        Simulation simulation =
                simulationService.runSimulation(
                        request.getScenario(),
                        request.getValue(),
                        "",
                        ""
                );

        return ResponseEntity.ok(
                simulation
        );
    }

    /*
     * Save/update a simulation.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Simulation>
    saveSimulation(
            @PathVariable String id,
            @RequestBody Simulation simulation) {

        simulation.setId(id);

        Simulation savedSimulation =
                simulationService.saveSimulation(
                        simulation
                );

        return ResponseEntity.ok(
                savedSimulation
        );
    }

    /*
     * Delete a simulation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteSimulation(
            @PathVariable String id) {

        boolean deleted =
                simulationService.deleteSimulation(
                        id
                );

        if (!deleted) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                "Simulation deleted successfully"
        );
    }
}