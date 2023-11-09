package main;

import java.util.*;
import java.util.stream.*;

public class CommunauteAgglomeration {

    private List<Ville> villes; // Une liste pour stocker les villes
    private List<Route> routes; // Une liste pour stocker les routes
    private List<Parking> parkings; // Une liste pour stocker les zones de recharge
    private Graph graph; // Un graphe pour représenter les relations entre les villes

    public CommunauteAgglomeration() {
        villes = new ArrayList<>();
        routes = new ArrayList<>();
        parkings = new ArrayList<>();
        graph = new Graph();
    }

    public void genererSolutionNaive() {
        // Chaque ville a une zone de recharge
        for (Ville ville : villes) {
            ville.setSourceVilleTrue();
        }
        // Ajouter une zone de recharge dans chaque ville
        for (Ville ville : villes) {
            parkings.add(new Parking(ville)); // Créer une zone de recharge pour la ville et l'ajouter à la liste des villes avec zone de recharge
            graph.addVertex(ville.getNom().name()); // Ajouter un sommet pour la ville dans le graphe
        }
        // Mettre à jour la liste des villes avec des zones de recharge
        mettreAJourVillesAvecRecharge();
    }

    public void configurerCommunaute() {
        Scanner scanner = new Scanner(System.in);
        int nombreVilles;
        boolean premiereIteration = true;
        do {
            if (premiereIteration) {
                System.out.print("Veuillez entrer le nombre de villes (entre 1 et 26) :\n");
                premiereIteration = false;
            } else {
                System.out.print("\nVeuillez entrer le nombre de villes (entre 1 et 26) :\n");
            }
            nombreVilles = lireEntier(scanner);
        } while (nombreVilles < 1 || nombreVilles > 26);
        // Ajouter les villes à la communauté d'agglomération en fonction du nombre spécifié
        for (int i = 0; i < nombreVilles; i++) {
            ajouterVille(new Ville(NomVille.values()[i]));
        }
        // Afficher la liste des villes disponibles pour manipulation
        System.out.println("\nVilles disponibles pour manipulation :");
        for (Ville ville : villes) {
            System.out.println("- " + ville.getNom());
        }
        int choixMenu;
        do {
            afficherMenuPrincipal();
            choixMenu = lireEntier(scanner);
            switch (choixMenu) {
                case 1:
                    ajouterRouteMenu(scanner);
                    break;
                case 2:
                    genererSolutionNaive();
                    System.out.println("\nLa communauté d’agglomération est générée avec la solution naive qui consiste à placer une zone de recharge dans chaque ville.");
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 2); // Continuer jusqu'à ce que l'utilisateur choisisse de quitter
    }

    public int lireEntier(Scanner scanner) {
        // Une boucle pour lire un nombre entier depuis l'entrée utilisateur
        while (true) {
            try {
                int result = scanner.nextInt();
                scanner.nextLine();
                return result;
            } catch (InputMismatchException e) {  // Gestion des erreurs : en cas de saisie incorrecte, afficher un message et réessayer
                System.out.println("\nVeuillez entrer un nombre entier :");
                scanner.nextLine();
            }
        }
    }

    public Ville trouverVilleParNom(String nom) {
        // Un flux pour parcourir la liste de villes, filtrer en fonction du nom et renvoyer la première correspondance trouvée
        return villes.stream().filter(ville -> ville.getNom().name().equalsIgnoreCase(nom)).findFirst().orElse(null);
    }

    public void gererMenuZonesRecharge() {
        Scanner scanner = new Scanner(System.in);
        int choixMenu;
        do {
            afficherVillesAvecOuSansRecharge();
            afficherMenuZonesRecharge();
            choixMenu = lireEntier(scanner);
            switch (choixMenu) {
                case 1:
                    ajouterZoneRechargeMenu(scanner);
                    break;
                case 2:
                    retirerZoneRechargeMenu(scanner);
                    break;
                case 3:
                	afficherVillesAvecRecharge();
                    break;
                case 4:
                    System.out.println("\nFin de la gestion des zones de recharge.");
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 4); // Continuer jusqu'à ce que l'utilisateur choisisse de quitter la gestion des zones de recharge
    }

    public void mettreAJourVillesAvecRecharge() {
	    List<Ville> newRechargeCities = new ArrayList<>();
        // Parcourir la liste de villes pour trouver celles qui n'ont pas de zone de recharge
	    for (Ville ville : villes) {
	        if (!contientZoneRecharge(ville)) {
	            newRechargeCities.add(ville);
	        }
	    }
        // Pour chaque ville sans zone de recharge, ajouter une zone de recharge correspondante
	    for (Ville newRechargeCity : newRechargeCities) {
	        parkings.add(new Parking(newRechargeCity));
	    }
	}

	public void ajusterZonesRechargeConnectees(Ville ville) {
        // Parcourir les routes pour trouver les villes connectées
	    for (Route route : routes) {
            // Vérifier si la ville de départ (VilleA) est égale à la ville spécifiée et si la ville d'arrivée (VilleB) n'a pas de zone de recharge
            if (route.getVilleA().equals(ville) && !contientZoneRecharge(route.getVilleB())) {
	            parkings.add(new Parking(route.getVilleB()));
	            System.out.println(route.getVilleB().getNom() + "est dans la zone de recharge.");
            // Vérifier si la ville d'arrivée (VilleB) est égale à la ville spécifiée et si la ville de départ (VilleA) n'a pas de zone de recharge
            } else if (route.getVilleB().equals(ville) && !contientZoneRecharge(route.getVilleA())) {
	            parkings.add(new Parking(route.getVilleA()));
	            System.out.println(route.getVilleA().getNom() + "est dans la zone de recharge.");
	        }
	    }
	}

	public void afficherMenuPrincipal() {
	    System.out.println("\nMenu :");
	    System.out.println("1) Ajouter une route");
	    System.out.println("2) Fin");
	    System.out.print("\nVotre choix :\n");
	}

	public void afficherVillesAvecRecharge() {
        System.out.println("\nVilles rechargées :");
        // Une boucle pour parcourir la liste des zones de recharge et afficher les noms des villes avec des zones de recharge
        parkings.forEach(parking -> System.out.println("- " + parking.getVille().getNom()));
    }

    public void afficherMenuZonesRecharge() {
        System.out.println("\nMenu Zones de Recharge :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Afficher la situation des différentes villes");
        System.out.println("4) Fin");
        System.out.print("\nVotre choix :\n");
    }
    
    public void afficherVillesAvecOuSansRecharge() {
        System.out.println("\nVilles avec leur propre zone de recharge :");
        for (Parking parking : getVillesAvecSourceRecharge()) {
            System.out.println("- " + parking.getVille().getNom());
        }
        System.out.println("\nVilles rechargées sans leur propre zone de recharge :");
        for (Parking parking : getVillesRechargeesSansSource()) {
            System.out.println("- " + parking.getVille().getNom());
        }
        System.out.println("\nVilles non rechargées :");
        for (Ville ville : getVillesSansZoneRecharge()) {
            System.out.println("- " + ville.getNom());
        }
    }
    
    public List<Parking> getVillesAvecSourceRecharge() {
        // Un flux pour filtrer les zones de recharge et récupérer celles qui correspondent aux villes avec leur propre zone de recharge, puis les collecter dans une liste
        return parkings.stream().filter(Parking::estSourceRecharge).collect(Collectors.toList());
    }

    public List<Parking> getVillesRechargeesSansSource() {
        // Un flux pour filtrer les zones de recharge et récupérer celles qui correspondent aux villes rechargées sans leur propre zone de recharge, puis les collecter dans une liste
        return parkings.stream().filter(parking -> !parking.estSourceRecharge()).collect(Collectors.toList());
    }

    public List<Ville> getVillesSansZoneRecharge() {
        List<Ville> villesSansZoneRecharge = new ArrayList<>();
        // Parcourir la liste des villes pour trouver celles qui n'ont pas de zone de recharge et qui ne sont pas des sources de recharge pour d'autres villes
        for (Ville ville : villes) {
            if (!contientZoneRecharge(ville) && !ville.getSourceVille()) {
                villesSansZoneRecharge.add(ville);
            }
        }
        return villesSansZoneRecharge;
    }

    public void ajouterZoneRechargeMenu(Scanner scanner) {
	    System.out.print("\nVeuillez entrer le nom de la ville où vous souhaitez ajouter une zone de recharge :\n");
	    String nomVille = scanner.nextLine();
	    Ville ville = trouverVilleParNom(nomVille);
	    if (ville != null) {
	        if (!ville.getSourceVille() ) {
	            ville.setSourceVilleTrue();
                // Vérifier si la ville n'a pas encore de zone de recharge, puis ajouter une zone de recharge correspondante
                if(!contientZoneRecharge(ville)) {
	            	parkings.add(new Parking(ville));
	            }
	            ajusterZonesRechargeConnectees(ville);
                // Vérifier si l'ajout respecte la contrainte d'accessibilité
                if (respecteContrainte(ville)) {
	                System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
	            } else {
	                parkings.removeIf(parking -> parking.getVille().equals(ville));
	                System.out.println("Impossible d'ajouter la zone de recharge à " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
	            }
	        } else {
	            System.out.println("Il y a déjà une zone de recharge dans " + ville.getNom() + ".");
	        }
	    } else {
	        System.out.println("Ville non trouvée. Veuillez réessayer.");
	    }
	}

    public void ajouterRouteMenu(Scanner scanner) {
        System.out.print("\nVeuillez entrer les noms des villes entre lesquelles vous souhaitez ajouter une route (séparés par un espace) :\n");
        String[] nomsVilles = scanner.nextLine().split(" ");
        if (nomsVilles.length == 2) {
            Ville villeA = trouverVilleParNom(nomsVilles[0]);
            Ville villeB = trouverVilleParNom(nomsVilles[1]);
            if (villeA != null && villeB != null) {
                if (!existeRouteEntreVilles(villeA, villeB)) {
                    ajouterRoute(villeA, villeB);
                    System.out.println("\nRoute ajoutée entre " + villeA.getNom() + " et " + villeB.getNom() + ".");
                } else {
                    System.out.println("\nLa route entre " + villeA.getNom() + " et " + villeB.getNom() + " a déjà été ajoutée.");
                }
            } else {
                System.out.println("\nVille(s) non trouvée(s). Veuillez réessayer.");
            }
        } else {
            System.out.println("\nNombre incorrect de noms de villes. Veuillez entrer deux noms de villes.");
        }
    }

    public boolean existeRouteEntreVilles(Ville villeA, Ville villeB) {
        for (Route route : routes) {
            if ((route.getVilleA().equals(villeA) && route.getVilleB().equals(villeB)) ||
                    (route.getVilleA().equals(villeB) && route.getVilleB().equals(villeA))) {
                return true;
            }
        }
        return false;
    }

	public void ajouterRoute(Ville villeA, Ville villeB) {
	    Route route = new Route(villeA, villeB);
	    routes.add(route);
        // Mettre à jour le graphe des relations en ajoutant une zone de recharge entre les noms des villes
        graph.addEdge(villeA.getNom().name(), villeB.getNom().name());
	}

	public void ajouterVille(Ville ville) {
	    villes.add(ville);
	    graph.addVertex(ville.getNom().name()); // Ajouter un sommet correspondant à la ville dans le graphe des relations
	}

	public void retirerZoneRecharge(Ville ville) {
        System.out.println("Tentative de retrait de la zone de recharge de " + ville.getNom());
        // Vérifier si le retrait de la zone de recharge de la ville violerait la contrainte d'accessibilité
        if (!peutRetirerZoneRecharge(ville)) {
            System.out.println("Impossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
            return;
        }
        // Vérifier si une ville connectée possède une source, si c'est le cas, la ville actuelle reste chargée
        boolean connectedCityHasSource = false;
        for (Route route : routes) {
            if (route.getVilleA().equals(ville) && contientZoneRecharge(route.getVilleB()) && route.getVilleB().getSourceVille()) {
                connectedCityHasSource = true;
                break;
            } else if (route.getVilleB().equals(ville) && contientZoneRecharge(route.getVilleA()) && route.getVilleA().getSourceVille()) {
                connectedCityHasSource = true;
                break;
            }
        }
        // Si aucune ville connectée a une source, retirer la zone de recharge de la ville
        if (!connectedCityHasSource) {
            parkings.removeIf(parking -> parking.getVille().equals(ville));
            // Si la ville n'a plus de zone de recharge connectée et était une source, marquer la ville comme non source
            if (!contientZoneRechargeConnectee(ville) && ville.getSourceVille()) {
                ville.setSourceVilleFalse();
            }
            System.out.println("Zone de recharge retirée de " + ville.getNom() + ".");
        } else {
            System.out.println("La zone de recharge de " + ville.getNom() + " ne peut pas être retirée car une ville connectée a encore une source.");
        }
    }

    public void retirerZonesRechargeConnectees(Ville ville) {
        System.out.println("Tentative de retrait des zones de recharge connectées à " + ville.getNom());
        List<Ville> villesConnectees = new ArrayList<>(); // Une liste pour stocker les villes connectées avec des zones de recharge et qui ne sont pas des sources
        // Parcourir les routes pour trouver les villes connectées avec des zones de recharge et qui ne sont pas des sources
        for (Route route : routes) {
            if (route.getVilleA().equals(ville) && contientZoneRecharge(route.getVilleB()) && !route.getVilleB().getSourceVille()) {
                villesConnectees.add(route.getVilleB());
            } else if (route.getVilleB().equals(ville) && contientZoneRecharge(route.getVilleA()) && !route.getVilleA().getSourceVille()) {
                villesConnectees.add(route.getVilleA());
            }
        }
        // Parcourir la liste des villes connectées pour retirer leurs zones de recharge
        for (Ville villeConnectee : villesConnectees) {
            retirerZoneRecharge(villeConnectee);
            System.out.println(villeConnectee.getNom() + " a été retirée de la zone de recharge.");
        }
        System.out.println(ville.getNom() + " a été retirée de la zone de recharge.");
    }

    public void retirerZoneRechargeMenu(Scanner scanner) {
	    System.out.print("\nVeuillez entrer le nom de la ville où vous souhaitez retirer une zone de recharge :\n");
	    String nomVille = scanner.nextLine();
	    Ville ville = trouverVilleParNom(nomVille);
	    if (ville != null) {
            // Vérifier si la ville contient une zone de recharge, peut retirer la zone et est une source
            if (contientZoneRecharge(ville) && peutRetirerZoneRecharge(ville) && ville.getSourceVille()) {
	            retirerZoneRecharge(ville);
	            ville.setSourceVilleFalse();
	            retirerZonesRechargeConnectees(ville);
                System.out.println("Zone de recharge retirée de " + ville.getNom() + ".");
	        } else if (!contientZoneRecharge(ville)) {
	            System.out.println("Il n'y a pas de zone de recharge dans " + ville.getNom() + ".");
	        } else {
	            System.out.println("Impossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
	        }
	    } else {
	        System.out.println("Ville non trouvée. Veuillez réessayer.");
	    }
	}

	public boolean peutRetirerZoneRecharge(Ville ville) {
        // Parcourir les routes pour vérifier si une zone de recharge peut être retirée sans violer les contraintes
        for (Route route : routes) {
            if ((route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) && !contientZoneRecharge(route.getVilleA()) && !contientZoneRecharge(route.getVilleB()) ) {
                return false; // Si une zone de recharge ne peut pas être retirée sans violer les contraintes, retourner faux
            }
        }
        return true; // Si aucune violation de contrainte n'est détectée, retourner vrai
    }

    public boolean contientZoneRecharge(Ville ville) {
        // Parcourir la liste des zones de recharge pour vérifier si la ville contient une zone de recharge
        for (Parking parking : parkings) {
            if (parking.getVille().equals(ville)) {
                return true; // Si une zone de recharge est trouvée, retourner vrai
            }
        }
        return false; // Si aucune zone de recharge n'est trouvée, retourner faux
    }
    
    public boolean respecteContrainte(Ville ville) {
        if (!contientZoneRecharge(ville)) {
            return false;
        }
        // Parcourir les routes pour vérifier si la ville est reliée à des villes sans zone de recharge et pas chargées
        for (Route route : routes) {
            Ville autreVille = route.getVilleA().equals(ville) ? route.getVilleB() : route.getVilleA();
            if (!contientZoneRecharge(autreVille) && !estRelieeAvecBorne(autreVille)) {
                return false;
            }
        }
        return true;
    }

    public boolean estRelieeAvecBorne(Ville ville) {
        // Parcourir les routes pour vérifier si la ville est reliée à des villes avec des zones de recharge ou chargées
        for (Route route : routes) {
            if (route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) {
                if (contientZoneRecharge(route.getVilleA()) || contientZoneRecharge(route.getVilleB())) {
                    return true; // Si une connexion avec une zone de recharge ou une ville chargée est trouvée, retourner vrai
                }
            }
        }
        return false; // Si aucune connexion avec une zone de recharge ou une ville chargée n'est trouvée, retourner faux
    }

    public boolean contientZoneRechargeConnectee(Ville ville) {
        // Parcourt les routes pour vérifier si la ville est connectée à des villes avec des zones de recharge
        for (Route route : routes) {
            if ((route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) && (contientZoneRecharge(route.getVilleA()) || contientZoneRecharge(route.getVilleB()))) {
                return true; // Si une connexion avec une zone de recharge est trouvée, retourner vrai
            }
        }
        return false; // Si aucune connexion avec une zone de recharge n'est trouvée, retourner faux
    }

    // Pour les tests unitaires
    public List<Ville> getVilles() {
        return villes;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Parking> getParkings() {
        return parkings;
    }

}
