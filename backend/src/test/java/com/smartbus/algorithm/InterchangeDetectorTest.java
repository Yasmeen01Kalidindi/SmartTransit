package com.smartbus.algorithm;

import java.util.*;

public class InterchangeDetectorTest {

    public static void main(String[] args) {

        // Define bus routes
        Map<String, List<String>> busRoutes =
                new LinkedHashMap<>();

        busRoutes.put(
                "101",
                Arrays.asList(
                        "SVECW",
                        "Railway Station",
                        "Town Hall",
                        "Market"
                )
        );

        busRoutes.put(
                "102",
                Arrays.asList(
                        "Railway Station",
                        "Town Hall",
                        "Market"
                )
        );

        busRoutes.put(
                "104",
                Arrays.asList(
                        "Market",
                        "Bus Stand"
                )
        );

        // Passenger's selected route
        List<String> passengerRoute =
                Arrays.asList(
                        "SVECW",
                        "Railway Station",
                        "Town Hall",
                        "Market",
                        "Bus Stand"
                );

        InterchangeDetector detector =
                new InterchangeDetector();

        List<String> interchanges =
                detector.findInterchanges(
                        passengerRoute,
                        busRoutes
                );

        System.out.println(
                "===== INTERCHANGE DETECTOR TEST ====="
        );

        System.out.println(
                "Passenger Route:"
        );

        System.out.println(
                passengerRoute
        );

        System.out.println();

        System.out.println(
                "Interchange Stops:"
        );

        for (String stop : interchanges) {

            int busCount =
                    detector.countBusesAtStop(
                            stop,
                            busRoutes
                    );

            System.out.println(
                    stop
                    + " -> "
                    + busCount
                    + " buses available"
            );
        }

        System.out.println();

        System.out.println(
                "Total Interchange Stops: "
                + interchanges.size()
        );

        System.out.println(
                "======================================"
        );
    }
}