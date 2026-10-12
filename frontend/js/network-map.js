/* =========================================
   SMARTTRANSIT NETWORK MAP
   Backend Connected Version
   ========================================= */


// =========================================
// 1. GRAPH DATA
// =========================================

let stops = [];

let connections = [];

let buses = [];


// =========================================
// 2. GET GRAPH ELEMENTS
// =========================================

const graph =
    document.getElementById("networkGraph");

const svg =
    document.getElementById("networkSvg");

const connectivityButton =
    document.getElementById("connectivityButton");


// =========================================
// 3. LOAD NETWORK DATA
// =========================================

async function loadNetworkData() {

    try {

        console.log("Loading network data...");


        // Get bus stops from backend
        stops = await getBusStops();


        // Get roads/connections from backend
        const roadsResponse =
            await fetch(
                "http://localhost:8080/api/admin/roads"
            );


        if (!roadsResponse.ok) {

            throw new Error(
                "Unable to load road data."
            );

        }


        connections =
            await roadsResponse.json();


        // Get buses from backend
        buses = await getBuses();


        console.log("Bus Stops:", stops);

        console.log("Connections:", connections);

        console.log("Buses:", buses);


        // Draw the graph
        createGraph();


        // Update analytics
        updateStatistics();


    } catch (error) {

        console.error(
            "Error loading network data:",
            error
        );


        showNetworkError();

    }

}


// =========================================
// 4. CREATE GRAPH
// =========================================

function createGraph() {

    // Clear old graph
    svg.innerHTML = "";


    // Remove previously created nodes
    const oldNodes =
        graph.querySelectorAll(
            ".network-node"
        );


    oldNodes.forEach(function (node) {

        node.remove();

    });


    if (!stops || stops.length === 0) {

        return;

    }


    // Calculate visual positions
    calculateStopPositions();


    // Edges must be created first
    // so nodes appear above them.
    createEdges();


    // Create vertices
    createNodes();

}


// =========================================
// 5. CALCULATE STOP POSITIONS
// =========================================

function calculateStopPositions() {

    /*
     * The backend provides latitude and longitude.
     *
     * We convert them into x/y percentages
     * so the stops can be displayed on the
     * graph without hardcoded positions.
     */


    const validStops =
        stops.filter(function (stop) {

            return (
                typeof stop.latitude === "number" &&
                typeof stop.longitude === "number"
            );

        });


    // If coordinates are unavailable,
    // use a simple fallback layout.
    if (validStops.length === 0) {

        stops.forEach(function (stop, index) {

            const angle =
                (2 * Math.PI * index) /
                stops.length;


            stop.x =
                50 +
                35 * Math.cos(angle);


            stop.y =
                50 +
                35 * Math.sin(angle);

        });


        return;

    }


    // Find coordinate boundaries

    const latitudes =
        validStops.map(function (stop) {

            return stop.latitude;

        });


    const longitudes =
        validStops.map(function (stop) {

            return stop.longitude;

        });


    const minLat =
        Math.min(...latitudes);


    const maxLat =
        Math.max(...latitudes);


    const minLong =
        Math.min(...longitudes);


    const maxLong =
        Math.max(...longitudes);


    stops.forEach(function (stop) {

        if (
            typeof stop.latitude !== "number" ||
            typeof stop.longitude !== "number"
        ) {

            return;

        }


        // Longitude controls horizontal position
        let x;

        if (maxLong === minLong) {

            x = 50;

        } else {

            x =
                ((stop.longitude - minLong) /
                (maxLong - minLong)) *
                70 + 15;

        }


        // Latitude controls vertical position.
        // Higher latitude is placed higher.
        let y;

        if (maxLat === minLat) {

            y = 50;

        } else {

            y =
                85 -
                ((stop.latitude - minLat) /
                (maxLat - minLat)) *
                70;

        }


        stop.x = x;

        stop.y = y;

    });

}


// =========================================
// 6. CREATE GRAPH NODES
// =========================================

function createNodes() {

    stops.forEach(function (stop) {

        const node =
            document.createElement("div");


        node.classList.add(
            "network-node"
        );


        node.textContent =
            stop.name;


        node.style.left =
            stop.x + "%";


        node.style.top =
            stop.y + "%";


        node.dataset.stopId =
            stop.id;


        // Node click
        node.addEventListener(
            "click",
            function () {

                showStopDetails(stop);

            }
        );


        graph.appendChild(node);

    });

}


// =========================================
// 7. CREATE GRAPH EDGES
// =========================================

function createEdges() {

    connections.forEach(
        function (connection) {


            const fromStop =
                stops.find(
                    function (stop) {

                        return (
                            String(stop.id) ===
                            String(connection.fromStop)
                        );

                    }
                );


            const toStop =
                stops.find(
                    function (stop) {

                        return (
                            String(stop.id) ===
                            String(connection.toStop)
                        );

                    }
                );


            if (!fromStop || !toStop) {

                return;

            }


            /*
             * SVG uses a 0–100 viewBox.
             * This means the graph scales with
             * the container.
             */

            const line =
                document.createElementNS(
                    "http://www.w3.org/2000/svg",
                    "line"
                );


            line.setAttribute(
                "x1",
                fromStop.x
            );


            line.setAttribute(
                "y1",
                fromStop.y
            );


            line.setAttribute(
                "x2",
                toStop.x
            );


            line.setAttribute(
                "y2",
                toStop.y
            );


            // Store road information
            line.dataset.roadId =
                connection.id;


            line.dataset.distance =
                connection.distance;


            /*
             * Active road
             * or blocked road.
             */

            if (connection.blocked) {

                line.classList.add(
                    "blocked-edge"
                );

            } else {

                line.classList.add(
                    "network-edge"
                );

            }


            // Clicking an edge
            line.addEventListener(
                "click",
                function () {

                    showRoadDetails(
                        connection,
                        fromStop,
                        toStop
                    );

                }
            );


            svg.appendChild(line);

        }
    );


    /*
     * Make SVG responsive.
     */

    svg.setAttribute(
        "viewBox",
        "0 0 100 100"
    );


    svg.setAttribute(
        "preserveAspectRatio",
        "none"
    );

}


// =========================================
// 8. SHOW STOP DETAILS
// =========================================

function showStopDetails(stop) {

    const connectedRoads =
        connections.filter(
            function (connection) {

                return (
                    connection.fromStop === stop.id ||
                    connection.toStop === stop.id
                );

            }
        );


    alert(

        "Bus Stop Details\n\n" +

        "Stop ID: " +
        stop.id +

        "\nName: " +
        stop.name +

        "\nLocation: " +
        (stop.location || "Not available") +

        "\nConnected Roads: " +
        connectedRoads.length +

        "\nLatitude: " +
        (stop.latitude ?? "N/A") +

        "\nLongitude: " +
        (stop.longitude ?? "N/A")

    );

}


// =========================================
// 9. SHOW ROAD DETAILS
// =========================================

function showRoadDetails(
    connection,
    fromStop,
    toStop
) {

    alert(

        "Road / Edge Details\n\n" +

        "Road ID: " +
        connection.id +

        "\nFrom: " +
        fromStop.name +

        "\nTo: " +
        toStop.name +

        "\nDistance: " +
        connection.distance +
        " km" +

        "\nTravel Time: " +
        connection.travelTime +
        " min" +

        "\nFare: ₹" +
        connection.fare +

        "\nTraffic: " +
        connection.trafficLevel +

        "\nStatus: " +
        (
            connection.blocked
                ? "BLOCKED"
                : "ACTIVE"
        )

    );

}


// =========================================
// 10. CALCULATE CONNECTED COMPONENTS
// =========================================

function calculateComponents() {

    const visited =
        new Set();


    let components = 0;


    function dfs(stopId) {

        visited.add(stopId);


        connections.forEach(
            function (connection) {


                // Blocked roads cannot be used
                if (connection.blocked) {

                    return;

                }


                let nextStop = null;


                if (
                    connection.fromStop ===
                    stopId
                ) {

                    nextStop =
                        connection.toStop;

                } else if (
                    connection.toStop ===
                    stopId
                ) {

                    nextStop =
                        connection.fromStop;

                }


                if (
                    nextStop &&
                    !visited.has(nextStop)
                ) {

                    dfs(nextStop);

                }

            }
        );

    }


    stops.forEach(function (stop) {

        if (!visited.has(stop.id)) {

            components++;

            dfs(stop.id);

        }

    });


    return components;

}


// =========================================
// 11. FIND MOST CONNECTED STOP
// =========================================

function findMostConnectedStop() {

    if (!stops.length) {

        return "No data";

    }


    const degree = {};


    stops.forEach(function (stop) {

        degree[stop.id] = 0;

    });


    connections.forEach(
        function (connection) {


            if (connection.blocked) {

                return;

            }


            if (
                degree[
                    connection.fromStop
                ] !== undefined
            ) {

                degree[
                    connection.fromStop
                ]++;

            }


            if (
                degree[
                    connection.toStop
                ] !== undefined
            ) {

                degree[
                    connection.toStop
                ]++;

            }

        }
    );


    let highestStop =
        stops[0];


    let highestDegree =
        degree[stops[0].id];


    stops.forEach(function (stop) {

        if (
            degree[stop.id] >
            highestDegree
        ) {

            highestDegree =
                degree[stop.id];

            highestStop =
                stop;

        }

    });


    return (
        highestStop.name +
        " (" +
        highestDegree +
        " connections)"
    );

}


// =========================================
// 12. CALCULATE NETWORK DISTANCE
// =========================================

function calculateNetworkDistance() {

    let total = 0;


    connections.forEach(
        function (connection) {


            // Ignore blocked roads
            if (!connection.blocked) {

                total +=
                    Number(
                        connection.distance
                    );

            }

        }
    );


    return total.toFixed(1) + " km";

}


// =========================================
// 13. UPDATE NETWORK STATISTICS
// =========================================

function updateStatistics() {

    const totalStops =
        stops.length;


    const totalConnections =
        connections.length;


    const blockedRoads =
        connections.filter(
            function (connection) {

                return connection.blocked;

            }
        ).length;


    const components =
        calculateComponents();


    const mostConnected =
        findMostConnectedStop();


    const totalDistance =
        calculateNetworkDistance();


    // Update HTML

    document.getElementById(
        "mapTotalStops"
    ).textContent =
        totalStops;


    document.getElementById(
        "mapTotalConnections"
    ).textContent =
        totalConnections;


    document.getElementById(
        "mapComponents"
    ).textContent =
        components;


    document.getElementById(
        "mapBlockedRoads"
    ).textContent =
        blockedRoads;


    document.getElementById(
        "mostConnectedStop"
    ).textContent =
        mostConnected;


    document.getElementById(
        "networkDistance"
    ).textContent =
        totalDistance;


    // Active buses
    document.getElementById(
        "activeBuses"
    ).textContent =
        buses.length +
        " buses";

}


// =========================================
// 14. CONNECTIVITY CHECK
// =========================================

async function checkNetworkConnectivity() {

    try {

        /*
         * First check the actual graph
         * using the current active connections.
         */

        const components =
            calculateComponents();


        if (components === 1) {

            alert(

                "Network Status: CONNECTED\n\n" +

                "All active bus stops are reachable " +
                "through the current road network."

            );

        } else {

            alert(

                "Network Status: DISCONNECTED\n\n" +

                "The network currently contains " +

                components +

                " connected components."

            );

        }


        /*
         * Also demonstrate backend BFS.
         *
         * We use the first bus stop as the
         * BFS starting vertex.
         */

        if (stops.length > 0) {

            try {

                const bfsResult =
                    await checkConnectivity(
                        stops[0].id
                    );


                console.log(
                    "Backend BFS Result:",
                    bfsResult
                );

            } catch (bfsError) {

                console.warn(
                    "Backend BFS could not be completed:",
                    bfsError
                );

            }

        }


    } catch (error) {

        console.error(
            "Connectivity check error:",
            error
        );


        alert(
            "Unable to check network connectivity."
        );

    }

}


// =========================================
// 15. BUTTON EVENT
// =========================================

if (connectivityButton) {

    connectivityButton.addEventListener(
        "click",
        checkNetworkConnectivity
    );

}


// =========================================
// 16. NETWORK ERROR
// =========================================

function showNetworkError() {

    document.getElementById(
        "mapTotalStops"
    ).textContent = "—";


    document.getElementById(
        "mapTotalConnections"
    ).textContent = "—";


    document.getElementById(
        "mapComponents"
    ).textContent = "—";


    document.getElementById(
        "mapBlockedRoads"
    ).textContent = "—";


    document.getElementById(
        "mostConnectedStop"
    ).textContent =
        "Unable to load";


    document.getElementById(
        "networkDistance"
    ).textContent =
        "Unable to load";


    document.getElementById(
        "activeBuses"
    ).textContent =
        "Unable to load";


    if (graph) {

        graph.innerHTML += `

            <div class="text-center text-danger p-4">

                <strong>
                    Unable to load network data.
                </strong>

                <br>

                Please make sure the
                SmartTransit backend is running.

            </div>

        `;

    }

}


// =========================================
// 17. INITIALIZE
// =========================================

loadNetworkData();