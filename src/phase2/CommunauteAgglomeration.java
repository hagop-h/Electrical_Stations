package phase2;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * La classe CommunauteAgglomeration représente une communauté d'agglomération comprenant des villes, des routes et des zones de recharge
 * Elle utilise un graph pour représenter la connectivité entre les villes
 */
public class CommunauteAgglomeration {
    private final Set<Ville> villes; // Ensemble de villes dans une communauté
    private Set<Route> routes; // Ensemble de routes entre les villes
    private List<ZoneRecharge> zonesRecharge; // Liste de zones de recharge présentes dans une communauté
    private final Graphe graphe; // Graph représentant la connectivité entre les villes
    private int scoreCourant; // Score actuel utilisé dans l'algorithme optimal
    
    /**
     * Constructeur de la classe CommunauteAgglomeration
     * Initialise les collections de villes, de routes, de zones de recharge, et le graphe
     */
    public CommunauteAgglomeration() {
        villes = new HashSet<>();
        routes = new HashSet<>();
        zonesRecharge = new ArrayList<>();
        graphe = new Graphe();
        scoreCourant = 0;
    }

    /**
     * Renvoie l'ensemble des villes.
     *
     * @return L'ensemble des villes.
     * @throws NullPointerException si l'ensemble des villes est null
     */
    public Set<Ville> getVilles() {
        try {
            // Retourner l'ensemble des villes
            return villes;
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la récupération de l'ensemble des villes : " + e.getMessage());
            return Collections.emptySet(); // Renvoyer une collection vide en cas de NullPointerException
        }
    }

    /**
     * Obtient la liste des objets Zones recharge
     *
     * @return Une liste d'objets Zones Recharge
     * @throws NullPointerException Si la liste des zones de recharge est null
     */
    public List<ZoneRecharge> getZonesRecharge() {
        try {
            return zonesRecharge;
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la récupération des zones de recharge : " + e.getMessage());
            return null; // Retourner null en cas d'erreur
        }
    }

    /**
     * Définit la liste des zones de recharge
     *
     * @param zonesRecharge La nouvelle liste des zones de recharge
     * @throws NullPointerException Si la liste des zones de recharge est nulle
     */
    public void setZonesRecharge(List<ZoneRecharge> zonesRecharge) {
        try {
            this.zonesRecharge = zonesRecharge;
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la modification de l'ensemble des routes : " + e.getMessage());
        }
    }

    /**
     * Ajoute une zone de recharge à la liste des zones de recharge de la communauté
     *
     * @param zoneRecharge La zone de recharge à ajouter
     * @throws NullPointerException Si la zone de recharge spécifiée est null
     */
    public void ajouterRecharge(ZoneRecharge zoneRecharge) {
        try {
            zonesRecharge.add(zoneRecharge);
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de l'ajout d'une zone de recharge : " + e.getMessage());
        }
    }

    /**
     * Modifie l'ensemble des routes de la communauté en remplaçant l'ensemble actuel par le nouvel ensemble spécifié
     *
     * @param routes Le nouvel ensemble de routes
     * @throws NullPointerException Si l'ensemble de routes spécifié est null
     */
    public void setRoutes(Set<Route> routes) {
        try {
            this.routes = routes;
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la modification de l'ensemble des routes : " + e.getMessage());
        }
    }

    /**
     * Lit un entier depuis le scanner tout en gérant les exceptions d'entrée incorrecte
     *
     * @param scanner Le scanner utilisé pour la lecture
     * @return L'entier lu depuis le scanner
     * @throws NullPointerException Si une NullPointerException se produit pendant la lecture
     * @throws IllegalArgumentException Si une IllegalArgumentException se produit pendant la lecture
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
            } catch (NullPointerException e) {
                // Gérer spécifiquement une éventuelle NullPointerException
                System.out.println("NullPointerException lors du retrait de la zone de recharge : " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // Gérer spécifiquement une éventuelle IllegalArgumentException
                System.out.println("IllegalArgumentException lors du retrait de la zone de recharge : " + e.getMessage());
            }  catch (InputMismatchException e) {
                // Gérer l'exception si l'entrée n'est pas un entier
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
     * @throws NullPointerException Si la liste de villes est null pendant la recherche
     */
    public Ville trouverVilleParNom(String nom) {
        try {
            return villes.stream().filter(ville -> ville.getNom().equalsIgnoreCase(nom)).findFirst().orElse(null);
        } catch (NullPointerException e) {
            // Gérer l'exception si la liste de villes est null
            System.out.println("La liste de villes n'est pas correctement initialisée.");
            return null; // Retourner null en cas d'erreur
        }
    }

    /**
     * Permet à l'utilisateur de trouver manuellement une solution en ajoutant ou retirant des zones de recharge
     * Affiche le menu interactif pour la gestion manuelle des zones de recharge
     * Si la liste des chargeurs est vide, génère une solution initiale
     *
     * @throws InputMismatchException Si l'entrée utilisateur n'est pas un entier dans le menu
     * @throws NullPointerException   Si la liste de villes n'est pas correctement initialisée lors de la recherche par nom
     * @throws IllegalArgumentException Si l'ajout d'une zone de recharge échoue avec une valeur null
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
            try {
                System.out.println("\nScore : " + score() + "\n");
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
            } catch (InputMismatchException e) {
                // Gérer l'exception si l'entrée n'est pas un entier dans le menu
                System.out.println("Veuillez entrer un nombre entier.");
                scanner.nextLine(); // Consommer la ligne incorrecte pour éviter une boucle infinie
                choixMenu = 0; // Réinitialiser le choix pour rester dans la boucle
            } catch (NullPointerException e) {
                // Gérer l'exception si la liste de villes n'est pas correctement initialisée lors de la recherche par nom
                System.out.println("La liste de villes n'est pas correctement initialisée.");
                choixMenu = 0; // Réinitialiser le choix pour rester dans la boucle
            } catch (IllegalArgumentException e) {
                // Gérer l'exception si l'ajout d'une zone de recharge échoue avec une valeur null
                System.out.println("IllegalArgumentException lors de l'ajout d'une zone de recharge : " + e.getMessage());
                choixMenu = 0; // Réinitialiser le choix pour rester dans la boucle
            }
        } while (choixMenu != 3); // Continuer jusqu'à ce que l'utilisateur choisisse la fin de la gestion manuelle
    }

    /**
     * Ajoute une route entre deux villes à partir de leurs noms
     * Recherche les villes correspondantes par leur nom et ajoute la route si les deux villes existent
     *
     * @param nomVilleA Nom de la première ville
     * @param nomVilleB Nom de la deuxième ville
     * @throws NullPointerException Si la liste de villes ou la méthode ajouterRoute n'est pas correctement initialisée
     */
    public void ajouterRoute(String nomVilleA, String nomVilleB) {
        // Rechercher des objets Ville correspondants aux noms fournis
        Ville villeA = trouverVilleParNom(nomVilleA);
        Ville villeB = trouverVilleParNom(nomVilleB);
        // Vérification de l'existence des deux villes
        try {
            if (villeA != null && villeB != null) {
                Route route = new Route(villeA, villeB); // Création d'une nouvelle route
                routes.add(route); // Ajout de la route à la liste des routes
                graphe.addEdge(villeA.getNom(), villeB.getNom()); // Mise à jour de la représentation du graphe
            } else {
                System.out.println("Villes non trouvées. Veuillez réessayer.");
            }
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de l'ajout de route : " + e.getMessage());
        }
	}

    /**
     * Vérifie si la communauté contient une route entre deux villes spécifiées
     *
     * @param villeA La première ville
     * @param villeB La deuxième ville
     * @return vrai si une route entre villeA et villeB existe, sinon faux
     * @throws IllegalArgumentException Si l'une des villes est null
     * @throws NullPointerException Si la liste de routes n'est pas correctement initialisée
     */
    public boolean contientRoute(Ville villeA, Ville villeB) {
        try {
            if (villeA == null || villeB == null) {
                throw new IllegalArgumentException("Les villes ne peuvent pas être null.");
            }
            // Vérifier si la route entre villeA et villeB existe dans la liste des routes
            return routes.stream().anyMatch(route -> (route.getVilleA().equals(villeA) && route.getVilleB().equals(villeB)) || (route.getVilleA().equals(villeB) && route.getVilleB().equals(villeA)));
        } catch (NullPointerException npe) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la vérification de la route : " + npe.getMessage());
            return false; // Retourner false en cas de ville null
        }
    }

    /**
     * Ajuste les zones de recharge connectées à une ville donnée
     * Parcourt les routes de la communauté d'agglomération
     * et ajoute des zones de recharge aux villes connectées qui n'ont pas encore de zone de recharge
     *
     * @param ville Ville pour laquelle ajuster les zones de recharge connectées
     * @throws IllegalArgumentException Si la ville est null
     * @throws NullPointerException Si la liste de routes, de zones de recharge, ou la méthode contientRecharge n'est pas correctement initialisée
     */
    public void ajusterRechargeConnectees(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            // Parcourir les routes de la communauté d'agglomération
            for (Route route : routes) {
                if (route == null) {
                    throw new NullPointerException("La liste des routes contient un élément null.");
                }
                // Vérifier si la ville en paramètre est la ville de départ de la route
                if (route.getVilleA().equals(ville) && !contientRecharge(route.getVilleB())) {
                    zonesRecharge.add(new ZoneRecharge(route.getVilleB())); // Ajouter une zone de recharge à la ville connectée si elle n'a pas de zone de recharge
                } else if (route.getVilleB().equals(ville) && !contientRecharge(route.getVilleA())) {
                    zonesRecharge.add(new ZoneRecharge(route.getVilleA())); // Ajouter une zone de recharge à la ville connectée si elle n'a pas de zone de recharge
                }
            }
        } catch (NullPointerException e) {
            // Gérer l'exception si la liste de routes, de zones de recharge, ou la méthode contientRecharge n'est pas correctement initialisée
            System.out.println("Erreur lors de l'ajustement des zones de recharge. Veuillez vérifier la configuration de la communauté d'agglomération.");
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
            if (zoneRecharge == null) {
                throw new NullPointerException("La liste des villes avec source de recharge contient un élément null.");
            }
            System.out.println("- " + zoneRecharge.getVille().getNom());
        }
        // Affichage des villes rechargées sans leurs propres zones de recharge
        System.out.println("\nVilles rechargées sans leurs propres zone de recharge :");
        for (ZoneRecharge zoneRecharge : getVillesRechargeesSansSource()) {
            if (zoneRecharge == null) {
                throw new NullPointerException("La liste des villes rechargées sans source de recharge contient un élément null.");
            }
            System.out.println("- " + zoneRecharge.getVille().getNom());
        }
        // Affichage des villes non rechargées
        System.out.println("\nVilles non rechargées :");
        for (Ville ville : getVillesSansZoneRecharge()) {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            System.out.println("- " + ville.getNom());
        }
    }

    /**
     * Sauvegarde la solution actuelle dans un fichier spécifié
     * Les informations sur les villes et les routes sont sauvegardées, ainsi que la catégorisation des villes en fonction de leur zone de recharge
     * Ajoute également le score de la solution
     *
     * @param cheminFichier Le chemin du fichier où sauvegarder la solution
     * @throws NullPointerException Si la liste des routes, des villes avec source de recharge, des villes rechargées sans source de recharge ou des villes sans zone de recharge contient un élément null
     * @throws IllegalArgumentException Si la ville ne peut pas être null lors de la sauvegarde des villes non rechargées
     * @throws IOException En cas d'erreur lors de la sauvegarde de la solution
     */
    public void sauvegarderSolution(String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            // Sauvegarder les informations sur les villes
            for (Ville ville : villes) {
                writer.write(ville.getNom() + " " + ville.getZoneDeRecharge());
                writer.newLine();
            }
            // Sauvegarder les informations sur les routes
            for (Route route : routes) {
                if (route == null) {
                    throw new NullPointerException("La liste des routes contient un élément null.");
                }
                writer.write("route " + route.getVilleA().getNom() + " " + route.getVilleB().getNom());
                writer.newLine();
            }
            // Sauvegarder les villes rechargées avec leurs propres zones de recharge
            writer.write("\nVilles rechargées avec leurs propres zone de recharge :");
            writer.newLine();
            for (ZoneRecharge zoneRecharge : getVillesAvecSourceRecharge()) {
                if (zoneRecharge == null) {
                    throw new NullPointerException("La liste des villes avec source de recharge contient un élément null.");
                }
                writer.write("- " + zoneRecharge.getVille().getNom());
                writer.newLine();
            }
            // Sauvegarder les villes rechargées sans leurs propres zones de recharge
            writer.write("\nVilles rechargées sans leurs propres zone de recharge :");
            writer.newLine();
            for (ZoneRecharge zoneRecharge : getVillesRechargeesSansSource()) {
                if (zoneRecharge == null) {
                    throw new NullPointerException("La liste des villes rechargées sans source de recharge contient un élément null.");
                }
                writer.write("- " + zoneRecharge.getVille().getNom());
                writer.newLine();
            }
            // Sauvegarder les villes non rechargées
            writer.write("\nVilles non rechargées :");
            writer.newLine();
            for (Ville ville : getVillesSansZoneRecharge()) {
                if (ville == null) {
                    throw new IllegalArgumentException("La ville ne peut pas être null.");
                }
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
     * @throws NullPointerException si la liste de zones de recharge n'est pas correctement initialisée
     */
    public List<ZoneRecharge> getVillesAvecSourceRecharge() {
        try {
            // Filtrage des zones de recharge qui sont des sources
            return zonesRecharge.stream().filter(ZoneRecharge::estSourceRecharge).collect(Collectors.toList());
        } catch (NullPointerException e) {
            // Gérer l'exception si la liste de zones de recharge n'est pas correctement initialisée
            System.out.println("Erreur lors de la récupération des villes avec source de recharge : " + e.getMessage());
            return null; // Retourner null en cas d'erreur
        }
    }

    /**
     * Récupère la liste des villes rechargées sans leur propre zone de recharge
     *
     * @return Liste des objets zones de recharge représentant les villes rechargées sans leur propre zone de recharge
     * @throws NullPointerException si la liste de zones de recharge n'est pas correctement initialisée
     */
    public List<ZoneRecharge> getVillesRechargeesSansSource() {
        try {
            // Filtrage des zones de recharge qui ne sont pas des sources
            return zonesRecharge.stream().filter(zoneRecharge -> !zoneRecharge.estSourceRecharge()).collect(Collectors.toList());
        } catch (NullPointerException e) {
            // Gérer l'exception si la liste de zones de recharge n'est pas correctement initialisée
            System.out.println("Erreur lors de la récupération des villes rechargeées sans source de recharge : " + e.getMessage());
            return null; // Retourner null en cas d'erreur
        }
    }

    /**
     * Récupère la liste des villes sans zone de recharge propre et sans zone de recharge connectée
     *
     * @return Liste des objets Ville représentant les villes sans zone de recharge propre et sans zone de recharge connectée
     * @throws NullPointerException si la liste de villes ou la méthode contientRecharge n'est pas correctement initialisée
     */
    public List<Ville> getVillesSansZoneRecharge() {
        try {
            List<Ville> villesSansZoneRecharge = new ArrayList<>(); // Initialisation d'une liste pour stocker les villes sans zone de recharge
            // Parcourir de la liste des villes
            for (Ville ville : villes) {
                // Vérification si la ville n'a pas de zone de recharge et que la zone de recharge est désactivée
                if (!contientRecharge(ville) && !ville.getZoneDeRecharge()) {
                    // Ajout de la ville à la liste si elle n'est pas déjà présente
                    if (!villesSansZoneRecharge.contains(ville)) {
                        villesSansZoneRecharge.add(ville);
                    }
                }
            }
            return villesSansZoneRecharge; // Retour de la liste des villes sans zone de recharge
        } catch (NullPointerException e) {
            // Gérer l'exception si la liste de villes ou la méthode contientRecharge n'est pas correctement initialisée
            System.out.println("Erreur lors de la récupération des villes sans zone de recharge : " + e.getMessage());
            return new ArrayList<>(); // Retourner une liste vide en cas d'erreur
        }
    }

    /**
     * Menu interactif pour ajouter une zone de recharge à une ville
     * Demande le nom de la ville, recherche la ville correspondante, puis ajoute la zone de recharge
     * Affiche un message approprié selon le résultat
     *
     * @param scanner Objet Scanner pour la saisie utilisateur
     * @throws NullPointerException si la liste de zones de recharge, la liste de villes, la méthode contientRecharge ou la méthode ajusterRechargeConnectees n'est pas correctement initialisée
     * @throws IllegalArgumentException si la méthode respecteContrainte détecte une violation de la contrainte d'accessibilité
     * @throws InputMismatchException si l'entrée utilisateur n'est pas valide
     */
    public void ajouterZoneRechargeMenu(Scanner scanner) {
        try {
            System.out.println("\nVeuillez entrer le nom de la ville où ajouter une zone de recharge :"); // Affichage du message d'invite
            String nomVille = scanner.nextLine(); // Lecture du nom de la ville depuis l'entrée utilisateur
            Ville ville = trouverVilleParNom(nomVille); // Rechercher la ville par son nom
            if (ville != null) {
                // Vérification si la ville n'a pas déjà une zone de recharge
                if (!ville.getZoneDeRecharge()) {
                    ville.setZoneDeRechargeTrue(); // Ajout de la zone de recharge à la ville
                    // Vérification si la zone de recharge n'existe pas déjà
                    if (!contientRecharge(ville)) {
                        zonesRecharge.add(new ZoneRecharge(ville)); // Ajout d'une nouvelle zone de recharge à la liste
                    }
                    ajusterRechargeConnectees(ville); // Ajustement des recharges connectées
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
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors du retrait de la zone de recharge : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Gérer spécifiquement une éventuelle IllegalArgumentException
            System.out.println("IllegalArgumentException lors du retrait de la zone de recharge : " + e.getMessage());
        }  catch (InputMismatchException e) {
            // Gérer l'exception si l'entrée n'est pas valide
            System.out.println("\nErreur de saisie : Veuillez entrer un nom de ville valide.");
            scanner.nextLine(); // Consommer la ligne incorrecte pour éviter une boucle infinie
        }
    }

    /**
     * Ajoute une zone de recharge à la ville spécifiée, met à jour la liste des zones de recharge
     * et ajuste les zones de recharge connectées. Affiche un message approprié selon le résultat
     *
     * @param nomVille Le nom de la ville où ajouter une zone de recharge
     * @throws NullPointerException si la liste de zones de recharge, la liste de villes, la méthode contientRecharge ou la méthode ajusterRechargeConnectees n'est pas correctement initialisée
     */
    public void recharge(String nomVille) {
        try {
            Ville ville = trouverVilleParNom(nomVille); // Rechercher la ville par son nom
            if (ville != null) {
                // Vérification si la ville n'a pas déjà une zone de recharge
                if (!ville.getZoneDeRecharge()) {
                    ville.setZoneDeRechargeTrue();  // Ajout de la zone de recharge à la ville
                    // Vérification si la zone de recharge n'existe pas déjà
                    if (!contientRecharge(ville)) {
                        zonesRecharge.add(new ZoneRecharge(ville)); // Ajout d'une nouvelle zone de recharge à la liste
                    }
                    ajusterRechargeConnectees(ville); // Ajustement des recharges connectées
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
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la recharge : " + e.getMessage());
        }
    }

    /**
     * Ajoute une nouvelle ville à la communauté d'agglomération
     *
     * @param ville Ville à ajouter
     * @throws IllegalArgumentException si la ville est null
     * @throws NullPointerException si une exception de type NullPointerException est levée lors de l'ajout de la ville
     */
    public void ajouterVille(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            villes.add(ville); // Ajout de la ville à la liste des villes
            graphe.addVertex(ville.getNom()); // Mise à jour du graphe avec le nom de la ville comme un nouveau sommet
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de l'ajout de la ville : " + e.getMessage());
        }
    }

    /**
     * Permet de retirer une zone de recharge d'une ville spécifiée
     * Utilise un objet Scanner pour récupérer le nom de la ville depuis l'entrée utilisateur
     * Affiche des messages appropriés en fonction du résultat de l'opération
     *
     * @param scanner Objet Scanner pour récupérer l'entrée utilisateur
     * @throws NullPointerException si une exception de type NullPointerException est levée lors du retrait de la zone de recharge
     * @throws IllegalArgumentException si une exception de type IllegalArgumentException est levée lors du retrait de la zone de recharge
     * @throws InputMismatchException si l'entrée utilisateur n'est pas valide (par exemple, si le nom de la ville n'est pas correct)
     */
    public void retirerZoneRechargeMenu(Scanner scanner) {
        try {
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
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors du retrait de la zone de recharge : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Gérer spécifiquement une éventuelle IllegalArgumentException
            System.out.println("IllegalArgumentException lors du retrait de la zone de recharge : " + e.getMessage());
        } catch (InputMismatchException e) {
            // Gérer l'exception si l'entrée n'est pas valide
            System.out.println("\nErreur de saisie : Veuillez entrer un nom de ville valide.");
            scanner.nextLine(); // Consommer la ligne incorrecte pour éviter une boucle infinie
        }
    }

    /**
     * Permet de retirer la zone de recharge d'une ville spécifiée
     * Vérifie les conditions nécessaires pour retirer la recharge et affiche des messages appropriés
     *
     * @param ville La ville dont on souhaite retirer la zone de recharge
     * @throws NullPointerException si une exception de type NullPointerException est levée lors du retrait de la zone de recharge
     * @throws IllegalArgumentException si une exception de type IllegalArgumentException est levée lors du retrait de la zone de recharge
     */
    public void retirerRecharge(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            // Vérifier si la ville a une zone de recharge
            if (ville.getZoneDeRecharge()) {
                boolean etat = ville.getZoneDeRecharge(); // Enregistrer l'état actuel de la zone de recharge
                ville.setZoneDeRechargeFalse(); // Supprimer la zone de recharge de la ville
                // Vérifier si la ville peut retirer sa zone de recharge
                if (peutRetirerRecharge(ville)) {
                    // Vérifier si la contrainte est respectée pour ses voisins
                    if (contrainteVoisins(ville)) {
                        // Afficher un message indiquant que la zone de recharge a été retirée avec succès
                        System.out.println("\nZone de recharge retirée de " + ville.getNom() + ".");
                    } else {
                        // Afficher un message si la contrainte n'est pas respectée pour les voisins
                        System.out.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
                        ville.setZoneDeRecharge(etat); // Rétablir l'état précédent de la zone de recharge
                    }
                } else {
                    // Afficher un message si aucun voisin avec une zone de recharge n'est trouvé
                    System.out.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". Aucun voisin avec une zone de recharge.");
                    ville.setZoneDeRecharge(etat); // Rétablir l'état précédent de la zone de recharge
                }
            } else {
                // Afficher un message d'erreur si la ville n'a pas de zone de recharge
                System.err.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". La ville n'a pas de zone de recharge.");
            }
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors du retrait de la zone de recharge : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Gérer spécifiquement une éventuelle IllegalArgumentException
            System.out.println("IllegalArgumentException lors du retrait de la zone de recharge : " + e.getMessage());
        }
    }

    /**
     * Vérifie si une ville spécifiée ou l'un de ses voisins possède une zone de recharge
     *
     * @param ville La ville pour laquelle on vérifie la possibilité de retirer la recharge
     * @return vrai si la ville elle-même ou l'un de ses voisins a une zone de recharge, sinon faux
     * @throws NullPointerException si une exception de type NullPointerException est levée lors de la vérification de la possibilité de retirer la recharge
     * @throws IllegalArgumentException si une exception de type IllegalArgumentException est levée lors de la vérification de la possibilité de retirer la recharge
     */
    public boolean peutRetirerRecharge(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            Set<String> voisins = graphe.getNeighbors(ville.getNom()); // Récupérer la liste des noms des voisins de la ville à partir du graphe
            // Retourner vrai si la ville elle-même a une zone de recharge ou si l'un de ses voisins a une zone de recharge
            return ville.getZoneDeRecharge() || voisins.stream().anyMatch(s -> trouverVilleParNom(s).getZoneDeRecharge());
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la vérification de la possibilité de retirer la recharge : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        } catch (IllegalArgumentException e) {
            // Gérer spécifiquement une éventuelle IllegalArgumentException
            System.out.println("IllegalArgumentException lors de la vérification de la possibilité de retirer la recharge : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        }
    }

    /**
     * Vérifie si la contrainte des voisins est respectée pour une ville spécifiée
     * La contrainte des voisins stipule que chaque ville doit avoir au moins un voisin avec une zone de recharge
     *
     * @param ville La ville pour laquelle on vérifie la contrainte des voisins
     * @return vrai si la contrainte des voisins est respectée, sinon faux
     * @throws NullPointerException si une exception de type NullPointerException est levée lors de la vérification de la contrainte des voisins
     * @throws IllegalArgumentException si une exception de type IllegalArgumentException est levée lors de la vérification de la contrainte des voisins
     */
    public boolean contrainteVoisins(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            Set<String> visited = new HashSet<>(); // Initialiser un ensemble pour suivre les villes déjà visitées pendant la vérification
            return contrainteVoisinsHelper(ville, visited); // Appeler la méthode auxiliaire pour effectuer la vérification
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la vérification de la contrainte des voisins : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        } catch (IllegalArgumentException e) {
            // Gérer spécifiquement une éventuelle IllegalArgumentException
            System.out.println("IllegalArgumentException lors de la vérification de la contrainte des voisins : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        }
    }

    /**
     * Méthode auxiliaire récursive utilisée par contrainteVoisins pour vérifier si la contrainte des voisins est respectée
     * La contrainte des voisins stipule qu'une ville doit avoir au moins un voisin avec une zone de recharge
     *
     * @param ville    La ville pour laquelle on vérifie la contrainte des voisins
     * @param visited  Un ensemble de villes déjà visitées pendant la vérification
     * @return vrai si la contrainte des voisins est respectée, sinon faux
     * @throws IllegalArgumentException si une exception de type IllegalArgumentException est levée lors de la vérification de la contrainte des voisins
     */
    public boolean contrainteVoisinsHelper(Ville ville, Set<String> visited) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
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
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la vérification de la contrainte des voisins : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        }
    }

    /**
     * Charge une communauté à partir d'un fichier contenant les informations des villes, des routes, et des recharges.
     *
     * @param cheminFichier Le chemin du fichier contenant les informations de la communauté.
     * @throws FileNotFoundException si le fichier spécifié n'est pas trouvé lors de la lecture
     */
    public void chargerCommunaute(String cheminFichier) {
        try (Scanner scanner = new Scanner(new File(cheminFichier))) {
            // Parcourir chaque ligne du fichier
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                try {
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
                            for (Ville v : villes) {
                                if (v.getNom().equals(ville.getNom())) {
                                    test = false;
                                    break;
                                }
                            }
                            if (test) {
                                villes.add(ville);
                                // Ajouter également la ville au graphe
                                ajouterVille(ville);
                            } else {
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
                            String nomVilleB = elements[2];
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
                } catch (ArrayIndexOutOfBoundsException e) {
                    // En cas d'accès à un indice inexistant dans le tableau, affiche un message d'erreur
                    System.err.println("Erreur lors du traitement de la ligne : " + ligne + " - " + e.getMessage());
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
     * @throws IllegalArgumentException si la ville spécifiée est null
     * @throws NullPointerException si la liste des zones de recharge contient un élément null
     */
    public boolean contientRecharge(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            // Parcourir la liste des chargeurs pour la communauté
            for (ZoneRecharge charger : zonesRecharge) {
                if (charger == null) {
                    throw new NullPointerException("La liste des zones de recharge contient un élément null.");
                }
                // Si le chargeur est associé à la ville spécifiée, la ville contient une zone de recharge
                if (charger.getVille().equals(ville)) {
                    return true;
                }
            }
            return false; // Si aucun chargeur associé à la ville n'est trouvé, la ville ne contient pas de zone de recharge
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la vérification de la présence de la recharge : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        }
    }

    /**
     * Génère une solution initiale en ajoutant une zone de recharge à chaque ville de la communauté
     * Cette méthode est appelée lorsque la liste des villes est vide
     *
     * @throws NullPointerException si la liste des villes est null.
     */
    public void genererSolutionInitiale() {
        try {
            // Vérifier s'il y a des villes dans la communauté
            if (villes.isEmpty()) {
                System.out.println("Aucune ville à prendre en compte pour la solution initiale.");
                return;
            }
            // Parcourir toutes les villes de la communauté et ajoute une zone de recharge à chacune
            for (Ville ville : villes) {
                recharge(ville.getNom());
            }
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la génération de la solution initiale : " + e.getMessage());
        }
    }

    /**
     * Résout automatiquement le problème d'optimisation
     * L'algorithme vise à améliorer le score en rechargeant sélectivement des villes
     *
     * @throws NullPointerException si la liste des villes est null lors de la résolution automatique
     */
    public void resoudreAutomatiquement() {
        try {
            int maxIterations = 4; // Nombre maximal d'itérations
            scoreCourant = score(); // Initialiser scoreCourant avec le score actuel
            // Obtenir la liste des sommets triés par degré en ordre décroissant
            List<Ville> liste = trierSommetsParDegree();
            Set<Ville> villesRechargees = new HashSet<>();
            int i = 0;
            while (i < maxIterations) {
                for (Ville ville : liste) {
                    if (!villesRechargees.contains(ville)) {
                        recharge(ville.getNom());
                        int nouveauScore = score();
                        if (nouveauScore < scoreCourant) {
                            scoreCourant = nouveauScore;
                            villesRechargees.add(ville);
                        } else {
                            retirerRecharge(ville);
                        }
                    }
                }
                i++;
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException lors de la résolution automatique : " + e.getMessage());
        }
    }

    /**
     * Trie les villes par degré décroissant dans le graphe
     *
     * @return Une liste triée de villes en fonction du nombre de voisins connectés
     * @throws NullPointerException si le graphe ou la liste des villes est null
     */
    public List<Ville> trierSommetsParDegree() {
        try {
            // Créer une copie de la liste des villes pour éviter de modifier l'ordre d'origine
            List<Ville> sommets = new ArrayList<>(villes);
            // Trier les sommets par degré croissant en utilisant le nombre de voisins connectés dans le graphe
            sommets.sort(Comparator.comparingInt((Ville s) -> graphe.getNeighbors(s.getNom()).size()));
	    // Retirer zone de recharge des villes
            for(int i = 0;i<sommets.size();i++) {
            	Ville ville = sommets.get(i);
            	if(ville.getZoneDeRecharge() || getVillesAvecSourceRecharge().contains(ville)) {
            		retirerRecharge(ville);
            	}
            }
            System.err.println("score: "+score());
            // Trier les sommets par degré décroissant en utilisant le nombre de voisins connectés dans le graphe
            sommets.sort(Comparator.comparingInt((Ville s) -> graphe.getNeighbors(s.getNom()).size()).reversed());
            return sommets;
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors du tri des sommets par degré : " + e.getMessage());
            return Collections.emptyList(); // Renvoyer une liste vide en cas de NullPointerException
        }
    }

    /**
     * Vérifie si la ville est reliée à une borne de recharge via au moins une route
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville est reliée à une borne de recharge, sinon faux
     * @throws IllegalArgumentException si la ville passée en paramètre est null
     * @throws NullPointerException si la liste des routes contient un élément null lors de la vérification
     */
    public boolean estRelieeAvecBorne(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            for (Route route : routes) {
                if (route == null) {
                    throw new NullPointerException("La liste des routes contient un élément null.");
                }
                if (route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) {
                    // Si la route va vers villeA ou villeB, vérifie que l'une de ces villes a une zone de recharge
                    if (contientRecharge(route.getVilleA()) || contientRecharge(route.getVilleB())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la vérification de la connexion avec une borne : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        }
    }

    /**
     * Vérifie si une ville respecte la contrainte d'accessibilité
     * Une ville doit avoir une zone de recharge
     * et toutes les villes reliées à elle par des routes doivent également avoir une zone de recharge
     * ou être reliées à une zone de recharge
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville respecte la contrainte, sinon faux
     * @throws IllegalArgumentException si la ville passée en paramètre est null
     * @throws NullPointerException si la liste des routes contient un élément null lors de la vérification
     */
    public boolean respecteContrainte(Ville ville) {
        try {
            if (ville == null) {
                throw new IllegalArgumentException("La ville ne peut pas être null.");
            }
            if (!contientRecharge(ville)) {
                // La ville doit avoir une zone de recharge ou être reliée à une ville avec zone de recharge
                return false;
            }
            for (Route route : routes) {
                if (route == null) {
                    throw new NullPointerException("La liste des routes contient un élément null.");
                }
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
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors de la vérification de la contrainte d'accessibilité : " + e.getMessage());
            return false; // Retourner faux en cas d'erreur
        } 
    }

    /**
     * Calcule le score actuel de la communauté, représenté par le nombre de villes avec une zone de recharge connectée
     *
     * @return Le score de la communauté
     * @throws NullPointerException si une NullPointerException survient lors du calcul du score
     */
    public int score() {
        try {
            // Utilisation de Stream pour filtrer les chargeurs en tant que sources et compter leur nombre
            return (int) zonesRecharge.stream().filter(Objects::nonNull).filter(ZoneRecharge::estSourceRecharge).count();
        } catch (NullPointerException e) {
            // Gérer spécifiquement une éventuelle NullPointerException
            System.out.println("NullPointerException lors du calcul du score : " + e.getMessage());
            return 0; // Retourner une valeur par défaut en cas d'erreur
        }
    }
}
