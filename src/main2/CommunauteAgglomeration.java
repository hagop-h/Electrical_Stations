package main2;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CommunauteAgglomeration {
    private List<Ville> villes;
    private List<Route> routes;
    private List<Parking> parkings;
    private Graph graph; // graph pour representer les relations

    public CommunauteAgglomeration() {
        villes = new ArrayList<>();
        routes = new ArrayList<>();
        parkings = new ArrayList<>();
        graph = new Graph();
    }    
    
    

    public void genererSolutionNaive() {
        // Tout les villes ont SOURCE recharge
        for (Ville ville : villes) {
            ville.setSourceVilleTrue();
        }

        // Ajouter une zone de recharge dans chaque ville
        for (Ville ville : villes) {
            parkings.add(new Parking(ville));
            graph.addVertex(ville.getNom());
        }

        // Mettre à jour la liste des villes avec des zones de recharge
        mettreAJourVillesAvecRecharge();

        
    }

    public int lireEntier(Scanner scanner) {
        while (true) {
            try {
                int result = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                return result;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre entier.");
                scanner.nextLine(); // To consume the remaining input
            }
        }
    }
    

    public Ville trouverVilleParNom(String nom) {
    	
        return villes.stream()
                .filter(ville -> ville.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }


    public void trouverSolutionManuelle() {
        Scanner scanner = new Scanner(System.in);
        int choixMenu;

        do {
            afficherVillesAvecRecharge();
            afficherMenuSolutionManuelle();
            choixMenu = lireEntier(scanner);

            switch (choixMenu) {
                case 1:
                    ajouterZoneRechargeMenu(scanner);
                    break;
                case 2:
                    retirerZoneRechargeMenu(scanner);
                    break;
                case 3:
                    System.out.println("Fin de la gestion manuelle.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 3);
    }

    public void menuPrincipal() {
        Scanner scanner = new Scanner(System.in);
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
                    System.out.println("La communauté d’agglomération est générée avec la solution naive qui consiste à placer une zone de recharge dans chaque ville.");
                    break;
                case 3:
                    gererMenuZonesRecharge();
                    break;
                case 4:
                    System.out.println("Veuillez entrer le chemin du fichier pour sauvegarder la solution : ");
                    String cheminFichier = scanner.nextLine();
                    sauvegarderSolution(cheminFichier);
                    break;
                case 5:
                    System.out.println("Fin du programme.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 5);
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
                    System.out.println("Fin de la gestion des zones de recharge.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
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
	            System.out.println("Zone de recharge ajoutée à " + route.getVilleB().getNom() + ".");
	        } else if (route.getVilleB().equals(ville) && !contientZoneRecharge(route.getVilleA())) {
	            parkings.add(new Parking(route.getVilleA()));
	            System.out.println("Zone de recharge ajoutée à " + route.getVilleA().getNom() + ".");
	        }
	    }
	}

	public void afficherMenuPrincipal() {
	    System.out.println("\nMenu :");
	    System.out.println("1) Ajouter une route");
	    System.out.println("2) Fin");
	    System.out.print("Votre choix : ");
	}

	public void afficherVillesAvecRecharge() {
        System.out.println("Villes qui sont recharge avec ou sans SOURCE :");
        parkings.forEach(parking -> System.out.println("- " + parking.getVille().getNom()));
    }


    public void afficherMenuSolutionManuelle() {
        System.out.println("\nMenu :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Fin");
        System.out.print("Votre choix : ");
    }

    public void afficherMenuZonesRecharge() {
        System.out.println("\nMenu Zones de Recharge :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une SOURCE de recharge");
        System.out.println("3) Afficher villes sans recharge");
        System.out.println("4) Fin");
        System.out.print("Votre choix : ");
    }
    
 // Dans la classe CommunauteAgglomeration
    public void afficherVillesAvecOuSansRecharge() {
        System.out.println("Villes avec zone de recharge (source) :");
        for (Parking parking : getVillesAvecSourceRecharge()) {
            System.out.println("- " + parking.getVille().getNom());
        }

        System.out.println("\nVilles rechargées sans source :");
        for (Parking parking : getVillesRechargeesSansSource()) {
            System.out.println("- " + parking.getVille().getNom());
        }

        System.out.println("\nVilles sans zone de recharge :");
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
	    System.out.print("Veuillez entrer le nom de la ville où ajouter une zone de recharge : ");
	    String nomVille = scanner.nextLine();
	
	    Ville ville = trouverVilleParNom(nomVille);
	
	    if (ville != null) {
	        if (!contientZoneRecharge(ville)) {
	            ville.setSourceVilleTrue();
	            parkings.add(new Parking(ville));
	
	            ajusterZonesRechargeConnectees(ville);
	
	            if (respecteContrainte(ville)) {
	                System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
	            } else {
	                // Remove the added parking if it violates the constraint
	                parkings.removeIf(parking -> parking.getVille().equals(ville));
	                System.out.println("Impossible d'ajouter la zone de recharge à " + ville.getNom() +
	                        ". Cela violerait la contrainte d'accessibilité.");
	            }
	        } else {
	            System.out.println("Il y a déjà une zone de recharge dans " + ville.getNom() + ".");
	        }
	    } else {
	        System.out.println("Ville non trouvée. Veuillez réessayer.");
	    }
	}

	public void ajouterRouteMenu(Scanner scanner) {
	    System.out.print("Veuillez entrer les noms des villes entre lesquelles ajouter une route (séparés par un espace) : ");
	    String[] nomsVilles = scanner.nextLine().split(" ");
	
	    if (nomsVilles.length == 2) {
	        Ville villeA = trouverVilleParNom(nomsVilles[0]);
	        Ville villeB = trouverVilleParNom(nomsVilles[1]);
	
	        if (villeA != null && villeB != null) {
	            ajouterRoute(villeA, villeB);
	            System.out.println("Route ajoutée entre " + villeA.getNom() + " et " + villeB.getNom() + ".");
	        } else {
	            System.out.println("Villes non trouvées. Veuillez réessayer.");
	        }
	    } else {
	        System.out.println("Nombre incorrect de noms de villes. Veuillez entrer deux noms de villes.");
	    }
	}

	private void ajouterRoute(Ville villeA, Ville villeB) {
	    if (villeA != null && villeB != null) {
	        Route route = new Route(villeA, villeB);
	        routes.add(route);
	        graph.addEdge(villeA.getNom(), villeB.getNom());
	    } else {
	        System.out.println("Villes non trouvées. Veuillez réessayer.");
	    }
	}

	public void ajouterVille(Ville ville) {
	    villes.add(ville);
	    graph.addVertex(ville.getNom());
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
            System.out.println("Zone de recharge retirée de " + villeConnectee.getNom() + ".");
        }

        System.out.println("Zones de recharge connectées retirées de " + ville.getNom() + ".");
    }



    
    public void retirerZoneRechargeMenu(Scanner scanner) {
	    System.out.print("Veuillez entrer le nom de la ville où retirer une zone de recharge : ");
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
	            System.out.println("Impossible de retirer la zone de recharge de " + ville.getNom() +
	                    ". Cela violerait la contrainte d'accessibilité ou ce n'est pas la source de la recharge.");
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
            // La ville doit avoir ses bornes ou être reliée à une ville avec des bornes
            return false;
        }

        for (Route route : routes) {
            if (route.getVilleA().equals(ville)) {
                // Si la route va vers villeA, vérifie que villeB a des bornes ou est reliée à une ville avec des bornes
                if (!contientZoneRecharge(route.getVilleB()) && !estRelieeAvecBorne(route.getVilleB())) {
                    return false;
                }
            } else if (route.getVilleB().equals(ville)) {
                // Si la route va vers villeB, vérifie que villeA a des bornes ou est reliée à une ville avec des bornes
                if (!contientZoneRecharge(route.getVilleA()) && !estRelieeAvecBorne(route.getVilleA())) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean contientZoneRechargeConnectee(Ville ville) {
	    for (Route route : routes) {
	        if (route.getVilleA().equals(ville) && contientZoneRecharge(route.getVilleB())) {
	            return true;
	        } else if (route.getVilleB().equals(ville) && contientZoneRecharge(route.getVilleA())) {
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
    
    
    public void genererSolutionInitiale() {
        if (villes.isEmpty()) {
            System.out.println("Aucune ville à prendre en compte pour la solution initiale.");
            return;
        }

        for (Ville ville : villes) {
            ville.setSourceVilleTrue();
            parkings.add(new Parking(ville));
            graph.addVertex(ville.getNom());
        }

        ajusterZonesRechargeCommunaute();
    }
   
    private void ajusterZonesRechargeCommunaute() {
        for (Ville ville : villes) {
            if (!contientZoneRecharge(ville)) {
                parkings.add(new Parking(ville));
            }
        }
    }


    public void retirerZoneRecharge(Ville ville) {
        System.out.println("Tentative de retrait de la zone de recharge de " + ville.getNom());

        if (!peutRetirerZoneRecharge(ville)) {
            System.out.println("Impossible de retirer la zone de recharge de " + ville.getNom() +
                    ". Cela violerait la contrainte d'accessibilité.");
            return;
        }

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

    public void chargerCommunaute(String cheminFichier) {
        try (Scanner scanner = new Scanner(new File(cheminFichier))) {
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();

                if (ligne.startsWith("ville")) {
                    String[] elements = ligne.split("[()]");
                    if (elements.length >= 2) {
                        String nomVille = elements[1].trim();
                        villes.add(new Ville(nomVille));
                    } 
                } else if (ligne.startsWith("route")) {
                    String[] elements = ligne.split("[(),.]");
                    if (elements.length >= 5) {
                        String nomVilleA = elements[1].trim();
                        String nomVilleB = elements[2].trim();
                        ajouterRoute(nomVilleA, nomVilleB);
                    } 
                } else if (ligne.startsWith("recharge")) {
                    String[] elements = ligne.split("[()]");
                    if (elements.length >= 2) {
                        String nomVille = elements[1].trim();
                        recharge(nomVille);
                    }
               }
                else {
                	continue;
                }
            }
            
            System.out.println("Communauté chargée depuis le fichier : " + cheminFichier);
        } catch (FileNotFoundException e) {
            System.err.println("Fichier non trouvé : " + cheminFichier);
        }
    }

    private void ajouterRoute(String nomVilleA, String nomVilleB) {
        Ville villeA = trouverVilleParNom(nomVilleA);
        Ville villeB = trouverVilleParNom(nomVilleB);

        if (villeA != null && villeB != null) {
            ajouterRoute(villeA, villeB);
        } else {
            System.out.println("Villes non trouvées. Veuillez réessayer.");
        }
    }

    public void recharge(String nomVille) {
	    
	    Ville ville = trouverVilleParNom(nomVille);
	
	    if (ville != null) {
	        if (!contientZoneRecharge(ville)) {
	            ville.setSourceVilleTrue();
	            parkings.add(new Parking(ville));
	
	            ajusterZonesRechargeConnectees(ville);
	
	            if (respecteContrainte(ville)) {
	                System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
	            } else {
	                // Remove the added parking if it violates the constraint
	                parkings.removeIf(parking -> parking.getVille().equals(ville));
	                System.out.println("Impossible d'ajouter la zone de recharge à " + ville.getNom() +
	                        ". Cela violerait la contrainte d'accessibilité.");
	            }
	        } else {
	            System.out.println("Il y a déjà une zone de recharge dans " + ville.getNom() + ".");
	        }
	    } else {
	        System.out.println("Ville non trouvée. Veuillez réessayer.");
	    }
	}


    public void resoudreAutomatiquementAlgo1(int k) {
        Random random = new Random();
        int i = 0;

        while (i < k) {
            Ville ville = villes.get(random.nextInt(villes.size()));
            if (ville.getSourceVille()) {
                ville.setSourceVilleFalse();
            } else {
                ville.setSourceVilleTrue();
            }
            i++;
        }

        mettreAJourVillesAvecRecharge();
    }

    public void resoudreAutomatiquementAlgo2(int k) {
        int i = 0;
        int scoreCourant = score();

        while (i < k) {
            Ville ville = villes.get(new Random().nextInt(villes.size()));

            if (ville.getSourceVille()) {
                ville.setSourceVilleFalse();
            } else {
                ville.setSourceVilleTrue();
            }

            int nouveauScore = score();
            if (nouveauScore < scoreCourant) {
                i = 0;
                scoreCourant = nouveauScore;
            } else {
                i++;
            }
        }

        mettreAJourVillesAvecRecharge();
    }

    // Ajouter la méthode score pour calculer le score actuel
    public int score() {
        return (int) parkings.stream().filter(Parking::estSourceRecharge).count();
    }

    // Modifier la méthode sauvegarderSolution pour inclure les routes
    public void sauvegarderSolution(String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            for (Ville ville : villes) {
                writer.write(ville.getNom() + " " + ville.getSourceVille());
                writer.newLine();
            }

            for (Route route : routes) {
                writer.write("route " + route.getVilleA().getNom() + " " + route.getVilleB().getNom());
                writer.newLine();
            }

            System.out.println("Solution sauvegardée dans : " + cheminFichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de la solution : " + e.getMessage());
        }
    }




}