package me.pinger.pschedulers.manager;

import lombok.Getter;
import me.pinger.pschedulers.api.task.ScheduledTask;
import me.pinger.pschedulers.factory.TaskFactory;
import me.pinger.pschedulers.scheduler.ScheduleConfig;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManager {
    @Getter
    private static final TaskManager instance = new TaskManager();
    private final Map<String, ScheduledTask> tasks = new ConcurrentHashMap<>();

    private TaskManager() {}

    public ScheduledTask createTask(String id, String command, ScheduleConfig scheduleConfig) {
        return createTask(id, command, scheduleConfig, null);
    }

    public ScheduledTask createTask(String id, String command, ScheduleConfig scheduleConfig, Location location) {
        if (tasks.containsKey(id)) {
            throw new IllegalArgumentException("Task with ID " + id + " already exists!");
        }

        ScheduledTask task = TaskFactory.createTask(id, command, scheduleConfig, location);
        tasks.put(id, task);
        return task;
    }

    public void removeTask(String id) {
        ScheduledTask task = tasks.remove(id);
        if (task != null && task.isRunning()) {
            task.stop();
        }
    }

    public ScheduledTask getTask(String id) {
        return tasks.get(id);
    }

    public Collection<ScheduledTask> getAllTasks() {
        return Collections.unmodifiableCollection(tasks.values());
    }

    public void startTask(String id) {
        ScheduledTask task = getTask(id);
        if (task != null) {
            task.start();
        }
    }

    public void stopTask(String id) {
        ScheduledTask task = getTask(id);
        if (task != null) {
            task.stop();
        }
    }

    public void stopAllTasks() {
        tasks.values().forEach(ScheduledTask::stop);
    }
} 