package phase1;

import java.util.*;
import java.util.stream.*;

/**
 * Classe représentant une communauté d'agglomération
 * Gère les villes, les routes, les zones de recharge et les relations entre les villes
 */
public class CommunauteAgglomeration {
    private List<Ville> villes; // Une liste pour stocker les villes
    private List<Route> routes; // Une liste pour stocker les routes
    private List<ZoneRecharge> zonesRecharge; // Une liste pour stocker les zones de recharge
    private Graphe graphe; // Un graphe pour représenter les relations entre les villes

    /**
     * Constructeur de la classe CommunauteAgglomeration
     * Initialise les listes de villes, de routes, de zone de recharges, et le graphe
     */
    public CommunauteAgglomeration() {
        villes = new ArrayList<>();
        routes = new ArrayList<>();
        zonesRecharge = new ArrayList<>();
        graphe = new Graphe();
    }

    /**
     * Génère une solution naïve où chaque ville a une zone de recharge
     * Ajoute une zone de recharge dans chaque ville et met à jour le graphe
     */
    public void genererSolutionNaive() {
        // Chaque ville a une zone de recharge
        for (Ville ville : villes) {
            ville.setSourceVilleTrue();
        }
        // Ajouter une zone de recharge dans chaque ville
        for (Ville ville : villes) {
            zonesRecharge.add(new ZoneRecharge(ville)); // Créer une zone de recharge pour la ville et l'ajouter à la liste des villes avec zone de recharge
            graphe.addVertex(ville.getNom().name()); // Ajouter un sommet pour la ville dans le graphe
        }
        // Mettre à jour la liste des villes avec des zones de recharge
        mettreAJourVillesAvecRecharge();
    }

    /**
     * Configure la communauté d'agglomération en ajoutant des villes, des routes, et en offrant des options de manipulation
     * Utilise un menu interactif pour permettre à l'utilisateur d'ajouter des routes ou de générer une solution naïve avec une zone de recharge dans chaque ville
     */
    public void configurerCommunaute() {
        Scanner scanner = new Scanner(System.in);
        int nombreVilles;
        boolean premiereIteration = true;
        // Demander à l'utilisateur le nombre de villes jusqu'à ce qu'une valeur valide soit fournie
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
        // Afficher le menu principal et traiter les choix de l'utilisateur jusqu'à ce qu'il choisisse de quitter
        do {
            afficherMenuPrincipal();
            choixMenu = lireEntier(scanner);
            switch (choixMenu) {
                case 1:
                    ajouterRouteMenu(scanner);
                    break;
                case 2:
                    genererSolutionNaive();
                    System.out.println("\nLa communauté d’agglomération est générée avec la solution naïve qui consiste à placer une zone de recharge dans chaque ville.");
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 2); // Continuer jusqu'à ce que l'utilisateur choisisse de quitter
    }

    /**
     * Lit un nombre entier depuis l'entrée utilisateur à l'aide du scanner
     * Gère les erreurs de saisie en demandant à l'utilisateur de réessayer
     *
     * @param scanner Le scanner utilisé pour la lecture
     * @return Le nombre entier lu depuis l'entrée utilisateur
     */
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

    /**
     * Trouve une ville dans la liste en fonction de son nom
     *
     * @param nom Le nom de la ville à rechercher
     * @return La ville correspondante ou null si aucune correspondance n'est trouvée
     */
    public Ville trouverVilleParNom(String nom) {
        // Un flux pour parcourir la liste de villes, filtrer en fonction du nom et renvoyer la première correspondance trouvée
        return villes.stream().filter(ville -> ville.getNom().name().equalsIgnoreCase(nom)).findFirst().orElse(null);
    }

    /**
     * Gère le menu des zones de recharge, offrant des options pour ajouter, retirer, afficher les villes avec recharge, et quitter
     * Utilise un menu interactif pour permettre à l'utilisateur de gérer les zones de recharge de la communauté d'agglomération
     */
    public void gererMenuZonesRecharge() {
        Scanner scanner = new Scanner(System.in);
        int choixMenu;
        // Afficher les villes avec ou sans recharge et le menu des zones de recharge jusqu'à ce que l'utilisateur choisisse de quitter
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
                    System.out.println("\nFin du programme.");
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 4); // Continuer jusqu'à ce que l'utilisateur choisisse de quitter la gestion des zones de recharge
    }

    /**
     * Met à jour la liste des villes avec des zones de recharge en ajoutant une zone de recharge pour chaque ville sans zone de recharge.
     */
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
            zonesRecharge.add(new ZoneRecharge(newRechargeCity));
        }
    }

    /**
     * Ajuste les zones de recharge connectées à une ville spécifiée
     * Parcourt les routes pour trouver les villes connectées et ajoute une zone de recharge pour chaque ville connectée sans zone de recharge
     *
     * @param ville La ville pour laquelle ajuster les zones de recharge connectées
     */
    public void ajusterZonesRechargeConnectees(Ville ville) {
        // Parcourir les routes pour trouver les villes connectées
        for (Route route : routes) {
            // Vérifier si la ville de départ (VilleA) est égale à la ville spécifiée et si la ville d'arrivée (VilleB) n'a pas de zone de recharge
            if (route.getVilleA().equals(ville) && !contientZoneRecharge(route.getVilleB())) {
                zonesRecharge.add(new ZoneRecharge(route.getVilleB()));
            }
            // Vérifier si la ville d'arrivée (VilleB) est égale à la ville spécifiée et si la ville de départ (VilleA) n'a pas de zone de recharge
            else if (route.getVilleB().equals(ville) && !contientZoneRecharge(route.getVilleA())) {
                zonesRecharge.add(new ZoneRecharge(route.getVilleA()));
            }
        }
    }

    /**
     * Affiche le menu principal du programme
     */
    public void afficherMenuPrincipal() {
        System.out.println("\nMenu :");
        System.out.println("1) Ajouter une route");
        System.out.println("2) Avancer");
        System.out.print("\nVotre choix :\n");
    }

    /**
     * Affiche les villes avec des zones de recharge
     * Parcourt la liste des zones de recharge et affiche les noms des villes avec des zones de recharge
     */
    public void afficherVillesAvecRecharge() {
        System.out.println("\nVilles rechargées :");
        // Une boucle pour parcourir la liste des zones de recharge et afficher les noms des villes avec des zones de recharge
        zonesRecharge.forEach(zoneRecharge -> System.out.println("- " + zoneRecharge.getVille().getNom()));
    }

    /**
     * Affiche le menu des zones de recharge du programme
     */
    public void afficherMenuZonesRecharge() {
        System.out.println("\nMenu Zones de Recharge :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Afficher la situation des différentes villes");
        System.out.println("4) Fin");
        System.out.print("\nVotre choix :\n");
    }

    /**
     * Affiche les villes avec ou sans leur propre zone de recharge
     * Affiche les villes avec leur propre zone de recharge, les villes rechargées sans leur propre zone, et les villes non rechargées
     */
    public void afficherVillesAvecOuSansRecharge() {
        System.out.println("\nVilles avec leur propre zone de recharge :");
        for (ZoneRecharge zoneRecharge : getVillesAvecSourceRecharge()) {
            System.out.println("- " + zoneRecharge.getVille().getNom());
        }
        System.out.println("\nVilles rechargées sans leur propre zone de recharge :");
        for (ZoneRecharge zoneRecharge : getVillesRechargeesSansSource()) {
            System.out.println("- " + zoneRecharge.getVille().getNom());
        }
        System.out.println("\nVilles non rechargées :");
        for (Ville ville : getVillesSansZoneRecharge()) {
            System.out.println("- " + ville.getNom());
        }
    }

    /**
     * Obtient la liste des zones de recharge correspondant aux villes avec leur propre zone de recharge
     *
     * @return La liste des zones de recharge correspondant aux villes avec leur propre zone de recharge
     */
    public List<ZoneRecharge> getVillesAvecSourceRecharge() {
        // Un flux pour filtrer les zones de recharge et récupérer celles qui correspondent aux villes avec leur propre zone de recharge, puis les collecter dans une liste
        return zonesRecharge.stream().filter(ZoneRecharge::estSourceRecharge).collect(Collectors.toList());
    }

    /**
     * Obtient la liste des zones de recharge correspondant aux villes rechargées sans leur propre zone de recharge
     *
     * @return La liste des zones de recharge correspondant aux villes rechargées sans leur propre zone de recharge
     */
    public List<ZoneRecharge> getVillesRechargeesSansSource() {
        // Un flux pour filtrer les zones de recharge et récupérer celles qui correspondent aux villes rechargées sans leur propre zone de recharge, puis les collecter dans une liste
        return zonesRecharge.stream().filter(zoneRecharge -> !zoneRecharge.estSourceRecharge()).collect(Collectors.toList());
    }

    /**
     * Obtient la liste des villes qui n'ont pas de zone de recharge et qui ne sont pas des sources de recharge pour d'autres villes
     *
     * @return La liste des villes sans zone de recharge et qui ne sont pas des sources de recharge pour d'autres villes
     */
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

    /**
     * Ajoute une zone de recharge à une ville spécifiée après avoir vérifié les conditions nécessaires
     *
     * @param scanner Le scanner pour la saisie utilisateur
     */
    public void ajouterZoneRechargeMenu(Scanner scanner) {
        System.out.print("\nVeuillez entrer le nom de la ville où vous souhaitez ajouter une zone de recharge :\n");
        String nomVille = scanner.nextLine();
        Ville ville = trouverVilleParNom(nomVille);
        if (ville != null) {
            if (!ville.getSourceVille()) {
                ville.setSourceVilleTrue();
                // Vérifier si la ville n'a pas encore de zone de recharge, puis ajouter une zone de recharge correspondante
                if (!contientZoneRecharge(ville)) {
                    zonesRecharge.add(new ZoneRecharge(ville));
                }
                ajusterZonesRechargeConnectees(ville);
                // Vérifier si l'ajout respecte la contrainte d'accessibilité
                if (respecteContrainte(ville)) {
                    System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
                } else {
                    zonesRecharge.removeIf(zoneRecharge -> zoneRecharge.getVille().equals(ville));
                    System.out.println("Impossible d'ajouter la zone de recharge à " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
                }
            } else {
                System.out.println("Il y a déjà une zone de recharge dans " + ville.getNom() + ".");
            }
        } else {
            System.out.println("Ville non trouvée. Veuillez réessayer.");
        }
    }

    /**
     * Ajoute une route entre deux villes spécifiées après avoir vérifié les conditions nécessaires
     *
     * @param scanner Le scanner pour la saisie utilisateur
     */
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

    /**
     * Vérifie si une route existe entre deux villes spécifiées
     *
     * @param villeA La première ville
     * @param villeB La deuxième ville
     * @return vrai si une route existe entre les deux villes, sinon faux
     */
    public boolean existeRouteEntreVilles(Ville villeA, Ville villeB) {
        for (Route route : routes) {
            if ((route.getVilleA().equals(villeA) && route.getVilleB().equals(villeB)) || (route.getVilleA().equals(villeB) && route.getVilleB().equals(villeA))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ajoute une route entre deux villes spécifiées et met à jour le graphe des relations
     *
     * @param villeA La première ville
     * @param villeB La deuxième ville
     */
    public void ajouterRoute(Ville villeA, Ville villeB) {
        Route route = new Route(villeA, villeB);
        routes.add(route);
        // Mettre à jour le graphe des relations en ajoutant une zone de recharge entre les noms des villes
        graphe.addEdge(villeA.getNom().name(), villeB.getNom().name());
    }

    /**
     * Ajoute une ville à la communauté d'agglomération et met à jour le graphe des relations
     *
     * @param ville La ville à ajouter
     */
    public void ajouterVille(Ville ville) {
        villes.add(ville);
        graphe.addVertex(ville.getNom().name()); // Ajouter un sommet correspondant à la ville dans le graphe des relations
    }

    /**
     * Gère le menu de retrait d'une zone de recharge pour une ville spécifiée
     *
     * @param scanner Le scanner pour la saisie utilisateur
     */
    public void retirerZoneRechargeMenu(Scanner scanner) {
        System.out.println("\nVeuillez entrer le nom de la ville où retirer une zone de recharge :\n");
        String nomVille = scanner.nextLine();
        Ville ville = trouverVilleParNom(nomVille);
        if (ville != null) {
            retirerRecharge(ville);
        } else {
            System.out.println("Ville non trouvée. Veuillez réessayer.");
        }
    }

    /**
     * Retire la zone de recharge d'une ville spécifiée après avoir vérifié les conditions nécessaires
     *
     * @param ville La ville à partir de laquelle retirer la zone de recharge
     */
    public void retirerRecharge(Ville ville) {
        if (ville.getSourceVille()) {
            boolean etat = ville.getSourceVille();
            ville.setSourceVilleFalse();
            if (peutRetirerRecharge(ville)) {
                if (contrainteVoisins(ville)) {
                    System.out.println("Zone de recharge retirée de " + ville.getNom());
                } else {
                    System.out.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
                    ville.setSourceVille(etat);
                }
            } else {
                System.out.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". Aucun voisin avec une zone de recharge.");
                ville.setSourceVille(etat);
            }
        } else {
            System.out.println("\nImpossible de retirer la zone de recharge de " + ville.getNom() + ". La ville n'a pas de zone de recharge.");
        }
    }

    /**
     * Vérifie si une ville spécifiée ou l'un de ses voisins a une zone de recharge
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville elle-même ou l'un de ses voisins a une zone de recharge, sinon faux
     */
    public boolean peutRetirerRecharge(Ville ville) {
        Set<String> voisins = graphe.getNeighbors(ville.getNom().toString());
        return ville.getSourceVille() || voisins.stream().anyMatch(s -> trouverVilleParNom(s).getSourceVille());
    }

    /**
     * Vérifie si la contrainte d'accessibilité est respectée pour une ville spécifiée et ses voisins
     *
     * @param ville La ville à vérifier
     * @return vrai si la contrainte d'accessibilité est respectée, sinon faux
     */
    public boolean contrainteVoisins(Ville ville) {
        Set<String> visited = new HashSet<>();
        return contrainteVoisinsHelper(ville, visited);
    }

    /**
     * Méthode auxiliaire pour vérifier la contrainte d'accessibilité récursivement pour une ville et ses voisins
     *
     * @param ville La ville à vérifier
     * @param visited Un ensemble de villes déjà visitées pour éviter la boucle infinie
     * @return vrai si la contrainte d'accessibilité est respectée, sinon faux
     */
    private boolean contrainteVoisinsHelper(Ville ville, Set<String> visited) {
        Set<String> voisins = graphe.getNeighbors(ville.getNom().toString());
        for (String s : voisins) {
            Ville v = trouverVilleParNom(s);
            if (!visited.contains(v.getNom().toString())) {
                visited.add(v.getNom().toString());
                if (!ville.getNom().equals(v.getNom()) && !peutRetirerRecharge(v)) {
                    return false;
                }
                if (!contrainteVoisinsHelper(v, visited)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Vérifie si la ville contient une zone de recharge
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville contient une zone de recharge, sinon faux
     */
    public boolean contientZoneRecharge(Ville ville) {
        // Parcourir la liste des zones de recharge pour vérifier si la ville contient une zone de recharge
        for (ZoneRecharge zoneRecharge : zonesRecharge) {
            if (zoneRecharge.getVille().equals(ville)) {
                return true; // Si une zone de recharge est trouvée, retourner vrai
            }
        }
        return false; // Si aucune zone de recharge n'est trouvée, retourner faux
    }

    /**
     * Vérifie si la ville respecte la contrainte d'accessibilité
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville respecte la contrainte d'accessibilité, sinon faux
     */
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

    /**
     * Vérifie si la ville est reliée à une ville avec une zone de recharge
     *
     * @param ville La ville à vérifier
     * @return vrai si la ville est reliée à une ville avec une zone de recharge, sinon faux
     */
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
}
