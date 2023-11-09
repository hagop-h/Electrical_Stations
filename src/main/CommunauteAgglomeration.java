package main;

import java.util.*;
import java.util.stream.*;

public class CommunauteAgglomeration {

    private List<Ville> villes;
    private List<Route> routes;
    private List<Parking> parkings;
    private Graph graph; // Graphe pour représenter les relations
 
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
            parkings.add(new Parking(ville));
            graph.addVertex(ville.getNom().name());
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
        for (int i = 0; i < nombreVilles; i++) {
            ajouterVille(new Ville(NomVille.values()[i]));
        }
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
        } while (choixMenu != 2);
    }

    public int lireEntier(Scanner scanner) {
        while (true) {
            try {
                int result = scanner.nextInt();
                scanner.nextLine();
                return result;
            } catch (InputMismatchException e) {
                System.out.println("\nVeuillez entrer un nombre entier :");
                scanner.nextLine();
            }
        }
    }

    public Ville trouverVilleParNom(String nom) {
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
        } while (choixMenu != 4);
    }

    public void mettreAJourVillesAvecRecharge() {
	    List<Ville> newRechargeCities = new ArrayList<>();
	    for (Ville ville : villes) {
	        if (!contientZoneRecharge(ville)) {
	            newRechargeCities.add(ville);
	        }
	    }
	    for (Ville newRechargeCity : newRechargeCities) {
	        parkings.add(new Parking(newRechargeCity));
	    }
	}

	private void ajusterZonesRechargeConnectees(Ville ville) {
	    for (Route route : routes) {
	        if (route.getVilleA().equals(ville) && !contientZoneRecharge(route.getVilleB())) {
	            parkings.add(new Parking(route.getVilleB()));
	            System.out.println(route.getVilleB().getNom() + "est dans la zone de recharge.");
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
        parkings.forEach(parking -> System.out.println("- " + parking.getVille().getNom()));
    }

    public void afficherMenuZonesRecharge() {
        System.out.println("\nMenu Zones de Recharge :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Afficher la situation des villes");
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
        return parkings.stream().filter(Parking::estSourceRecharge).collect(Collectors.toList());
    }

    public List<Parking> getVillesRechargeesSansSource() {
        return parkings.stream().filter(parking -> !parking.estSourceRecharge()).collect(Collectors.toList());
    }

    public List<Ville> getVillesSansZoneRecharge() {
        List<Ville> villesSansZoneRecharge = new ArrayList<>();
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
	            if(!contientZoneRecharge(ville)) {
	            	parkings.add(new Parking(ville));
	            }
	            ajusterZonesRechargeConnectees(ville);
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
	    graph.addEdge(villeA.getNom().name(), villeB.getNom().name());
	}

	public void ajouterVille(Ville ville) {
	    villes.add(ville);
	    graph.addVertex(ville.getNom().name());
	}

	public void retirerZoneRecharge(Ville ville) {
        System.out.println("Tentative de retrait de la zone de recharge de " + ville.getNom());
        if (!peutRetirerZoneRecharge(ville)) {
            System.out.println("Impossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
            return;
        }
        // Vérifier si une ville connectée possède une source, si c'est le cas, A reste dans les parkings
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
        if (!connectedCityHasSource) {
            parkings.removeIf(parking -> parking.getVille().equals(ville));
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
        List<Ville> villesConnectees = new ArrayList<>();
        for (Route route : routes) {
            if (route.getVilleA().equals(ville) && contientZoneRecharge(route.getVilleB()) && !route.getVilleB().getSourceVille()) {
                villesConnectees.add(route.getVilleB());
            } else if (route.getVilleB().equals(ville) && contientZoneRecharge(route.getVilleA()) && !route.getVilleA().getSourceVille()) {
                villesConnectees.add(route.getVilleA());
            }
        }
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
        for (Route route : routes) {
            if ((route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) && !contientZoneRecharge(route.getVilleA()) && !contientZoneRecharge(route.getVilleB()) ) {
                return false;
            }
        }
        return true;
    }

    public boolean contientZoneRecharge(Ville ville) {
        for (Parking parking : parkings) {
            if (parking.getVille().equals(ville)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean respecteContrainte(Ville ville) {
        if (!contientZoneRecharge(ville)) {
            return false;
        }
        for (Route route : routes) {
            Ville autreVille = route.getVilleA().equals(ville) ? route.getVilleB() : route.getVilleA();
            if (!contientZoneRecharge(autreVille) && !estRelieeAvecBorne(autreVille)) {
                return false;
            }
        }
        return true;
    }

    public boolean estRelieeAvecBorne(Ville ville) {
        for (Route route : routes) {
            if (route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) {
                if (contientZoneRecharge(route.getVilleA()) || contientZoneRecharge(route.getVilleB())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contientZoneRechargeConnectee(Ville ville) {
        for (Route route : routes) {
            if ((route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) && (contientZoneRecharge(route.getVilleA()) || contientZoneRecharge(route.getVilleB()))) {
                return true;
            }
        }
        return false;
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
