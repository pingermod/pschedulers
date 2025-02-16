package me.pinger.pschedulers.scheduler;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

@Getter
@Builder
public class TaskConditions {
    private Integer minPlayers;
    private Integer maxPlayers;
    private List<String> weatherTypes;
    private Integer minTime;
    private Integer maxTime;
    private String permission;
    private String worldName;
    private Integer worldMinPlayers;
    private Double minTps;
    private Integer maxMemoryUsage;

    /**
     * Checks if all conditions are met for task execution
     * @return true if all conditions are met, false otherwise
     */
    public boolean areMet() {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        // Vérification du nombre de joueurs global
        if (minPlayers != null && onlinePlayers < minPlayers) {
            return false;
        }
        if (maxPlayers != null && onlinePlayers > maxPlayers) {
            return false;
        }

        // Vérification de la météo
        if (weatherTypes != null && !weatherTypes.isEmpty()) {
            boolean validWeather = false;
            String currentWeather = getCurrentWeather();
            for (String weather : weatherTypes) {
                if (weather.equalsIgnoreCase(currentWeather)) {
                    validWeather = true;
                    break;
                }
            }
            if (!validWeather) {
                return false;
            }
        }

        // Vérification du temps dans le monde
        World world = Bukkit.getWorlds().get(0);
        long time = world.getTime();
        if (minTime != null && time < minTime) {
            return false;
        }
        if (maxTime != null && time > maxTime) {
            return false;
        }

        // Vérification de la permission
        if (permission != null) {
            boolean hasPermission = false;
            for (var player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(permission)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                return false;
            }
        }

        // Vérification des joueurs dans un monde spécifique
        if (worldName != null && worldMinPlayers != null) {
            World specificWorld = Bukkit.getWorld(worldName);
            if (specificWorld != null) {
                int worldPlayers = specificWorld.getPlayers().size();
                if (worldPlayers < worldMinPlayers) {
                    return false;
                }
            }
        }

        // Vérification des TPS
        if (minTps != null) {
            double currentTps = Bukkit.getTPS()[0];
            if (currentTps < minTps) {
                return false;
            }
        }

        // Vérification de l'utilisation de la mémoire
        if (maxMemoryUsage != null) {
            long maxMemory = Runtime.getRuntime().maxMemory();
            long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            double memoryUsage = (usedMemory * 100.0) / maxMemory;
            if (memoryUsage > maxMemoryUsage) {
                return false;
            }
        }

        return true;
    }

    private String getCurrentWeather() {
        World world = Bukkit.getWorlds().get(0);
        if (world.isThundering()) {
            return "THUNDER";
        } else if (world.hasStorm()) {
            return "RAIN";
        } else {
            return "CLEAR";
        }
    }

    /**
     * Creates a default conditions object with no restrictions
     * @return default conditions
     */
    public static TaskConditions getDefault() {
        return TaskConditions.builder().build();
    }

    /**
     * Creates a conditions object from a configuration section
     * @param config The configuration section containing the conditions
     * @return The conditions object
     */
    public static TaskConditions fromConfig(ConfigurationSection config) {
        TaskConditions.TaskConditionsBuilder builder = TaskConditions.builder();

        if (config.contains("min_players")) {
            builder.minPlayers(config.getInt("min_players"));
        }
        if (config.contains("max_players")) {
            builder.maxPlayers(config.getInt("max_players"));
        }

        ConfigurationSection weather = config.getConfigurationSection("weather");
        if (weather != null) {
            builder.weatherTypes(weather.getStringList("type"));
        }

        ConfigurationSection time = config.getConfigurationSection("time");
        if (time != null) {
            if (time.contains("min")) {
                builder.minTime(time.getInt("min"));
            }
            if (time.contains("max")) {
                builder.maxTime(time.getInt("max"));
            }
        }

        if (config.contains("permission")) {
            builder.permission(config.getString("permission"));
        }

        ConfigurationSection worldPlayers = config.getConfigurationSection("world_players");
        if (worldPlayers != null) {
            builder.worldName(worldPlayers.getString("world"));
            builder.worldMinPlayers(worldPlayers.getInt("min"));
        }

        ConfigurationSection tps = config.getConfigurationSection("tps");
        if (tps != null && tps.contains("min")) {
            builder.minTps(tps.getDouble("min"));
        }

        ConfigurationSection memory = config.getConfigurationSection("memory");
        if (memory != null && memory.contains("max_usage")) {
            builder.maxMemoryUsage(memory.getInt("max_usage"));
        }

        return builder.build();
    }
} 