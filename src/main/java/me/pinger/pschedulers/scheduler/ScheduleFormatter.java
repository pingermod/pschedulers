package me.pinger.pschedulers.scheduler;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.stream.Collectors;

public class ScheduleFormatter {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static String getReadableSchedule(ScheduleConfig config) {
        if (config == null) {
            return "Invalid schedule";
        }

        switch (config.getType()) {
            case INTERVAL:
                long seconds = config.getIntervalTicks() / 20;
                if (seconds < 60) {
                    return String.format("Every %d seconds", seconds);
                } else if (seconds < 3600) {
                    return String.format("Every %d minutes", seconds / 60);
                } else {
                    return String.format("Every %d hours", seconds / 3600);
                }

            case HOURLY:
                return String.format("Every hour at minute %02d", config.getMinute());

            case DAILY:
                return String.format("Every day at %s", 
                    config.getTime().format(TIME_FORMAT));

            case WEEKLY:
                return String.format("Every %s at %s",
                    formatDays(config),
                    config.getWeeklyTime().format(TIME_FORMAT));

            default:
                return "Unknown schedule type: " + config.getType();
        }
    }

    public static String getNextExecutionText(ScheduleConfig config) {
        long nextTime = config.getNextExecutionTime();
        long now = System.currentTimeMillis();
        long diffSeconds = (nextTime - now) / 1000;

        if (diffSeconds < 0) {
            return "Immediately";
        } else if (diffSeconds < 60) {
            return String.format("In %d seconds", diffSeconds);
        } else if (diffSeconds < 3600) {
            return String.format("In %d minutes", diffSeconds / 60);
        } else if (diffSeconds < 86400) {
            return String.format("In %d hours", diffSeconds / 3600);
        } else {
            return String.format("In %d days", diffSeconds / 86400);
        }
    }

    private static String formatDays(ScheduleConfig config) {
        if (config.getDays() == null || config.getDays().isEmpty()) {
            return "no days";
        }

        return config.getDays().stream()
            .map(day -> day.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
            .sorted()
            .collect(Collectors.joining(", "));
    }
} 