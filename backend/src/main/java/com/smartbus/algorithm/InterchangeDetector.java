package com.smartbus.algorithm;

import java.util.*;

public class InterchangeDetector {

    /*
     * Finds the bus stops where a passenger
     * may need to change buses.
     *
     * A stop is considered an interchange point
     * when more than one bus route serves that stop.
     */

    public List<String> findInterchanges(
            List<String> routeStops,
            Map<String, List<String>> busRoutes) {

        List<String> interchanges =
                new ArrayList<>();

        for (String stop : routeStops) {

            int busCount = 0;

            for (List<String> busStops : busRoutes.values()) {

                if (busStops.contains(stop)) {
                    busCount++;
                }
            }

            // More than one bus serves this stop
            if (busCount > 1) {
                interchanges.add(stop);
            }
        }

        return interchanges;
    }

    /*
     * Returns the number of buses available
     * at a particular stop.
     */
    public int countBusesAtStop(
            String stop,
            Map<String, List<String>> busRoutes) {

        int count = 0;

        for (List<String> busStops : busRoutes.values()) {

            if (busStops.contains(stop)) {
                count++;
            }
        }

        return count;
    }
}