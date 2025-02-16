# PSchedulers - Plugin de planification avancé pour Paper/Folia

[![GitHub Release](https://img.shields.io/github/v/release/pingermod/pschedulers)](https://github.com/pingermod/pschedulers/releases/latest)
[![GitHub License](https://img.shields.io/github/license/pingermod/pschedulers)](https://github.com/pingermod/pschedulers/blob/main/LICENSE)
[![Discord](https://img.shields.io/discord/1164397902723022918)](https://discord.gg/pingermod)

PSchedulers est un plugin Minecraft puissant et flexible permettant de planifier l'exécution automatique de commandes selon différents types de planification. Compatible avec Paper et Folia, il offre une gestion avancée des tâches planifiées avec support des fuseaux horaires et des régions.

## 🌟 Caractéristiques

- **Types de planification multiples** :
  - ⏱️ Intervalle régulier (toutes les X secondes/minutes/heures)
  - 🕐 Horaire (à une minute précise chaque heure)
  - 📅 Quotidien (à une heure précise chaque jour)
  - 📆 Hebdomadaire (certains jours à une heure précise)

- **Compatibilité étendue** :
  - ✅ Support complet de Paper
  - ✅ Support natif de Folia avec gestion des régions
  - ✅ Compatible Java 17+

- **Fonctionnalités avancées** :
  - 🔄 Rechargement à chaud de la configuration
  - 📍 Support des tâches liées à une localisation
  - 🔒 Système de permissions intégré
  - 💬 Messages entièrement personnalisables

## 📋 Commandes

### Commande principale : `/scheduler` (alias: `/ps`, `/pschedulers`)

| Commande | Permission | Description |
|----------|------------|-------------|
| `/scheduler create <id> [TYPE:<type>] <params> <command>` | `pschedulers.admin` | Crée une nouvelle tâche planifiée |
| `/scheduler remove <id>` | `pschedulers.admin` | Supprime une tâche existante |
| `/scheduler start <id>` | `pschedulers.admin` | Démarre une tâche |
| `/scheduler stop <id>` | `pschedulers.admin` | Arrête une tâche |
| `/scheduler list` | `pschedulers.admin` | Liste toutes les tâches |
| `/scheduler reload` | `pschedulers.admin` | Recharge la configuration |

## 🎯 Types de planification

### 1. Intervalle (INTERVAL)
Exécute une commande à intervalles réguliers.

```bash
/scheduler create backup TYPE:INTERVAL 6000 backup world
```
- `6000` : Intervalle en ticks (20 ticks = 1 seconde)
- Exemple : Toutes les 5 minutes (6000 ticks)

### 2. Horaire (HOURLY)
Exécute une commande à une minute précise chaque heure.

```bash
/scheduler create broadcast TYPE:HOURLY 30 broadcast "Nouvelle demi-heure !"
```
- `30` : Minute de l'heure (0-59)
- Exemple : À la 30ème minute de chaque heure

### 3. Quotidien (DAILY)
Exécute une commande à une heure précise chaque jour.

```bash
/scheduler create morning TYPE:DAILY 09:00 broadcast "Bonjour à tous !"
```
- `09:00` : Heure au format HH:mm (24h)
- Exemple : Tous les jours à 9h00

### 4. Hebdomadaire (WEEKLY)
Exécute une commande certains jours de la semaine à une heure précise.

```bash
/scheduler create weekend TYPE:WEEKLY SAT,SUN 10:00 broadcast "Bon weekend !"
```
- `SAT,SUN` : Jours de la semaine (MON,TUE,WED,THU,FRI,SAT,SUN)
- `10:00` : Heure au format HH:mm (24h)
- Exemple : Les samedis et dimanches à 10h00

## ⚙️ Configuration

### config.yml
```yaml
# PSchedulers Configuration
tasks:
  example_interval:
    command: "broadcast Hello World!"
    type: INTERVAL
    interval: 6000  # 5 minutes
    enabled: false

  example_hourly:
    command: "broadcast It's a new hour!"
    type: HOURLY
    minute: 0
    enabled: false

  example_daily:
    command: "broadcast Good morning!"
    type: DAILY
    time: "09:00"
    enabled: false

  example_weekly:
    command: "broadcast Weekly maintenance!"
    type: WEEKLY
    days: [MONDAY, WEDNESDAY, FRIDAY]
    time: "20:00"
    enabled: false
```

## 🔧 Installation

1. Téléchargez le fichier JAR depuis la page des releases
2. Placez le fichier dans le dossier `plugins` de votre serveur
3. Redémarrez le serveur ou utilisez un gestionnaire de plugins
4. La configuration par défaut sera générée automatiquement

## 📝 Permissions

- `pschedulers.admin` : Accès à toutes les commandes du plugin
  - Par défaut : Opérateurs uniquement

## 🌐 Compatibilité Folia

PSchedulers supporte nativement Folia avec :
- Gestion intelligente des régions
- Optimisation des performances
- Exécution thread-safe des commandes

Pour les serveurs Folia, les tâches peuvent être liées à des régions spécifiques en les créant à l'emplacement d'un joueur :
```bash
# En tant que joueur, la tâche sera liée à votre position
/scheduler create region_task TYPE:INTERVAL 1200 broadcast "Message régional"
```

## 🤝 Support et Contribution

- 🐛 Signalement de bugs : [GitHub Issues](https://github.com/pingermod/pschedulers/issues)
- 💡 Suggestions : [GitHub Discussions](https://github.com/pingermod/pschedulers/discussions)
- 🔧 Contributions : [Pull Requests](https://github.com/pingermod/pschedulers/pulls) bienvenues

## 📜 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails. 