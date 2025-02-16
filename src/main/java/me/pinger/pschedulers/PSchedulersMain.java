package me.pinger.pschedulers;

import me.pinger.pschedulers.command.SchedulerCommand;
import me.pinger.pschedulers.config.TaskConfig;
import me.pinger.pschedulers.factory.TaskFactory;
import me.pinger.pschedulers.manager.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PSchedulersMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Initialize components
        TaskFactory.init(this);
        TaskConfig.init(this);

        // Register command
        SchedulerCommand command = new SchedulerCommand();
        getCommand("scheduler").setExecutor(command);
        getCommand("scheduler").setTabCompleter(command);

        getLogger().info("PSchedulers has been enabled!");
        getLogger().info("Running on " + (TaskFactory.isFolia() ? "Folia" : "Paper") + " server.");
    }

    @Override
    public void onDisable() {
        // Stop all tasks
        TaskManager.getInstance().stopAllTasks();
        
        // Save configuration
        TaskConfig.getInstance().saveConfig();

        getLogger().info("PSchedulers has been disabled!");
    }
}
