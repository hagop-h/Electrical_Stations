package phase2;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * La classe CommunauteAgglomeration représente une communauté d'agglomération comprenant des villes, des routes et des zones de recharge
 * Elle utilise un graph pour représenter la connectivité entre les villes
 */
public class CommunauteAgglomeration {
    private Set<Ville> villes; // Ensemble de villes dans une communauté
    private Set<Route> routes; // Ensemble de routes entre les villes
    private List<ZoneRecharge> zonesRecharge; // Liste de zones de recharge présentes dans une communauté
    private Graphe graphe; // Graph représentant la connectivité entre les villes

    /**
     * Constructeur de la classe CommunauteAgglomeration
     * Initialise les collections de villes, de routes, de zones de recharge, et le graphe
     */
    public CommunauteAgglomeration() {
        villes = new HashSet<>();
        routes = new HashSet<>();
        zonesRecharge = new ArrayList<>();
        graphe = new Graphe();
    }

    /**
     * Lit un entier depuis le scanner tout en gérant les exceptions d'entrée incorrecte
     *
     * @param scanner Le scanner utilisé pour la lecture
     * @return L'entier lu depuis le scanner
     */
    public int lireEntier(Scanner scanner) {
        while (true) {
            try {
                // Tenter de lire un entier
                int result = scanner.nextInt();
                // Consommer le caractère de nouvelle ligne restant
                scanner.nextLine();
                // Retourner le résultat si la lecture réussit
                return result;
            } catch (java.util.InputMismatchException e) {
                // Gèrer l'exception si l'entrée n'est pas un entier
                System.out.println("Veuillez entrer un nombre entier.");
                // Consommer la ligne incorrecte pour éviter une boucle infinie
                scanner.nextLine();
            }
        }
    }

    /**
     * Recherche et renvoie une ville par son nom
     *
     * @param nom Le nom de la ville à rechercher
     * @return La ville correspondant au nom ou null si aucune correspondance n'est trouvée
     */
    public Ville trouverVilleParNom(String nom) {
        return villes.stream().filter(ville -> ville.getNom().equalsIgnoreCase(nom)).findFirst().orElse(null);
    }

    /**
     * Permet à l'utilisateur de trouver manuellement une solution en ajoutant ou retirant des zones de recharge
     * Affiche le menu interactif pour la gestion manuelle des zones de recharge
     */
    public void trouverSolutionManuelle() {
        Scanner scanner = new Scanner(System.in);
        int choixMenu;
        // Générer une solution initiale si la liste des chargeurs est vide
        if (zonesRecharge.isEmpty()) {
            genererSolutionInitiale();
        }
        // Boucle pour la gestion manuelle
        do {
            System.out.println("\nScore : "+score()+"\n");
            afficherVillesAvecOuSansRecharge(); // Afficher l'état actuel des villes avec ou sans recharge
            afficherMenuSolutionManuelle(); // Afficher le menu pour la gestion manuelle
            choixMenu = lireEntier(scanner); // Lire le choix de l'utilisateur
            // Effectuer l'action correspondante au choix de l'utilisateur
            switch (choixMenu) {
                case 1:
                    ajouterZoneRechargeMenu(scanner);
                    break;
                case 2:
                    retirerZoneRechargeMenu(scanner);
                    break;
                case 3:
                    System.out.println("\nFin de la gestion manuelle.\n");
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 3); // Continuer jusqu'à ce que l'utilisateur choisisse la fin de la gestion manuelle
    }

    /**
     * Ajoute une route entre deux villes à partir de leurs noms
     * Recherche les villes correspondantes par leur nom et ajoute la route si les deux villes existent
     *
     * @param nomVilleA Nom de la première ville
     * @param nomVilleB Nom de la deuxième ville
     */
    public void ajouterRoute(String nomVilleA, String nomVilleB) {
        // Rechercher des objets Ville correspondants aux noms fournis
        Ville villeA = trouverVilleParNom(nomVilleA);
        Ville villeB = trouverVilleParNom(nomVilleB);
        // Vérification de l'existence des deux villes
        if (villeA != null && villeB != null) {
            ajouterRoute(villeA, villeB); // Ajout de la route entre les deux villes
        } else {
            System.out.println("Villes non trouvées. Veuillez réessayer."); // Affichage d'un message d'erreur si l'une ou les deux villes ne sont pas trouvées
        }
    }

    /**
     * Ajuste les zones de recharge connectées à une ville donnée
     * Parcourt les routes de la communauté d'agglomération
     * et ajoute des zones de recharge aux villes connectées qui n'ont pas encore de zone de recharge
     *
     * @param ville Ville pour laquelle ajuster les zones de recharge connectées
     */
    public void ajusterRechargeConnectees(Ville ville) {
        // Parcourir les routes de la communauté d'agglomération
        for (Route route : routes) {
            // Vérifier si la ville en paramètre est la ville de départ de la route
            if (route.getVilleA().equals(ville) && !contientRecharge(route.getVilleB())) {
                zonesRecharge.add(new ZoneRecharge(route.getVilleB())); // Ajouter une zone de recharge à la ville connectée si elle n'a pas de zone de recharge
            } else if (route.getVilleB().equals(ville) && !contientRecharge(route.getVilleA())) {
                zonesRecharge.add(new ZoneRecharge(route.getVilleA())); // Ajouter une zone de recharge à la ville connectée si elle n'a pas de zone de recharge
            }
        }
    }

    /**
     * Affiche le menu pour la gestion manuelle des zones de recharge
     * Propose des options pour ajouter ou retirer une zone de recharge, ainsi que pour terminer la gestion manuelle
     */
    public void afficherMenuSolutionManuelle() {
        System.out.println("\nMenu :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Revenir");
        System.out.print("\nVotre choix :\n");
    }

    /**
     * Affiche les informations sur les villes rechargées, avec ou sans leur propre zone de recharge, ainsi que les villes non rechargées
     * Utilise les listes de zones de recharge pour distinguer les différentes catégories
     */
    public void afficherVillesAvecOuSansRecharge() {
        // Affichage des villes rechargées avec leurs propres zones de recharge
        System.out.println("\nVilles rechargées avec leurs propres zone de recharge :");
        for (ZoneRecharge zoneRecharge : getVillesAvecSourceRecharge()) {
            System.out.println("- " + zoneRecharge.getVille().getNom());
        }
        // Affichage des villes rechargées sans leurs propres zones de recharge
        System.out.println("\nVilles rechargées sans leurs propres zone de recharge :");
        for (ZoneRecharge zoneRecharge : getVillesRechargeesSansSource()) {
            System.out.println("- " + zoneRecharge.getVille().getNom());
        }
        // Affichage des villes non rechargées
        System.out.println("\nVilles non rechargées :");
        for (Ville ville : getVillesSansZoneRecharge()) {
            System.out.println("- " + ville.getNom());
        }
    }

    /**
     * Sauvegarde la solution actuelle dans un fichier spécifié
     * Les informations sur les villes et les routes sont sauvegardées, ainsi que la catégorisation des villes en fonction de leur zone de recharge
     * Ajoute également le score de la solution
     *
     * @param cheminFichier Le chemin du fichier où sauvegarder la solution
     */
    public void sauvegarderSolution(String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            // Sauvegarder les informations sur les villes
            for (Ville ville : villes) {
                writer.write(ville.getNom() + " " + ville.getzoneDeRecharge());
                writer.newLine();
            }
            // Sauvegarder les informations sur les routes
            for (Route route : routes) {
                writer.write("route " + route.getVilleA().getNom() + " " + route.getVilleB().getNom());
                writer.newLine();
            }
            // Sauvegarder les villes rechargées avec leurs propres zones de recharge
            writer.write("\nVilles rechargées avec leurs propres zone de recharge :");
            writer.newLine();
            for (ZoneRecharge zoneRecharge : getVillesAvecSourceRecharge()) {
                writer.write("- " + zoneRecharge.getVille().getNom());
                writer.newLine();
            }
            // Sauvegarder les villes rechargées sans leurs propres zones de recharge
            writer.write("\nVilles rechargées sans leurs propres zone de recharge :");
            writer.newLine();
            for (ZoneRecharge zoneRecharge : getVillesRechargeesSansSource()) {
                writer.write("- " + zoneRecharge.getVille().getNom());
                writer.newLine();
            }
            // Sauvegarder les villes non rechargées
            writer.write("\nVilles non rechargées :");
            writer.newLine();
            for (Ville ville : getVillesSansZoneRecharge()) {
                writer.write("- " + ville.getNom());
                writer.newLine();
            }
            // Sauvegarder le score
            writer.write("\nScore : "+ score());
            writer.newLine();
            System.out.println("\nSolution sauvegardée dans : " + cheminFichier + "\n");
        } catch (IOException e) {
            System.err.println("\nErreur lors de la sauvegarde de la solution : " + e.getMessage() + "\n");
        }
    }

    /**
     * Récupère la liste des villes rechargées avec leur propre zone de recharge
     *
     * @return Liste des objets zones de recharge représentant les villes avec leur propre zone de recharge
     */
    public List<ZoneRecharge> getVillesAvecSourceRecharge() {
        // Filtrage des zones de recharge qui sont des sources
        return zonesRecharge.stream().filter(ZoneRecharge::estSourceRecharge).collect(Collectors.toList());
    }

    /**
     * Récupère la liste des villes rechargées sans leur propre zone de recharge
     *
     * @return Liste des objets zones de recharge représentant les villes rechargées sans leur propre zone de recharge
     */
    public List<ZoneRecharge> getVillesRechargeesSansSource() {
        // Filtrage des zones de recharge qui ne sont pas des sources
        return zonesRecharge.stream().filter(zoneRecharge -> !zoneRecharge.estSourceRecharge()).collect(Collectors.toList());
    }

    /**
     * Récupère la liste des villes sans zone de recharge propre et sans zone de recharge connectée
     *
     * @return Liste des objets Ville représentant les villes sans zone de recharge propre et sans zone de recharge connectée
     */
    public List<Ville> getVillesSansZoneRecharge() {
        List<Ville> villesSansZoneRecharge = new ArrayList<>(); // Initialisation d'une liste pour stocker les villes sans zone de recharge
        // Parcourir de la liste des villes
        for (Ville ville : villes) {
            // Vérification si la ville n'a pas de zone de recharge et que la zone de recharge est désactivée
            if (!contientRecharge(ville) && !ville.getzoneDeRecharge()) {
                // Ajout de la ville à la liste si elle n'est pas déjà présente
                if (!villesSansZoneRecharge.contains(ville)) {
                    villesSansZoneRecharge.add(ville);
                }
            }
        }
        return villesSansZoneRecharge; // Retour de la liste des villes sans zone de recharge
    }

    /**
     * Menu interactif pour ajouter une zone de recharge à une ville
     * Demande le nom de la ville, recherche la ville correspondante, puis ajoute la zone de recharge
     * Affiche un message approprié selon le résultat
     *
     * @param scanner Objet Scanner pour la saisie utilisateur
     */
    public void ajouterZoneRechargeMenu(Scanner scanner) {
        System.out.println("\nVeuillez entrer le nom de la ville où ajouter une zone de recharge :"); // Affichage du message d'invite
        String nomVille = scanner.nextLine(); // Lecture du nom de la ville depuis l'entrée utilisateur
        Ville ville = trouverVilleParNom(nomVille); // Rechercher la ville par son nom
        if (ville != null) {
            // Vérification si la ville n'a pas déjà une zone de recharge
            if (!ville.getzoneDeRecharge() ) {
                ville.setzoneDeRechargeTrue(); // Ajout de la zone de recharge à la ville
                // Vérification si la zone de recharge n'existe pas déjà
                if(!contientRecharge(ville)) {
                    zonesRecharge.add(new ZoneRecharge(ville)); // Ajout d'une nouvelle zone de recharge à la liste
                }
                ajusterRechargeConnectees(ville); // Ajustement des recharge connectées
                // Vérification de la contrainte d'accessibilité
                if (respecteContrainte(ville)) {
                    System.out.println("\nZone de recharge ajoutée à " + ville.getNom() + ".");
                } else {
                    // Retrait de la zone de recharge en cas de violation de la contrainte
                    zonesRecharge.removeIf(parking -> parking.getVille().equals(ville));
                    System.out.println("\nImpossible d'ajouter la zone de recharge à " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
                }
            } else {
                System.out.println("\nIl y a déjà une zone de recharge dans " + ville.getNom() + ".");
            }
        } else {
            System.out.println("\nVille non trouvée. Veuillez réessayer.");
        }
    }

    /**
     * Ajoute une zone de recharge à la ville spécifiée, met à jour la liste des zones de recharge
     * et ajuste les zones de recharge connectées. Affiche un message approprié selon le résultat
     *
     * @param nomVille Le nom de la ville où ajouter une zone de recharge
     */
    public void recharge(String nomVille) {
        Ville ville = trouverVilleParNom(nomVille); // Rechercher la ville par son nom
        if (ville != null) {
            // Vérification si la ville n'a pas déjà une zone de recharge
            if (!ville.getzoneDeRecharge() ) {
                ville.setzoneDeRechargeTrue();  // Ajout de la zone de recharge à la ville
                // Vérification si la zone de recharge n'existe pas déjà
                if(!contientRecharge(ville)) {
                    zonesRecharge.add(new ZoneRecharge(ville)); // Ajout d'une nouvelle zone de recharge à la liste
                }
                ajusterRechargeConnectees(ville); // Ajustement des recharge connectées
                // Vérification de la contrainte d'accessibilité
                if (respecteContrainte(ville)) {
                    System.out.println("\nZone de recharge ajoutée à " + ville.getNom() + ".");
                } else {
                    // Retrait de la zone de recharge en cas de violation de la contrainte
                    zonesRecharge.removeIf(parking -> parking.getVille().equals(ville));
                    System.out.println("\nImpossible d'ajouter la zone de recharge à " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
                }
            } else {
                System.out.println("\nIl y a déjà une zone de recharge dans " + ville.getNom() + ".");
            }
        } else {
            System.out.println("\nVille non trouvée. Veuillez réessayer.");
        }
    }

    /**
     * Ajoute une nouvelle route entre deux villes spécifiées, met à jour la liste des routes
     * et la représentation du graphe associé
     *
     * @param villeA Première ville de la route
     * @param villeB Deuxième ville de la route
     */
    public void ajouterRoute(Ville villeA, Ville villeB) {
        if (villeA != null && villeB != null) {
            Route route = new Route(villeA, villeB); // Création d'une nouvelle route
            routes.add(route); // Ajout de la route à la liste des routes
            graphe.addEdge(villeA.getNom(), villeB.getNom()); // Mise à jour de la représentation du graphe
        } else {
            System.out.println("Villes non trouvées. Veuillez réessayer.");
        }
    }

    /**
     * Ajoute une nouvelle ville à la communauté d'agglomération
     *
     * @param ville Ville à ajouter
     */
    public void ajouterVille(Ville ville) {
        villes.add(ville); // Ajout de la ville à la liste des villes
        graphe.addVertex(ville.getNom()); // Mise à jour du graphe avec le nom de la ville comme un nouveau sommet
    }

    /**
     * Permet de retirer une zone de recharge d'une ville spécifiée
     * Utilise un objet Scanner pour récupérer le nom de la ville depuis l'entrée utilisateur
     * Affiche des messages appropriés en fonction du résultat de l'opération
     *
     * @param scanner Objet Scanner pour récupérer l'entrée utilisateur
     */
    public void retirerZoneRechargeMenu(Scanner scanner) {
        // Afficher un message demandant à l'utilisateur d'entrer le nom de la ville
        System.out.println("\nVeuillez entrer le nom de la ville où retirer une zone de recharge :");
        String nomVille = scanner.nextLine(); // Lire le nom de la ville à partir de l'entrée utilisateur
        Ville ville = trouverVilleParNom(nomVille); // Trouver la ville correspondant au nom entré
        // Vérifier si la ville a été trouvée
        if (ville != null) {
            retirerRecharge(ville); // Appeler la méthode retirerRecharge pour retirer la zone de recharge de la ville
        } else {
            System.out.println("Ville non trouvée. Veuillez réessayer."); // Afficher un message si la ville n'a pas été trouvée
        }
    }

    /**
     * Permet de retirer la zone de recharge d'une ville spécifiée
     * Vérifie les conditions nécessaires pour retirer la recharge et affiche des messages appropriés
     *
     * @param ville La ville dont on souhaite retirer la zone de recharge
     */
    public void retirerRecharge(Ville ville) {
        // Vérifier si la ville a une zone de recharge
        if (ville.getzoneDeRecharge()) {
            boolean etat = ville.getzoneDeRecharge(); // Enregistrer l'état actuel de la zone de recharge
            ville.setzoneDeRechargeFalse(); // Supprimer la zone de recharge de la ville
            // Vérifier si la ville peut retirer sa zone de recharge
            if (peutRetirerRecharge(ville)) {
                // Vérifier si la contrainte est respectée pour ses voisins
                if (contrainteVoisins(ville)) {
                    // Afficher un message indiquant que la zone de recharge a été retirée avec succès
                    System.out.println("Zone de recharge retirée de " + ville.getNom());
                } else {
                    // Afficher un message si la contrainte n'est pas respectée pour les voisins
                    System.out.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
                    ville.setzoneDeRecharge(etat); // Rétablir l'état précédent de la zone de recharge
                }
            } else {
                // Afficher un message si aucun voisin avec une zone de recharge n'est trouvé
                System.out.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". Aucun voisin avec une zone de recharge.");
                ville.setzoneDeRecharge(etat); // Rétablir l'état précédent de la zone de recharge
            }
        } else {
            // Afficher un message d'erreur si la ville n'a pas de zone de recharge
            System.err.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". La ville n'a pas de zone de recharge.");
        }
    }

    /**
     * Vérifie si une ville spécifiée ou l'un de ses voisins possède une zone de recharge
     *
     * @param ville La ville pour laquelle on vérifie la possibilité de retirer la recharge
     * @return vrai si la ville elle-même ou l'un de ses voisins a une zone de recharge, sinon faux
     */
    public boolean peutRetirerRecharge(Ville ville) {
        Set<String> voisins = graphe.getNeighbors(ville.getNom()); // Récupérer la liste des noms des voisins de la ville à partir du graphe
        // Retourner vrai si la ville elle-même a une zone de recharge ou si l'un de ses voisins a une zone de recharge
        return ville.getzoneDeRecharge() || voisins.stream().anyMatch(s -> trouverVilleParNom(s).getzoneDeRecharge());
    }

    /**
     * Vérifie si la contrainte des voisins est respectée pour une ville spécifiée
     * La contrainte des voisins stipule que chaque ville doit avoir au moins un voisin avec une zone de recharge
     *
     * @param ville La ville pour laquelle on vérifie la contrainte des voisins
     * @return vrai si la contrainte des voisins est respectée, sinon faux
     */
    public boolean contrainteVoisins(Ville ville) {
        Set<String> visited = new HashSet<>(); // Initialiser un ensemble pour suivre les villes déjà visitées pendant la vérification
        return contrainteVoisinsHelper(ville, visited); // Appeler la méthode auxiliaire pour effectuer la vérification
    }

    /**
     * Méthode auxiliaire récursive utilisée par contrainteVoisins pour vérifier si la contrainte des voisins est respectée
     * La contrainte des voisins stipule qu'une ville doit avoir au moins un voisin avec une zone de recharge
     *
     * @param ville    La ville pour laquelle on vérifie la contrainte des voisins
     * @param visited  Un ensemble de villes déjà visitées pendant la vérification
     * @return vrai si la contrainte des voisins est respectée, sinon faux
     */
    public boolean contrainteVoisinsHelper(Ville ville, Set<String> visited) {
        Set<String> voisins = graphe.getNeighbors(ville.getNom()); // Récupérer la liste des noms des voisins de la ville à partir du graphe
        // Parcourir les voisins de la ville
        for (String s : voisins) {
            Ville v = trouverVilleParNom(s);
            // Vérifier si la ville n'a pas déjà été visitée pour éviter une boucle infinie dans le graphe
            if (!visited.contains(v.getNom())) {
                visited.add(v.getNom()); // Ajouter la ville à l'ensemble des villes visitées
                // Vérifier si la ville actuelle et son voisin respectent la contrainte des voisins
                if (!ville.getNom().equals(v.getNom()) && !peutRetirerRecharge(v)) {
                    return false;
                }
                // Appeler récursivement la méthode pour le voisin actuel
                if (!contrainteVoisinsHelper(v, visited)) {
                    return false;
                }
            }
        }
        return true; // Si toutes les vérifications sont passées, retourner vrai
    }

    /**
     * Charge une communauté à partir d'un fichier
     *
     * @param cheminFichier Chemin du fichier contenant les informations de la communauté
     */
    public void chargerCommunaute(String cheminFichier) {
        try (Scanner scanner = new Scanner(new File(cheminFichier))) {
            // Parcourir chaque ligne du fichier
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                // Si la ligne commence par "ville"
                if (ligne.startsWith("ville")) {
                    // Séparer les éléments de la ligne entre parenthèses
                    String[] elements = ligne.split("[()]");
                    // Vérifier s'il y a au moins deux éléments
                    if (elements.length >= 2) {
                    	boolean test = true;
                    	// Extraire le nom de la ville
                        String nomVille = elements[1].trim();
                        // Créer une nouvelle ville et l'ajoute à la liste des villes
                        Ville ville = new Ville(nomVille);
                        for(Ville v : villes) {
                        	if(v.getNom().equals(ville.getNom())) {
                        		test=false;
                        	}
                        }
                        if(test) {
                        	villes.add(ville);
                        	// Ajoute également la ville au graphe
                        	ajouterVille(ville);
                        }
                        else {
                        	System.out.println("Ville existe déjà !");
                        }
                    }
                }
                // Si la ligne commence par "route"
                else if (ligne.startsWith("route")) {
                    // Séparer les éléments de la ligne entre parenthèses et virgules
                    String[] elements = ligne.split("[(),]");
                    // Vérifier s'il y a au moins deux éléments
                    if (elements.length >= 2) {
                        // Extraire les noms des villes de départ et d'arrivée
                        String nomVilleA = elements[1];
                        String nomVilleB = elements[2].substring(1);
                        // Ajoute une route entre les deux villes
                        ajouterRoute(nomVilleA, nomVilleB);
                    }
                }
                // Si la ligne commence par "recharge"
                else if (ligne.startsWith("recharge")) {
                    // Séparer les éléments de la ligne entre parenthèses
                    String[] elements = ligne.split("[()]");
                    // Vérifier s'il y a au moins un élément
                    if (elements.length >= 1) {
                        // Extraire le nom de la ville à recharger
                        String nomVille = elements[1].trim();
                        // Recharger la ville
                        recharge(nomVille);
                    }
                }
            }
            // Afficher un message indiquant que la communauté a été chargée avec succès
            System.out.println("\nCommunauté chargée depuis le fichier : " + cheminFichier + "\n");
        } catch (FileNotFoundException e) {
            // En cas d'erreur de fichier non trouvé, affiche un message d'erreur
            System.err.println("\nFichier non trouvé : " + cheminFichier);
        }
    }

    /**
     * Vérifie si une ville contient une zone de recharge
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville contient une zone de recharge, sinon faux
     */
    public boolean contientRecharge(Ville ville) {
        // Parcourir la liste des chargeurs pour la communauté
        for (ZoneRecharge charger : zonesRecharge) {
            // Si le chargeur est associé à la ville spécifiée, la ville contient une zone de recharge
            if (charger.getVille().equals(ville)) {
                return true;
            }
        }
        return false; // Si aucun chargeur associé à la ville n'est trouvé, la ville ne contient pas de zone de recharge
    }

    /**
     * Génère une solution initiale en ajoutant une zone de recharge à chaque ville de la communauté
     * Cette méthode est appelée lorsque la liste des villes est vide
     */
    public void genererSolutionInitiale() {
        // Vérifier s'il y a des villes dans la communauté
        if (villes.isEmpty()) {
            System.out.println("Aucune ville à prendre en compte pour la solution initiale.");
            return;
        }
        // Parcourir toutes les villes de la communauté et ajoute une zone de recharge à chacune
        for (Ville ville : villes) {
            recharge(ville.getNom());
        }
    }

    /**
     * Choisi de manière aléatoire une ville parmi celles présentes dans la communauté
     *
     * @return Une ville choisie aléatoirement
     */
    public Ville choisirVilleAleatoire() {
        List<Ville> villeList = new ArrayList<>(villes); // Convertir la liste de villes en une liste modifiable
        // Utiliser ThreadLocalRandom pour obtenir un index aléatoire dans la liste
        return villeList.get(ThreadLocalRandom.current().nextInt(villeList.size()));
    }

    /**
     * Résout automatiquement le problème en utilisant un algorithme itératif
     * Choisi aléatoirement une ville, la recharge si elle ne l'est pas, et évalue le score
     * Répète le processus jusqu'à atteindre le nombre d'itérations spécifié
     *
     * @param k Le nombre d'itérations maximum.
     */
    public void resoudreAutomatiquementAlgo2(int k) {
        int i = 0; // Compteur d'itérations
        int scoreCourant = score(); // Score initial
        while (i < k) {
            Ville ville = choisirVilleAleatoire(); // Choix aléatoire d'une ville
            if (!ville.getzoneDeRecharge()) {
                recharge(ville.getNom()); // Recharger la ville si elle ne l'est pas
            }
            int nouveauScore = score(); // Évaluer le nouveau score
            if (nouveauScore <= scoreCourant) {
                i = 0; // Réinitialiser le compteur si le score s'améliore ou reste inchangé
                scoreCourant = nouveauScore;
            }
            else {
                i++; // Incrémenter le compteur sinon
            }
        }
    }

    /**
     * Vérifie si la ville est reliée à une borne de recharge via au moins une route
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville est reliée à une borne de recharge, sinon faux
     */
    public boolean estRelieeAvecBorne(Ville ville) {
        for (Route route : routes) {
            if (route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) {
                // Si la route va vers villeA ou villeB, vérifie que l'une de ces villes a une zone de recharge
                if (contientRecharge(route.getVilleA()) || contientRecharge(route.getVilleB())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si une ville respecte la contrainte d'accessibilité
     * Une ville doit avoir une zone de recharge
     * et toutes les villes reliées à elle par des routes doivent également avoir une zone de recharge
     * ou être reliées à une zone de recharge
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville respecte la contrainte, sinon faux
     */
    public boolean respecteContrainte(Ville ville) {
        if (!contientRecharge(ville)) {
            // La ville doit avoir une zone de recharge ou être reliée à une ville avec zone de recharge
            return false;
        }
        for (Route route : routes) {
            if (route.getVilleA().equals(ville)) {
                // Si la route va vers villeA, vérifie que villeB a une zone de recharge ou est reliée à une ville avec zone de recharge
                if (!(contientRecharge(route.getVilleB()) || estRelieeAvecBorne(route.getVilleB()))) {
                    return false;
                }
            } else if (route.getVilleB().equals(ville)) {
                // Si la route va vers villeB, vérifie que villeA a une zone de recharge ou est reliée à une ville avec zone de recharge
                if (!(contientRecharge(route.getVilleA()) || estRelieeAvecBorne(route.getVilleA()))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calcule le score actuel de la communauté, représenté par le nombre de villes avec une zone de recharge connectée
     *
     * @return Le score de la communauté
     */
    public int score() {
        // Utilisation de Stream pour filtrer les chargeurs en tant que sources et compter leur nombre
        return (int) zonesRecharge.stream().filter(ZoneRecharge::estSourceRecharge).count();
    }
}