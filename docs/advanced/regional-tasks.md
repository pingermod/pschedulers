# Tâches régionales

Les tâches régionales sont une fonctionnalité avancée de PSchedulers qui permet d'exécuter des commandes dans des régions spécifiques du monde. Cette fonctionnalité est particulièrement utile avec Folia, mais peut aussi être utilisée avec Paper.

## Concept

Une tâche régionale est une tâche planifiée qui est liée à une position spécifique dans le monde. Cela permet :
- L'exécution de commandes dans un contexte spatial précis
- L'optimisation des performances avec Folia
- La création d'événements localisés

## Configuration

### Structure d'une tâche régionale
```yaml
tasks:
  example_regional:
    type: INTERVAL
    interval: 6000
    command: "particle flame"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100
    enabled: true
```

### Paramètres de localisation
- `world` : Nom du monde
- `x` : Coordonnée X
- `y` : Coordonnée Y
- `z` : Coordonnée Z

## Création de tâches régionales

### Via commande (en jeu)

#### À la position du joueur
```bash
/scheduler create local_effect TYPE:INTERVAL 6000 particle flame
```

#### À une position spécifique
```bash
/scheduler create remote_effect TYPE:INTERVAL 6000 -loc world,100,64,100 particle flame
```

### Via configuration

#### Tâche simple
```yaml
tasks:
  spawn_particles:
    type: INTERVAL
    interval: 1200
    command: "particle flame"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
```

#### Tâche multiple
```yaml
tasks:
  spawn_effect_1:
    type: INTERVAL
    interval: 1200
    command: "particle flame"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100

  spawn_effect_2:
    type: INTERVAL
    interval: 1200
    command: "particle flame"
    location:
      world: "world"
      x: -100
      y: 64
      z: -100
```

## Types d'utilisation

### 1. Effets visuels
```yaml
tasks:
  portal_effect:
    type: INTERVAL
    interval: 200
    command: "particle portal"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
```

### 2. Spawns de mobs
```yaml
tasks:
  zombie_spawn:
    type: HOURLY
    minute: 0
    command: "summon zombie"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100
```

### 3. Événements locaux
```yaml
tasks:
  local_event:
    type: DAILY
    time: "18:00"
    command: "broadcast Événement local en cours!"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
```

## Optimisation

### 1. Groupement par région
```yaml
# Bon : Tâches groupées par région
tasks:
  spawn_effect_1:
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
  spawn_effect_2:
    location:
      world: "world"
      x: 5
      y: 64
      z: 5

# Mauvais : Tâches dispersées
tasks:
  effect_1:
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
  effect_2:
    location:
      world: "world"
      x: 1000
      y: 64
      z: 1000
```

### 2. Intervalles optimisés
```yaml
# Bon : Intervalles décalés
tasks:
  effect_1:
    interval: 6000
  effect_2:
    interval: 6100

# Mauvais : Intervalles synchronisés
tasks:
  effect_1:
    interval: 6000
  effect_2:
    interval: 6000
```

## Exemples avancés

### Système d'arène
```yaml
tasks:
  # Annonce de début
  arena_announce:
    type: HOURLY
    minute: 0
    command: "broadcast L'arène ouvre dans 5 minutes!"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100

  # Spawn de mobs
  arena_mobs:
    type: INTERVAL
    interval: 6000
    command: "summon zombie"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100

  # Effets visuels
  arena_effects:
    type: INTERVAL
    interval: 200
    command: "particle flame"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100
```

### Système de récompenses régionales
```yaml
tasks:
  # Spawn de coffres
  reward_spawn_1:
    type: DAILY
    time: "12:00"
    command: "setblock ~ ~ ~ chest"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100

  # Annonce locale
  reward_announce_1:
    type: DAILY
    time: "12:00"
    command: "broadcast Un coffre est apparu!"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100
```

## Dépannage

### Problèmes courants

1. **La tâche ne s'exécute pas**
   - Vérifiez que le monde existe
   - Confirmez que les coordonnées sont valides
   - Assurez-vous que la région est chargée

2. **Performance dégradée**
   - Réduisez le nombre de tâches par région
   - Augmentez les intervalles
   - Groupez les tâches similaires

3. **Commandes non exécutées**
   - Vérifiez les permissions
   - Testez la commande manuellement
   - Confirmez la syntaxe

### Solutions

#### Vérification de région
```yaml
# Ajoutez des commandes de test
tasks:
  region_test:
    type: INTERVAL
    interval: 1200
    command: "say Test de région"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
```

#### Debug
```yaml
# Activez le mode debug
debug: true
```

## Migration

### De global à régional
```diff
tasks:
  example:
    type: INTERVAL
    interval: 6000
    command: "particle flame"
+   location:
+     world: "world"
+     x: 0
+     y: 64
+     z: 0
```

### Entre régions
```diff
tasks:
  example:
    type: INTERVAL
    interval: 6000
    command: "particle flame"
    location:
-     world: "world"
-     x: 0
-     y: 64
-     z: 0
+     world: "world_nether"
+     x: 100
+     y: 64
+     z: 100
``` 