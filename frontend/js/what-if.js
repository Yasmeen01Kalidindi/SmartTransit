document.addEventListener("DOMContentLoaded", function () {

    const result =
        document.getElementById("simulationResult");

    /*
     * Displays the backend simulation response.
     */
    function showSimulationResult(simulation) {

        if (!simulation) {

            result.classList.remove("d-none");

            result.innerHTML =
                "<div class='alert alert-danger'>" +
                "No simulation response received." +
                "</div>";

            return;
        }

        const before =
            simulation.beforeResult || "No before result.";

        const after =
            simulation.afterResult || "No after result.";

        result.classList.remove("d-none");

        result.innerHTML = `
            <div class="card mt-4">
                <div class="card-body">

                    <h4 class="mb-3">
                        What-If Simulation Result
                    </h4>

                    <p>
                        <strong>Scenario:</strong>
                        ${simulation.scenario}
                    </p>

                    <p>
                        <strong>Value:</strong>
                        ${simulation.value}
                    </p>

                    <hr>

                    <h5>
                        Before Simulation
                    </h5>

                    <div class="alert alert-secondary">
                        <pre style="white-space: pre-wrap; margin-bottom: 0;">${escapeHtml(before)}</pre>
                    </div>

                    <h5>
                        After Simulation
                    </h5>

                    <div class="alert alert-info">
                        <pre style="white-space: pre-wrap; margin-bottom: 0;">${escapeHtml(after)}</pre>
                    </div>

                </div>
            </div>
        `;

        result.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });
    }

    /*
     * Prevents HTML injection when displaying
     * backend text.
     */
    function escapeHtml(text) {

        const div =
            document.createElement("div");

        div.textContent = text;

        return div.innerHTML;
    }

    /*
     * Sends the What-If request to Spring Boot.
     */
    async function runWhatIfSimulation(
        scenario,
        value) {

        try {

            if (!value) {

                alert(
                    "Please select a value."
                );

                return;
            }

            result.classList.remove("d-none");

            result.innerHTML =
                "<div class='alert alert-info'>" +
                "Running simulation..." +
                "</div>";

            const response =
                await fetch(
                    "http://localhost:8080/api/simulation",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            scenario: scenario,
                            value: value
                        })
                    }
                );

            if (!response.ok) {

                throw new Error(
                    "Simulation request failed. HTTP " +
                    response.status
                );
            }

            const simulation =
                await response.json();

            console.log(
                "Simulation response:",
                simulation
            );

            showSimulationResult(
                simulation
            );

        } catch (error) {

            console.error(
                "What-If simulation error:",
                error
            );

            result.classList.remove("d-none");

            result.innerHTML =
                "<div class='alert alert-danger'>" +
                "<strong>Simulation failed.</strong><br>" +
                error.message +
                "</div>";
        }
    }

    /*
     * ROAD BLOCK
     */
    window.simulateRoadBlocked =
        function () {

            const road =
                document.getElementById(
                    "roadSelect"
                ).value;

            if (road === "") {

                alert(
                    "Please select a road."
                );

                return;
            }

            runWhatIfSimulation(
                "roadBlocked",
                road
            );
        };

    /*
     * STOP CLOSURE
     */
    window.simulateStopClosed =
        function () {

            const stop =
                document.getElementById(
                    "stopSelect"
                ).value;

            if (stop === "") {

                alert(
                    "Please select a bus stop."
                );

                return;
            }

            runWhatIfSimulation(
                "stopClosed",
                stop
            );
        };

    /*
     * TRAFFIC INCREASE
     */
    window.simulateTraffic =
        function () {

            const traffic =
                document.getElementById(
                    "trafficSelect"
                ).value;

            if (traffic === "") {

                alert(
                    "Please select a traffic level."
                );

                return;
            }

            runWhatIfSimulation(
                "trafficIncreased",
                traffic
            );
        };

    /*
     * BUS UNAVAILABLE
     */
    window.simulateBusUnavailable =
        function () {

            const bus =
                document.getElementById(
                    "busSelect"
                ).value;

            if (bus === "") {

                alert(
                    "Please select a bus."
                );

                return;
            }

            runWhatIfSimulation(
                "busUnavailable",
                bus
            );
        };

    /*
     * NEW ROAD
     */
    window.simulateNewRoad =
        function () {

            const from =
                document.getElementById(
                    "newRoadFrom"
                ).value;

            const to =
                document.getElementById(
                    "newRoadTo"
                ).value;

            if (from === ""
                    || to === "") {

                alert(
                    "Please select both From and To stops."
                );

                return;
            }

            if (from === to) {

                alert(
                    "From and To stops cannot be the same."
                );

                return;
            }

            const value =
                from + " -> " + to;

            runWhatIfSimulation(
                "newRoad",
                value
            );
        };

});