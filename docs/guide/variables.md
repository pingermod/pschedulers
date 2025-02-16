# Variables personnalisées

Les variables personnalisées permettent de définir des valeurs réutilisables dans vos tâches. Elles sont particulièrement utiles pour centraliser des messages, des récompenses ou des séquences de commandes.

## Configuration

Les variables sont définies dans la section `variables` du fichier `config.yml` :

```yaml
variables:
  # Messages personnalisés
  custom_messages:
    morning: "Bonjour à tous!"
    evening: "Bonne soirée!"
    
  # Récompenses prédéfinies
  rewards:
    daily: "diamond 1"
    weekly: "diamond_block 1"
    
  # Séquences de commandes
  commands:
    restart_warning:
      - "broadcast Le serveur redémarre dans {time}"
      - "title @a title Redémarrage"
```

## Types de variables

### Messages personnalisés
- Définis dans `custom_messages`
- Utilisés pour centraliser les messages fréquemment utilisés
- Accessibles via `{message.nom_du_message}`

### Récompenses
- Définies dans `rewards`
- Utilisées pour standardiser les récompenses
- Accessibles via `{reward.nom_de_la_recompense}`

### Séquences de commandes
- Définies dans `commands`
- Permettent d'exécuter plusieurs commandes en séquence
- Utiles pour les actions complexes

## Utilisation dans les tâches

### Messages
```yaml
tasks:
  morning_message:
    command: "broadcast {message.morning}"
    type: DAILY
    time: "09:00"
```

### Récompenses
```yaml
tasks:
  daily_reward:
    command: "give @a {reward.daily}"
    type: DAILY
    time: "12:00"
```

### Séquences de commandes
```yaml
tasks:
  server_restart:
    commands: "{commands.restart_warning}"
    type: DAILY
    time: "04:00"
```

## Variables système

En plus des variables personnalisées, certaines variables système sont disponibles :

- `%players%` : Nombre de joueurs en ligne
- `%time%` : Heure actuelle
- `%date%` : Date actuelle
- `%world%` : Monde actuel
- `%tps%` : TPS du serveur

## Bonnes pratiques

1. **Organisation**
   - Groupez les variables par catégorie
   - Utilisez des noms descriptifs
   - Commentez leur utilisation

2. **Maintenance**
   - Centralisez les messages fréquents
   - Standardisez les récompenses
   - Documentez les séquences complexes

3. **Performance**
   - Évitez les séquences trop longues
   - Préférez les variables aux répétitions
   - Utilisez les variables système avec modération 