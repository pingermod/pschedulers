# API PSchedulers

L'API PSchedulers permet aux développeurs d'intégrer et d'étendre les fonctionnalités de planification dans leurs propres plugins.

## Installation

### Maven
```xml
<repositories>
    <repository>
        <id>pingermod-repo</id>
        <url>https://github.com/pingermod/pschedulers/raw/repository/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>me.pinger</groupId>
        <artifactId>pschedulers</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle
```groovy
repositories {
    maven {
        url 'https://github.com/pingermod/pschedulers/raw/repository/'
    }
}

dependencies {
    compileOnly 'me.pinger:pschedulers:1.0.0'
}
```

## Utilisation de base

### Obtenir l'instance
```java
import me.pinger.pschedulers.PSchedulers;

public class MonPlugin extends JavaPlugin {
    private PSchedulers scheduler;

    @Override
    public void onEnable() {
        scheduler = PSchedulers.getInstance();
    }
}
```

### Créer une tâche simple
```java
// Tâche à intervalle
ScheduleConfig config = ScheduleConfig.interval(6000); // 5 minutes
scheduler.createTask("backup", "backup world", config);

// Tâche horaire
ScheduleConfig config = ScheduleConfig.hourly(30); // Minute 30
scheduler.createTask("announce", "broadcast Message", config);

// Tâche quotidienne
ScheduleConfig config = ScheduleConfig.daily("09:00");
scheduler.createTask("morning", "time set day", config);

// Tâche hebdomadaire
ScheduleConfig config = ScheduleConfig.weekly(Arrays.asList("SATURDAY", "SUNDAY"), "10:00");
scheduler.createTask("weekend", "broadcast Weekend!", config);
```

## API avancée

### Gestion des tâches

#### Création avec localisation
```java
// Créer une tâche liée à une position
Location loc = player.getLocation();
ScheduleConfig config = ScheduleConfig.interval(6000);
scheduler.createTask("local_effect", "particle flame", config, loc);
```

#### Gestion du cycle de vie
```java
// Démarrer une tâche
scheduler.startTask("taskId");

// Arrêter une tâche
scheduler.stopTask("taskId");

// Supprimer une tâche
scheduler.removeTask("taskId");

// Vérifier l'état
boolean isRunning = scheduler.isTaskRunning("taskId");
```

#### Obtenir des informations
```java
// Obtenir une tâche
ScheduledTask task = scheduler.getTask("taskId");

// Obtenir toutes les tâches
Collection<ScheduledTask> tasks = scheduler.getTasks();

// Obtenir la prochaine exécution
long nextRun = scheduler.getNextExecution("taskId");
```

### Événements

#### Écouter les événements
```java
public class TaskListener implements Listener {
    @EventHandler
    public void onTaskCreate(TaskCreateEvent event) {
        String taskId = event.getTaskId();
        ScheduleConfig config = event.getConfig();
        // Traitement...
    }

    @EventHandler
    public void onTaskExecute(TaskExecuteEvent event) {
        String taskId = event.getTaskId();
        String command = event.getCommand();
        // Traitement...
    }

    @EventHandler
    public void onTaskComplete(TaskCompleteEvent event) {
        String taskId = event.getTaskId();
        boolean success = event.isSuccess();
        // Traitement...
    }
}
```

#### Enregistrer les écouteurs
```java
public class MonPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new TaskListener(), this);
    }
}
```

### Configuration personnalisée

#### Créer une configuration personnalisée
```java
public class CustomScheduleConfig extends ScheduleConfig {
    private final String customParam;

    public CustomScheduleConfig(ScheduleType type, String customParam) {
        super(type);
        this.customParam = customParam;
    }

    @Override
    public long getNextExecutionTime() {
        // Logique personnalisée...
        return System.currentTimeMillis() + 6000L;
    }
}
```

#### Utiliser la configuration personnalisée
```java
CustomScheduleConfig config = new CustomScheduleConfig(ScheduleType.INTERVAL, "param");
scheduler.createTask("custom", "command", config);
```

## Exemples d'utilisation

### Système d'événements
```java
public class EventSystem {
    private final PSchedulers scheduler;

    public EventSystem(PSchedulers scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleEvent(String name, Location location, String command) {
        // Configuration de l'événement
        ScheduleConfig config = ScheduleConfig.weekly(
            Arrays.asList("SATURDAY", "SUNDAY"),
            "20:00"
        );

        // Création de la tâche principale
        scheduler.createTask(
            "event_" + name,
            command,
            config,
            location
        );

        // Création des annonces
        ScheduleConfig announceConfig = ScheduleConfig.interval(6000);
        scheduler.createTask(
            "announce_" + name,
            "broadcast L'événement " + name + " commence bientôt!",
            announceConfig
        );
    }
}
```

### Système de sauvegarde
```java
public class BackupSystem {
    private final PSchedulers scheduler;

    public BackupSystem(PSchedulers scheduler) {
        this.scheduler = scheduler;
    }

    public void setupBackups() {
        // Sauvegarde rapide toutes les heures
        ScheduleConfig quickConfig = ScheduleConfig.hourly(0);
        scheduler.createTask(
            "backup_quick",
            "backup quick",
            quickConfig
        );

        // Sauvegarde complète quotidienne
        ScheduleConfig fullConfig = ScheduleConfig.daily("04:00");
        scheduler.createTask(
            "backup_full",
            "backup full",
            fullConfig
        );
    }
}
```

## Bonnes pratiques

### 1. Gestion des erreurs
```java
try {
    scheduler.createTask("taskId", "command", config);
} catch (InvalidScheduleException e) {
    getLogger().warning("Configuration invalide : " + e.getMessage());
} catch (TaskAlreadyExistsException e) {
    getLogger().warning("La tâche existe déjà : " + e.getMessage());
}
```

### 2. Nettoyage des ressources
```java
public class MonPlugin extends JavaPlugin {
    @Override
    public void onDisable() {
        // Arrêter toutes les tâches du plugin
        PSchedulers.getInstance().getTasks().stream()
            .filter(task -> task.getPlugin().equals(this))
            .forEach(task -> task.stop());
    }
}
```

### 3. Performance
```java
// Mise en cache des tâches fréquemment utilisées
private final Map<String, ScheduledTask> taskCache = new HashMap<>();

public void cacheTask(String taskId) {
    taskCache.put(taskId, scheduler.getTask(taskId));
}

public ScheduledTask getCachedTask(String taskId) {
    return taskCache.getOrDefault(taskId, scheduler.getTask(taskId));
}
```

## Dépannage

### Problèmes courants

1. **La tâche ne s'exécute pas**
```java
// Vérification de l'état
if (!scheduler.isTaskRunning("taskId")) {
    // Vérifier la configuration
    ScheduledTask task = scheduler.getTask("taskId");
    getLogger().info("État de la tâche : " + task.getState());
    getLogger().info("Prochaine exécution : " + task.getNextExecutionTime());
}
```

2. **Erreurs de permission**
```java
// Vérifier les permissions avant création
if (!sender.hasPermission("pschedulers.admin")) {
    sender.sendMessage("Permission insuffisante");
    return;
}
```

3. **Conflits de tâches**
```java
// Vérifier les conflits avant création
if (scheduler.getTask("taskId") != null) {
    // Gérer le conflit
    String newId = "taskId_" + System.currentTimeMillis();
    scheduler.createTask(newId, "command", config);
}
``` 