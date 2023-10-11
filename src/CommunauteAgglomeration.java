package projet_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        graph.addVertex(ville.nom.name());
    }

    public void ajouterRoute(Ville villeA, Ville villeB) {
        Route route = new Route(villeA, villeB);
        routes.add(route);
        graph.addEdge(villeA.nom.name(), villeB.nom.name());
    }

    public void genererSolutionNaive() {
        // Mark all cities as source recharge
        for (Ville ville : villes) {
            ville.setSourceVilleTrue();
        }

        // Ajouter une zone de recharge dans chaque ville
        for (Ville ville : villes) {
            parkings.add(new Parking(ville));
            graph.addVertex(ville.nom.name());
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



    private void mettreAJourVillesAvecRecharge() {
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
            System.out.println("- " + ville.nom);
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

    private void ajouterRouteMenu(Scanner scanner) {
        System.out.print("Veuillez entrer les noms des villes entre lesquelles ajouter une route (séparés par un espace) : ");
        String[] nomsVilles = scanner.nextLine().split(" ");

        if (nomsVilles.length == 2) {
            Ville villeA = trouverVilleParNom(nomsVilles[0]);
            Ville villeB = trouverVilleParNom(nomsVilles[1]);

            if (villeA != null && villeB != null) {
                ajouterRoute(villeA, villeB);
                System.out.println("Route ajoutée entre " + villeA.nom + " et " + villeB.nom + ".");
            } else {
                System.out.println("Villes non trouvées. Veuillez réessayer.");
            }
        } else {
            System.out.println("Nombre incorrect de noms de villes. Veuillez entrer deux noms de villes.");
        }
    }

    private int lireEntier(Scanner scanner) {
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


    private Ville trouverVilleParNom(String nom) {
        return villes.stream()
                .filter(ville -> ville.nom.name().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }


    private void afficherMenuPrincipal() {
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

    private void afficherVillesAvecRecharge() {
        System.out.println("Villes avec zone de recharge :");
        parkings.forEach(parking -> System.out.println("- " + parking.ville.nom));
    }


    private void afficherMenuSolutionManuelle() {
        System.out.println("\nMenu :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Fin");
        System.out.print("Votre choix : ");
    }

    private void afficherMenuZonesRecharge() {
        System.out.println("\nMenu Zones de Recharge :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Afficher villes sans recharge");
        System.out.println("4) Fin");
        System.out.print("Votre choix : ");
    }
    
    private void afficherVillesAvecOuSansRecharge() {
        System.out.println("Villes avec zones de recharge :");
        for (Parking parking : parkings) {
            System.out.println("- " + parking.ville.nom);
        }

        System.out.println("\nVilles sans zone de recharge :");
        for (Ville ville : villes) {
            if (!contientZoneRecharge(ville)) {
                System.out.println("- " + ville.nom);
            }
        }
    }

    private void ajouterZoneRechargeMenu(Scanner scanner) {
        System.out.print("Veuillez entrer le nom de la ville où ajouter une zone de recharge : ");
        String nomVille = scanner.nextLine();

        Ville ville = trouverVilleParNom(nomVille);

        if (ville != null) {
            if (!contientZoneRecharge(ville)) {
            	ville.setSourceVilleTrue();
                parkings.add(new Parking(ville));

                if (respecteContrainte(ville)) {
                    System.out.println("Zone de recharge ajoutée à " + ville.nom + ".");

                    // Iterate through connected cities and add recharge zones for them
                    for (Route route : routes) {
                        if (route.villeA.equals(ville) && !contientZoneRecharge(route.villeB)) {
                            parkings.add(new Parking(route.villeB));
                            System.out.println("Zone de recharge ajoutée à " + route.villeB.nom + ".");
                        } else if (route.villeB.equals(ville) && !contientZoneRecharge(route.villeA)) {
                            parkings.add(new Parking(route.villeA));
                            System.out.println("Zone de recharge ajoutée à " + route.villeA.nom + ".");
                        }
                    }
                } else {
                    // Remove the added parking if it violates the constraint
                    parkings.removeIf(parking -> parking.ville.equals(ville));
                    System.out.println("Impossible d'ajouter la zone de recharge à " + ville.nom +
                            ". Cela violerait la contrainte d'accessibilité.");
                }
            } else {
                System.out.println("Il y a déjà une zone de recharge dans " + ville.nom + ".");
            }
        } else {
            System.out.println("Ville non trouvée. Veuillez réessayer.");
        }
    }

    private void retirerZoneRechargeMenu(Scanner scanner) {
        System.out.print("Veuillez entrer le nom de la ville où retirer une zone de recharge : ");
        String nomVille = scanner.nextLine();

        Ville ville = trouverVilleParNom(nomVille);

        if (ville != null) {
            if (contientZoneRecharge(ville) && peutRetirerZoneRecharge(ville) && ville.getSourceVille()){
                retirerZoneRecharge(ville);
                ville.setSourceVilleFalse();
                
                System.out.println("Zone de recharge retirée de " + ville.nom + ".");

                for (Route route : routes) {
                    if (route.villeA.equals(ville) || route.villeB.equals(ville)) {
                        graph.removeEdge(route.villeA.nom.name(), route.villeB.nom.name());
                    }
                }
            } else if (!contientZoneRecharge(ville)) {
                System.out.println("Il n'y a pas de zone de recharge dans " + ville.nom + ".");
            } else {
                System.out.println("Impossible de retirer la zone de recharge de " + ville.nom +
                        ". Cela violerait la contrainte d'accessibilité ou ce n'est pas la source de la recharge.");
            }
        } else {
            System.out.println("Ville non trouvée. Veuillez réessayer.");
        }
    }


    private void retirerZoneRecharge(Ville ville) {

    	parkings.removeIf(parking -> parking.ville.equals(ville));

        retirerZonesRechargeConnectees(ville);
    }

    private void retirerZonesRechargeConnectees(Ville ville) {
        for (Route route : routes) {
            if (route.villeA.equals(ville) && contientZoneRecharge(route.villeB) && !route.villeB.getSourceVille()) {
                parkings.removeIf(parking -> parking.ville.equals(route.villeB));
            } else if (route.villeB.equals(ville) && contientZoneRecharge(route.villeA) && !route.villeA.getSourceVille()) {
                parkings.removeIf(parking -> parking.ville.equals(route.villeA));
            }
        }
    }
    
    private boolean peutRetirerZoneRecharge(Ville ville) {
        for (Route route : routes) {
            if ((route.villeA.equals(ville) || route.villeB.equals(ville)) && !contientZoneRecharge(route.villeA) && !contientZoneRecharge(route.villeB) ) {
                return false;
            }
        }
        return true;
    }


    private boolean contientZoneRecharge(Ville ville) {
        for (Parking parking : parkings) {
            if (parking.ville.equals(ville)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean respecteContrainte(Ville ville) {
        if (!contientZoneRecharge(ville)) {
            // La ville doit avoir ses bornes ou être reliée à une ville avec des bornes
            return false;
        }

        for (Route route : routes) {
            if (route.villeA.equals(ville)) {
                // Si la route va vers villeA, vérifie que villeB a des bornes ou est reliée à une ville avec des bornes
                if (!contientZoneRecharge(route.villeB) && !estRelieeAvecBorne(route.villeB)) {
                    return false;
                }
            } else if (route.villeB.equals(ville)) {
                // Si la route va vers villeB, vérifie que villeA a des bornes ou est reliée à une ville avec des bornes
                if (!contientZoneRecharge(route.villeA) && !estRelieeAvecBorne(route.villeA)) {
                    return false;
                }
            }
        }
        return true;
    }
    // Méthode pour vérifier si une ville est reliée directement à une ville avec des bornes
    private boolean estRelieeAvecBorne(Ville ville) {
        for (Route route : routes) {
            if (route.villeA.equals(ville) || route.villeB.equals(ville)) {
                if (contientZoneRecharge(route.villeA) || contientZoneRecharge(route.villeB)) {
                    return true;
                }
            }
        }
        return false;
    }
   
  
}
