# Electrical_Stations (Gestion des zones de recharge dans une communauté d'agglomération)

* [Introduction](#introduction)
* [Installation](#installation)
* [Compilation](#compilation)
* [Execution](#execution)
* [Phase 1 (produit initial)](#phase-1-produit-initial)
* [Phase 2 (produit final)](#phase-2-produit-final)
* [Utilisation](#utilisation)
* [Fichier de configuration](#fichier-de-configuration)
* [Code](#code)
* [Remarques](#remarques)
* [Licence](#licence)
* [Contributeurs](#contributeurs)

## Introduction

Ce projet vise à développer un logiciel pour aider les communautés d'agglomération à déterminer stratégiquement l'emplacement des bornes de recharge pour véhicules électriques.
Les contraintes majeures sont l'accessibilité et l'optimisation des coûts. 
Le programme permettra à l'utilisateur de configurer manuellement les zones de recharge ou de laisser le logiciel résoudre automatiquement le problème. 
De plus, des fonctionnalités de sauvegarde et de chargement de configurations seront intégrées pour une gestion flexible des zones de recharge.

## Installation

Pour installer et exécuter le logiciel, suivez les étapes ci-dessous :

### Prérequis

* Assurez-vous que votre environnement de développement Java est correctement configuré avec Java Development Kit (JDK) version 17 ou supérieure.
* Vérifiez si JavaFX (version 17 ou supérieure) est installé sur votre système. Si ce n'est pas le cas, téléchargez et configurez JavaFX en suivant les instructions officielles d'Oracle/OpenJFX : [JavaFX - Téléchargement et Configuration](https://openjfx.io/openjfx-docs/).
* JUnit (version 5) doit être configuré dans votre projet. Ajoutez les dépendances nécessaires dans votre fichier de configuration de dépendances (par exemple, pom.xml pour Maven ou build.gradle pour Gradle).

### Téléchargement du code source

Clonez le dépôt GitHub du projet en utilisant la commande suivante dans votre terminal :

```
git clone https://github.com/sepanta007/Electrical_Stations
```

Ou téléchargez le code source directement depuis la page du projet.

## Compilation

Note : Sur les systèmes Windows, utilisez le backslash (\) comme séparateur de chemin au lieu de slash (/)
pour toutes les commandes que vous retrouvez dans ce document.

### Compilation de la Phase 1 (produit initial)

Pour compiler la phase 1, utilisez la commande suivante dans le répertoire racine du projet :

```bash
javac src/phase1/*.java -d out/production/Electrical_Stations
```

Pour compiler la phase 2, utilisez la commande suivante dans le répertoire racine du projet :

```bash
javac src/phase2/*.java -d out/production/Electrical_Stations
```

Pour compiler l'application JavaFX, utilisez la commande suivante dans le répertoire racine du projet :

```bash
javac --add-modules javafx.controls,javafx.fxml,javafx.chart -d out/production/Electrical_Stations src/application/Main.java
```

Note : Si JavaFX n'est pas installé de manière globale sur le système et que les modules nécessaires ne sont pas accessibles sans spécifier un chemin personnalisé,
utilisez la commande suivante dans le répertoire racine du projet : 

```bash
javac --module-path <chemin_vers_javafx_sdk>/lib --add-modules javafx.controls,javafx.fxml,javafx.chart -d out/production/Electrical_Stations src/application/Main.java
```

Notez bien que vous devez remplacer <chemin_vers_javafx_sdk> par le chemin réel vers votre JavaFX SDK.

## Execution

Pour exécuter la phase 1, utilisez la commande suivante dans le répertoire racine du projet :

```bash
java -cp out/production/Electrical_Stations phase1.Main
```

Pour exécuter la phase 2, utilisez la commande suivante dans le répertoire racine du projet :

```bash
java -cp out/production/Electrical_Stations phase2.Main chemin/vers/fichier/nom_fichier.txt
```

Notez bien que vous devez remplacer "chemin/vers/fichier/nom_fichier.txt" par le chemin absolu ou relatif vers votre fichier texte représentant la communauté d'agglomération.

Pour exécuter l'application JavaFX, utilisez la commande suivante dans le répertoire racine du projet :

```bash
java -cp out/production/Electrical_Stations application.Main
```

## Phase 1 (produit initial)

### Configuration manuelle des zones de recharge (le répertoire [phase1](https://github.com/sepanta007/Electrical_Stations/tree/master/src/phase1))

Au démarrage, l'utilisateur est invité à spécifier le nombre de villes dans la communauté.
Les villes sont automatiquement nommées de A à Z.
Ensuite, l'utilisateur peut ajouter des routes entre les villes.
Une fois la configuration terminée, l'utilisateur peut gérer les zones de recharge manuellement en ajoutant, retirant, ou visualisant les zones de recharge dans chaque ville.

### Options du menu

* Ajouter une route : Permet à l'utilisateur d'ajouter une route entre deux villes.
* Fin : Termine la configuration manuelle de la communauté. 
* Gestion des zones de recharge
  * Ajout : L'utilisateur peut ajouter une zone de recharge dans une ville.
  * Retrait : L'utilisateur peut retirer une zone de recharge d'une ville (sous réserve de contraintes).
  * Visualisation : Afficher la liste des villes avec des zones de recharge.

## Phase 2 (produit final)

### Entrées-sorties (les répertoires [phase2](https://github.com/sepanta007/Electrical_Stations/tree/master/src/phase2), [application](https://github.com/sepanta007/Electrical_Stations/tree/master/src/application) pour JavaFX et [test](https://github.com/sepanta007/Electrical_Stations/tree/master/src/test) pour JUnit)

Cette phase vise à permettre à l'utilisateur de représenter une communauté d'agglomération via un fichier texte, évitant ainsi la saisie manuelle à chaque utilisation.

### Options du menu

* Affichage graphique (pour JavaFX) : Visualiser graphiquement l'état de charge des villes (le nombre de villes rechargées avec ou sans source de recharge et le nombre de villes non rechargées).
* Résoudre manuellement : L'utilisateur peut configurer les zones de recharge comme dans la phase 1.
* Résoudre automatiquement : Appliquer un algorithme pour résoudre le problème automatiquement.
* Sauvegarde : Enregistrer la configuration actuelle dans un fichier texte.
* Fin : Quitter le programme.

## Utilisation

Pour la [phase 2](#phase-2-produit-final), le programme prend en entrée le chemin vers un fichier texte représentant la communauté d'agglomération.
Les paramètres de la ligne de commande sont utilisés via String[] args dans la méthode main.
Le fichier d'entrée suit les spécifications du projet. 
Le programme permet également d'enregistrer les opérations effectuées dans un fichier de sortie.
Elle contient les résultats ou la configuration actuelle, selon le contexte de l'exécution.

## Fichier de configuration

Le fichier de configuration doit respecter une structure spécifique pour être interprété correctement par le programme. 
Chaque commande doit être placée sur une ligne distincte,
et aucune espace supplémentaire entre les caractères de chaque commande n'est autorisée entre les arguments des commandes.
Ainsi chaque commande doit se terminer par un point. Voici la structure générale attendue :

```text
ville(NOM).
route(VILLE_A,VILLE_B).
recharge(VILLE_X).
```

### Description des commandes

* `ville(NOM)`: Définit une ville avec le nom spécifié.
* `route(VILLE_A,VILLE_B)`: Définit une route entre les deux villes spécifiées.
* `recharge(VILLE_X)`: Indique qu'il y a une zone de recharge dans la ville spécifiée.

Note : Comme précisé précédemment, aucune espace n'est autorisé entre les arguments, par exemple, utilisez `route(VILLE_A,VILLE_B)` et non pas `route(VILLE_A, VILLE_B)`.

Assurez-vous de suivre scrupuleusement cette structure pour que le programme puisse interpréter 
correctement le fichier de configuration.

Exemple du fichier :

```text
ville(A).
ville(B).
route(A,B).
recharge(A).
```

## Code

La classe `CommunauteAgglomeration` est le point central du programme et est responsable de la gestion de la communauté d'agglomération.
Sa fonctionnalité principale est de représenter et de manipuler les différentes entités au sein de cette communauté, telles que les villes, les routes et les zones de recharge.

## Remarques

1. La solution naïve est utilisée si le fichier ne décrit aucune zone de recharge ou ne respecte pas l'accessibilité.
2. L'algorithme automatique démarre avec la solution naïve, sauf s'il existe une configuration valide dans le fichier.
3. Les classes Graphe, Ville, et Route interagissent de manière complémentaire pour représenter et gérer la communauté d'agglomération.
4. Les fonctionnalités d'entrées-sorties utilisent des classes Java standard comme Scanner et FileWriter pour assurer la manipulation correcte des fichiers et des données.
5. L'architecture du code permet une gestion modulaire et extensible des fonctionnalités, facilitant l'ajout d'algorithmes plus avancés pour la résolution automatique du problème.

## Licence

Elecrical_Stations est sous licence [MIT](https://github.com/sepanta007/Electrical_Stations/blob/master/LICENSE.md).

## Contributeurs

* [Sepanta Farzollahi](https://github.com/sepanta007) 
* [Hagop Hannachian](https://github.com/hagop-h)
