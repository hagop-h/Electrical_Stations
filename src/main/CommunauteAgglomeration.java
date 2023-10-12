package main;



import java.util.*;

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

    public void ajouterVille(Ville ville) {
        villes.add(ville);
        graph.addVertex(ville.getNom().name());
    }

    public void ajouterRoute(Ville villeA, Ville villeB) {
        Route route = new Route(villeA, villeB);
        routes.add(route);
        graph.addEdge(villeA.getNom().name(), villeB.getNom().name());
    }

    public void genererSolutionNaive() {
        // Mark all cities as source recharge
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

        // Ajouter des routes entre les villes (à titre d'exemple)
        if (villes.size() > 1) {
            for (int i = 0; i < villes.size() - 1; i++) {
                ajouterRoute(villes.get(i), villes.get(i + 1));
            }
        }
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

    public void configurerCommunaute() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Veuillez entrer le nombre de villes (inférieur à 26) : ");
        int nombreVilles = lireEntier(scanner);

        for (int i = 0; i < nombreVilles; i++) {
            ajouterVille(new Ville(NomVille.Nom.values()[i]));
        }

        // Display available cities
        System.out.println("Villes disponibles pour manipulation :");
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
                    System.out.println("La communauté d’agglomération est générée avec la solution naive qui consiste à placer une zone de recharge dans chaque ville.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 2);
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
                .filter(ville -> ville.getNom().name().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }


    public void afficherMenuPrincipal() {
        System.out.println("\nMenu :");
        System.out.println("1) Ajouter une route");
        System.out.println("2) Fin");
        System.out.print("Votre choix : ");
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

    public void afficherVillesAvecRecharge() {
        System.out.println("Villes avec zone de recharge :");
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
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Afficher villes sans recharge");
        System.out.println("4) Fin");
        System.out.print("Votre choix : ");
    }
    
    public void afficherVillesAvecOuSansRecharge() {
        System.out.println("Villes avec zones de recharge :");
        for (Parking parking : parkings) {
            System.out.println("- " + parking.getVille().getNom());
        }

        System.out.println("\nVilles sans zone de recharge :");
        for (Ville ville : villes) {
            if (!contientZoneRecharge(ville)) {
                System.out.println("- " + ville.getNom());
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

        parkings.removeIf(parking -> parking.getVille().equals(ville));

        if (!contientZoneRechargeConnectee(ville) && ville.getSourceVille()) {
            ville.setSourceVilleFalse();
        }
        System.out.println("Zone de recharge retirée de " + ville.getNom() + ".");
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
            parkings.removeIf(parking -> parking.getVille().equals(villeConnectee));
            System.out.println("Zone de recharge retirée de " + villeConnectee.getNom() + ".");
        }
        
        System.out.println("Zones de recharge connectées retirées de " + ville.getNom() + ".");
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

    // Méthode pour vérifier si une ville est reliée directement à une ville avec des bornes
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




}