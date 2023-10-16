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
    private List<Charger> charger;
    private Graph graph; // graph pour representer les relations

    public CommunauteAgglomeration() {
        villes = new ArrayList<>();
        routes = new ArrayList<>();
        charger = new ArrayList<>();
        graph = new Graph();
    }    
    
    

    public void genererSolutionNaive() {
        // Tout les villes ont SOURCE recharge
        for (Ville ville : villes) {
            ville.setzoneDeRechargeTrue();
        }

        // Ajouter une zone de recharge dans chaque ville
        for (Ville ville : villes) {
            charger.add(new Charger(ville));
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
        	System.out.println("Score :"+score());
            afficherVillesAvecOuSansRecharge();
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

    
    public void mettreAJourVillesAvecRecharge() {
        List<Ville> newRechargeCities = new ArrayList<>();

        for (Ville ville : villes) {
            if (!contientZoneRecharge(ville) && ville.getzoneDeRecharge()) {
                newRechargeCities.add(ville);
            }
        }

        for (Ville newRechargeCity : newRechargeCities) {
            charger.add(new Charger(newRechargeCity));
        }
    }
 

	private void ajusterZonesRechargeConnectees(Ville ville) {
	    for (Route route : routes) {
	        if (route.getVilleA().equals(ville) && !contientZoneRecharge(route.getVilleB())) {
	            charger.add(new Charger(route.getVilleB()));
	        } else if (route.getVilleB().equals(ville) && !contientZoneRecharge(route.getVilleA())) {
	            charger.add(new Charger(route.getVilleA()));
	        }
	    }
	}

	public void afficherMenuPrincipal() {
	    System.out.println("\nMenu :");
	    System.out.println("1) Ajouter une route");
	    System.out.println("2) Fin");
	    System.out.print("Votre choix : ");
	}





    public void afficherMenuSolutionManuelle() {
        System.out.println("\nMenu :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Fin");
        System.out.print("Votre choix : ");
    }
    

    
 // Dans la classe CommunauteAgglomeration
    public void afficherVillesAvecOuSansRecharge() {
        System.out.println("Villes avec un zone de recharge :");
        for (Charger charger : getVillesAvecSourceRecharge()) {
            System.out.println("- " + charger.getVille().getNom());
        }

        System.out.println("\nVilles dans la zone de recharge :");
        for (Charger charger : getVillesRechargeesSansSource()) {
            System.out.println("- " + charger.getVille().getNom());
        }

        System.out.println("\nVilles sans zone de recharge :");
        for (Ville ville : getVillesSansZoneRecharge()) {
            System.out.println("- " + ville.getNom());
        }
    }
    
    public List<Charger> getVillesAvecSourceRecharge() {
        return charger.stream().filter(Charger::estSourceRecharge).collect(Collectors.toList());
    }

    public List<Charger> getVillesRechargeesSansSource() {
        return charger.stream().filter(parking -> !parking.estSourceRecharge()).collect(Collectors.toList());
    }

    public List<Ville> getVillesSansZoneRecharge() {
        List<Ville> villesSansZoneRecharge = new ArrayList<>();

        for (Ville ville : villes) {
            if (!contientZoneRecharge(ville) && !ville.getzoneDeRecharge()) {
                if (!villesSansZoneRecharge.contains(ville)) {
                    villesSansZoneRecharge.add(ville);
                }
            }
        }

        return villesSansZoneRecharge;
    }




    public void ajouterZoneRechargeMenu(Scanner scanner) {
	    System.out.print("Veuillez entrer le nom de la ville où ajouter une zone de recharge : ");
	    String nomVille = scanner.nextLine();
	
	    Ville ville = trouverVilleParNom(nomVille);
	    if (ville != null) {
	        if (!ville.getzoneDeRecharge() ) {
	            ville.setzoneDeRechargeTrue();
	            if(!contientZoneRecharge(ville)) {
	            	charger.add(new Charger(ville));
	            }
	
	            ajusterZonesRechargeConnectees(ville);
	
	            if (respecteContrainte(ville)) {
	                System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
	            } else {
	                // Remove the added parking if it violates the constraint
	                charger.removeIf(parking -> parking.getVille().equals(ville));
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
        List<Ville> villesConnectees = new ArrayList<>();

        for (Route route : routes) {
            if (route.getVilleA().equals(ville) && contientZoneRecharge(route.getVilleB()) && !route.getVilleB().getzoneDeRecharge()) {
                villesConnectees.add(route.getVilleB());
            } else if (route.getVilleB().equals(ville) && contientZoneRecharge(route.getVilleA()) && !route.getVilleA().getzoneDeRecharge()) {
                villesConnectees.add(route.getVilleA());
            }
        }

        for (Ville villeConnectee : villesConnectees) {
            retirerZoneRecharge(villeConnectee);
        }

    }



    
    public void retirerZoneRechargeMenu(Scanner scanner) {
	    System.out.print("Veuillez entrer le nom de la ville où retirer une zone de recharge : ");
	    String nomVille = scanner.nextLine();
	
	    Ville ville = trouverVilleParNom(nomVille);
	
	    if (ville != null) {
	        if (contientZoneRecharge(ville) && peutRetirerZoneRecharge(ville) && ville.getzoneDeRecharge()) {
	            retirerZoneRecharge(ville);
	            ville.setzoneDeRechargeFalse();
	
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
        for (Charger charger : charger) {
            if (charger.getVille().equals(ville)) {
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

    public List<Charger> getcharger() {
        return charger;
    }
    
    
    public void genererSolutionInitiale() {
        if (villes.isEmpty()) {
            System.out.println("Aucune ville à prendre en compte pour la solution initiale.");
            return;
        }

        for (Ville ville : villes) {
            ville.setzoneDeRechargeTrue();
            charger.add(new Charger(ville));
            graph.addVertex(ville.getNom());
        }

        ajusterZonesRechargeCommunaute();
    }
   
    private void ajusterZonesRechargeCommunaute() {
        for (Ville ville : villes) {
            if (!contientZoneRecharge(ville)) {
                charger.add(new Charger(ville));
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
            if (route.getVilleA().equals(ville) && contientZoneRecharge(route.getVilleB()) && route.getVilleB().getzoneDeRecharge()) {
                connectedCityHasSource = true;
                break;
            } else if (route.getVilleB().equals(ville) && contientZoneRecharge(route.getVilleA()) && route.getVilleA().getzoneDeRecharge()) {
                connectedCityHasSource = true;
                break;
            }
        }

        if (!connectedCityHasSource) {
            charger.removeIf(parking -> parking.getVille().equals(ville));

            if (!contientZoneRechargeConnectee(ville) && ville.getzoneDeRecharge()) {
                ville.setzoneDeRechargeFalse();
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
                        Ville ville = new Ville(nomVille);
                        villes.add(ville);
                        ajouterVille(ville);
                    } 
                } else if (ligne.startsWith("route")) {
                    String[] elements = ligne.split("[(),]");
                    if (elements.length >= 2) {
                        String nomVilleA = elements[1];
                        String nomVilleB = elements[2].substring(1);
                        ajouterRoute(nomVilleA, nomVilleB);
                        
                    } 
                } else if (ligne.startsWith("recharge")) {
                    String[] elements = ligne.split("[()]");
                    if (elements.length >= 1) {
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
	        if (!ville.getzoneDeRecharge() ) {
	            ville.setzoneDeRechargeTrue();
	            if(!contientZoneRecharge(ville)) {
	            	charger.add(new Charger(ville));
	            }
	            ajusterZonesRechargeConnectees(ville);
	
	            if (respecteContrainte(ville)) {
	                System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
	            } else {
	                charger.removeIf(parking -> parking.getVille().equals(ville));
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



    public void resoudreAutomatiquementAlgo2(int k) {
        int i = 0;
        int scoreCourant = score();

        while (i < k) {
            Ville ville = villes.get(new Random().nextInt(villes.size()));

            if (ville.getzoneDeRecharge()) {
                ville.setzoneDeRechargeFalse();
            } else {
                ville.setzoneDeRechargeTrue();
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
        return (int) charger.stream().filter(Charger::estSourceRecharge).count();
    }

    // Modifier la méthode sauvegarderSolution pour inclure les routes
    public void sauvegarderSolution(String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            for (Ville ville : villes) {
                writer.write(ville.getNom() + " " + ville.getzoneDeRecharge());
                writer.newLine();
            }

            for (Route route : routes) {
                writer.write("route " + route.getVilleA().getNom() + " " + route.getVilleB().getNom());
                writer.newLine();
            }

            writer.write("Villes avec zone de recharge (source) :");
            writer.newLine();

            for (Charger charger : getVillesAvecSourceRecharge()) {
            	writer.write("- " + charger.getVille().getNom());
                writer.newLine();

            }

            writer.write("\nVilles rechargées sans source :");
            writer.newLine();

            for (Charger charger : getVillesRechargeesSansSource()) {
            	writer.write("- " + charger.getVille().getNom());
	writer.newLine();
            }

            writer.write("\nVilles sans zone de recharge :");
            writer.newLine();

            for (Ville ville : getVillesSansZoneRecharge()) {
            	writer.write("- " + ville.getNom());
                writer.newLine();

            }
            
            System.out.println("Solution sauvegardée dans : " + cheminFichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de la solution : " + e.getMessage());
        }
    }

}