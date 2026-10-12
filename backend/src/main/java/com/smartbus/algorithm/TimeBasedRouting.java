package com.smartbus.algorithm;

import com.smartbus.enums.TimePeriod;

public class TimeBasedRouting {

    public static double getTimeMultiplier(
            String trafficLevel,
            TimePeriod timePeriod) {

        double multiplier = 1.0;

        // Traffic effect
        if ("MEDIUM".equalsIgnoreCase(trafficLevel)) {
            multiplier = 1.5;
        } else if ("HIGH".equalsIgnoreCase(trafficLevel)) {
            multiplier = 2.0;
        }

        // Time-period effect
        if (timePeriod == TimePeriod.MORNING_PEAK) {
            multiplier = multiplier * 1.3;

        } else if (timePeriod == TimePeriod.EVENING_PEAK) {
            multiplier = multiplier * 1.4;

        } else if (timePeriod == TimePeriod.AFTERNOON) {
            multiplier = multiplier * 1.1;

        } else if (timePeriod == TimePeriod.NIGHT) {
            multiplier = multiplier * 0.9;
        }

        return multiplier;
    }

    public static double getEffectiveTravelTime(
            double baseTravelTime,
            String trafficLevel,
            TimePeriod timePeriod) {

        double multiplier =
                getTimeMultiplier(
                        trafficLevel,
                        timePeriod
                );

        return baseTravelTime * multiplier;
    }
}