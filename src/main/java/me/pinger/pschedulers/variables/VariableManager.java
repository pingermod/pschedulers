package me.pinger.pschedulers.variables;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableManager {
    @Getter
    private static VariableManager instance;

    private final Map<String, String> customMessages;
    private final Map<String, String> rewards;
    private final Map<String, List<String>> commands;

    private VariableManager(ConfigurationSection config) {
        this.customMessages = new HashMap<>();
        this.rewards = new HashMap<>();
        this.commands = new HashMap<>();

        ConfigurationSection variables = config.getConfigurationSection("variables");
        if (variables != null) {
            loadCustomMessages(variables.getConfigurationSection("custom_messages"));
            loadRewards(variables.getConfigurationSection("rewards"));
            loadCommands(variables.getConfigurationSection("commands"));
        }
    }

    private void loadCustomMessages(ConfigurationSection section) {
        if (section != null) {
            for (String key : section.getKeys(false)) {
                customMessages.put(key, section.getString(key));
            }
        }
    }

    private void loadRewards(ConfigurationSection section) {
        if (section != null) {
            for (String key : section.getKeys(false)) {
                rewards.put(key, section.getString(key));
            }
        }
    }

    private void loadCommands(ConfigurationSection section) {
        if (section != null) {
            for (String key : section.getKeys(false)) {
                commands.put(key, section.getStringList(key));
            }
        }
    }

    public static void init(ConfigurationSection config) {
        instance = new VariableManager(config);
    }

    public String getMessage(String key) {
        return customMessages.getOrDefault(key, key);
    }

    public String getReward(String key) {
        return rewards.getOrDefault(key, key);
    }

    public List<String> getCommands(String key) {
        return commands.getOrDefault(key, List.of());
    }

    public String replaceVariables(String text) {
        // Remplacer les variables personnalisées
        for (Map.Entry<String, String> entry : customMessages.entrySet()) {
            text = text.replace("{message." + entry.getKey() + "}", entry.getValue());
        }
        for (Map.Entry<String, String> entry : rewards.entrySet()) {
            text = text.replace("{reward." + entry.getKey() + "}", entry.getValue());
        }
        return text;
    }
} 