# Système de notifications

Le système de notifications permet d'informer les administrateurs et les joueurs des événements importants du plugin via différents canaux.

## Configuration

Les notifications sont configurées dans la section `notifications` du fichier `config.yml` :

```yaml
notifications:
  # Configuration Discord
  discord:
    enabled: false
    webhook_url: ""
    events: [START, STOP, FAILURE]

  # Configuration console
  console:
    level: INFO              # Niveau de log (INFO, WARNING, ERROR)

  # Configuration joueurs
  players:
    permission: "scheduler.notify"  # Permission pour recevoir les notifications
```

## Canaux de notification

### Discord
- Utilise les webhooks Discord
- Notifications en temps réel
- Personnalisation possible du format
- Configuration sécurisée du webhook

### Console
- Logs dans la console du serveur
- Différents niveaux de sévérité
- Format standard des logs
- Filtrage par niveau

### Joueurs en jeu
- Messages aux joueurs avec permission
- Format personnalisable
- Support des couleurs
- Ciblage par permission

## Types d'événements

Les événements disponibles sont :

- `START` : Démarrage d'une tâche
- `STOP` : Arrêt d'une tâche
- `FAILURE` : Échec d'une tâche
- `SUCCESS` : Succès d'une tâche
- `RETRY` : Nouvelle tentative d'une tâche
- `RELOAD` : Rechargement de la configuration

## Configuration Discord

### Configuration de base
```yaml
discord:
  enabled: true
  webhook_url: "https://discord.com/api/webhooks/..."
  events: [START, STOP, FAILURE]
```

### Événements personnalisés
```yaml
discord:
  enabled: true
  webhook_url: "https://discord.com/api/webhooks/..."
  events: 
    - START
    - STOP
    - FAILURE
    - SUCCESS
    - RELOAD
```

## Niveaux de log console

Les niveaux disponibles sont :
- `INFO` : Informations générales
- `WARNING` : Avertissements
- `ERROR` : Erreurs

Configuration :
```yaml
console:
  level: WARNING  # Ne montre que les warnings et erreurs
```

## Notifications joueurs

### Configuration de base
```yaml
players:
  permission: "scheduler.notify"
```

### Exemple d'utilisation
```yaml
tasks:
  backup:
    command: "backup start"
    type: DAILY
    time: "04:00"
    notify_players: true  # Active les notifications pour cette tâche
```

## Bonnes pratiques

1. **Sécurité**
   - Protégez votre webhook Discord
   - Limitez les permissions de notification
   - Filtrez les informations sensibles

2. **Performance**
   - Évitez trop de notifications Discord
   - Utilisez le bon niveau de log
   - Limitez les notifications aux joueurs

3. **Organisation**
   - Groupez les notifications par importance
   - Définissez des canaux dédiés
   - Documentez les événements importants

## Exemples avancés

### Configuration complète
```yaml
notifications:
  discord:
    enabled: true
    webhook_url: "https://discord.com/api/webhooks/..."
    events: [START, STOP, FAILURE, SUCCESS]
    format: "[{task}] {message}"
  
  console:
    level: INFO
    format: "[PSchedulers] {level} - {message}"
  
  players:
    permission: "scheduler.notify"
    format: "&7[&bPS&7] {message}"
```

### Notifications conditionnelles
```yaml
tasks:
  critical_backup:
    command: "backup critical"
    type: DAILY
    time: "00:00"
    notifications:
      success:
        discord: true
        console: INFO
        players: true
      failure:
        discord: true
        console: ERROR
        players: true
```

## Dépannage

1. **Discord**
   - Vérifiez la validité du webhook
   - Confirmez les permissions du webhook
   - Testez la connexion

2. **Console**
   - Vérifiez le niveau de log
   - Consultez les logs complets
   - Activez le mode debug

3. **Joueurs**
   - Vérifiez les permissions
   - Testez avec différents formats
   - Confirmez la réception 