package me.pinger.pschedulers.api.task;

import me.pinger.pschedulers.scheduler.ScheduleConfig;
import org.bukkit.Location;

/**
 * Represents a scheduled task that can be executed at regular intervals.
 */
public interface ScheduledTask {
    /**
     * Gets the unique identifier of this task.
     *
     * @return The task's identifier
     */
    String getId();

    /**
     * Gets the command to be executed.
     *
     * @return The command string
     */
    String getCommand();

    /**
     * Gets the schedule configuration for this task.
     *
     * @return The schedule configuration
     */
    ScheduleConfig getScheduleConfig();

    /**
     * Gets the location where this task should be executed, if region-specific.
     *
     * @return The location, or null if global
     */
    Location getLocation();

    /**
     * Starts the task execution.
     */
    void start();

    /**
     * Stops the task execution.
     */
    void stop();

    /**
     * Executes the task once immediately.
     */
    void executeNow();

    /**
     * Checks if the task is currently running.
     *
     * @return true if running, false otherwise
     */
    boolean isRunning();
} 