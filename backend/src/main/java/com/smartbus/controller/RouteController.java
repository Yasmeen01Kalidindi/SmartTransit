package com.smartbus.controller;

import com.smartbus.dto.RouteRequest;
import com.smartbus.dto.RouteResponse;
import com.smartbus.dto.RouteOptionResponse;
import com.smartbus.algorithm.RouteResult;
import com.smartbus.algorithm.RouteMetrics;
import com.smartbus.service.RouteService;
import com.smartbus.enums.TimePeriod;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/shortest")
    public ResponseEntity<RouteResponse> findShortestRoute(
            @RequestBody RouteRequest request) {
        return findRoute(request, "SHORTEST");
    }

    @PostMapping("/fastest")
    public ResponseEntity<RouteResponse> findFastestRoute(
            @RequestBody RouteRequest request) {
        return findRoute(request, "FASTEST");
    }

    @PostMapping("/cheapest")
    public ResponseEntity<RouteResponse> findCheapestRoute(
            @RequestBody RouteRequest request) {
        return findRoute(request, "CHEAPEST");
    }

    @PostMapping("/traffic-aware")
    public ResponseEntity<RouteResponse> findTrafficAwareRoute(
            @RequestBody RouteRequest request) {
        return findRoute(request, "TRAFFIC_AWARE");
    }

    @PostMapping("/alternatives")
    public ResponseEntity<List<RouteOptionResponse>> findAlternativeRoutes(
            @RequestBody RouteRequest request) {

        validateRequest(request);
        TimePeriod timePeriod = getTimePeriod(request.getTimePeriod());

        return ResponseEntity.ok(
                routeService.findAlternativeRoutes(
                        request.getSource(),
                        request.getDestination(),
                        timePeriod
                )
        );
    }

    private ResponseEntity<RouteResponse> findRoute(
            RouteRequest request,
            String preference) {

        validateRequest(request);
        TimePeriod timePeriod = getTimePeriod(request.getTimePeriod());

        RouteResult result = routeService.findRoute(
                request.getSource(),
                request.getDestination(),
                preference,
                timePeriod
        );

        List<String> route = routeService.getRouteStopNames(result);

        RouteResponse response = new RouteResponse();
        response.setSource(request.getSource());
        response.setDestination(request.getDestination());
        response.setPreference(preference);
        response.setTimePeriod(request.getTimePeriod());

        if (route.isEmpty()) {
            response.setSuccess(false);
            return ResponseEntity.ok(response);
        }

        RouteMetrics metrics = routeService.getRouteMetrics(
                request.getSource(),
                request.getDestination(),
                preference,
                timePeriod
        );

        response.setSuccess(true);
        response.setRoute(route);
        response.setDistance(metrics.getDistance());
        response.setTravelTime(metrics.getTravelTime());
        response.setFare(metrics.getFare());
        response.setStops(route.size());
        response.setTrafficLevel(metrics.getTrafficLevel());

        return ResponseEntity.ok(response);
    }

    private void validateRequest(RouteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Route request is required.");
        }

        if (request.getSource() == null || request.getSource().trim().isEmpty()) {
            throw new IllegalArgumentException("Source stop is required.");
        }

        if (request.getDestination() == null || request.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination stop is required.");
        }

        if (request.getSource().equals(request.getDestination())) {
            throw new IllegalArgumentException("Source and destination cannot be the same.");
        }
    }

    private TimePeriod getTimePeriod(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return TimePeriod.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid time period: " + value
            );
        }
    }
}
