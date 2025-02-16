package me.pinger.pschedulers.config;

import lombok.Getter;
import me.pinger.pschedulers.api.task.ScheduledTask;
import me.pinger.pschedulers.manager.TaskManager;
import me.pinger.pschedulers.scheduler.ScheduleConfig;
import me.pinger.pschedulers.scheduler.ScheduleType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskConfig {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    
    @Getter
    private static TaskConfig instance;
    private final JavaPlugin plugin;
    private FileConfiguration config;

    private TaskConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public static void init(JavaPlugin plugin) {
        instance = new TaskConfig(plugin);
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        loadTasks();
    }

    public void saveConfig() {
        ConfigurationSection tasksSection = config.createSection("tasks");
        for (ScheduledTask task : TaskManager.getInstance().getAllTasks()) {
            ConfigurationSection taskSection = tasksSection.createSection(task.getId());
            taskSection.set("command", task.getCommand());
            
            ScheduleConfig scheduleConfig = task.getScheduleConfig();
            taskSection.set("type", scheduleConfig.getType().name());
            
            switch (scheduleConfig.getType()) {
                case INTERVAL:
                    taskSection.set("interval", scheduleConfig.getIntervalTicks());
                    break;
                case HOURLY:
                    taskSection.set("minute", scheduleConfig.getMinute());
                    break;
                case DAILY:
                    taskSection.set("time", scheduleConfig.getTime().format(TIME_FORMAT));
                    break;
                case WEEKLY:
                    taskSection.set("time", scheduleConfig.getWeeklyTime().format(TIME_FORMAT));
                    taskSection.set("days", scheduleConfig.getDays().stream()
                        .map(DayOfWeek::name)
                        .collect(Collectors.toList()));
                    break;
            }
            
            if (task.getLocation() != null) {
                Location loc = task.getLocation();
                taskSection.set("world", loc.getWorld().getName());
                taskSection.set("x", loc.getX());
                taskSection.set("y", loc.getY());
                taskSection.set("z", loc.getZ());
            }
            taskSection.set("enabled", task.isRunning());
        }
        plugin.saveConfig();
    }

    private void loadTasks() {
        ConfigurationSection tasksSection = config.getConfigurationSection("tasks");
        if (tasksSection == null) return;

        for (String id : tasksSection.getKeys(false)) {
            ConfigurationSection taskSection = tasksSection.getConfigurationSection(id);
            if (taskSection == null) continue;

            String command = taskSection.getString("command");
            ScheduleType type = ScheduleType.valueOf(taskSection.getString("type", "INTERVAL"));
            Location location = null;

            if (taskSection.contains("world")) {
                World world = Bukkit.getWorld(Objects.requireNonNull(taskSection.getString("world")));
                if (world != null) {
                    location = new Location(
                        world,
                        taskSection.getDouble("x"),
                        taskSection.getDouble("y"),
                        taskSection.getDouble("z")
                    );
                }
            }

            ScheduleConfig scheduleConfig;
            switch (type) {
                case INTERVAL:
                    scheduleConfig = ScheduleConfig.createInterval(taskSection.getLong("interval", 20L));
                    break;
                    
                case HOURLY:
                    scheduleConfig = ScheduleConfig.createHourly(taskSection.getInt("minute", 0));
                    break;
                    
                case DAILY:
                    LocalTime dailyTime = LocalTime.parse(
                        taskSection.getString("time", "00:00"),
                        TIME_FORMAT
                    );
                    scheduleConfig = ScheduleConfig.createDaily(dailyTime);
                    break;
                    
                case WEEKLY:
                    LocalTime weeklyTime = LocalTime.parse(
                        taskSection.getString("time", "00:00"),
                        TIME_FORMAT
                    );
                    List<String> dayStrings = taskSection.getStringList("days");
                    Set<DayOfWeek> days = dayStrings.stream()
                        .map(DayOfWeek::valueOf)
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(DayOfWeek.class)));
                    if (days.isEmpty()) {
                        days.add(DayOfWeek.MONDAY);
                    }
                    scheduleConfig = ScheduleConfig.createWeekly(days, weeklyTime);
                    break;
                    
                default:
                    plugin.getLogger().warning("Unknown schedule type: " + type + " for task: " + id);
                    continue;
            }

            ScheduledTask task = TaskManager.getInstance().createTask(id, command, scheduleConfig, location);
            if (taskSection.getBoolean("enabled", false)) {
                task.start();
            }
        }
    }
} 