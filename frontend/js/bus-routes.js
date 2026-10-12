// ================= BUS ROUTES =================

document.addEventListener("DOMContentLoaded", function () {

    let routes = [];

    let busStops = [];
    let connections = [];


    // ================= GET HTML ELEMENTS =================

    const tableBody =
        document.getElementById("routeTableBody");

    const searchInput =
        document.getElementById("routeSearch");

    const searchButton =
        document.getElementById("searchButton");

    const totalRoutes =
        document.getElementById("totalRoutes");

    const totalStops =
        document.getElementById("totalStops");

    const totalDistance =
        document.getElementById("totalDistance");

    const routeDetails =
        document.getElementById("routeDetails");

    const routeDetailsContent =
        document.getElementById("routeDetailsContent");


    // ================= LOAD BACKEND DATA =================

    async function loadBusRoutes() {

        try {

            console.log("Loading bus routes...");


            // Get buses from backend

            const buses =
                await getBuses();


            // Get bus stops from backend

            busStops =
                await getBusStops();


            // Get graph connections from backend

            const roadResponse =
                await fetch(
                    API_BASE_URL + "/admin/roads"
                );


            if (!roadResponse.ok) {

                throw new Error(
                    "Unable to load road connections."
                );

            }


            connections =
                await roadResponse.json();


            console.log("Buses:", buses);

            console.log(
                "Bus Stops:",
                busStops
            );

            console.log(
                "Connections:",
                connections
            );


            // Build route information
            // for every backend bus

            routes =
                buses.map(function (bus) {

                    return buildBusRoute(bus);

                });


            console.log(
                "Calculated Routes:",
                routes
            );


            // Display routes

            displayRoutes(routes);


            // Update summary

            updateSummary();


        } catch (error) {

            console.error(
                "Error loading bus routes:",
                error
            );


            tableBody.innerHTML = `
                <tr>
                    <td colspan="9"
                        class="text-center text-danger py-4">

                        Unable to load bus route data.

                    </td>
                </tr>
            `;


            totalRoutes.textContent =
                "0";


            totalStops.textContent =
                "0";


            totalDistance.textContent =
                "0 km";

        }

    }


    // ================= FIND STOP BY NAME =================

    function findStopByName(name) {

        if (!name) {

            return null;

        }


        const searchName =
            name
                .trim()
                .toLowerCase();


        // -----------------------------------------
        // First try exact match
        // -----------------------------------------

        let stop =
            busStops.find(function (item) {

                return (
                    item.name
                        .trim()
                        .toLowerCase() ===
                    searchName
                );

            });


        if (stop) {

            return stop;

        }


        // -----------------------------------------
        // Then try partial match
        //
        // Example:
        // "Bus Stand"
        // matches
        // "Bhimavaram Bus Stand"
        // -----------------------------------------

        stop =
            busStops.find(function (item) {

                const stopName =
                    item.name
                        .trim()
                        .toLowerCase();


                return (
                    stopName.includes(
                        searchName
                    )
                    ||
                    searchName.includes(
                        stopName
                    )
                );

            });


        return stop || null;

    }


    // ================= BUILD BUS ROUTE =================

    function buildBusRoute(bus) {

        /*
         * Backend example:
         *
         * "SVECW â†’ Bus Stand"
         *
         * We normalize the arrow character
         * before processing the route.
         */


        let routeText =
            String(
                bus.route || ""
            );


        // Fix incorrectly decoded arrow

        routeText =
            routeText.replace(
                /â†’/g,
                "→"
            );


        // Also support normal ASCII arrow

        routeText =
            routeText.replace(
                /->/g,
                "→"
            );


        // Split start and destination

        const parts =
            routeText
                .split("→")
                .map(function (item) {

                    return item.trim();

                });


        const startName =
            parts[0] || "—";


        const destinationName =
            parts[1] || "—";


        // -----------------------------------------
        // Find actual backend bus stops
        // -----------------------------------------

        const startStop =
            findStopByName(
                startName
            );


        const destinationStop =
            findStopByName(
                destinationName
            );


        // -----------------------------------------
        // Create initial route object
        // -----------------------------------------

        let routeData = {

            number:
                String(
                    bus.number
                ),

            route:
                routeText,

            start:
                startName,

            destination:
                destinationName,

            stops: [],

            distance: 0,

            time: 0,

            fare: 0,

            status:
                bus.status ||
                getBusStatus(bus)

        };


        // -----------------------------------------
        // If source/destination cannot
        // be found, return basic information
        // -----------------------------------------

        if (
            !startStop ||
            !destinationStop
        ) {

            console.warn(
                "Could not find route stops:",
                startName,
                destinationName
            );


            return routeData;

        }


        console.log(
            "Calculating path:",
            startStop.id,
            "→",
            destinationStop.id
        );


        // -----------------------------------------
        // Find shortest graph path
        // -----------------------------------------

        const path =
            findShortestPath(
                startStop.id,
                destinationStop.id
            );


        if (!path) {

            console.warn(
                "No graph path found:",
                startName,
                destinationName
            );


            return routeData;

        }


        // -----------------------------------------
        // Convert stop IDs into stop names
        // -----------------------------------------

        routeData.stops =
            path.stopIds.map(
                function (stopId) {

                    const stop =
                        busStops.find(
                            function (item) {

                                return (
                                    item.id ===
                                    stopId
                                );

                            }
                        );


                    return stop
                        ? stop.name
                        : stopId;

                }
            );


        // -----------------------------------------
        // Store calculated values
        // -----------------------------------------

        routeData.distance =
            path.distance;


        routeData.time =
            path.time;


        routeData.fare =
            path.fare;


        return routeData;

    }


    // ================= DIJKSTRA SHORTEST PATH =================

    function findShortestPath(
        startId,
        destinationId
    ) {

        const distances = {};

        const times = {};

        const fares = {};

        const previous = {};

        const unvisited = [];


        // -----------------------------------------
        // Initialize graph nodes
        // -----------------------------------------

        busStops.forEach(function (stop) {

            distances[stop.id] =
                Infinity;

            times[stop.id] =
                0;

            fares[stop.id] =
                0;

            previous[stop.id] =
                null;

            unvisited.push(
                stop.id
            );

        });


        // Distance from source to itself

        distances[startId] =
            0;


        // -----------------------------------------
        // Dijkstra processing
        // -----------------------------------------

        while (
            unvisited.length > 0
        ) {

            let currentId =
                null;

            let smallestDistance =
                Infinity;


            // Find unvisited node
            // with smallest distance

            unvisited.forEach(
                function (id) {

                    if (
                        distances[id] <
                        smallestDistance
                    ) {

                        smallestDistance =
                            distances[id];

                        currentId =
                            id;

                    }

                }
            );


            // No reachable node remains

            if (
                currentId === null
            ) {

                break;

            }


            // Destination reached

            if (
                currentId ===
                destinationId
            ) {

                break;

            }


            // Remove current node

            const index =
                unvisited.indexOf(
                    currentId
                );


            if (index !== -1) {

                unvisited.splice(
                    index,
                    1
                );

            }


            // -----------------------------------------
            // Examine outgoing graph connections
            // -----------------------------------------

            connections.forEach(
                function (connection) {

                    // Ignore blocked roads

                    if (
                        connection.blocked
                    ) {

                        return;

                    }


                    // Only consider edges
                    // starting from current node

                    if (
                        connection.fromStop !==
                        currentId
                    ) {

                        return;

                    }


                    const nextId =
                        connection.toStop;


                    // Skip already processed node

                    if (
                        !unvisited.includes(
                            nextId
                        )
                    ) {

                        return;

                    }


                    // Calculate new distance

                    const newDistance =
                        distances[currentId] +
                        Number(
                            connection.distance
                        );


                    // If this path is better,
                    // update it

                    if (
                        newDistance <
                        distances[nextId]
                    ) {

                        distances[nextId] =
                            newDistance;


                        times[nextId] =
                            times[currentId] +
                            Number(
                                connection.travelTime
                            );


                        fares[nextId] =
                            fares[currentId] +
                            Number(
                                connection.fare
                            );


                        previous[nextId] =
                            currentId;

                    }

                }
            );

        }


        // -----------------------------------------
        // Destination unreachable
        // -----------------------------------------

        if (
            distances[destinationId] ===
            Infinity
        ) {

            return null;

        }


        // -----------------------------------------
        // Reconstruct shortest path
        // -----------------------------------------

        const stopIds = [];

        let current =
            destinationId;


        while (
            current !== null
        ) {

            stopIds.unshift(
                current
            );


            current =
                previous[current];

        }


        // -----------------------------------------
        // Return calculated route
        // -----------------------------------------

        return {

            stopIds:
                stopIds,

            distance:
                Number(
                    distances[
                        destinationId
                    ]
                ),

            time:
                Number(
                    times[
                        destinationId
                    ]
                ),

            fare:
                Number(
                    fares[
                        destinationId
                    ]
                )

        };

    }


    // ================= BUS STATUS =================

    function getBusStatus(bus) {

        if (!bus) {

            return "UNKNOWN";

        }


        const capacity =
            Number(
                bus.capacity || 0
            );


        const occupied =
            Number(
                bus.occupied || 0
            );


        // Full

        if (
            capacity > 0 &&
            occupied >= capacity
        ) {

            return "FULL";

        }


        // Crowded

        if (
            capacity > 0 &&
            occupied >=
            capacity * 0.8
        ) {

            return "CROWDED";

        }


        // Filling

        if (
            capacity > 0 &&
            occupied >=
            capacity * 0.5
        ) {

            return "FILLING";

        }


        // Available

        return "AVAILABLE";

    }


    // ================= DISPLAY ROUTES =================

    function displayRoutes(routeList) {

        tableBody.innerHTML =
            "";


        if (
            routeList.length === 0
        ) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="9"
                        class="text-center text-muted py-4">

                        No routes found.

                    </td>
                </tr>
            `;

            return;

        }


        routeList.forEach(
            function (route) {

                let statusClass =
                    "status-active";


                const status =
                    String(
                        route.status || ""
                    ).toUpperCase();


                // Filling / Crowded

                if (
                    status ===
                    "CROWDED"
                    ||
                    status ===
                    "FILLING"
                ) {

                    statusClass =
                        "status-delayed";

                }


                // Full / Inactive

                if (
                    status ===
                    "FULL"
                    ||
                    status ===
                    "INACTIVE"
                ) {

                    statusClass =
                        "status-inactive";

                }


                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td class="route-number">

                        Route ${route.number}

                    </td>


                    <td>

                        ${route.start}

                    </td>


                    <td>

                        ${route.destination}

                    </td>


                    <td>

                        ${route.stops.length}

                    </td>


                    <td>

                        ${route.distance.toFixed(1)}
                        km

                    </td>


                    <td>

                        ${route.time.toFixed(0)}
                        min

                    </td>


                    <td>

                        ₹${route.fare.toFixed(0)}

                    </td>


                    <td>

                        <span
                            class="route-status ${statusClass}">

                            ${status}

                        </span>

                    </td>


                    <td>

                        <button
                            class="btn btn-sm btn-outline-primary"
                            onclick="viewRoute('${route.number}')">

                            View

                        </button>

                    </td>

                `;


                tableBody.appendChild(
                    row
                );

            }
        );

    }


    // ================= SUMMARY =================

    function updateSummary() {

        // Total routes

        totalRoutes.textContent =
            routes.length;


        // Unique connected stops

        const uniqueStops =
            new Set();


        routes.forEach(
            function (route) {

                route.stops.forEach(
                    function (stop) {

                        uniqueStops.add(
                            stop
                        );

                    }
                );

            }
        );


        totalStops.textContent =
            uniqueStops.size;


        // Total route distance

        let distance =
            0;


        routes.forEach(
            function (route) {

                distance +=
                    route.distance;

            }
        );


        totalDistance.textContent =
            distance.toFixed(1) +
            " km";

    }


    // ================= VIEW ROUTE =================

    window.viewRoute =
        function (routeNumber) {

            const route =
                routes.find(
                    function (item) {

                        return (
                            item.number ===
                            routeNumber
                        );

                    }
                );


            if (!route) {

                return;

            }


            let stopsHTML =
                "";


            // -----------------------------------------
            // Display stop path
            // -----------------------------------------

            route.stops.forEach(
                function (
                    stop,
                    index
                ) {

                    stopsHTML += `

                        <span class="route-stop">

                            ${stop}

                        </span>

                    `;


                    if (
                        index <
                        route.stops.length - 1
                    ) {

                        stopsHTML += `

                            <span class="route-arrow">

                                →

                            </span>

                        `;

                    }

                }
            );


            // -----------------------------------------
            // Route details
            // -----------------------------------------

            routeDetailsContent.innerHTML = `

                <h5 class="fw-bold">

                    Route ${route.number}

                </h5>


                <div class="mb-3">

                    ${stopsHTML}

                </div>


                <div class="row g-3">


                    <div class="col-md-3">

                        <strong>
                            Distance
                        </strong>

                        <br>

                        ${route.distance.toFixed(1)}
                        km

                    </div>


                    <div class="col-md-3">

                        <strong>
                            Travel Time
                        </strong>

                        <br>

                        ${route.time.toFixed(0)}
                        minutes

                    </div>


                    <div class="col-md-3">

                        <strong>
                            Fare
                        </strong>

                        <br>

                        ₹${route.fare.toFixed(0)}

                    </div>


                    <div class="col-md-3">

                        <strong>
                            Status
                        </strong>

                        <br>

                        ${String(
                            route.status
                        ).toUpperCase()}

                    </div>


                </div>

            `;


            // Show details card

            routeDetails.classList.remove(
                "d-none"
            );


            // Scroll to details

            routeDetails.scrollIntoView({

                behavior:
                    "smooth",

                block:
                    "center"

            });

        };


    // ================= SEARCH =================

    function searchRoutes() {

        const searchText =
            searchInput.value
                .trim()
                .toLowerCase();


        // Empty search

        if (
            searchText === ""
        ) {

            displayRoutes(
                routes
            );

            return;

        }


        // Filter routes

        const filteredRoutes =
            routes.filter(
                function (route) {

                    const routeNumber =
                        route.number
                            .toLowerCase();


                    const routeName =
                        route.route
                            .toLowerCase();


                    const start =
                        route.start
                            .toLowerCase();


                    const destination =
                        route.destination
                            .toLowerCase();


                    const stops =
                        route.stops
                            .join(" ")
                            .toLowerCase();


                    return (

                        routeNumber.includes(
                            searchText
                        )

                        ||

                        routeName.includes(
                            searchText
                        )

                        ||

                        start.includes(
                            searchText
                        )

                        ||

                        destination.includes(
                            searchText
                        )

                        ||

                        stops.includes(
                            searchText
                        )

                    );

                }
            );


        displayRoutes(
            filteredRoutes
        );

    }


    // ================= SEARCH EVENTS =================

    searchButton.addEventListener(
        "click",
        searchRoutes
    );


    searchInput.addEventListener(
        "keyup",
        function (event) {

            if (
                event.key ===
                "Enter"
            ) {

                searchRoutes();

            }

        }
    );


    // ================= START APPLICATION =================

    loadBusRoutes();

});