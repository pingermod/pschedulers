# Configuration

PSchedulers utilise plusieurs fichiers de configuration pour gérer ses différentes fonctionnalités. Cette page détaille chaque fichier et ses options.

## Structure des fichiers

```
plugins/PSchedulers/
├── config.yml
├── tasks.yml
└── messages.yml
```

## config.yml

Le fichier de configuration principal du plugin.

```yaml
# Configuration générale
debug: false                 # Mode debug pour plus de logs
timezone: "Europe/Paris"     # Fuseau horaire pour les planifications
language: "fr"              # Langue des messages (fr, en)

# Configuration des performances
max_concurrent_tasks: 10     # Nombre maximum de tâches simultanées
task_timeout: 30            # Timeout en secondes pour les tâches

# Configuration Folia
folia:
  enabled: true             # Activer le support Folia
  region_aware: true        # Activer la gestion des régions
```

## tasks.yml

Configuration des tâches planifiées.

```yaml
tasks:
  # Exemple de tâche à intervalle
  backup:
    command: "backup world"
    type: INTERVAL
    interval: 72000         # 1 heure (en ticks)
    enabled: true

  # Exemple de tâche horaire
  broadcast:
    command: "broadcast L'heure est passée!"
    type: HOURLY
    minute: 0              # À la minute 0 de chaque heure
    enabled: true

  # Exemple de tâche quotidienne
  morning:
    command: "time set day"
    type: DAILY
    time: "06:00"          # Tous les jours à 6h
    enabled: true

  # Exemple de tâche hebdomadaire
  weekend:
    command: "broadcast C'est le weekend!"
    type: WEEKLY
    days: [SATURDAY, SUNDAY]
    time: "10:00"
    enabled: true

  # Exemple de tâche régionale (Folia)
  region_broadcast:
    command: "broadcast Message local"
    type: INTERVAL
    interval: 6000
    location:              # Coordonnées de la région
      world: "world"
      x: 0
      y: 64
      z: 0
    enabled: true
```

### Types de tâches

#### INTERVAL
- `interval`: Nombre de ticks entre chaque exécution (20 ticks = 1 seconde)

#### HOURLY
- `minute`: Minute de l'heure (0-59)

#### DAILY
- `time`: Heure d'exécution (format "HH:mm")

#### WEEKLY
- `days`: Liste des jours [MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]
- `time`: Heure d'exécution (format "HH:mm")

## messages.yml

Personnalisation des messages du plugin.

```yaml
prefix: "&7[&bPSchedulers&7] "
messages:
  task:
    created: "&aNouvelle tâche créée avec succès : &e%task%"
    removed: "&cTâche supprimée : &e%task%"
    started: "&aTâche démarrée : &e%task%"
    stopped: "&cTâche arrêtée : &e%task%"
    not_found: "&cTâche introuvable : &e%task%"
    already_exists: "&cUne tâche avec cet ID existe déjà"
    invalid_type: "&cType de tâche invalide : &e%type%"
    
  command:
    no_permission: "&cVous n'avez pas la permission d'utiliser cette commande"
    invalid_syntax: "&cSyntaxe invalide. Utilisez : &e%usage%"
    
  scheduler:
    reloaded: "&aConfiguration rechargée avec succès"
    error: "&cUne erreur est survenue : &e%error%"
```

## Validation

PSchedulers valide automatiquement votre configuration au démarrage :

- Vérification des types de tâches
- Validation des intervalles et des horaires
- Contrôle des permissions
- Vérification de la syntaxe des commandes

En cas d'erreur, consultez les logs du serveur pour plus de détails.

## Rechargement

Pour recharger la configuration sans redémarrer :

1. Modifiez les fichiers de configuration
2. Exécutez `/scheduler reload`

## Bonnes pratiques

1. **Sauvegarde** : Faites une copie de vos fichiers avant modification
2. **Tests** : Testez vos modifications sur un serveur de développement
3. **Permissions** : Limitez l'accès aux commandes administratives
4. **Performance** : Évitez les intervalles trop courts
5. **Logs** : Activez le mode debug temporairement en cas de problème

## Variables disponibles

Dans les commandes, vous pouvez utiliser :

- `%time%` : Heure actuelle
- `%date%` : Date actuelle
- `%server%` : Nom du serveur
- `%world%` : Monde actuel
- `%task%` : ID de la tâche
- `%type%` : Type de planification 