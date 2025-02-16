# Conditions d'exécution

Les conditions d'exécution permettent de contrôler dynamiquement quand une tâche doit être exécutée en fonction de certains critères. Cette fonctionnalité est particulièrement utile pour adapter l'exécution des tâches en fonction de l'état du serveur.

## Conditions de nombre de joueurs

### Configuration

Les conditions de nombre de joueurs peuvent être configurées pour chaque tâche dans le fichier de configuration :

```yaml
tasks:
  ma_tache:
    command: "broadcast Hello!"
    type: INTERVAL
    interval: 6000
    enabled: true
    conditions:
      min_players: 5    # Minimum 5 joueurs requis
      max_players: 20   # Maximum 20 joueurs autorisés
```

### Types de conditions disponibles

#### Nombre minimum de joueurs (`min_players`)
- Définit le nombre minimum de joueurs qui doivent être connectés pour que la tâche s'exécute
- Si le nombre de joueurs est inférieur, la tâche est mise en pause jusqu'à ce que la condition soit remplie
- Exemple : `min_players: 5` - La tâche ne s'exécute que s'il y a au moins 5 joueurs en ligne

#### Nombre maximum de joueurs (`max_players`)
- Définit le nombre maximum de joueurs autorisés pour que la tâche s'exécute
- Si le nombre de joueurs est supérieur, la tâche est mise en pause jusqu'à ce que la condition soit remplie
- Exemple : `max_players: 20` - La tâche ne s'exécute que s'il y a 20 joueurs ou moins en ligne

### Utilisation combinée

Les conditions peuvent être utilisées individuellement ou combinées :

```yaml
# Condition minimum uniquement
conditions:
  min_players: 5

# Condition maximum uniquement
conditions:
  max_players: 20

# Les deux conditions ensemble
conditions:
  min_players: 5
  max_players: 20
```

Quand les deux conditions sont utilisées ensemble, elles créent une plage valide de nombre de joueurs. La tâche ne s'exécutera que si le nombre de joueurs est dans cette plage.

## Fonctionnement

1. Les conditions sont vérifiées juste avant l'exécution de chaque tâche
2. Si les conditions ne sont pas remplies, la tâche est sautée
3. Pour les tâches de type INTERVAL, la prochaine vérification aura lieu au prochain intervalle
4. Pour les autres types, la tâche attendra la prochaine occurrence planifiée

## Cas d'utilisation

### Gestion de la charge serveur
```yaml
backup_task:
  command: "backup start"
  type: DAILY
  time: "03:00"
  conditions:
    max_players: 5  # Faire la sauvegarde quand le serveur est peu peuplé
```

### Événements spéciaux
```yaml
event_task:
  command: "broadcast L'événement va commencer!"
  type: HOURLY
  minute: 0
  conditions:
    min_players: 10  # Lancer l'événement uniquement avec assez de joueurs
```

### Optimisation des ressources
```yaml
cleanup_task:
  command: "cleanup start"
  type: INTERVAL
  interval: 12000
  conditions:
    max_players: 30  # Limiter les tâches lourdes quand il y a beaucoup de joueurs
```

## Bonnes pratiques

1. **Définissez des seuils raisonnables**
   - Évitez les seuils trop restrictifs qui empêcheraient l'exécution des tâches importantes
   - Adaptez les seuils à la taille moyenne de votre serveur

2. **Utilisez les conditions à bon escient**
   - Pour les tâches critiques, évitez les conditions trop restrictives
   - Privilégiez les conditions pour les tâches non essentielles ou consommatrices de ressources

3. **Testez vos configurations**
   - Vérifiez que les tâches s'exécutent comme prévu avec différents nombres de joueurs
   - Surveillez les logs pour vous assurer que les tâches importantes ne sont pas bloquées

4. **Documentation**
   - Commentez vos conditions dans la configuration
   - Expliquez pourquoi certains seuils ont été choisis

## Dépannage

### La tâche ne s'exécute jamais
- Vérifiez que les conditions ne sont pas trop restrictives
- Assurez-vous que les seuils min/max sont cohérents entre eux
- Consultez les logs pour voir si les conditions sont la cause

### La tâche s'exécute à des moments inattendus
- Vérifiez que les conditions sont correctement définies
- Assurez-vous que les valeurs sont dans la bonne plage
- Vérifiez qu'il n'y a pas de conflit avec d'autres paramètres

## Variables disponibles

Dans les commandes des tâches, vous pouvez utiliser :
- `%players%` : Nombre actuel de joueurs en ligne
- `%min_players%` : Seuil minimum configuré
- `%max_players%` : Seuil maximum configuré 