package main2;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * La classe CommunauteAgglomeration représente une communauté d'agglomération comprenant des villes, des routes et des zones de recharge.
 * Elle utilise un graph pour représenter la connectivité entre les villes.
 */
public class CommunauteAgglomeration {
    private Set<Ville> villes; // Ensemble de villes dans une communauté
    private Set<Route> routes; // Ensemble de routes entre les villes
    private List<Charger> charger; // Liste de zones de recharge présentes dans une communauté
    private Graph graph; // Graph représentant la connectivité entre les villes

	/**
	 * Constructeur de la classe CommunauteAgglomeration.
	 * Initialise les collections de villes, de routes, de chargeurs, et le graphe.
	 */
	public CommunauteAgglomeration() {
        villes = new HashSet<>();
        routes = new HashSet<>();
        charger = new ArrayList<>();
        graph = new Graph();
    }

	/**
	 * Lit un entier depuis le scanner tout en gérant les exceptions d'entrée incorrecte.
	 *
	 * @param scanner Le scanner utilisé pour la lecture.
	 * @return L'entier lu depuis le scanner.
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
	 * Recherche et renvoie une ville par son nom.
	 *
	 * @param nom Le nom de la ville à rechercher.
	 * @return La ville correspondant au nom ou null si aucune correspondance n'est trouvée.
	 */
    public Ville trouverVilleParNom(String nom) {
        return villes.stream().filter(ville -> ville.getNom().equalsIgnoreCase(nom)).findFirst().orElse(null);
    }

	/**
	 * Permet à l'utilisateur de trouver manuellement une solution en ajoutant ou retirant des zones de recharge.
	 * Affiche le menu interactif pour la gestion manuelle des zones de recharge.
	 */
    public void trouverSolutionManuelle() {
        Scanner scanner = new Scanner(System.in);
        int choixMenu;
		// Générer une solution initiale si la liste des chargeurs est vide
        if (charger.isEmpty()) {
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
                    System.out.println("Fin de la gestion manuelle.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 3); // Continuer jusqu'à ce que l'utilisateur choisisse la fin de la gestion manuelle
    }

	/**
	 * Ajoute une route entre deux villes à partir de leurs noms.
	 * Recherche les villes correspondantes par leur nom et ajoute la route si les deux villes existent.
	 *
	 * @param nomVilleA Nom de la première ville.
	 * @param nomVilleB Nom de la deuxième ville.
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
	 * Ajuste les zones de recharge connectées à une ville donnée.
	 * Parcourt les routes de la communauté d'agglomération et ajoute des zones de recharge aux villes connectées qui n'ont pas encore de zone de recharge.
	 *
	 * @param ville Ville pour laquelle ajuster les zones de recharge connectées.
	 */
	public void ajusterRechargeConnectees(Ville ville) {
		// Parcourir les routes de la communauté d'agglomération
	    for (Route route : routes) {
			// Vérifier si la ville en paramètre est la ville de départ de la route
	        if (route.getVilleA().equals(ville) && !contientRecharge(route.getVilleB())) {
	            charger.add(new Charger(route.getVilleB())); // Ajouter une zone de recharge à la ville connectée si elle n'a pas de zone de recharge
	        } else if (route.getVilleB().equals(ville) && !contientRecharge(route.getVilleA())) {
	            charger.add(new Charger(route.getVilleA())); // Ajouter une zone de recharge à la ville connectée si elle n'a pas de zone de recharge
	        }
	    }
	}

	/**
	 * Affiche le menu pour la gestion manuelle des zones de recharge.
	 * Propose des options pour ajouter ou retirer une zone de recharge, ainsi que pour terminer la gestion manuelle.
	 */
    public void afficherMenuSolutionManuelle() {
        System.out.println("\nMenu :");
        System.out.println("1) Ajouter une zone de recharge");
        System.out.println("2) Retirer une zone de recharge");
        System.out.println("3) Fin");
        System.out.print("\nVotre choix :\n");
    }

	/**
	 * Affiche les informations sur les villes rechargées, avec ou sans leur propre zone de recharge, ainsi que les villes non rechargées.
	 * Utilise les listes de zones de recharge (Charger) pour distinguer les différentes catégories.
	 */
    public void afficherVillesAvecOuSansRecharge() {
		// Affichage des villes rechargées avec leurs propres zones de recharge
        System.out.println("Villes rechargées avec leurs propres zone de recharge :");
        for (Charger charger : getVillesAvecSourceRecharge()) {
            System.out.println("- " + charger.getVille().getNom());
        }
		// Affichage des villes rechargées sans leurs propres zones de recharge
        System.out.println("\nVilles rechargées sans leurs propres zone de recharge :");
        for (Charger charger : getVillesRechargeesSansSource()) {
            System.out.println("- " + charger.getVille().getNom());
        }
		// Affichage des villes non rechargées
        System.out.println("\nVilles non rechargées :");
        for (Ville ville : getVillesSansZoneRecharge()) {
            System.out.println("- " + ville.getNom());
        }
    }
    
    // Pour les tests unitaires
	public Set<Ville> getVilles() {
	    return villes;
	}

	public Set<Route> getRoutes() {
	    return routes;
	}

	public List<Charger> getcharger() {
	    return charger;
	}

	/**
	 * Sauvegarde la solution actuelle dans un fichier spécifié.
	 * Les informations sur les villes et les routes sont sauvegardées, ainsi que la catégorisation des villes en fonction de leur zone de recharge.
	 * Ajoute également le score de la solution.
	 *
	 * @param cheminFichier Le chemin du fichier où sauvegarder la solution.
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
	        for (Charger charger : getVillesAvecSourceRecharge()) {
	        	writer.write("- " + charger.getVille().getNom());
	            writer.newLine();
	        }
			// Sauvegarder les villes rechargées sans leurs propres zones de recharge
			writer.write("\nVilles rechargées sans leurs propres zone de recharge :");
	        writer.newLine();
	        for (Charger charger : getVillesRechargeesSansSource()) {
	        	writer.write("- " + charger.getVille().getNom());
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
	        System.out.println("Solution sauvegardée dans : " + cheminFichier);
	    } catch (IOException e) {
	        System.err.println("Erreur lors de la sauvegarde de la solution : " + e.getMessage());
	    }
	}

	/**
	 * Récupère la liste des villes rechargées avec leur propre zone de recharge.
	 *
	 * @return Liste des objets Charger représentant les villes avec leur propre zone de recharge.
	 */
	public List<Charger> getVillesAvecSourceRecharge() {
		// Filtrage des zones de recharge qui sont des sources
        return charger.stream().filter(Charger::estSourceRecharge).collect(Collectors.toList());
    }

	/**
	 * Récupère la liste des villes rechargées sans leur propre zone de recharge.
	 *
	 * @return Liste des objets Charger représentant les villes rechargées sans leur propre zone de recharge.
	 */
    public List<Charger> getVillesRechargeesSansSource() {
		// Filtrage des zones de recharge qui ne sont pas des sources
        return charger.stream().filter(parking -> !parking.estSourceRecharge()).collect(Collectors.toList());
    }

	/**
	 * Récupère la liste des villes sans zone de recharge propre et sans zone de recharge connectée.
	 *
	 * @return Liste des objets Ville représentant les villes sans zone de recharge propre et sans zone de recharge connectée.
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
	 * Menu interactif pour ajouter une zone de recharge à une ville.
	 * Demande le nom de la ville, recherche la ville correspondante, puis ajoute la zone de recharge.
	 * Affiche un message approprié selon le résultat.
	 *
	 * @param scanner Objet Scanner pour la saisie utilisateur.
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
	            	charger.add(new Charger(ville)); // Ajout d'une nouvelle zone de recharge à la liste
	            }
	            ajusterRechargeConnectees(ville); // Ajustement des recharge connectées
				// Vérification de la contrainte d'accessibilité
	            if (respecteContrainte(ville)) {
	                System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
	            } else {
					// Retrait de la zone de recharge en cas de violation de la contrainte
	                charger.removeIf(parking -> parking.getVille().equals(ville));
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
	 * Ajoute une zone de recharge à la ville spécifiée, met à jour la liste des zones de recharge
	 * et ajuste les zones de recharge connectées. Affiche un message approprié selon le résultat.
	 *
	 * @param nomVille Le nom de la ville où ajouter une zone de recharge.
	 */
	public void recharge(String nomVille) {
	    Ville ville = trouverVilleParNom(nomVille); // Rechercher la ville par son nom
	    if (ville != null) {
			// Vérification si la ville n'a pas déjà une zone de recharge
	        if (!ville.getzoneDeRecharge() ) {
	            ville.setzoneDeRechargeTrue();  // Ajout de la zone de recharge à la ville
				// Vérification si la zone de recharge n'existe pas déjà
	            if(!contientRecharge(ville)) {
	            	charger.add(new Charger(ville)); // Ajout d'une nouvelle zone de recharge à la liste
	            }
	            ajusterRechargeConnectees(ville); // Ajustement des recharge connectées
				// Vérification de la contrainte d'accessibilité
	            if (respecteContrainte(ville)) {
	                System.out.println("Zone de recharge ajoutée à " + ville.getNom() + ".");
	            } else {
					// Retrait de la zone de recharge en cas de violation de la contrainte
	                charger.removeIf(parking -> parking.getVille().equals(ville));
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
	 * Ajoute une nouvelle route entre deux villes spécifiées, met à jour la liste des routes
	 * et la représentation du graphe associé.
	 *
	 * @param villeA Première ville de la route.
	 * @param villeB Deuxième ville de la route.
	 */
	public void ajouterRoute(Ville villeA, Ville villeB) {
	    if (villeA != null && villeB != null) {
	        Route route = new Route(villeA, villeB); // Création d'une nouvelle route
	        routes.add(route); // Ajout de la route à la liste des routes
	        graph.addEdge(villeA.getNom(), villeB.getNom()); // Mise à jour de la représentation du graphe
	    } else {
	        System.out.println("Villes non trouvées. Veuillez réessayer.");
	    }
	}

	/**
	 * Ajoute une nouvelle ville à la communauté d'agglomération.
	 *
	 * @param ville Ville à ajouter.
	 */
	public void ajouterVille(Ville ville) {
	    villes.add(ville); // Ajout de la ville à la liste des villes
	    graph.addVertex(ville.getNom()); // Mise à jour du graphe avec le nom de la ville comme un nouveau sommet
	}

	/**
	 * Retire les zones de recharge connectées à une ville spécifiée.
	 *
	 * @param ville Ville pour laquelle les zones de recharge connectées seront retirées.
	 */
    public void retirerRechargeConnectees(Ville ville) {
        List<Ville> villesConnectees = new ArrayList<>();
		// Parcourir des routes pour trouver les villes connectées avec des zones de recharge
        for (Route route : routes) {
            if (route.getVilleA().equals(ville) && contientRecharge(route.getVilleB()) && !route.getVilleB().getzoneDeRecharge()) {
                villesConnectees.add(route.getVilleB());
            } else if (route.getVilleB().equals(ville) && contientRecharge(route.getVilleA()) && !route.getVilleA().getzoneDeRecharge()) {
                villesConnectees.add(route.getVilleA());
            }
        }
		// Retrait des zones de recharge connectées
        for (Ville villeConnectee : villesConnectees) {
            retirerRecharge(villeConnectee);
        }
    }

	/**
	 * Gère le retrait d'une zone de recharge pour une ville spécifiée, avec interaction utilisateur.
	 *
	 * @param scanner Scanner pour obtenir l'entrée utilisateur.
	 */
    public void retirerZoneRechargeMenu(Scanner scanner) {
	    System.out.println("\nVeuillez entrer le nom de la ville où retirer une zone de recharge :");
	    String nomVille = scanner.nextLine();
	    Ville ville = trouverVilleParNom(nomVille);
	    if (ville != null) {
	        if (contientRecharge(ville) && peutRetirerRecharge(ville) && ville.getzoneDeRecharge()) {
	            retirerRecharge(ville);
	            ville.setzoneDeRechargeFalse();
	            retirerRechargeConnectees(ville);
	            System.out.println("Zone de recharge retirée de " + ville.getNom() + ".");
	        } else if (!contientRecharge(ville)) {
	            System.out.println("Il n'y a pas de zone de recharge dans " + ville.getNom() + ".");
	        } else {
	            System.out.println("Impossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité ou ce n'est pas la source de la recharge.");
	        }
	    } else {
	        System.out.println("Ville non trouvée. Veuillez réessayer.");
	    }
	}
    
	/**
	 * Retire la zone de recharge pour une ville donnée, respectant les contraintes d'accessibilité.
	 *
	 * @param ville Ville pour laquelle retirer la zone de recharge.
	 */
	public void retirerRecharge(Ville ville) {
		// Vérifier si le retrait de la zone de recharge est possible sans violer les contraintes
	    if (!peutRetirerRecharge(ville)) {
	        System.out.println("Impossible de retirer la zone de recharge de " + ville.getNom() + ". Cela violerait la contrainte d'accessibilité.");
	        return;
	    }
		// Vérifier si une ville connectée possède une source, si c'est le cas, la ville actuelle conserve sa zone de recharge
	    boolean connectedCityHasSource = false;
	    for (Route route : routes) {
	        if (route.getVilleA().equals(ville) && contientRecharge(route.getVilleB()) && route.getVilleB().getzoneDeRecharge()) {
	            connectedCityHasSource = true;
	            break;
	        } else if (route.getVilleB().equals(ville) && contientRecharge(route.getVilleA()) && route.getVilleA().getzoneDeRecharge()) {
	            connectedCityHasSource = true;
	            break;
	        }
	    }
		// Si aucune ville connectée n'a de source, retire la zone de recharge de la ville
	    if (!connectedCityHasSource) {
	        charger.removeIf(parking -> parking.getVille().equals(ville));
	        if (!contientZoneRechargeConnectee(ville) && ville.getzoneDeRecharge()) {
	            ville.setzoneDeRechargeFalse();
	        }
	    } 
	}

	/**
	 * Vérifie si la zone de recharge peut être retirée pour une ville sans violer les contraintes d'accessibilité.
	 *
	 * @param ville Ville pour laquelle vérifier la possibilité de retrait de la zone de recharge.
	 * @return true si la zone de recharge peut être retirée, sinon false.
	 */
	public boolean peutRetirerRecharge(Ville ville) {
		// Parcourir toutes les routes associées à la ville
        for (Route route : routes) {
			// Vérifier si la ville est l'une des extrémités de la route et si aucune de ces extrémités n'a de zone de recharge
            if ((route.getVilleA().equals(ville) || route.getVilleB().equals(ville)) && !contientRecharge(route.getVilleA()) && !contientRecharge(route.getVilleB()) ) {
                return false; // Si une telle route est trouvée, la zone de recharge ne peut pas être retirée
            }
        }
        return true; // Aucune route ne viole les contraintes, la zone de recharge peut être retirée
    }

	/**
	 * Charge une communauté à partir d'un fichier.
	 *
	 * @param cheminFichier Chemin du fichier contenant les informations de la communauté.
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
						// Extraire le nom de la ville
	                    String nomVille = elements[1].trim();
						// Créer une nouvelle ville et l'ajoute à la liste des villes
	                    Ville ville = new Ville(nomVille);
	                    villes.add(ville);
						// Ajoute également la ville au graphe
	                    ajouterVille(ville);
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
	        System.out.println("Communauté chargée depuis le fichier : " + cheminFichier + "\n");
	    } catch (FileNotFoundException e) {
			// En cas d'erreur de fichier non trouvé, affiche un message d'erreur
	        System.err.println("Fichier non trouvé : " + cheminFichier);
	    }
	}

	/**
	 * Vérifie si une ville est connectée à une zone de recharge via une route.
	 *
	 * @param ville La ville à vérifier.
	 * @return true si la ville est connectée à une zone de recharge, sinon false.
	 */
	public boolean contientZoneRechargeConnectee(Ville ville) {
		// Parcourir toutes les routes de la communauté
	    for (Route route : routes) {
			// Si la ville est le point de départ de la route et que la ville d'arrivée a une zone de recharge
			if (route.getVilleA().equals(ville) && contientRecharge(route.getVilleB())) {
	            return true;
	        }
			// Si la ville est le point d'arrivée de la route et que la ville de départ a une zone de recharge
			else if (route.getVilleB().equals(ville) && contientRecharge(route.getVilleA())) {
	            return true;
	        }
	    }
	    return false; // Si aucune connexion à une zone de recharge n'est trouvée
	}

	/**
	 * Vérifie si une ville contient une zone de recharge.
	 *
	 * @param ville La ville à vérifier.
	 * @return true si la ville contient une zone de recharge, sinon false.
	 */
	public boolean contientRecharge(Ville ville) {
		// Parcourir la liste des chargeurs pour la communauté
		for (Charger charger : charger) {
			// Si le chargeur est associé à la ville spécifiée, la ville contient une zone de recharge
			if (charger.getVille().equals(ville)) {
	            return true;
	        }
	    }
	    return false; // Si aucun chargeur associé à la ville n'est trouvé, la ville ne contient pas de zone de recharge
	}

	/**
	 * Génère une solution initiale en ajoutant une zone de recharge à chaque ville de la communauté.
	 * Cette méthode est appelée lorsque la liste des villes est vide.
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
	 * Choisi de manière aléatoire une ville parmi celles présentes dans la communauté.
	 *
	 * @return Une ville choisie aléatoirement.
	 */
	public Ville choisirVilleAleatoire() {
	    List<Ville> villeList = new ArrayList<>(villes); // Convertir la liste de villes en une liste modifiable
		// Utiliser ThreadLocalRandom pour obtenir un index aléatoire dans la liste
		return villeList.get(ThreadLocalRandom.current().nextInt(villeList.size()));
	}

	/**
	 * Résout automatiquement le problème en utilisant un algorithme itératif.
	 * Choisi aléatoirement une ville, la recharge si elle ne l'est pas, et évalue le score.
	 * Répète le processus jusqu'à atteindre le nombre d'itérations spécifié.
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
	 * Vérifie si la ville est reliée à une borne de recharge via au moins une route.
	 *
	 * @param ville La ville à vérifier.
	 * @return true si la ville est reliée à une borne de recharge, sinon false.
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
	 * Vérifie si une ville respecte la contrainte d'accessibilité. Une ville doit avoir une zone de recharge, et toutes les villes reliées à elle par des routes doivent également avoir une zone de recharge ou être reliées à une zone de recharge.
	 *
	 * @param ville La ville à vérifier.
	 * @return true si la ville respecte la contrainte, sinon false.
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
	 * Calcule le score actuel de la communauté, représenté par le nombre de villes avec une zone de recharge connectée.
	 *
	 * @return Le score de la communauté.
	 */
    public int score() {
		// Utilisation de Stream pour filtrer les chargeurs en tant que sources et compter leur nombre
        return (int) charger.stream().filter(Charger::estSourceRecharge).count();
    }
}
