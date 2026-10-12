// ==========================================
// SMARTTRANSIT - ADMIN DASHBOARD
// ==========================================

// Make sure api.js is loaded before this file.
// admin-dashboard.html already loads admin.js.


// ==========================================
// GLOBAL DATA
// ==========================================

let stops = [];
let roads = [];
let buses = [];


// ==========================================
// DOM ELEMENTS
// ==========================================

const adminTotalStops = document.getElementById("adminTotalStops");
const adminTotalRoads = document.getElementById("adminTotalRoads");
const adminActiveBuses = document.getElementById("adminActiveBuses");
const adminBlockedRoads = document.getElementById("adminBlockedRoads");


// Bus Stop Management
const stopName = document.getElementById("stopName");
const stopLocation = document.getElementById("stopLocation");
const stopStatus = document.getElementById("stopStatus");
const addStopButton = document.getElementById("addStopButton");
const updateStopButton = document.getElementById("updateStopButton");


// Road Management
const roadFrom = document.getElementById("roadFrom");
const roadTo = document.getElementById("roadTo");
const roadDistance = document.getElementById("roadDistance");
const addRoadButton = document.getElementById("addRoadButton");
const deleteRoadButton = document.getElementById("deleteRoadButton");


// Emergency Road Control
const controlRoad = document.getElementById("controlRoad");
const blockRoadButton = document.getElementById("blockRoadButton");
const restoreRoadButton = document.getElementById("restoreRoadButton");


// Traffic
const trafficRoad = document.getElementById("trafficRoad");
const trafficLevel = document.getElementById("trafficLevel");
const updateTrafficButton =
    document.getElementById("updateTrafficButton");


// Bus Capacity
const capacityBus = document.getElementById("capacityBus");
const occupiedSeats = document.getElementById("occupiedSeats");
const updateCapacityButton =
    document.getElementById("updateCapacityButton");


// Simulation
const simulationType =
    document.getElementById("simulationType");

const simulationValue =
    document.getElementById("simulationValue");

const runSimulationButton =
    document.getElementById("runSimulationButton");

const simulationResult =
    document.getElementById("simulationResult");


// Algorithms
const bfsButton = document.getElementById("bfsButton");
const dfsButton = document.getElementById("dfsButton");
const mstButton = document.getElementById("mstButton");

const algorithmResult =
    document.getElementById("algorithmResult");


// ==========================================
// PAGE INITIALIZATION
// ==========================================

async function initializeDashboard() {

    try {

        await loadAllData();

        console.log("SmartTransit Admin Dashboard loaded.");

    } catch (error) {

        console.error(
            "Dashboard initialization failed:",
            error
        );

        showMessage(
            "Unable to load dashboard data. Make sure the Spring Boot backend is running.",
            "danger"
        );
    }
}


// ==========================================
// LOAD ALL BACKEND DATA
// ==========================================

async function loadAllData() {

    // Load bus stops
    stops = await getAdminStops();

    // Load roads
    roads = await getRoads();

    // Load buses
    buses = await getAdminBuses();

    // Update dashboard
    updateOverview();

    // Populate dropdowns
    populateStopDropdowns();
    populateRoadDropdowns();
    populateBusDropdown();

}


// ==========================================
// NETWORK OVERVIEW
// ==========================================

function updateOverview() {

    adminTotalStops.textContent = stops.length;

    adminTotalRoads.textContent = roads.length;

    adminBlockedRoads.textContent =
        roads.filter(
            road => road.blocked === true
        ).length;

    adminActiveBuses.textContent = buses.length;
}


// ==========================================
// POPULATE STOP DROPDOWNS
// ==========================================

function populateStopDropdowns() {

    // Keep first option
    roadFrom.innerHTML =
        '<option value="">Select source stop</option>';

    roadTo.innerHTML =
        '<option value="">Select destination stop</option>';


    stops.forEach(stop => {

        const option1 =
            document.createElement("option");

        option1.value = stop.id;
        option1.textContent = stop.name;

        roadFrom.appendChild(option1);


        const option2 =
            document.createElement("option");

        option2.value = stop.id;
        option2.textContent = stop.name;

        roadTo.appendChild(option2);

    });
}


// ==========================================
// POPULATE ROAD DROPDOWNS
// ==========================================

function populateRoadDropdowns() {

    controlRoad.innerHTML =
        '<option value="">Select road</option>';

    trafficRoad.innerHTML =
        '<option value="">Select road</option>';


    roads.forEach(road => {

        const fromName =
            getStopName(road.fromStop);

        const toName =
            getStopName(road.toStop);


        // Emergency control
        const controlOption =
            document.createElement("option");

        controlOption.value = road.id;

        controlOption.textContent =
            fromName + " → " + toName;

        controlRoad.appendChild(controlOption);


        // Traffic control
        const trafficOption =
            document.createElement("option");

        trafficOption.value = road.id;

        trafficOption.textContent =
            fromName + " → " + toName;

        trafficRoad.appendChild(trafficOption);

    });
}


// ==========================================
// POPULATE BUS DROPDOWN
// ==========================================

function populateBusDropdown() {

    capacityBus.innerHTML =
        '<option value="">Select bus</option>';


    buses.forEach(bus => {

        const option =
            document.createElement("option");

        option.value = bus.number;

        option.textContent =
            "Bus " + bus.number;

        capacityBus.appendChild(option);

    });
}


// ==========================================
// FIND STOP NAME
// ==========================================

function getStopName(stopId) {

    const stop =
        stops.find(
            item => item.id === stopId
        );

    if (stop) {
        return stop.name;
    }

    return stopId;
}


// ==========================================
// ADD BUS STOP
// ==========================================

addStopButton.addEventListener(
    "click",
    async function () {

        const name =
            stopName.value.trim();

        const location =
            stopLocation.value.trim();

        const status =
            stopStatus.value;


        if (!name || !location) {

            showMessage(
                "Please enter stop name and location.",
                "warning"
            );

            return;
        }


        try {

            const newStop = {

                name: name,

                location: location,

                status: status,

                connectedRoads: "",

                latitude: 0,

                longitude: 0

            };


            await addBusStop(newStop);


            showMessage(
                "Bus stop added successfully.",
                "success"
            );


            clearStopForm();

            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to add bus stop.",
                "danger"
            );
        }

    }
);


// ==========================================
// UPDATE BUS STOP
// ==========================================

updateStopButton.addEventListener(
    "click",
    async function () {

        const name =
            stopName.value.trim();

        const location =
            stopLocation.value.trim();

        const status =
            stopStatus.value;


        if (!name) {

            showMessage(
                "Enter the existing stop name to update it.",
                "warning"
            );

            return;
        }


        const existingStop =
            stops.find(
                stop =>
                    stop.name.toLowerCase() ===
                    name.toLowerCase()
            );


        if (!existingStop) {

            showMessage(
                "Stop not found. Enter an existing stop name.",
                "warning"
            );

            return;
        }


        try {

            const updatedStop = {

                name: existingStop.name,

                location:
                    location ||
                    existingStop.location,

                status: status,

                connectedRoads:
                    existingStop.connectedRoads,

                latitude:
                    existingStop.latitude,

                longitude:
                    existingStop.longitude

            };


            await updateBusStop(
                existingStop.id,
                updatedStop
            );


            showMessage(
                "Bus stop updated successfully.",
                "success"
            );


            clearStopForm();

            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to update bus stop.",
                "danger"
            );
        }

    }
);


// ==========================================
// CLEAR STOP FORM
// ==========================================

function clearStopForm() {

    stopName.value = "";
    stopLocation.value = "";
    stopStatus.value = "Active";

}


// ==========================================
// ADD ROAD
// ==========================================

addRoadButton.addEventListener(
    "click",
    async function () {

        const fromId =
            roadFrom.value;

        const toId =
            roadTo.value;

        const distance =
            parseFloat(roadDistance.value);


        if (!fromId || !toId || isNaN(distance)) {

            showMessage(
                "Please select both stops and enter distance.",
                "warning"
            );

            return;
        }


        if (fromId === toId) {

            showMessage(
                "Source and destination stops cannot be the same.",
                "warning"
            );

            return;
        }


        try {

            // Default values for a newly created road.
            // These can later be edited through backend/admin controls.

            const newRoad = {

                fromStop: fromId,

                toStop: toId,

                distance: distance,

                travelTime: distance * 3,

                fare: distance * 4,

                trafficLevel: "LOW",

                blocked: false

            };


            await addRoad(newRoad);


            showMessage(
                "Road added successfully.",
                "success"
            );


            roadFrom.value = "";
            roadTo.value = "";
            roadDistance.value = "";


            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to add road.",
                "danger"
            );
        }

    }
);


// ==========================================
// DELETE ROAD
// ==========================================

deleteRoadButton.addEventListener(
    "click",
    async function () {

        const roadId =
            controlRoad.value ||
            trafficRoad.value;


        if (!roadId) {

            showMessage(
                "Select a road before deleting.",
                "warning"
            );

            return;
        }


        try {

            await deleteRoad(roadId);


            showMessage(
                "Road deleted successfully.",
                "success"
            );


            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to delete road.",
                "danger"
            );
        }

    }
);


// ==========================================
// BLOCK ROAD
// ==========================================

blockRoadButton.addEventListener(
    "click",
    async function () {

        const roadId =
            controlRoad.value;


        if (!roadId) {

            showMessage(
                "Select a road first.",
                "warning"
            );

            return;
        }


        try {

            await blockRoad(roadId);


            showMessage(
                "Road blocked successfully.",
                "success"
            );


            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to block road.",
                "danger"
            );
        }

    }
);


// ==========================================
// RESTORE ROAD
// ==========================================

restoreRoadButton.addEventListener(
    "click",
    async function () {

        const roadId =
            controlRoad.value;


        if (!roadId) {

            showMessage(
                "Select a road first.",
                "warning"
            );

            return;
        }


        try {

            await restoreRoad(roadId);


            showMessage(
                "Road restored successfully.",
                "success"
            );


            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to restore road.",
                "danger"
            );
        }

    }
);


// ==========================================
// UPDATE TRAFFIC
// ==========================================

updateTrafficButton.addEventListener(
    "click",
    async function () {

        const roadId =
            trafficRoad.value;

        const level =
            trafficLevel.value.toUpperCase();


        if (!roadId) {

            showMessage(
                "Select a road first.",
                "warning"
            );

            return;
        }


        try {

            await updateTraffic(
                roadId,
                level
            );


            showMessage(
                "Traffic level updated successfully.",
                "success"
            );


            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to update traffic.",
                "danger"
            );
        }

    }
);


// ==========================================
// UPDATE BUS CAPACITY
// ==========================================

updateCapacityButton.addEventListener(
    "click",
    async function () {

        const busNumber =
            capacityBus.value;

        const occupied =
            parseInt(
                occupiedSeats.value
            );


        if (!busNumber || isNaN(occupied)) {

            showMessage(
                "Select a bus and enter occupied seats.",
                "warning"
            );

            return;
        }


        const selectedBus =
            buses.find(
                bus =>
                    String(bus.number) ===
                    String(busNumber)
            );


        if (
            selectedBus &&
            occupied > selectedBus.capacity
        ) {

            showMessage(
                "Occupied seats cannot exceed bus capacity.",
                "warning"
            );

            return;
        }


        if (occupied < 0) {

            showMessage(
                "Occupied seats cannot be negative.",
                "warning"
            );

            return;
        }


        try {

            await updateBusCapacity(
                busNumber,
                occupied
            );


            showMessage(
                "Bus capacity updated successfully.",
                "success"
            );


            occupiedSeats.value = "";

            await loadAllData();


        } catch (error) {

            console.error(error);

            showMessage(
                "Failed to update bus capacity.",
                "danger"
            );
        }

    }
);


// ==========================================
// WHAT-IF SIMULATION
// ==========================================

runSimulationButton.addEventListener(
    "click",
    async function () {

        const scenario =
            simulationType.value;

        const value =
            simulationValue.value.trim();


        if (!scenario || !value) {

            showSimulationMessage(
                "Please select a scenario and enter a value.",
                "warning"
            );

            return;
        }


        try {

            const result =
                await runWhatIfSimulation(
                    scenario,
                    value
                );


            showSimulationMessage(
                formatSimulationResult(result),
                "success"
            );


        } catch (error) {

            console.error(error);

            showSimulationMessage(
                "Simulation failed. Check the backend console.",
                "danger"
            );
        }

    }
);


// ==========================================
// FORMAT SIMULATION RESULT
// ==========================================

function formatSimulationResult(result) {

    if (!result) {
        return "Simulation completed.";
    }


    if (typeof result === "string") {
        return result;
    }


    let output =
        "<strong>Simulation Result</strong><br><br>";


    if (result.scenario !== undefined) {

        output +=
            "<strong>Scenario:</strong> " +
            result.scenario +
            "<br>";
    }


    if (result.value !== undefined) {

        output +=
            "<strong>Value:</strong> " +
            result.value +
            "<br>";
    }


    if (result.beforeResult !== undefined) {

        output +=
            "<strong>Before:</strong> " +
            result.beforeResult +
            "<br>";
    }


    if (result.afterResult !== undefined) {

        output +=
            "<strong>After:</strong> " +
            result.afterResult +
            "<br>";
    }


    if (result.message !== undefined) {

        output +=
            "<strong>Message:</strong> " +
            result.message;
    }


    return output;
}


// ==========================================
// BFS
// ==========================================

bfsButton.addEventListener(
    "click",
    async function () {

        if (stops.length === 0) {

            showAlgorithmMessage(
                "No bus stops available.",
                "warning"
            );

            return;
        }


        try {

            const startStop =
                stops[0].id;


            const result =
                await checkConnectivity(
                    startStop
                );


            showAlgorithmMessage(
                "<strong>BFS Result</strong><br>" +
                formatObject(result),
                "info"
            );


        } catch (error) {

            console.error(error);

            showAlgorithmMessage(
                "BFS execution failed.",
                "danger"
            );
        }

    }
);


// ==========================================
// DFS
// ==========================================

dfsButton.addEventListener(
    "click",
    function () {

        if (stops.length === 0) {

            showAlgorithmMessage(
                "No bus stops available.",
                "warning"
            );

            return;
        }


        const visited =
            new Set();


        const adjacency =
            {};


        stops.forEach(
            stop => {
                adjacency[stop.id] = [];
            }
        );


        roads.forEach(
            road => {

                if (road.blocked === true) {
                    return;
                }


                if (!adjacency[road.fromStop]) {
                    adjacency[road.fromStop] = [];
                }

                if (!adjacency[road.toStop]) {
                    adjacency[road.toStop] = [];
                }


                adjacency[road.fromStop].push(
                    road.toStop
                );

                adjacency[road.toStop].push(
                    road.fromStop
                );

            }
        );


        function dfs(node) {

            visited.add(node);


            adjacency[node].forEach(
                nextNode => {

                    if (!visited.has(nextNode)) {

                        dfs(nextNode);

                    }

                }
            );
        }


        dfs(stops[0].id);


        const visitedNames =
            Array.from(visited)
                .map(
                    id => getStopName(id)
                );


        showAlgorithmMessage(
            "<strong>DFS Result</strong><br>" +
            "Starting Stop: " +
            getStopName(stops[0].id) +
            "<br>" +
            "Visited Stops: " +
            visitedNames.join(" → "),
            "info"
        );

    }
);


// ==========================================
// MST
// ==========================================

mstButton.addEventListener(
    "click",
    async function () {

        try {

            const primResult =
                await getPrimMST();

            const kruskalResult =
                await getKruskalMST();


            let output =
                "<strong>MST Analysis</strong><br><br>";


            output +=
                "<strong>Prim's Algorithm:</strong><br>";

            output +=
                formatObject(primResult);


            output +=
                "<br><br><strong>Kruskal's Algorithm:</strong><br>";

            output +=
                formatObject(kruskalResult);


            showAlgorithmMessage(
                output,
                "success"
            );


        } catch (error) {

            console.error(error);

            showAlgorithmMessage(
                "MST generation failed.",
                "danger"
            );
        }

    }
);


// ==========================================
// SHOW ALGORITHM MESSAGE
// ==========================================

function showAlgorithmMessage(
    message,
    type
) {

    algorithmResult.className =
        "alert alert-" +
        type +
        " mt-4";

    algorithmResult.innerHTML =
        message;

}


// ==========================================
// SHOW SIMULATION MESSAGE
// ==========================================

function showSimulationMessage(
    message,
    type
) {

    simulationResult.className =
        "alert alert-" +
        type +
        " mt-3";

    simulationResult.innerHTML =
        message;

}


// ==========================================
// GENERAL MESSAGE
// ==========================================

function showMessage(
    message,
    type
) {

    let existing =
        document.getElementById(
            "adminMessage"
        );


    if (!existing) {

        existing =
            document.createElement("div");

        existing.id =
            "adminMessage";

        existing.style.position =
            "fixed";

        existing.style.top =
            "80px";

        existing.style.right =
            "20px";

        existing.style.zIndex =
            "9999";

        existing.style.maxWidth =
            "350px";

        document.body.appendChild(
            existing
        );
    }


    existing.className =
        "alert alert-" +
        type +
        " shadow";


    existing.textContent =
        message;


    setTimeout(
        function () {

            existing.remove();

        },
        3500
    );
}


// ==========================================
// FORMAT OBJECT FOR DISPLAY
// ==========================================

function formatObject(data) {

    if (data === null ||
        data === undefined) {

        return "No result returned.";
    }


    if (typeof data === "string") {

        return data;
    }


    return (
        "<pre style='white-space:pre-wrap;'>" +
        JSON.stringify(
            data,
            null,
            2
        ) +
        "</pre>"
    );
}


// ==========================================
// START DASHBOARD
// ==========================================

initializeDashboard();