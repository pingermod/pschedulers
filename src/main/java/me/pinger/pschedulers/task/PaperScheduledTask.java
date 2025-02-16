package me.pinger.pschedulers.task;

import me.pinger.pschedulers.scheduler.ScheduleConfig;
import me.pinger.pschedulers.scheduler.ScheduleType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class PaperScheduledTask extends BaseScheduledTask {
    private BukkitTask task;

    public PaperScheduledTask(JavaPlugin plugin, String id, String command, ScheduleConfig scheduleConfig, Location location) {
        super(plugin, id, command, scheduleConfig, location);
    }

    @Override
    protected void onStart() {
        if (getScheduleConfig().getType() == ScheduleType.INTERVAL) {
            // Pour le type INTERVAL, on utilise le comportement existant
            task = Bukkit.getScheduler().runTaskTimer(
                getPlugin(), 
                this::onExecute, 
                0L, 
                getScheduleConfig().getIntervalTicks()
            );
        } else {
            // Pour les autres types, on calcule le délai jusqu'à la prochaine exécution
            scheduleNextExecution();
        }
    }

    private void scheduleNextExecution() {
        long nextTime = getNextExecutionTime();
        long currentTime = System.currentTimeMillis();
        long delayTicks = (nextTime - currentTime) / 50; // Convertir ms en ticks

        if (delayTicks < 0) {
            delayTicks = 0;
        }

        task = Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            // Exécuter la tâche
            onExecute();
            
            // Si la tâche est toujours active, planifier la prochaine exécution
            if (isRunning()) {
                scheduleNextExecution();
            }
        }, delayTicks);
    }

    @Override
    protected void onStop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    protected void onExecute() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> 
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getCommand())
        );
    }
} 