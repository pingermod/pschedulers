# Guide de contribution

Merci de votre intérêt pour contribuer à PSchedulers ! Ce guide vous aidera à comprendre comment participer au développement du projet.

## Table des matières

1. [Prérequis](#prérequis)
2. [Configuration de l'environnement](#configuration-de-lenvironnement)
3. [Structure du projet](#structure-du-projet)
4. [Workflow de développement](#workflow-de-développement)
5. [Standards de code](#standards-de-code)
6. [Tests](#tests)
7. [Soumission de modifications](#soumission-de-modifications)
8. [Documentation](#documentation)

## Prérequis

- Java 17 ou supérieur
- Maven 3.6.0 ou supérieur
- Git
- IDE (IntelliJ IDEA recommandé)
- Compte GitHub

## Configuration de l'environnement

1. **Fork du projet**
```bash
# Cloner votre fork
git clone https://github.com/votre-username/pschedulers.git
cd pschedulers

# Ajouter le dépôt upstream
git remote add upstream https://github.com/pingermod/pschedulers.git
```

2. **Configuration Maven**
```bash
# Compiler le projet
mvn clean install

# Exécuter les tests
mvn test
```

3. **Configuration IDE**
- Importer le projet comme projet Maven
- Configurer Java 17
- Installer les plugins recommandés

## Structure du projet

```
pschedulers/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── me/
│   │   │       └── pinger/
│   │   │           └── pschedulers/
│   │   │               ├── api/
│   │   │               ├── command/
│   │   │               ├── config/
│   │   │               ├── scheduler/
│   │   │               └── task/
│   │   └── resources/
│   └── test/
├── docs/
├── pom.xml
└── README.md
```

### Description des packages

- `api/` : Classes d'API publique
- `command/` : Gestionnaire de commandes
- `config/` : Gestion de la configuration
- `scheduler/` : Cœur du système de planification
- `task/` : Implémentation des tâches

## Workflow de développement

1. **Créer une branche**
```bash
# Mettre à jour master
git checkout master
git pull upstream master

# Créer une branche pour la fonctionnalité
git checkout -b feature/ma-fonctionnalite
```

2. **Développer**
- Écrire le code
- Ajouter des tests
- Mettre à jour la documentation

3. **Commit**
```bash
# Ajouter les modifications
git add .

# Commit avec un message descriptif
git commit -m "feat: ajout de la fonctionnalité X"
```

4. **Push et Pull Request**
```bash
# Push vers votre fork
git push origin feature/ma-fonctionnalite

# Créer une Pull Request sur GitHub
```

## Standards de code

### Style de code

```java
// Bon style
public class TaskManager {
    private final Map<String, ScheduledTask> tasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
    }

    public void addTask(String id, ScheduledTask task) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(task, "Task cannot be null");
        
        tasks.put(id, task);
    }
}

// Mauvais style
public class taskManager {
    private Map<String,ScheduledTask> Tasks;
    public taskManager() {
        Tasks=new HashMap<>();
    }
    public void AddTask(String id,ScheduledTask task) {
        Tasks.put(id,task);
    }
}
```

### Conventions de nommage

- Classes : PascalCase (`TaskManager`)
- Méthodes : camelCase (`addTask`)
- Variables : camelCase (`scheduledTask`)
- Constantes : SNAKE_CASE (`MAX_TASKS`)

### Messages de commit

Format : `type: description`

Types :
- `feat` : Nouvelle fonctionnalité
- `fix` : Correction de bug
- `docs` : Documentation
- `style` : Formatage
- `refactor` : Refactoring
- `test` : Tests
- `chore` : Maintenance

Exemple :
```
feat: ajout du support des tâches hebdomadaires
fix: correction du calcul de la prochaine exécution
docs: mise à jour de la documentation API
```

## Tests

### Tests unitaires
```java
@Test
public void testTaskCreation() {
    TaskManager manager = new TaskManager();
    ScheduledTask task = new ScheduledTask("test");
    
    manager.addTask("test", task);
    
    assertNotNull(manager.getTask("test"));
    assertEquals(task, manager.getTask("test"));
}
```

### Tests d'intégration
```java
@Test
public void testTaskExecution() {
    PSchedulers scheduler = PSchedulers.getInstance();
    ScheduleConfig config = ScheduleConfig.interval(20);
    
    scheduler.createTask("test", "say Hello", config);
    
    // Attendre l'exécution
    Thread.sleep(30);
    
    assertTrue(scheduler.isTaskCompleted("test"));
}
```

## Soumission de modifications

### Checklist Pull Request

1. **Description**
   - Expliquer les changements
   - Référencer les issues liées
   - Décrire les tests effectués

2. **Tests**
   - Tous les tests passent
   - Nouveaux tests ajoutés
   - Couverture de code maintenue

3. **Documentation**
   - JavaDoc mis à jour
   - README mis à jour si nécessaire
   - Exemples ajoutés si pertinent

4. **Code**
   - Suit les standards
   - Pas de code mort
   - Optimisé et propre

### Exemple de Pull Request

```markdown
## Description
Ajout du support des tâches hebdomadaires avec validation des jours et heures.

Fixes #123

## Changements
- Ajout de `WeeklyScheduleConfig`
- Validation des jours de la semaine
- Tests unitaires pour la validation
- Documentation mise à jour

## Tests
- [x] Tests unitaires
- [x] Tests d'intégration
- [x] Test manuel sur Paper et Folia

## Documentation
- [x] JavaDoc
- [x] [Guide utilisateur](https://github.com/pingermod/pschedulers/wiki)
- [x] Exemples d'utilisation
```

## Documentation

### JavaDoc

```java
/**
 * Gère l'exécution des tâches planifiées.
 * 
 * @since 1.0.0
 */
public class TaskManager {
    /**
     * Crée une nouvelle tâche planifiée.
     *
     * @param id     Identifiant unique de la tâche
     * @param task   Tâche à planifier
     * @throws IllegalArgumentException si l'ID existe déjà
     * @throws NullPointerException si id ou task est null
     */
    public void addTask(String id, ScheduledTask task) {
        // Implementation
    }
}
```

### Documentation utilisateur

1. **README.md**
   - Vue d'ensemble
   - Installation rapide
   - Exemples simples
   - Liens vers la documentation

2. **Wiki**
   - Guides détaillés
   - Tutoriels
   - FAQ
   - Exemples avancés

3. **Javadoc**
   - Documentation technique
   - Référence API
   - Exemples de code 