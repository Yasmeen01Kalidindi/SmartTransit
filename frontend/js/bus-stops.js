// ================= BUS STOPS =================

// Bus stops will now come from the Spring Boot backend
let busStops = [];


// ================= GET HTML ELEMENTS =================

const tableBody =
    document.getElementById("busStopTableBody");

const totalStops =
    document.getElementById("totalStops");

const connectedStops =
    document.getElementById("connectedStops");

const searchInput =
    document.getElementById("stopSearch");

const nearestButton =
    document.getElementById("nearestStopButton");


// ================= LOAD BUS STOPS =================

async function loadBusStops() {

    try {

        // Get actual bus stops from backend
        const response = await getBusStops();

        console.log(
            "Bus Stops from Backend:",
            response
        );


        // Store backend data
        busStops = response;


        // Display stops
        displayBusStops(busStops);


        // Update summary
        updateSummary();


    } catch (error) {

        console.error(
            "Error loading bus stops:",
            error
        );


        tableBody.innerHTML = `

            <tr>

                <td
                    colspan="6"
                    class="text-center text-danger">

                    Unable to load bus stops
                    from backend.

                </td>

            </tr>

        `;

    }

}


// ================= DISPLAY BUS STOPS =================

function displayBusStops(stops) {

    tableBody.innerHTML = "";


    if (
        !stops ||
        stops.length === 0
    ) {

        tableBody.innerHTML = `

            <tr>

                <td
                    colspan="6"
                    class="text-center">

                    No bus stops found.

                </td>

            </tr>

        `;

        return;

    }


    stops.forEach(function(stop) {

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                ${stop.id}
            </td>


            <td>

                <strong>
                    ${stop.name}
                </strong>

            </td>


            <td>
                ${stop.location || "Bhimavaram"}
            </td>


            <td>
                ${getConnectedRoadCount(stop)}
            </td>


            <td>

                <span class="stop-status">

                    ${stop.status || "Active"}

                </span>

            </td>


            <td>

                <button
                    class="btn btn-outline-primary btn-sm"
                    onclick="viewStop('${stop.id}')">

                    View

                </button>

            </td>

        `;


        tableBody.appendChild(row);

    });

}


// ================= CONNECTED ROAD COUNT =================

function getConnectedRoadCount(stop) {

    // Backend may return connectedRoads
    // as a number or as a collection.

    if (
        Array.isArray(stop.connectedRoads)
    ) {

        return stop.connectedRoads.length;

    }


    if (
        typeof stop.connectedRoads === "number"
    ) {

        return stop.connectedRoads;

    }


    if (
        typeof stop.connectedRoads === "string" &&
        stop.connectedRoads.trim() !== ""
    ) {

        return stop.connectedRoads;

    }


    return 0;

}


// ================= UPDATE SUMMARY =================

function updateSummary() {

    totalStops.textContent =
        busStops.length;


    let connectedCount = 0;


    busStops.forEach(function(stop) {

        if (
            getConnectedRoadCount(stop) > 0
        ) {

            connectedCount++;

        }

    });


    connectedStops.textContent =
        connectedCount;

}


// ================= SEARCH BUS STOPS =================

searchInput.addEventListener(
    "input",
    function() {

        const searchText =
            searchInput.value
                .toLowerCase()
                .trim();


        const filteredStops =
            busStops.filter(function(stop) {

                const name =
                    (stop.name || "")
                        .toLowerCase();


                const location =
                    (stop.location || "")
                        .toLowerCase();


                return (
                    name.includes(searchText) ||
                    location.includes(searchText)
                );

            });


        displayBusStops(
            filteredStops
        );

    }
);


// ================= VIEW STOP =================

function viewStop(stopId) {

    const stop =
        busStops.find(function(item) {

            return item.id === stopId;

        });


    if (!stop) {

        return;

    }


    alert(

        "Bus Stop Details\n\n" +

        "Stop ID: " +
        stop.id +

        "\nName: " +
        stop.name +

        "\nLocation: " +
        (stop.location || "Bhimavaram") +

        "\nConnected Roads: " +
        getConnectedRoadCount(stop) +

        "\nStatus: " +
        (stop.status || "Active") +

        "\nLatitude: " +
        (stop.latitude ?? "N/A") +

        "\nLongitude: " +
        (stop.longitude ?? "N/A")

    );

}


// ================= FIND NEAREST STOP =================

nearestButton.addEventListener(
    "click",
    function() {

        /*
         * Demo user coordinates.
         *
         * These are intentionally kept as
         * predefined coordinates for the
         * college project.
         *
         * They can later be replaced by
         * browser GPS if required.
         */

        const userLatitude = 16.541;

        const userLongitude = 81.524;


        let nearestStop = null;

        let minimumDistance =
            Infinity;


        busStops.forEach(function(stop) {

            // Ignore stops without coordinates
            if (
                stop.latitude === null ||
                stop.latitude === undefined ||
                stop.longitude === null ||
                stop.longitude === undefined
            ) {

                return;

            }


            const distance =
                calculateDistance(
                    userLatitude,
                    userLongitude,
                    Number(stop.latitude),
                    Number(stop.longitude)
                );


            if (
                distance < minimumDistance
            ) {

                minimumDistance =
                    distance;

                nearestStop =
                    stop;

            }

        });


        if (nearestStop) {

            alert(

                "Nearest Bus Stop\n\n" +

                nearestStop.name +

                "\n\nLocation: " +
                (nearestStop.location || "Bhimavaram") +

                "\nDistance: " +
                minimumDistance.toFixed(2) +
                " km"

            );

        } else {

            alert(
                "Bus-stop location coordinates are not available."
            );

        }

    }
);


// ================= DISTANCE CALCULATION =================

function calculateDistance(
    lat1,
    lon1,
    lat2,
    lon2
) {

    const earthRadius = 6371;


    const latitudeDifference =
        toRadians(lat2 - lat1);

    const longitudeDifference =
        toRadians(lon2 - lon1);


    const a =
        Math.sin(latitudeDifference / 2) *
        Math.sin(latitudeDifference / 2)

        +

        Math.cos(toRadians(lat1)) *
        Math.cos(toRadians(lat2)) *
        Math.sin(longitudeDifference / 2) *
        Math.sin(longitudeDifference / 2);


    const c =
        2 *
        Math.atan2(
            Math.sqrt(a),
            Math.sqrt(1 - a)
        );


    return earthRadius * c;

}


// ================= CONVERT TO RADIANS =================

function toRadians(degrees) {

    return degrees *
        Math.PI /
        180;

}


// ================= INITIAL LOAD =================

loadBusStops();