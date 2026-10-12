package com.smartbus.controller;

import com.smartbus.algorithm.Edge;
import com.smartbus.dto.NetworkResponse;
import com.smartbus.service.NetworkService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/network")
public class NetworkController {

    private final NetworkService networkService;

    public NetworkController(NetworkService networkService) {
        this.networkService = networkService;
    }

    /*
     * Performs BFS traversal from a given bus stop.
     */
    @GetMapping("/bfs/{startStopId}")
    public ResponseEntity<List<String>> runBFS(
            @PathVariable String startStopId) {

        return ResponseEntity.ok(
                networkService.runBFS(startStopId)
        );
    }

    /*
     * Performs DFS traversal from a given bus stop.
     */
    @GetMapping("/dfs/{startStopId}")
    public ResponseEntity<List<String>> runDFS(
            @PathVariable String startStopId) {

        return ResponseEntity.ok(
                networkService.runDFS(startStopId)
        );
    }

    /*
     * Returns complete network statistics.
     */
    @GetMapping("/statistics")
    public ResponseEntity<NetworkResponse> getNetworkStatistics() {

        return ResponseEntity.ok(
                networkService.getNetworkStatistics()
        );
    }

    /*
     * Generates Minimum Spanning Tree using Prim's algorithm.
     */
    @GetMapping("/mst/prim")
    public ResponseEntity<List<Edge>> generatePrimMST() {

        return ResponseEntity.ok(
                networkService.generatePrimMST()
        );
    }

    /*
     * Generates Minimum Spanning Tree using Kruskal's algorithm.
     */
    @GetMapping("/mst/kruskal")
    public ResponseEntity<List<Edge>> generateKruskalMST() {

        return ResponseEntity.ok(
                networkService.generateKruskalMST()
        );
    }
}