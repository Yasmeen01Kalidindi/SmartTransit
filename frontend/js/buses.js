// ================= BUSES =================


// ================= GET HTML ELEMENTS =================

const busTableBody =
    document.getElementById("busTableBody");

const totalBuses =
    document.getElementById("totalBuses");

const availableBuses =
    document.getElementById("availableBuses");

const totalCapacity =
    document.getElementById("totalCapacity");

const availableSeats =
    document.getElementById("availableSeats");


// Store buses received from backend
let buses = [];


// ================= GET BUS STATUS =================

function getBusStatus(occupancy) {

    if (occupancy >= 100) {

        return "Full";

    }

    if (occupancy >= 75) {

        return "Crowded";

    }

    if (occupancy >= 50) {

        return "Filling";

    }

    return "Available";

}


// ================= LOAD BUSES =================

async function loadBuses() {

    try {

        console.log("Loading buses from backend...");


        // Get buses from Spring Boot backend
        buses = await getBuses();


        console.log("Buses received:", buses);


        // Display buses
        displayBuses();


        // Update summary cards
        updateSummary();


    } catch (error) {

        console.error(
            "Error loading buses:",
            error
        );


        busTableBody.innerHTML = `

            <tr>

                <td colspan="8"
                    class="text-center text-danger">

                    Unable to load bus information.

                    <br>

                    Please make sure the
                    SmartTransit backend is running.

                </td>

            </tr>

        `;


        // Reset summary
        totalBuses.textContent = "0";

        availableBuses.textContent = "0";

        totalCapacity.textContent = "0";

        availableSeats.textContent = "0";

    }

}


// ================= DISPLAY BUSES =================

function displayBuses() {

    busTableBody.innerHTML = "";


    if (!buses || buses.length === 0) {

        busTableBody.innerHTML = `

            <tr>

                <td colspan="8"
                    class="text-center">

                    No buses found.

                </td>

            </tr>

        `;

        return;

    }


    buses.forEach(function(bus) {


        // Calculate available seats
        const available =
            bus.available !== undefined
                ? bus.available
                : bus.capacity - bus.occupied;


        // Calculate occupancy percentage
        const occupancy =
            bus.capacity > 0
                ? (bus.occupied / bus.capacity) * 100
                : 0;


        // Use backend status if available
        const status =
            bus.status
                ? formatStatus(bus.status)
                : getBusStatus(occupancy);


        // Create table row
        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>

                <strong>
                    Bus ${bus.number}
                </strong>

            </td>


            <td>
                ${formatRoute(bus.route)}
            </td>


            <td>
                ${bus.capacity}
            </td>


            <td>
                ${bus.occupied}
            </td>


            <td>
                ${available}
            </td>


            <td>

                <div class="occupancy-container">

                    <div class="occupancy-bar">

                        <div
                            class="occupancy-fill"
                            style="width: ${Math.min(occupancy, 100)}%">
                        </div>

                    </div>

                    <span class="occupancy-text">

                        ${occupancy.toFixed(0)}%

                    </span>

                </div>

            </td>


            <td>

                <span class="bus-status">

                    ${status}

                </span>

            </td>


            <td>

                <button
                    class="btn btn-outline-primary btn-sm"
                    onclick="viewBus('${bus.number}')">

                    View

                </button>

            </td>

        `;


        busTableBody.appendChild(row);

    });

}


// ================= FORMAT STATUS =================

function formatStatus(status) {

    if (!status) {

        return "Unknown";

    }


    switch (status.toUpperCase()) {

        case "AVAILABLE":
            return "Available";

        case "FILLING":
            return "Filling";

        case "CROWDED":
            return "Crowded";

        case "FULL":
            return "Full";

        default:
            return status;

    }

}


// ================= FORMAT ROUTE =================

function formatRoute(route) {

    if (!route) {

        return "Route not available";

    }


    // Fix encoding issue if arrow appears incorrectly
    return route
        .replace(/â†’/g, "→")
        .replace(/->/g, "→");

}


// ================= UPDATE SUMMARY =================

function updateSummary() {

    let capacity = 0;

    let seats = 0;

    let availableCount = 0;


    buses.forEach(function(bus) {


        const available =
            bus.available !== undefined
                ? bus.available
                : bus.capacity - bus.occupied;


        capacity += Number(bus.capacity);

        seats += Number(available);


        if (available > 0) {

            availableCount++;

        }

    });


    totalBuses.textContent =
        buses.length;


    availableBuses.textContent =
        availableCount;


    totalCapacity.textContent =
        capacity;


    availableSeats.textContent =
        seats;

}


// ================= VIEW BUS =================

function viewBus(busNumber) {


    const bus =
        buses.find(function(item) {

            return String(item.number) ===
                   String(busNumber);

        });


    if (!bus) {

        alert("Bus information not found.");

        return;

    }


    const available =
        bus.available !== undefined
            ? bus.available
            : bus.capacity - bus.occupied;


    const occupancy =
        bus.capacity > 0
            ? (bus.occupied / bus.capacity) * 100
            : 0;


    const status =
        bus.status
            ? formatStatus(bus.status)
            : getBusStatus(occupancy);


    alert(

        "Bus Details\n\n" +

        "Bus Number: " +
        bus.number +

        "\nRoute: " +
        formatRoute(bus.route) +

        "\nCapacity: " +
        bus.capacity +

        "\nOccupied: " +
        bus.occupied +

        "\nAvailable Seats: " +
        available +

        "\nOccupancy: " +
        occupancy.toFixed(0) +
        "%" +

        "\nStatus: " +
        status

    );

}


// ================= INITIAL LOAD =================

loadBuses();