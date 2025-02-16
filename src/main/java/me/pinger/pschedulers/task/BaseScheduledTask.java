package me.pinger.pschedulers.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.pinger.pschedulers.api.task.ScheduledTask;
import me.pinger.pschedulers.scheduler.ScheduleConfig;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@RequiredArgsConstructor
public abstract class BaseScheduledTask implements ScheduledTask {
    private final JavaPlugin plugin;
    private final String id;
    private final String command;
    private final ScheduleConfig scheduleConfig;
    private final Location location;
    private boolean running;

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        running = true;
        onStart();
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        running = false;
        onStop();
    }

    @Override
    public void executeNow() {
        onExecute();
    }

    /**
     * Called when the task is started.
     * Implementations should handle the actual scheduling here.
     */
    protected abstract void onStart();

    /**
     * Called when the task is stopped.
     * Implementations should handle cleanup here.
     */
    protected abstract void onStop();

    /**
     * Called when the task should be executed.
     * Implementations should handle the actual command execution here.
     */
    protected abstract void onExecute();

    /**
     * Gets the next time this task should be executed.
     *
     * @return The next execution time in milliseconds since epoch
     */
    protected long getNextExecutionTime() {
        return scheduleConfig.getNextExecutionTime();
    }

    /**
     * Checks if the task can be executed based on its conditions
     * @return true if the task can be executed, false otherwise
     */
    protected boolean canExecute() {
        return scheduleConfig.getConditions().areMet();
    }
} 