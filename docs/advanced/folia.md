# Support Folia

PSchedulers intègre un support natif de Folia, offrant une gestion optimisée des tâches planifiées dans un environnement multi-thread.

## Qu'est-ce que Folia ?

Folia est une fork de Paper qui apporte une nouvelle architecture multi-thread au serveur Minecraft. Au lieu d'exécuter tout le serveur sur un seul thread, Folia divise le monde en régions qui peuvent être traitées indépendamment.

## Avantages du support Folia

1. **Performance optimisée**
   - Exécution des tâches dans les threads appropriés
   - Répartition de la charge sur plusieurs cœurs
   - Meilleure gestion des ressources

2. **Gestion des régions**
   - Tâches liées à des régions spécifiques
   - Exécution locale des commandes
   - Isolation des performances

3. **Sécurité thread-safe**
   - Synchronisation automatique
   - Prévention des conflits
   - Stabilité accrue

## Configuration

### config.yml
```yaml
folia:
  enabled: true                # Activer le support Folia
  region_aware: true          # Activer la gestion des régions
  default_region: "global"    # Région par défaut
  sync_threshold: 100         # Seuil de synchronisation (en ticks)
```

### tasks.yml avec support régional
```yaml
tasks:
  local_broadcast:
    type: INTERVAL
    interval: 6000
    command: "broadcast Message local"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
    enabled: true
```

## Création de tâches régionales

### Via commande (en jeu)
```bash
# La tâche sera liée à la position actuelle du joueur
/scheduler create local_task TYPE:INTERVAL 6000 broadcast "Message local"

# Spécifier une position précise
/scheduler create region_task TYPE:INTERVAL 6000 -loc world,0,64,0 broadcast "Message régional"
```

### Via configuration
```yaml
tasks:
  region_task:
    type: INTERVAL
    interval: 6000
    command: "broadcast Message régional"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
    enabled: true
```

## Types de tâches Folia

### 1. Tâches globales
- Exécutées dans le thread global
- Affectent tout le serveur
- Idéales pour les annonces générales

```yaml
tasks:
  global_announce:
    type: HOURLY
    minute: 0
    command: "broadcast Message global"
    # Pas de location = tâche globale
```

### 2. Tâches régionales
- Exécutées dans le thread de la région
- Affectent une zone spécifique
- Parfaites pour les événements locaux

```yaml
tasks:
  region_event:
    type: DAILY
    time: "14:00"
    command: "summon firework"
    location:
      world: "world"
      x: 100
      y: 64
      z: 100
```

### 3. Tâches asynchrones
- Exécutées dans un thread séparé
- Pour les opérations non-Minecraft
- Idéales pour les sauvegardes

```yaml
tasks:
  async_backup:
    type: INTERVAL
    interval: 72000
    command: "backup"
    async: true
```

## Bonnes pratiques

### 1. Gestion des régions
- Regroupez les tâches par région
- Évitez trop de tâches dans une même région
- Utilisez des régions appropriées pour les événements

### 2. Performance
- Limitez le nombre de tâches régionales
- Utilisez des intervalles raisonnables
- Préférez les tâches asynchrones quand possible

### 3. Commandes
- Testez les commandes en mode synchrone d'abord
- Vérifiez la compatibilité avec Folia
- Utilisez des commandes optimisées

## Dépannage

### Problèmes courants

1. **La tâche ne s'exécute pas dans la bonne région**
   ```yaml
   # Solution : Vérifiez les coordonnées
   location:
     world: "world"
     x: 0
     y: 64
     z: 0
   ```

2. **Erreurs de synchronisation**
   ```yaml
   # Solution : Augmentez le seuil de synchronisation
   folia:
     sync_threshold: 200
   ```

3. **Conflits entre tâches**
   ```yaml
   # Solution : Utilisez des délais différents
   tasks:
     task1:
       interval: 6000
     task2:
       interval: 6100
   ```

### Messages d'erreur

| Message | Cause | Solution |
|---------|-------|----------|
| "Region not found" | Région invalide | Vérifiez les coordonnées |
| "Thread violation" | Mauvais thread | Utilisez async: true |
| "Sync required" | Opération synchrone nécessaire | Désactivez async |

## Migration depuis Paper

### 1. Adaptation des tâches
```diff
tasks:
  example:
    type: INTERVAL
    interval: 6000
    command: "worldedit clear"
+   location:
+     world: "world"
+     x: 0
+     y: 64
+     z: 0
```

### 2. Optimisation des commandes
```diff
tasks:
  backup:
    type: HOURLY
    minute: 0
    command: "backup"
+   async: true
```

## Exemples avancés

### Système d'événements régionaux
```yaml
tasks:
  # Événement principal
  event_main:
    type: WEEKLY
    days: [SATURDAY]
    time: "20:00"
    command: "event start"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0

  # Effets secondaires
  event_effects:
    type: INTERVAL
    interval: 1200
    command: "particle"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0
```

### Système de nettoyage multi-régions
```yaml
tasks:
  cleanup_spawn:
    type: HOURLY
    minute: 0
    command: "clean"
    location:
      world: "world"
      x: 0
      y: 64
      z: 0

  cleanup_nether:
    type: HOURLY
    minute: 30
    command: "clean"
    location:
      world: "world_nether"
      x: 0
      y: 64
      z: 0
``` 