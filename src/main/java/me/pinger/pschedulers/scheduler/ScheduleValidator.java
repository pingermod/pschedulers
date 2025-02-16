package me.pinger.pschedulers.scheduler;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public class ScheduleValidator {
    public static void validate(ScheduleConfig config) throws IllegalArgumentException {
        if (config == null) {
            throw new IllegalArgumentException("Schedule configuration cannot be null");
        }
        
        if (config.getType() == null) {
            throw new IllegalArgumentException("Schedule type cannot be null");
        }

        switch (config.getType()) {
            case INTERVAL:
                validateInterval(config);
                break;
            case HOURLY:
                validateHourly(config);
                break;
            case DAILY:
                validateDaily(config);
                break;
            case WEEKLY:
                validateWeekly(config);
                break;
            default:
                throw new IllegalArgumentException("Unknown schedule type: " + config.getType());
        }
    }

    private static void validateInterval(ScheduleConfig config) {
        if (config.getIntervalTicks() <= 0) {
            throw new IllegalArgumentException(
                "Interval must be positive (got: " + config.getIntervalTicks() + ")"
            );
        }
        if (config.getIntervalTicks() < 20) {
            throw new IllegalArgumentException(
                "Interval cannot be less than 1 second (20 ticks)"
            );
        }
    }

    private static void validateHourly(ScheduleConfig config) {
        if (config.getMinute() < 0 || config.getMinute() > 59) {
            throw new IllegalArgumentException(
                "Minute must be between 0 and 59 (got: " + config.getMinute() + ")"
            );
        }
    }

    private static void validateDaily(ScheduleConfig config) {
        LocalTime time = config.getTime();
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null for DAILY tasks");
        }
    }

    private static void validateWeekly(ScheduleConfig config) {
        LocalTime time = config.getWeeklyTime();
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null for WEEKLY tasks");
        }

        Set<DayOfWeek> days = config.getDays();
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("At least one day must be specified for WEEKLY tasks");
        }
    }
} 