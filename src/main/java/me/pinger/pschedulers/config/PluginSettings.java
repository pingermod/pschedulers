package me.pinger.pschedulers.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.time.ZoneId;

@Getter
public class PluginSettings {
    private static PluginSettings instance;
    
    private final boolean debug;
    private final ZoneId timezone;
    private final String language;
    private final int maxConcurrentTasks;

    private PluginSettings(ConfigurationSection config) {
        ConfigurationSection settings = config.getConfigurationSection("settings");
        if (settings == null) {
            this.debug = false;
            this.timezone = ZoneId.systemDefault();
            this.language = "en";
            this.maxConcurrentTasks = 10;
            return;
        }

        this.debug = settings.getBoolean("debug", false);
        this.timezone = ZoneId.of(settings.getString("timezone", ZoneId.systemDefault().getId()));
        this.language = settings.getString("language", "en");
        this.maxConcurrentTasks = settings.getInt("max_concurrent_tasks", 10);
    }

    public static void init(ConfigurationSection config) {
        instance = new PluginSettings(config);
    }

    public static PluginSettings getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PluginSettings not initialized!");
        }
        return instance;
    }
} 