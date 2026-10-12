/* =========================================
   SMARTTRANSIT API LAYER
   ========================================= */

const API_BASE_URL = "http://localhost:8080/api";


/* =========================================
   GENERIC API REQUEST
   ========================================= */

async function apiRequest(endpoint, options = {}) {

    try {

        const response = await fetch(
            API_BASE_URL + endpoint,
            {
                ...options,

                headers: {
                    "Content-Type": "application/json",
                    ...(options.headers || {})
                }
            }
        );


        if (!response.ok) {

            const errorText = await response.text();

            throw new Error(
                "API request failed: " +
                response.status +
                " - " +
                errorText
            );
        }


        return await response.json();

    } catch (error) {

        console.error("API Error:", error);

        throw error;
    }
}


/* =========================================
   ROUTE REQUEST HELPER
   ========================================= */

function createRouteRequest(
    source,
    destination,
    preference,
    trafficLevel,
    timePeriod
) {

    return {
        source: source,
        destination: destination,
        preference: preference,
        trafficLevel: trafficLevel || "LOW",
        timePeriod: timePeriod || "AFTERNOON"
    };
}


/* =========================================
   1. SHORTEST ROUTE
   ========================================= */

async function findShortestRoute(
    source,
    destination,
    timePeriod
) {

    return apiRequest(
        "/routes/shortest",
        {
            method: "POST",

            body: JSON.stringify(
                createRouteRequest(
                    source,
                    destination,
                    "SHORTEST",
                    "LOW",
                    timePeriod
                )
            )
        }
    );
}


/* =========================================
   2. FASTEST ROUTE
   ========================================= */

async function findFastestRoute(
    source,
    destination,
    timePeriod
) {

    return apiRequest(
        "/routes/fastest",
        {
            method: "POST",

            body: JSON.stringify(
                createRouteRequest(
                    source,
                    destination,
                    "FASTEST",
                    "LOW",
                    timePeriod
                )
            )
        }
    );
}


/* =========================================
   3. CHEAPEST ROUTE
   ========================================= */

async function findCheapestRoute(
    source,
    destination,
    timePeriod
) {

    return apiRequest(
        "/routes/cheapest",
        {
            method: "POST",

            body: JSON.stringify(
                createRouteRequest(
                    source,
                    destination,
                    "CHEAPEST",
                    "LOW",
                    timePeriod
                )
            )
        }
    );
}


/* =========================================
   4. TRAFFIC-AWARE ROUTE
   ========================================= */

async function findTrafficAwareRoute(
    source,
    destination,
    trafficLevel,
    timePeriod
) {

    return apiRequest(
        "/routes/traffic-aware",
        {
            method: "POST",

            body: JSON.stringify(
                createRouteRequest(
                    source,
                    destination,
                    "TRAFFIC_AWARE",
                    trafficLevel,
                    timePeriod
                )
            )
        }
    );
}

/* =========================================
   5. ALTERNATIVE ROUTES
   ========================================= */

async function findAlternativeRoutes(
    source,
    destination,
    timePeriod
) {

    return apiRequest(
        "/routes/alternatives",
        {
            method: "POST",

            body: JSON.stringify({
                source: source,
                destination: destination,
                timePeriod: timePeriod || "AFTERNOON"
            })
        }
    );
}


/* =========================================
   6. GET ALL BUS STOPS
   ========================================= */

async function getBusStops() {

    return apiRequest(
        "/stops",
        {
            method: "GET"
        }
    );
}


/* =========================================
   7. GET BUS STOP BY ID
   ========================================= */

async function getBusStop(stopId) {

    return apiRequest(
        "/stops/" + stopId,
        {
            method: "GET"
        }
    );
}


/* =========================================
   8. GET ALL BUSES
   ========================================= */

async function getBuses() {

    return apiRequest(
        "/buses",
        {
            method: "GET"
        }
    );
}


/* =========================================
   9. GET BUS BY NUMBER
   ========================================= */

async function getBus(busNumber) {

    return apiRequest(
        "/buses/" + busNumber,
        {
            method: "GET"
        }
    );
}


/* =========================================
   10. CHECK CONNECTIVITY USING BFS
   ========================================= */

async function checkConnectivity(
    startStopId
) {

    return apiRequest(
        "/network/bfs/" + startStopId,
        {
            method: "GET"
        }
    );
}


/* =========================================
   11. GET NETWORK STATISTICS
   ========================================= */

async function getNetworkStatistics() {

    return apiRequest(
        "/network/statistics",
        {
            method: "GET"
        }
    );
}


/* =========================================
   12. GENERATE PRIM MST
   ========================================= */

async function getPrimMST() {

    return apiRequest(
        "/network/mst/prim",
        {
            method: "GET"
        }
    );
}


/* =========================================
   13. GENERATE KRUSKAL MST
   ========================================= */

async function getKruskalMST() {

    return apiRequest(
        "/network/mst/kruskal",
        {
            method: "GET"
        }
    );
}


/* =========================================
   14. GET ALL ADMIN STOPS
   ========================================= */

async function getAdminStops() {

    return apiRequest(
        "/admin/stops",
        {
            method: "GET"
        }
    );
}


/* =========================================
   15. ADD BUS STOP
   ========================================= */

async function addBusStop(stop) {

    return apiRequest(
        "/admin/stops",
        {
            method: "POST",
            body: JSON.stringify(stop)
        }
    );
}


/* =========================================
   16. UPDATE BUS STOP
   ========================================= */

async function updateBusStop(
    stopId,
    stop
) {

    return apiRequest(
        "/admin/stops/" + stopId,
        {
            method: "PUT",
            body: JSON.stringify(stop)
        }
    );
}


/* =========================================
   17. DELETE BUS STOP
   ========================================= */

async function deleteBusStop(stopId) {

    return apiRequest(
        "/admin/stops/" + stopId,
        {
            method: "DELETE"
        }
    );
}


/* =========================================
   18. GET ADMIN ROADS
   ========================================= */

async function getRoads() {

    return apiRequest(
        "/admin/roads",
        {
            method: "GET"
        }
    );
}


/* =========================================
   19. ADD ROAD
   ========================================= */

async function addRoad(road) {

    return apiRequest(
        "/admin/roads",
        {
            method: "POST",
            body: JSON.stringify(road)
        }
    );
}


/* =========================================
   20. DELETE ROAD
   ========================================= */

async function deleteRoad(roadId) {

    return apiRequest(
        "/admin/roads/" + roadId,
        {
            method: "DELETE"
        }
    );
}


/* =========================================
   21. BLOCK ROAD
   ========================================= */

async function blockRoad(roadId) {

    return apiRequest(
        "/admin/roads/" + roadId + "/block",
        {
            method: "PUT"
        }
    );
}


/* =========================================
   22. RESTORE ROAD
   ========================================= */

async function restoreRoad(roadId) {

    return apiRequest(
        "/admin/roads/" + roadId + "/restore",
        {
            method: "PUT"
        }
    );
}


/* =========================================
   23. UPDATE TRAFFIC
   ========================================= */

async function updateTraffic(
    roadId,
    trafficLevel
) {

    return apiRequest(
        "/admin/roads/" + roadId + "/traffic",
        {
            method: "PUT",

            body: JSON.stringify({
                trafficLevel: trafficLevel
            })
        }
    );
}


/* =========================================
   24. GET ADMIN BUSES
   ========================================= */

async function getAdminBuses() {

    return apiRequest(
        "/admin/buses",
        {
            method: "GET"
        }
    );
}


/* =========================================
   25. UPDATE BUS CAPACITY
   ========================================= */

async function updateBusCapacity(
    busNumber,
    occupiedSeats
) {

    return apiRequest(
        "/admin/buses/" + busNumber + "/capacity",
        {
            method: "PUT",

            body: JSON.stringify({
                occupied: occupiedSeats
            })
        }
    );
}


/* =========================================
   26. GET ALL SIMULATIONS
   ========================================= */

async function getSimulations() {

    return apiRequest(
        "/simulation",
        {
            method: "GET"
        }
    );
}


/* =========================================
   27. RUN WHAT-IF SIMULATION
   ========================================= */

async function runWhatIfSimulation(
    scenario,
    value
) {

    return apiRequest(
        "/simulation",
        {
            method: "POST",

            body: JSON.stringify({
                scenario: scenario,
                value: value
            })
        }
    );
}


/* =========================================
   28. GET SIMULATION BY ID
   ========================================= */

async function getSimulation(
    simulationId
) {

    return apiRequest(
        "/simulation/" + simulationId,
        {
            method: "GET"
        }
    );
}


/* =========================================
   29. DELETE SIMULATION
   ========================================= */

async function deleteSimulation(
    simulationId
) {

    return apiRequest(
        "/simulation/" + simulationId,
        {
            method: "DELETE"
        }
    );
}