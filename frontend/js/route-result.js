// ================= ROUTE RESULT =================


// Get saved route request from planner page
const routeRequest = JSON.parse(
    sessionStorage.getItem("routeRequest")
);


// Check whether route request exists
if (!routeRequest) {

    console.log("No route request found.");

    displayError();

} else {

    console.log(
        "Route Request:",
        routeRequest
    );


    // =========================================
    // DISPLAY SOURCE AND DESTINATION
    // =========================================

    document.getElementById(
        "resultSource"
    ).textContent =
        routeRequest.source;


    document.getElementById(
        "resultDestination"
    ).textContent =
        routeRequest.destination;


    // =========================================
    // LOAD ACTUAL ROUTE
    // =========================================

    loadRoute();

}


// ================= LOAD ROUTE =================

async function loadRoute() {

    try {

        // =========================================
        // CHECK FOR SELECTED ALTERNATIVE
        // =========================================

        if (
            routeRequest.preference ===
            "alternative" &&
            routeRequest.selectedAlternative
        ) {

            console.log(
                "Using selected alternative route:",
                routeRequest.selectedAlternative
            );


            displayRouteResult(
                routeRequest.selectedAlternative
            );


            return;
        }


        // =========================================
        // GET ALL BUS STOPS FROM BACKEND
        // =========================================

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

            console.error(
                "Source stop not found:",
                routeRequest.source
            );

            displayError();

            return;
        }


        // =========================================
        // CHECK DESTINATION
        // =========================================

        if (!destinationStop) {

            console.error(
                "Destination stop not found:",
                routeRequest.destination
            );

            displayError();

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
        // FIND ROUTE USING BACKEND
        // =========================================

        const routeResponse =
            await findRouteFromBackend(
                sourceStop.id,
                destinationStop.id
            );


        console.log(
            "Backend Route Response:",
            routeResponse
        );


        // =========================================
        // DISPLAY ROUTE RESULT
        // =========================================

        displayRouteResult(
            routeResponse
        );


    } catch (error) {

        console.error(
            "Error loading route:",
            error
        );


        displayError();

    }

}


// ================= FIND ROUTE =================

async function findRouteFromBackend(
    sourceId,
    destinationId
) {

    const preference =
        routeRequest.preference;


    const timePeriod =
        routeRequest.timePeriod;


    // =========================================
    // TRAFFIC-AWARE ROUTE
    // =========================================

    if (
        preference === "traffic" ||
        routeRequest.trafficMode === true
    ) {

        return await findTrafficAwareRoute(
            sourceId,
            destinationId,
            "MEDIUM",
            timePeriod
        );

    }


    // =========================================
    // SHORTEST ROUTE
    // =========================================

    if (
        preference === "shortest"
    ) {

        return await findShortestRoute(
            sourceId,
            destinationId,
            timePeriod
        );

    }


    // =========================================
    // FASTEST ROUTE
    // =========================================

    if (
        preference === "fastest"
    ) {

        return await findFastestRoute(
            sourceId,
            destinationId,
            timePeriod
        );

    }


    // =========================================
    // CHEAPEST ROUTE
    // =========================================

    if (
        preference === "cheapest"
    ) {

        return await findCheapestRoute(
            sourceId,
            destinationId,
            timePeriod
        );

    }


    // =========================================
    // DEFAULT
    // =========================================

    return await findShortestRoute(
        sourceId,
        destinationId,
        timePeriod
    );

}


// ================= DISPLAY RESULT =================

function displayRouteResult(
    routeResponse
) {

    if (!routeResponse) {

        displayError();

        return;
    }


    // =========================================
    // DISPLAY DISTANCE
    // =========================================

    document.getElementById(
        "resultDistance"
    ).textContent =
        formatNumber(
            routeResponse.distance
        ) + " km";


    // =========================================
    // DISPLAY TRAVEL TIME
    // =========================================

    document.getElementById(
        "resultTime"
    ).textContent =
        formatNumber(
            routeResponse.travelTime
        ) + " min";


    // =========================================
    // DISPLAY FARE
    // =========================================

    document.getElementById(
        "resultFare"
    ).textContent =
        "₹" +
        formatNumber(
            routeResponse.fare
        );


    // =========================================
    // DISPLAY STOPS
    // =========================================

    document.getElementById(
        "resultStops"
    ).textContent =
        routeResponse.stops;


    // =========================================
    // DISPLAY ROUTE PATH
    // =========================================

    displayRoutePath(
        routeResponse.route
    );

}


// ================= FORMAT NUMBER =================

function formatNumber(
    value
) {

    const number =
        Number(value);


    if (
        isNaN(number)
    ) {

        return value;

    }


    return number
        .toFixed(2)
        .replace(/\.00$/, "");

}


// ================= DISPLAY ROUTE PATH =================

function displayRoutePath(
    route
) {

    const routePath =
        document.getElementById(
            "routePath"
        );


    // =========================================
    // CHECK ROUTE
    // =========================================

    if (
        !route ||
        route.length === 0
    ) {

        routePath.innerHTML = `

            <div class="alert alert-warning">

                No route found between
                the selected stops.

            </div>

        `;

        return;

    }


    let html = "";


    // =========================================
    // CREATE ROUTE STOPS
    // =========================================

    route.forEach(
        function(stop, index) {

            let markerClass = "";


            // =================================
            // FIRST STOP
            // =================================

            if (
                index === 0
            ) {

                markerClass =
                    "start";

            }


            // =================================
            // LAST STOP
            // =================================

            else if (
                index ===
                route.length - 1
            ) {

                markerClass =
                    "end";

            }


            // =================================
            // CREATE STOP
            // =================================

            html += `

                <div class="route-stop">

                    <div class="stop-marker ${markerClass}">

                        ${String.fromCharCode(
                            65 + index
                        )}

                    </div>


                    <div class="stop-info">

                        <h4>
                            ${stop}
                        </h4>


                        <span>

                            ${
                                index === 0
                                    ? "Starting Point"
                                    : index ===
                                      route.length - 1
                                        ? "Destination"
                                        : "Bus Stop"
                            }

                        </span>

                    </div>

                </div>

            `;


            // =================================
            // ADD LINE BETWEEN STOPS
            // =================================

            if (
                index <
                route.length - 1
            ) {

                html += `

                    <div class="route-line"></div>

                `;

            }

        }
    );


    routePath.innerHTML =
        html;

}


// ================= ERROR =================

function displayError() {

    const routePath =
        document.getElementById(
            "routePath"
        );


    if (!routePath) {

        return;

    }


    routePath.innerHTML = `

        <div class="alert alert-danger">

            Unable to find route.

            <br>

            Please check the selected
            source and destination.

        </div>

    `;

}