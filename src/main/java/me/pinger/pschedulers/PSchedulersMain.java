package me.pinger.pschedulers;

import me.pinger.pschedulers.command.SchedulerCommand;
import me.pinger.pschedulers.config.PluginSettings;
import me.pinger.pschedulers.config.TaskConfig;
import me.pinger.pschedulers.factory.TaskFactory;
import me.pinger.pschedulers.group.TaskGroup;
import me.pinger.pschedulers.manager.TaskManager;
import me.pinger.pschedulers.notification.NotificationEvent;
import me.pinger.pschedulers.notification.NotificationManager;
import me.pinger.pschedulers.template.TaskTemplate;
import me.pinger.pschedulers.variables.VariableManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PSchedulersMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Sauvegarder la configuration par défaut
        saveDefaultConfig();

        // Initialiser les composants
        PluginSettings.init(getConfig());
        NotificationManager.init(getConfig());
        VariableManager.init(getConfig());
        TaskTemplate.loadTemplates(getConfig());
        TaskGroup.loadGroups(getConfig());
        
        TaskFactory.init(this);
        TaskConfig.init(this);

        // Enregistrer la commande
        SchedulerCommand command = new SchedulerCommand();
        getCommand("scheduler").setExecutor(command);
        getCommand("scheduler").setTabCompleter(command);

        // Message de démarrage
        NotificationManager.getInstance().broadcast(
            "PSchedulers has been enabled! Running on " + 
            (TaskFactory.isFolia() ? "Folia" : "Paper") + " server.",
            NotificationEvent.START
        );
    }

    @Override
    public void onDisable() {
        // Arrêter toutes les tâches
        TaskManager.getInstance().stopAllTasks();
        
        // Sauvegarder la configuration
        TaskConfig.getInstance().saveConfig();

        // Message d'arrêt
        NotificationManager.getInstance().broadcast(
            "PSchedulers has been disabled!",
            NotificationEvent.STOP
        );
    }
}
