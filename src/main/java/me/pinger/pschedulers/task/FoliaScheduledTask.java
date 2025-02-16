package me.pinger.pschedulers.task;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.pinger.pschedulers.scheduler.ScheduleConfig;
import me.pinger.pschedulers.scheduler.ScheduleType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class FoliaScheduledTask extends BaseScheduledTask {
    private ScheduledTask task;

    public FoliaScheduledTask(JavaPlugin plugin, String id, String command, ScheduleConfig scheduleConfig, Location location) {
        super(plugin, id, command, scheduleConfig, location);
    }

    @Override
    protected void onStart() {
        if (getScheduleConfig().getType() == ScheduleType.INTERVAL) {
            // Pour le type INTERVAL, on utilise le comportement existant
            if (getLocation() != null) {
                task = Bukkit.getServer().getRegionScheduler()
                    .runAtFixedRate(getPlugin(), getLocation(),
                        (scheduledTask) -> onExecute(),
                        0L, getScheduleConfig().getIntervalTicks());
            } else {
                task = Bukkit.getServer().getGlobalRegionScheduler()
                    .runAtFixedRate(getPlugin(),
                        (scheduledTask) -> onExecute(),
                        0L, getScheduleConfig().getIntervalTicks());
            }
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

        if (getLocation() != null) {
            task = Bukkit.getServer().getRegionScheduler()
                .runDelayed(getPlugin(), getLocation(), (scheduledTask) -> {
                    // Exécuter la tâche
                    onExecute();
                    
                    // Si la tâche est toujours active, planifier la prochaine exécution
                    if (isRunning()) {
                        scheduleNextExecution();
                    }
                }, delayTicks);
        } else {
            task = Bukkit.getServer().getGlobalRegionScheduler()
                .runDelayed(getPlugin(), (scheduledTask) -> {
                    // Exécuter la tâche
                    onExecute();
                    
                    // Si la tâche est toujours active, planifier la prochaine exécution
                    if (isRunning()) {
                        scheduleNextExecution();
                    }
                }, delayTicks);
        }
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
        // Pour Folia, nous sommes déjà dans le bon contexte de thread
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getCommand());
    }
} 