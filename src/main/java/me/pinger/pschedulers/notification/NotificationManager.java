package me.pinger.pschedulers.notification;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class NotificationManager {
    @Getter
    private static NotificationManager instance;

    private final boolean discordEnabled;
    private final String webhookUrl;
    private final List<NotificationEvent> discordEvents;
    private final Level consoleLevel;
    private final String playerPermission;

    private final HttpClient httpClient;

    private NotificationManager(ConfigurationSection config) {
        ConfigurationSection notifications = config.getConfigurationSection("notifications");
        if (notifications == null) {
            this.discordEnabled = false;
            this.webhookUrl = "";
            this.discordEvents = new ArrayList<>();
            this.consoleLevel = Level.INFO;
            this.playerPermission = "scheduler.notify";
        } else {
            ConfigurationSection discord = notifications.getConfigurationSection("discord");
            this.discordEnabled = discord != null && discord.getBoolean("enabled", false);
            this.webhookUrl = discord != null ? discord.getString("webhook_url", "") : "";
            this.discordEvents = loadEvents(discord);

            ConfigurationSection console = notifications.getConfigurationSection("console");
            this.consoleLevel = console != null ? Level.parse(console.getString("level", "INFO")) : Level.INFO;

            ConfigurationSection players = notifications.getConfigurationSection("players");
            this.playerPermission = players != null ? players.getString("permission", "scheduler.notify") : "scheduler.notify";
        }

        this.httpClient = HttpClient.newHttpClient();
    }

    private List<NotificationEvent> loadEvents(ConfigurationSection discord) {
        List<NotificationEvent> events = new ArrayList<>();
        if (discord != null) {
            List<String> eventNames = discord.getStringList("events");
            for (String eventName : eventNames) {
                try {
                    events.add(NotificationEvent.valueOf(eventName));
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("Invalid notification event: " + eventName);
                }
            }
        }
        if (events.isEmpty()) {
            events.add(NotificationEvent.START);
            events.add(NotificationEvent.STOP);
            events.add(NotificationEvent.FAILURE);
        }
        return events;
    }

    public static void init(ConfigurationSection config) {
        instance = new NotificationManager(config);
    }

    public void sendDiscordNotification(String message, NotificationEvent event) {
        if (!discordEnabled || !discordEvents.contains(event) || webhookUrl.isEmpty()) {
            return;
        }

        String json = String.format("{\"content\": \"%s\"}", message.replace("\"", "\\\""));
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(webhookUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .exceptionally(e -> {
                Bukkit.getLogger().warning("Failed to send Discord notification: " + e.getMessage());
                return null;
            });
    }

    public void sendConsoleNotification(String message, Level level) {
        if (level.intValue() >= consoleLevel.intValue()) {
            Bukkit.getLogger().log(level, message);
        }
    }

    public void sendPlayerNotification(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(playerPermission)) {
                player.sendMessage(message);
            }
        }
    }

    public void broadcast(String message, NotificationEvent event) {
        sendConsoleNotification(message, Level.INFO);
        sendDiscordNotification(message, event);
        sendPlayerNotification(message);
    }
} 