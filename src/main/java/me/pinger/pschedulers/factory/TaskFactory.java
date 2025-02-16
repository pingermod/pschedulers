package me.pinger.pschedulers.factory;

import me.pinger.pschedulers.api.task.ScheduledTask;
import me.pinger.pschedulers.scheduler.ScheduleConfig;
import me.pinger.pschedulers.task.FoliaScheduledTask;
import me.pinger.pschedulers.task.PaperScheduledTask;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class TaskFactory {
    private static boolean isFolia;
    private static JavaPlugin plugin;

    public static void init(JavaPlugin plugin) {
        TaskFactory.plugin = plugin;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
    }

    public static ScheduledTask createTask(String id, String command, ScheduleConfig scheduleConfig) {
        return createTask(id, command, scheduleConfig, null);
    }

    public static ScheduledTask createTask(String id, String command, ScheduleConfig scheduleConfig, Location location) {
        if (plugin == null) {
            throw new IllegalStateException("TaskFactory not initialized! Call init() first.");
        }

        if (isFolia) {
            return new FoliaScheduledTask(plugin, id, command, scheduleConfig, location);
        } else {
            return new PaperScheduledTask(plugin, id, command, scheduleConfig, location);
        }
    }

    public static boolean isFolia() {
        return isFolia;
    }
} 