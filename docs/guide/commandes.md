# Commandes

PSchedulers offre un ensemble complet de commandes pour gérer vos tâches planifiées. Cette page détaille chaque commande et son utilisation.

## Commande principale

La commande principale du plugin est `/scheduler`, avec les alias `/ps` et `/pschedulers`.

## Liste des commandes

### Création de tâches

```bash
/scheduler create <id> [TYPE:<type>] <params> <command>
```

#### Paramètres
- `id` : Identifiant unique de la tâche
- `type` : Type de planification (INTERVAL, HOURLY, DAILY, WEEKLY)
- `params` : Paramètres spécifiques au type
- `command` : Commande à exécuter

#### Exemples par type

##### INTERVAL
```bash
# Exécute toutes les 5 minutes (6000 ticks)
/scheduler create backup TYPE:INTERVAL 6000 backup world

# Exécute toutes les heures (72000 ticks)
/scheduler create hourly TYPE:INTERVAL 72000 broadcast "Une heure est passée"
```

##### HOURLY
```bash
# Exécute à la 30ème minute de chaque heure
/scheduler create half TYPE:HOURLY 30 broadcast "Nouvelle demi-heure"

# Exécute au début de chaque heure
/scheduler create hour TYPE:HOURLY 0 broadcast "Nouvelle heure"
```

##### DAILY
```bash
# Exécute tous les jours à 9h00
/scheduler create morning TYPE:DAILY 09:00 broadcast "Bonjour à tous"

# Exécute tous les jours à 22h00
/scheduler create night TYPE:DAILY 22:00 broadcast "Bonne nuit"
```

##### WEEKLY
```bash
# Exécute les samedis et dimanches à 10h00
/scheduler create weekend TYPE:WEEKLY SAT,SUN 10:00 broadcast "Bon weekend"

# Exécute du lundi au vendredi à 18h00
/scheduler create workdays TYPE:WEEKLY MON,TUE,WED,THU,FRI 18:00 broadcast "Fin de journée"
```

### Gestion des tâches

#### Suppression
```bash
/scheduler remove <id>
```
Supprime une tâche existante.

#### Démarrage
```bash
/scheduler start <id>
```
Démarre une tâche qui a été arrêtée.

#### Arrêt
```bash
/scheduler stop <id>
```
Arrête une tâche en cours d'exécution.

#### Liste
```bash
/scheduler list
```
Affiche la liste de toutes les tâches.

#### Informations
```bash
/scheduler info <id>
```
Affiche les détails d'une tâche spécifique.

### Administration

#### Rechargement
```bash
/scheduler reload
```
Recharge la configuration du plugin.

#### Version
```bash
/scheduler version
```
Affiche la version du plugin.

## Permissions

| Commande | Permission | Description |
|----------|------------|-------------|
| `/scheduler create` | `pschedulers.admin` | Créer des tâches |
| `/scheduler remove` | `pschedulers.admin` | Supprimer des tâches |
| `/scheduler start` | `pschedulers.admin` | Démarrer des tâches |
| `/scheduler stop` | `pschedulers.admin` | Arrêter des tâches |
| `/scheduler list` | `pschedulers.admin` | Lister les tâches |
| `/scheduler info` | `pschedulers.admin` | Voir les détails d'une tâche |
| `/scheduler reload` | `pschedulers.admin` | Recharger la configuration |
| `/scheduler version` | `pschedulers.admin` | Voir la version du plugin |

## Syntaxe des commandes

### Format général
```
/scheduler <sous-commande> [arguments...]
```

### Formats spéciaux

#### Format des jours (WEEKLY)
- Jours complets : MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
- Abréviations : MON, TUE, WED, THU, FRI, SAT, SUN
- Séparateur : virgule (,)

#### Format de l'heure
- Format 24h : HH:mm
- Exemples : 09:00, 13:30, 22:45

## Messages d'erreur courants

| Message | Cause | Solution |
|---------|-------|----------|
| "Tâche introuvable" | L'ID de la tâche n'existe pas | Vérifiez l'ID avec `/scheduler list` |
| "Type invalide" | Type de planification incorrect | Utilisez un des types valides |
| "Syntaxe invalide" | Arguments manquants ou incorrects | Vérifiez la syntaxe de la commande |
| "Permission manquante" | Permissions insuffisantes | Contactez un administrateur |

## Astuces

1. **Nommage des tâches**
   - Utilisez des noms descriptifs
   - Évitez les caractères spéciaux
   - Préfixez les tâches par catégorie (backup_, broadcast_, etc.)

2. **Gestion des erreurs**
   - Vérifiez les logs en cas d'erreur
   - Testez les commandes avant de les planifier
   - Utilisez le mode debug si nécessaire

3. **Performance**
   - Évitez les intervalles trop courts
   - Limitez le nombre de tâches simultanées
   - Utilisez des commandes optimisées 