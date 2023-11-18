# Electrical_Stations (Gestion des zones de recharge dans une communauté d'agglomération)

* [Introdution](#introduction)
* [Installation](#installation)
* [Phase 1 (produit initial)](#phase-1-produit-initial)
* [Phase 2 (produit final)](#phase-2-produit-final)
* [Utilisation](#utilisation)
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

Pour installer et exécuter le logiciel suivez les étapes ci-dessous :

### Prérequis

* Assurez-vous que votre environnement de développement Java est correctement configuré avec Java Development Kit (JDK) version 17 ou supérieure.
* Vérifiez si JavaFX (version 17 ou supérieure) est installé sur votre système. Si ce n'est pas le cas, téléchargez et configurez JavaFX en suivant les instructions officielles d'Oracle/OpenJFX : [JavaFX - Téléchargement et Configuration](https://openjfx.io/openjfx-docs/).
* JUnit (version 5) doit être configuré dans votre projet. Ajoutez les dépendances nécessaires dans votre fichier de configuration de dépendances (par exemple, pom.xml pour Maven ou build.gradle pour Gradle).

### Téléchargement du code source

Clonez le dépôt GitHub du projet en utilisant la commande suivante dans votre terminal :

```
git clone https://github.com/sepanta007/Electrical_Stations
```

ou téléchargez le code source directement depuis la page du projet.

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

## Code

### Classe CommunauteAgglomeration

Elle est le point central du programme et est responsable de la gestion de la communauté d'agglomération.
Sa fonctionalité principale est de représenter et de manipuler les différentes entités au sein de cette communauté, telles que les villes, les routes et les zones de recharge.

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
