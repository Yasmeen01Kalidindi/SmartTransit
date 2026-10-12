// ================= ROUTE PLANNER =================


// Get the button
const findRouteButton =
    document.getElementById("findRouteButton");


// Get the error message
const errorMessage =
    document.getElementById("errorMessage");


// When user clicks Find Route
findRouteButton.addEventListener("click", function () {

    // Get selected source
    const source =
        document.getElementById("source").value;


    // Get selected destination
    const destination =
        document.getElementById("destination").value;


    // Get travel date
    const travelDate =
        document.getElementById("travelDate").value;


    // Get travel time
    const travelTime =
        document.getElementById("travelTime").value;


    // Check whether source or destination is empty
    if (source === "" || destination === "") {

        errorMessage.textContent =
            "Please select both starting point and destination.";

        errorMessage.classList.remove("d-none");

        return;
    }


    // Check whether source and destination are same
    if (source === destination) {

        errorMessage.textContent =
            "Starting point and destination cannot be the same.";

        errorMessage.classList.remove("d-none");

        return;
    }


    // Check whether travel date is selected
    if (travelDate === "") {

        errorMessage.textContent =
            "Please select a travel date.";

        errorMessage.classList.remove("d-none");

        return;
    }


    // Check whether travel time is selected
    if (travelTime === "") {

        errorMessage.textContent =
            "Please select a travel time.";

        errorMessage.classList.remove("d-none");

        return;
    }


    // Hide error
    errorMessage.classList.add("d-none");


    // Get selected route preference
    const preference =
        document.querySelector(
            'input[name="routePreference"]:checked'
        ).value;


    // Get traffic mode
    const trafficMode =
        document.getElementById("trafficMode").checked;


    // =========================================
    // DETERMINE TIME PERIOD
    // =========================================

    const timeParts =
        travelTime.split(":");

    const hour =
        parseInt(timeParts[0]);

    const minute =
        parseInt(timeParts[1]);


    const totalMinutes =
        (hour * 60) + minute;


    let timePeriod;


    // Morning Peak: 8:00 AM - 10:00 AM
    if (
        totalMinutes >= 480 &&
        totalMinutes <= 600
    ) {

        timePeriod = "MORNING_PEAK";

    }


    // Afternoon: 12:00 PM - 3:00 PM
    else if (
        totalMinutes >= 720 &&
        totalMinutes <= 900
    ) {

        timePeriod = "AFTERNOON";

    }


    // Evening Peak: 4:30 PM - 7:30 PM
    else if (
        totalMinutes >= 990 &&
        totalMinutes <= 1170
    ) {

        timePeriod = "EVENING_PEAK";

    }


    // Night: after 7:30 PM
    else if (totalMinutes > 1170) {

        timePeriod = "NIGHT";

    }


    // Other times
    else {

        timePeriod = "AFTERNOON";

    }


    console.log("Source:", source);
    console.log("Destination:", destination);
    console.log("Travel Date:", travelDate);
    console.log("Travel Time:", travelTime);
    console.log("Preference:", preference);
    console.log("Traffic Mode:", trafficMode);
    console.log("Time Period:", timePeriod);


    // =========================================
    // SAVE ROUTE REQUEST
    // =========================================

    const routeRequest = {

        source: source,

        destination: destination,

        travelDate: travelDate,

        travelTime: travelTime,

        preference: preference,

        trafficMode: trafficMode,

        timePeriod: timePeriod
    };


    sessionStorage.setItem(
        "routeRequest",
        JSON.stringify(routeRequest)
    );


    // Go to route result page
    window.location.href =
        "route-result.html";

});