# Configuration

PSchedulers utilise un fichier de configuration pour gérer ses tâches planifiées. Cette page détaille toutes les options disponibles.

## Structure des fichiers

```
plugins/PSchedulers/
└── config.yml
```

## config.yml

Le fichier de configuration principal du plugin contient toutes les tâches planifiées et leurs paramètres.

```yaml
# PSchedulers Configuration
# 
# Tasks are defined in the 'tasks' section below.
# Each task has the following properties:
#   - command: The command to execute
#   - type: Type of schedule (INTERVAL, HOURLY, DAILY, WEEKLY)
#   - enabled: Whether the task should start automatically
#   - world, x, y, z: Optional location for region-specific tasks (Folia)
#   - conditions: Optional conditions for task execution
#     - min_players: Minimum number of players required (optional)
#     - max_players: Maximum number of players allowed (optional)

tasks:
  # Exemple de tâche à intervalle avec condition de joueurs
  example_interval:
    command: "broadcast Hello World!"
    type: INTERVAL
    interval: 6000         # 5 minutes (en ticks)
    enabled: false
    conditions:
      min_players: 5       # Exécuter seulement si 5+ joueurs sont en ligne

  # Exemple de tâche horaire avec condition maximale
  example_hourly:
    command: "broadcast It's a new hour!"
    type: HOURLY
    minute: 0             # À la minute 0 de chaque heure
    enabled: false
    conditions:
      max_players: 10     # Exécuter seulement si 10- joueurs sont en ligne

  # Exemple de tâche quotidienne avec conditions min/max
  example_daily:
    command: "broadcast Good morning!"
    type: DAILY
    time: "09:00"        # Tous les jours à 9h
    enabled: false
    conditions:
      min_players: 1     # Au moins 1 joueur
      max_players: 50    # Maximum 50 joueurs

  # Exemple de tâche hebdomadaire
  example_weekly:
    command: "broadcast Weekly maintenance in 1 hour!"
    type: WEEKLY
    days: [MONDAY, WEDNESDAY, FRIDAY]
    time: "20:00"        # À 20h les jours spécifiés
    enabled: false
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

### Conditions d'exécution

Chaque tâche peut avoir des conditions qui doivent être remplies pour que la tâche s'exécute :

#### Conditions de nombre de joueurs
- `min_players`: Nombre minimum de joueurs requis en ligne
- `max_players`: Nombre maximum de joueurs autorisés en ligne

Les conditions sont optionnelles. Si aucune condition n'est spécifiée, la tâche s'exécutera sans restriction.

Exemples :
```yaml
conditions:
  min_players: 5    # La tâche ne s'exécute que s'il y a au moins 5 joueurs
  max_players: 20   # La tâche ne s'exécute que s'il y a 20 joueurs ou moins
```

Vous pouvez utiliser une seule condition ou les deux ensemble :
```yaml
conditions:
  min_players: 5    # Au moins 5 joueurs requis
```
```yaml
conditions:
  max_players: 20   # Maximum 20 joueurs autorisés
```
```yaml
conditions:
  min_players: 5    # Entre 5 et 20 joueurs
  max_players: 20
```

## Validation

PSchedulers valide automatiquement votre configuration au démarrage :

- Vérification des types de tâches
- Validation des intervalles et des horaires
- Vérification de la syntaxe des commandes
- Validation des conditions d'exécution

En cas d'erreur, consultez les logs du serveur pour plus de détails.

## Rechargement

Pour recharger la configuration sans redémarrer :

1. Modifiez le fichier de configuration
2. Exécutez `/scheduler reload`

## Bonnes pratiques

1. **Sauvegarde** : Faites une copie de votre fichier avant modification
2. **Tests** : Testez vos modifications sur un serveur de développement
3. **Performance** : Évitez les intervalles trop courts
4. **Conditions** : Utilisez des conditions raisonnables pour le nombre de joueurs

## Variables disponibles

Dans les commandes, vous pouvez utiliser :
- `%players%` : Nombre de joueurs en ligne 