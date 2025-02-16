# Validation et formatage

PSchedulers intègre des systèmes avancés de validation et de formatage pour garantir la fiabilité des tâches planifiées et améliorer l'expérience utilisateur.

## Validation

### Types de validation

#### 1. Validation des types
```java
public enum ScheduleType {
    INTERVAL,
    HOURLY,
    DAILY,
    WEEKLY
}
```

#### 2. Validation des paramètres
```yaml
# Intervalles valides
interval: 6000    # OK
interval: -1      # Erreur : doit être positif
interval: 0       # Erreur : doit être positif

# Minutes valides (HOURLY)
minute: 30        # OK
minute: 60        # Erreur : doit être entre 0 et 59
minute: -1        # Erreur : doit être entre 0 et 59

# Format d'heure (DAILY/WEEKLY)
time: "09:00"     # OK
time: "25:00"     # Erreur : heure invalide
time: "9:00"      # Erreur : format incorrect (HH:mm)

# Jours (WEEKLY)
days: [MONDAY, WEDNESDAY]  # OK
days: [INVALID]           # Erreur : jour invalide
```

### Messages d'erreur

```yaml
validation:
  errors:
    interval:
      negative: "L'intervalle doit être positif"
      zero: "L'intervalle ne peut pas être zéro"
    
    hourly:
      invalid_minute: "La minute doit être entre 0 et 59"
    
    daily:
      invalid_time: "Le format de l'heure doit être HH:mm"
      invalid_hour: "L'heure doit être entre 00 et 23"
      invalid_minute: "La minute doit être entre 00 et 59"
    
    weekly:
      invalid_day: "Jour invalide : %day%"
      empty_days: "La liste des jours ne peut pas être vide"
```

### Exemples de validation

#### Configuration valide
```yaml
tasks:
  backup:
    type: INTERVAL
    interval: 6000
    command: "backup"
    enabled: true

  announce:
    type: HOURLY
    minute: 30
    command: "broadcast Message"
    enabled: true

  morning:
    type: DAILY
    time: "09:00"
    command: "time set day"
    enabled: true

  weekend:
    type: WEEKLY
    days: [SATURDAY, SUNDAY]
    time: "10:00"
    command: "broadcast Weekend!"
    enabled: true
```

#### Erreurs courantes
```yaml
tasks:
  # Erreur : intervalle négatif
  invalid_interval:
    type: INTERVAL
    interval: -6000
    command: "backup"

  # Erreur : minute invalide
  invalid_hourly:
    type: HOURLY
    minute: 60
    command: "broadcast"

  # Erreur : format d'heure incorrect
  invalid_daily:
    type: DAILY
    time: "9:0"
    command: "time set day"

  # Erreur : jour invalide
  invalid_weekly:
    type: WEEKLY
    days: [SOMEDAY]
    time: "10:00"
    command: "broadcast"
```

## Formatage

### Formatage des horaires

#### 1. Format d'affichage
```java
// Exemples de formatage
"Toutes les 5 minutes"           // INTERVAL
"À la minute 30 de chaque heure" // HOURLY
"Tous les jours à 09:00"         // DAILY
"Le samedi et dimanche à 10:00"  // WEEKLY
```

#### 2. Format de configuration
```yaml
# Format d'intervalle
interval: 6000    # -> "5 minutes"
interval: 72000   # -> "1 heure"

# Format horaire
minute: 30        # -> "minute 30"
minute: 0         # -> "début d'heure"

# Format quotidien
time: "09:00"     # -> "09:00"

# Format hebdomadaire
days: [MON,WED]   # -> "lundi et mercredi"
time: "10:00"     # -> "à 10:00"
```

### Exemples de formatage

#### Affichage des tâches
```
[PSchedulers] Liste des tâches :
- backup : Toutes les 5 minutes
- announce : À la minute 30 de chaque heure
- morning : Tous les jours à 09:00
- weekend : Le samedi et dimanche à 10:00
```

#### Prochaine exécution
```
[PSchedulers] Prochaine exécution :
- backup : Dans 2 minutes et 30 secondes
- announce : Dans 15 minutes
- morning : Demain à 09:00
- weekend : Samedi à 10:00
```

## Validation avancée

### Validation des commandes

#### 1. Syntaxe
```yaml
# Validation de la syntaxe des commandes
command: "backup world"     # OK
command: ""                 # Erreur : commande vide
command: "invalid command"  # Erreur : commande inconnue
```

#### 2. Permissions
```yaml
# Vérification des permissions
command: "backup world"     # Nécessite pschedulers.command.backup
command: "broadcast"        # Nécessite pschedulers.command.broadcast
```

### Validation des dépendances

```yaml
# Vérification des plugins requis
tasks:
  worldedit_task:
    type: INTERVAL
    interval: 6000
    command: "//set stone"
    requires:
      - WorldEdit
```

### Validation des conditions

```yaml
tasks:
  weather_announce:
    type: HOURLY
    minute: 0
    command: "broadcast Il pleut!"
    conditions:
      weather: "RAIN"
      world: "world"
```

## Formatage avancé

### Messages personnalisés

```yaml
messages:
  task:
    created: "&aNouvelle tâche créée : &e%task%"
    schedule: "&7Planification : &f%schedule%"
    next_run: "&7Prochaine exécution : &f%next%"
```

### Variables disponibles

```yaml
variables:
  time: "%time%"           # Heure actuelle
  date: "%date%"           # Date actuelle
  server: "%server%"       # Nom du serveur
  world: "%world%"         # Monde actuel
  task: "%task%"           # ID de la tâche
  type: "%type%"           # Type de planification
  schedule: "%schedule%"   # Description de la planification
  next: "%next%"          # Prochaine exécution
```

### Exemples de personnalisation

```yaml
messages:
  task:
    created: "&aNouvelle tâche &e%task% &acréée"
    schedule: "&7Planifiée : &f%schedule%"
    next_run: "&7Prochaine exécution le &f%date% &7à &f%time%"
    
  format:
    interval: "toutes les %value% minutes"
    hourly: "à la minute %minute% de chaque heure"
    daily: "tous les jours à %time%"
    weekly: "%days% à %time%"
```

## Bonnes pratiques

### 1. Validation
- Validez toujours les entrées utilisateur
- Utilisez des valeurs par défaut sécurisées
- Fournissez des messages d'erreur clairs

### 2. Formatage
- Gardez un style cohérent
- Utilisez des couleurs avec modération
- Adaptez les messages au contexte

### 3. Performance
- Mettez en cache les validations fréquentes
- Optimisez les expressions régulières
- Limitez les opérations de formatage coûteuses 