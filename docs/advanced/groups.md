# Groupes de tâches

Les groupes de tâches permettent d'organiser et d'exécuter plusieurs tâches ensemble, avec des conditions communes et des modes d'exécution spécifiques.

## Configuration

Les groupes sont définis dans la section `groups` du fichier `config.yml` :

```yaml
groups:
  backup:
    tasks: [world_backup, player_data_backup]
    sequential: true        # Exécution séquentielle
    conditions:
      min_players: 0
      tps:
        min: 18.0
  
  morning:
    tasks: [clear_weather, heal_players, give_morning_bonus]
    parallel: true         # Exécution parallèle
```

## Modes d'exécution

### Séquentiel (`sequential: true`)
- Les tâches sont exécutées l'une après l'autre
- L'ordre est garanti
- Une tâche n'est exécutée que si la précédente est terminée
- Utile pour les opérations dépendantes

### Parallèle (`parallel: true`)
- Les tâches sont exécutées simultanément
- Pas d'ordre garanti
- Toutes les tâches démarrent en même temps
- Utile pour les opérations indépendantes

## Conditions de groupe

Les conditions s'appliquent à l'ensemble du groupe :

```yaml
groups:
  event:
    tasks: [start_event, give_rewards, announce_winners]
    sequential: true
    conditions:
      min_players: 10
      max_players: 50
      weather:
        type: [CLEAR]
      time:
        min: 0
        max: 12000
```

## Gestion des erreurs

### Mode séquentiel
- Si une tâche échoue, les suivantes ne sont pas exécutées
- Les erreurs sont enregistrées dans les logs
- Le groupe est marqué comme échoué

### Mode parallèle
- Les erreurs d'une tâche n'affectent pas les autres
- Chaque tâche est indépendante
- Le groupe continue même en cas d'erreur

## Exemples d'utilisation

### Sauvegarde complète
```yaml
groups:
  full_backup:
    tasks: 
      - world_backup
      - player_data_backup
      - economy_backup
    sequential: true
    conditions:
      max_players: 5
      tps:
        min: 18.0
```

### Événement de serveur
```yaml
groups:
  server_event:
    tasks:
      - announce_event
      - teleport_players
      - start_minigame
    parallel: true
    conditions:
      min_players: 10
```

### Maintenance quotidienne
```yaml
groups:
  daily_maintenance:
    tasks:
      - clear_logs
      - cleanup_temp
      - optimize_database
    sequential: true
    conditions:
      min_players: 0
```

## Bonnes pratiques

1. **Organisation**
   - Groupez les tâches logiquement liées
   - Choisissez le bon mode d'exécution
   - Définissez des conditions appropriées

2. **Performance**
   - Évitez trop de tâches parallèles
   - Surveillez l'impact sur les TPS
   - Planifiez les groupes lourds aux heures creuses

3. **Maintenance**
   - Documentez les dépendances
   - Testez les groupes complets
   - Surveillez les logs d'erreurs

## Limitations

- Maximum 10 tâches par groupe
- Les groupes ne peuvent pas être imbriqués
- Une tâche ne peut appartenir qu'à un seul groupe actif 