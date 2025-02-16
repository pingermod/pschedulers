# Types de planification

PSchedulers propose quatre types de planification différents pour répondre à tous vos besoins d'automatisation. Cette page détaille chaque type et son utilisation.

## Vue d'ensemble

| Type | Description | Cas d'utilisation |
|------|-------------|-------------------|
| INTERVAL | Exécution à intervalles réguliers | Sauvegardes, nettoyages réguliers |
| HOURLY | Exécution à une minute précise chaque heure | Annonces horaires, mises à jour |
| DAILY | Exécution à une heure précise chaque jour | Redémarrage quotidien, événements journaliers |
| WEEKLY | Exécution certains jours à une heure précise | Événements hebdomadaires, maintenance |

## INTERVAL

### Description
Le type INTERVAL exécute une tâche à intervalles réguliers, mesurés en ticks (20 ticks = 1 seconde).

### Configuration
```yaml
tasks:
  backup:
    type: INTERVAL
    interval: 6000  # 5 minutes
    command: "backup world"
    enabled: true
```

### Commande
```bash
/scheduler create backup TYPE:INTERVAL 6000 backup world
```

### Paramètres
- `interval` : Nombre de ticks entre chaque exécution
  - 1200 ticks = 1 minute
  - 6000 ticks = 5 minutes
  - 72000 ticks = 1 heure

### Bonnes pratiques
1. Évitez les intervalles trop courts (< 600 ticks)
2. Utilisez des multiples de 20 pour des intervalles précis
3. Considérez l'impact sur les performances

## HOURLY

### Description
Le type HOURLY exécute une tâche à une minute précise de chaque heure.

### Configuration
```yaml
tasks:
  announce:
    type: HOURLY
    minute: 30  # À la 30ème minute
    command: "broadcast Nouvelle demi-heure!"
    enabled: true
```

### Commande
```bash
/scheduler create announce TYPE:HOURLY 30 broadcast "Nouvelle demi-heure!"
```

### Paramètres
- `minute` : Minute de l'heure (0-59)
  - 0 : Début de l'heure
  - 30 : Demi-heure
  - 15/45 : Quarts d'heure

### Bonnes pratiques
1. Répartissez les tâches sur différentes minutes
2. Évitez de surcharger les minutes populaires (0, 15, 30, 45)
3. Utilisez des minutes différentes pour des tâches similaires

## DAILY

### Description
Le type DAILY exécute une tâche à une heure précise chaque jour.

### Configuration
```yaml
tasks:
  restart:
    type: DAILY
    time: "04:00"  # À 4h du matin
    command: "broadcast Le serveur redémarre dans 5 minutes!"
    enabled: true
```

### Commande
```bash
/scheduler create restart TYPE:DAILY 04:00 broadcast "Le serveur redémarre dans 5 minutes!"
```

### Paramètres
- `time` : Heure d'exécution (format HH:mm)
  - Format 24h
  - Précision à la minute
  - Basé sur le fuseau horaire du serveur

### Bonnes pratiques
1. Planifiez les tâches lourdes pendant les heures creuses
2. Utilisez des heures décalées pour éviter les pics d'activité
3. Considérez les différents fuseaux horaires de vos joueurs

## WEEKLY

### Description
Le type WEEKLY exécute une tâche certains jours de la semaine à une heure précise.

### Configuration
```yaml
tasks:
  event:
    type: WEEKLY
    days: [SATURDAY, SUNDAY]
    time: "20:00"  # À 20h
    command: "broadcast L'événement commence dans 5 minutes!"
    enabled: true
```

### Commande
```bash
/scheduler create event TYPE:WEEKLY SAT,SUN 20:00 broadcast "L'événement commence dans 5 minutes!"
```

### Paramètres
- `days` : Liste des jours
  - Format complet : MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
  - Format court : MON, TUE, WED, THU, FRI, SAT, SUN
- `time` : Heure d'exécution (format HH:mm)

### Bonnes pratiques
1. Groupez les événements similaires
2. Planifiez en fonction des pics de fréquentation
3. Utilisez des jours consécutifs pour les événements liés

## Validation et erreurs

### Validation des intervalles
- INTERVAL : > 0 ticks
- HOURLY : 0-59 minutes
- DAILY/WEEKLY : Format HH:mm valide
- WEEKLY : Jours valides

### Messages d'erreur courants
```
"Intervalle invalide" : L'intervalle doit être positif
"Minute invalide" : La minute doit être entre 0 et 59
"Format d'heure invalide" : L'heure doit être au format HH:mm
"Jour invalide" : Le jour spécifié n'existe pas
```

## Exemples avancés

### Combinaison de types
Pour des besoins complexes, utilisez plusieurs tâches :

```yaml
tasks:
  # Sauvegarde toutes les 30 minutes
  backup_frequent:
    type: INTERVAL
    interval: 36000
    command: "backup quick"
    
  # Sauvegarde complète quotidienne
  backup_full:
    type: DAILY
    time: "03:00"
    command: "backup full"
    
  # Maintenance hebdomadaire
  maintenance:
    type: WEEKLY
    days: [MONDAY]
    time: "04:00"
    command: "maintenance start"
```

### Chaînage de commandes
Utilisez des scripts ou des alias pour des séquences complexes :

```yaml
tasks:
  restart_sequence:
    type: DAILY
    time: "04:00"
    command: "restart-sequence"  # Script personnalisé
```

## Migration entre types

### D'INTERVAL vers HOURLY
```diff
tasks:
  announce:
-   type: INTERVAL
-   interval: 72000  # 1 heure
+   type: HOURLY
+   minute: 0
    command: "broadcast Message"
```

### D'HOURLY vers DAILY
```diff
tasks:
  backup:
-   type: HOURLY
-   minute: 0
+   type: DAILY
+   time: "00:00"
    command: "backup world"
``` 