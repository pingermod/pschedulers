package me.pinger.pschedulers.scheduler;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
@Builder
public class TaskConditions {
    private Integer minPlayers;
    private Integer maxPlayers;

    /**
     * Checks if all conditions are met for task execution
     * @return true if all conditions are met, false otherwise
     */
    public boolean areMet() {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        if (minPlayers != null && onlinePlayers < minPlayers) {
            return false;
        }

        if (maxPlayers != null && onlinePlayers > maxPlayers) {
            return false;
        }

        return true;
    }

    /**
     * Creates a default conditions object with no restrictions
     * @return default conditions
     */
    public static TaskConditions getDefault() {
        return TaskConditions.builder().build();
    }
} 