# Installation

## Prérequis

Avant d'installer PSchedulers, assurez-vous d'avoir :

- Un serveur Minecraft fonctionnant sous Paper ou Folia
- Java 17 ou supérieur
- Les permissions nécessaires pour installer des plugins

## Étapes d'installation

### 1. Téléchargement

1. Rendez-vous sur la [page des releases](https://github.com/pingermod/pschedulers/releases)
2. Téléchargez la dernière version de `pschedulers-x.x.x.jar`

### 2. Installation du plugin

1. Arrêtez votre serveur si celui-ci est en cours d'exécution
2. Accédez au dossier `plugins` de votre serveur
3. Copiez le fichier JAR téléchargé dans ce dossier
4. Démarrez votre serveur

### 3. Vérification

Après le démarrage du serveur :

1. Vérifiez les logs du serveur pour confirmer que PSchedulers s'est correctement chargé
2. Exécutez la commande `/scheduler` ou `/ps` pour vérifier que le plugin répond
3. Consultez le dossier `plugins/PSchedulers` pour voir les fichiers de configuration générés

## Configuration initiale

Lors du premier démarrage, PSchedulers va créer :

- `config.yml` : Configuration principale du plugin
- `tasks.yml` : Configuration des tâches planifiées
- `messages.yml` : Messages personnalisables

### Exemple de configuration minimale

```yaml
# config.yml
debug: false
timezone: "Europe/Paris"

# tasks.yml
tasks:
  example:
    command: "broadcast Test"
    type: INTERVAL
    interval: 6000
    enabled: false
```

## Résolution des problèmes courants

### Le plugin ne se charge pas

1. Vérifiez que vous utilisez Java 17+
2. Assurez-vous d'utiliser Paper ou Folia
3. Consultez les logs pour voir les erreurs éventuelles

### Les commandes ne fonctionnent pas

1. Vérifiez que vous avez la permission `pschedulers.admin`
2. Assurez-vous que la syntaxe de la commande est correcte
3. Consultez le fichier de log pour plus de détails

## Support

Si vous rencontrez des problèmes :

1. Consultez notre [FAQ](https://github.com/pingermod/pschedulers/wiki/FAQ)
2. Ouvrez une [issue sur GitHub](https://github.com/pingermod/pschedulers/issues)
3. Rejoignez notre [Discord](https://discord.gg/pingermod) pour de l'aide

## Mise à jour

Pour mettre à jour PSchedulers :

1. Arrêtez votre serveur
2. Sauvegardez vos fichiers de configuration
3. Remplacez l'ancien fichier JAR par la nouvelle version
4. Redémarrez votre serveur

Les fichiers de configuration existants seront préservés lors de la mise à jour. 