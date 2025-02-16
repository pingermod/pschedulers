package me.pinger.pschedulers.group;

import lombok.Getter;
import me.pinger.pschedulers.api.task.ScheduledTask;
import me.pinger.pschedulers.manager.TaskManager;
import me.pinger.pschedulers.scheduler.TaskConditions;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public class TaskGroup {
    private static final Map<String, TaskGroup> groups = new HashMap<>();

    private final String id;
    private final List<String> taskIds;
    private final boolean sequential;
    private final boolean parallel;
    private final TaskConditions conditions;

    private TaskGroup(String id, ConfigurationSection config) {
        this.id = id;
        this.taskIds = config.getStringList("tasks");
        this.sequential = config.getBoolean("sequential", false);
        this.parallel = config.getBoolean("parallel", false);

        ConfigurationSection conditionsSection = config.getConfigurationSection("conditions");
        this.conditions = conditionsSection != null ? 
            TaskConditions.fromConfig(conditionsSection) : 
            TaskConditions.getDefault();
    }

    public static void loadGroups(ConfigurationSection config) {
        groups.clear();
        ConfigurationSection groupsSection = config.getConfigurationSection("groups");
        if (groupsSection != null) {
            for (String id : groupsSection.getKeys(false)) {
                ConfigurationSection groupConfig = groupsSection.getConfigurationSection(id);
                if (groupConfig != null) {
                    groups.put(id, new TaskGroup(id, groupConfig));
                }
            }
        }
    }

    public static TaskGroup getGroup(String id) {
        return groups.get(id);
    }

    public List<ScheduledTask> getTasks() {
        List<ScheduledTask> tasks = new ArrayList<>();
        for (String taskId : taskIds) {
            ScheduledTask task = TaskManager.getInstance().getTask(taskId);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void executeGroup() {
        if (!conditions.areMet()) {
            return;
        }

        List<ScheduledTask> tasks = getTasks();
        if (tasks.isEmpty()) {
            return;
        }

        if (sequential) {
            executeSequentially(tasks);
        } else if (parallel) {
            executeInParallel(tasks);
        } else {
            // Par défaut, exécution séquentielle
            executeSequentially(tasks);
        }
    }

    private void executeSequentially(List<ScheduledTask> tasks) {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        for (ScheduledTask task : tasks) {
            future = future.thenRunAsync(() -> {
                if (task.getScheduleConfig().getConditions().areMet()) {
                    task.executeNow();
                }
            });
        }
    }

    private void executeInParallel(List<ScheduledTask> tasks) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (ScheduledTask task : tasks) {
            if (task.getScheduleConfig().getConditions().areMet()) {
                futures.add(CompletableFuture.runAsync(task::executeNow));
            }
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
} 