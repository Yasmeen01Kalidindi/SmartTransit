// ================= ROUTE COMPARISON =================


// Get the route request saved from Route Planner
const routeRequest = JSON.parse(
    sessionStorage.getItem("routeRequest")
);


// Get HTML elements
const tableBody =
    document.getElementById("comparisonTableBody");

const routeCards =
    document.getElementById("routeCards");


// ================= UPDATE FROM / TO =================

// Update the journey summary with the
// actual source and destination selected
// in Route Planner.

const comparisonSource =
    document.getElementById("comparisonSource");

const comparisonDestination =
    document.getElementById("comparisonDestination");


if (routeRequest) {

    if (comparisonSource) {

        comparisonSource.textContent =
            routeRequest.source;

    }


    if (comparisonDestination) {

        comparisonDestination.textContent =
            routeRequest.destination;

    }

}


// Store real routes returned by backend
let routes = [];


// ================= START =================

if (!routeRequest) {

    console.log("No route request found.");

    showError(
        "No route request found. Please go back to Route Planner."
    );

} else {

    console.log(
        "Route Request:",
        routeRequest
    );

    loadRouteComparison();

}


// ================= LOAD ROUTE COMPARISON =================

async function loadRouteComparison() {

    try {

        // Get all bus stops from backend
        const stops =
            await getBusStops();

        console.log(
            "Bus Stops:",
            stops
        );


        // =========================================
        // FIND SOURCE STOP
        // =========================================

        const sourceStop =
            stops.find(function(stop) {

                return stop.name ===
                    routeRequest.source;

            });


        // =========================================
        // FIND DESTINATION STOP
        // =========================================

        const destinationStop =
            stops.find(function(stop) {

                return stop.name ===
                    routeRequest.destination;

            });


        // =========================================
        // CHECK SOURCE
        // =========================================

        if (!sourceStop) {

            showError(
                "Source stop was not found in the backend."
            );

            return;

        }


        // =========================================
        // CHECK DESTINATION
        // =========================================

        if (!destinationStop) {

            showError(
                "Destination stop was not found in the backend."
            );

            return;

        }


        console.log(
            "Source ID:",
            sourceStop.id
        );


        console.log(
            "Destination ID:",
            destinationStop.id
        );


        // =========================================
        // GET ROUTES FROM BACKEND
        // =========================================

        const results =
            await Promise.all([

                // 1. Shortest
                findShortestRoute(
                    sourceStop.id,
                    destinationStop.id,
                    routeRequest.timePeriod
                ),


                // 2. Fastest
                findFastestRoute(
                    sourceStop.id,
                    destinationStop.id,
                    routeRequest.timePeriod
                ),


                // 3. Cheapest
                findCheapestRoute(
                    sourceStop.id,
                    destinationStop.id,
                    routeRequest.timePeriod
                ),


                // 4. Traffic-Aware
                findTrafficAwareRoute(
                    sourceStop.id,
                    destinationStop.id,
                    "MEDIUM",
                    routeRequest.timePeriod
                ),


                // 5. Alternative Routes
                findAlternativeRoutes(
                    sourceStop.id,
                    destinationStop.id,
                    routeRequest.timePeriod
                )

            ]);


        console.log(
            "Backend Route Results:",
            results
        );


        // =========================================
        // CREATE MAIN ROUTE OBJECTS
        // =========================================

        routes = [

            createRouteObject(
                "Shortest Route",
                "🛣️",
                results[0],
                "shortest"
            ),


            createRouteObject(
                "Fastest Route",
                "⚡",
                results[1],
                "fastest"
            ),


            createRouteObject(
                "Cheapest Route",
                "💰",
                results[2],
                "cheapest"
            ),


            createRouteObject(
                "Traffic-Aware Route",
                "🚦",
                results[3],
                "traffic"
            )

        ];


        // =========================================
        // ADD ACTUAL ALTERNATIVE ROUTES
        // =========================================

        const alternativeRoutes =
            results[4];


        if (
            Array.isArray(alternativeRoutes) &&
            alternativeRoutes.length > 1
        ) {

            // The backend returns:
            //
            // index 0 = shortest route
            // index 1+ = actual alternatives
            //
            // Therefore we skip index 0
            // to avoid showing duplicate
            // "Backend Shortest Route".

            alternativeRoutes
                .slice(1)
                .forEach(function(
                    alternative,
                    index
                ) {

                    routes.push({

                        type:
                            "Alternative Route " +
                            (index + 1),


                        icon:
                            "🔀",


                        // Store as alternative
                        preference:
                            "alternative",


                        distance:
                            formatNumber(
                                alternative.distance
                            ) + " km",


                        time:
                            formatNumber(
                                alternative.travelTime
                            ) + " min",


                        fare:
                            "₹" +
                            formatNumber(
                                alternative.fare
                            ),


                        stops:
                            alternative.stops,


                        transfers:
                            alternative.transfers !== undefined
                                ? alternative.transfers
                                : "—",


                        traffic:
                            alternative.trafficLevel ||
                            "UNKNOWN",


                        backendResponse:
                            alternative

                    });

                });

        }


        // =========================================
        // DISPLAY REAL BACKEND DATA
        // =========================================

        displayComparison();


    } catch (error) {

        console.error(
            "Error loading route comparison:",
            error
        );


        showError(
            "Unable to load route comparison from backend."
        );

    }

}


// ================= CREATE ROUTE OBJECT =================

function createRouteObject(
    type,
    icon,
    response,
    preference
) {

    return {

        type:
            type,


        icon:
            icon,


        preference:
            preference,


        distance:
            formatNumber(
                response.distance
            ) + " km",


        time:
            formatNumber(
                response.travelTime
            ) + " min",


        fare:
            "₹" +
            formatNumber(
                response.fare
            ),


        stops:
            response.stops,


        // Backend currently does not return
        // transfer count for these routes
        transfers:
            response.transfers !== undefined
                ? response.transfers
                : "—",


        traffic:
            response.trafficLevel ||
            "UNKNOWN",


        backendResponse:
            response

    };

}


// ================= FORMAT NUMBER =================

function formatNumber(value) {

    const number =
        Number(value);


    if (isNaN(number)) {

        return value;

    }


    return number
        .toFixed(2)
        .replace(/\.00$/, "");

}


// ================= DISPLAY COMPARISON =================

function displayComparison() {

    // Clear old content
    tableBody.innerHTML = "";

    routeCards.innerHTML = "";


    // =========================================
    // CREATE TABLE ROWS
    // =========================================

    routes.forEach(function(
        route,
        index
    ) {

        createTableRow(
            route,
            index
        );

    });


    // =========================================
    // CREATE ROUTE CARDS
    // =========================================

    routes.forEach(function(
        route,
        index
    ) {

        createRouteCard(
            route,
            index
        );

    });

}


// ================= CREATE TABLE ROW =================

function createTableRow(
    route,
    index
) {

    const row =
        document.createElement("tr");


    row.innerHTML = `

        <td class="route-type">

            ${route.icon}
            ${route.type}

        </td>


        <td>
            ${route.distance}
        </td>


        <td>
            ${route.time}
        </td>


        <td>
            ${route.fare}
        </td>


        <td>
            ${route.stops}
        </td>


        <td>
            ${route.transfers}
        </td>


        <td>

            <span class="traffic-badge">

                ${route.traffic}

            </span>

        </td>


        <td>

            <button
                class="btn btn-primary btn-sm"
                onclick="selectRoute(${index})">

                Select

            </button>

        </td>

    `;


    tableBody.appendChild(row);

}


// ================= CREATE ROUTE CARD =================

function createRouteCard(
    route,
    index
) {

    const column =
        document.createElement("div");


    column.className =
        "col-lg-6";


    column.innerHTML = `

        <div class="route-card">

            <div class="route-card-header">

                <h3>
                    ${route.type}
                </h3>


                <span class="route-icon">

                    ${route.icon}

                </span>

            </div>


            <div class="route-details">


                <div class="route-detail">

                    <small>
                        Distance
                    </small>

                    <strong>
                        ${route.distance}
                    </strong>

                </div>


                <div class="route-detail">

                    <small>
                        Travel Time
                    </small>

                    <strong>
                        ${route.time}
                    </strong>

                </div>


                <div class="route-detail">

                    <small>
                        Fare
                    </small>

                    <strong>
                        ${route.fare}
                    </strong>

                </div>


                <div class="route-detail">

                    <small>
                        Stops
                    </small>

                    <strong>
                        ${route.stops}
                    </strong>

                </div>


                <div class="route-detail">

                    <small>
                        Transfers
                    </small>

                    <strong>
                        ${route.transfers}
                    </strong>

                </div>


                <div class="route-detail">

                    <small>
                        Traffic
                    </small>

                    <strong>
                        ${route.traffic}
                    </strong>

                </div>


            </div>


            <button
                class="btn btn-primary select-route-btn"
                onclick="selectRoute(${index})">

                Select This Route

            </button>

        </div>

    `;


    routeCards.appendChild(column);

}


// ================= SELECT ROUTE =================

function selectRoute(index) {

    const selectedRoute =
        routes[index];


    if (!selectedRoute) {

        return;

    }


    console.log(
        "Selected Route:",
        selectedRoute
    );


    // =========================================
    // UPDATE ROUTE REQUEST
    // =========================================

    routeRequest.preference =
        selectedRoute.preference;


    // =========================================
    // TRAFFIC-AWARE ROUTE
    // =========================================

    if (
        selectedRoute.preference ===
        "traffic"
    ) {

        routeRequest.trafficMode =
            true;

    } else {

        routeRequest.trafficMode =
            false;

    }


    // =========================================
    // FOR ALTERNATIVE ROUTES
    // =========================================

    if (
        selectedRoute.preference ===
        "alternative"
    ) {

        // Store the actual backend route
        // so route-result.js can use it
        // if needed.

        routeRequest.selectedAlternative =
            selectedRoute.backendResponse;

    } else {

        // Remove previous alternative
        // selection.

        delete routeRequest.selectedAlternative;

    }


    // =========================================
    // SAVE UPDATED REQUEST
    // =========================================

    sessionStorage.setItem(
        "routeRequest",
        JSON.stringify(routeRequest)
    );


    // =========================================
    // GO TO ROUTE RESULT
    // =========================================

    window.location.href =
        "route-result.html";

}


// ================= ERROR MESSAGE =================

function showError(message) {

    if (!tableBody || !routeCards) {

        console.error(message);

        return;

    }


    tableBody.innerHTML = `

        <tr>

            <td
                colspan="8"
                class="text-center text-danger">

                ${message}

            </td>

        </tr>

    `;


    routeCards.innerHTML = `

        <div class="col-12">

            <div class="alert alert-danger">

                ${message}

            </div>

        </div>

    `;

}