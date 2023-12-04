package phase2;

import java.util.Scanner;

/**
 * Classe principale contenant la méthode main pour exécuter le programme de gestion de la communauté d'agglomération
 */
public class Main {
    /**
     * Méthode principale exécutant le programme
     *
     * @param args Les arguments en ligne de commande (le chemin du fichier peut être fourni en argument)
     */
    public static void main(String[] args) {
        String cheminFichier; // Déclaration et initialisation d'une variable pour stocker le chemin du fichier
        Scanner scanner = new Scanner(System.in); // Création d'une instance de Scanner pour lire l'entrée utilisateur
        // Vérifier si un chemin de fichier est fourni en argument
        if (args.length != 1) {
            // Si aucun argument n'est fourni, demander à l'utilisateur d'entrer le chemin du fichier via la console
            System.out.println("Veuillez entrer le chemin du fichier :");
            cheminFichier = scanner.nextLine(); // Lire le chemin du fichier depuis la console
        } else {
            // Si un argument est fourni, utiliser le premier argument comme chemin de fichier
            cheminFichier = args[0];
        }
        CommunauteAgglomeration communaute = new CommunauteAgglomeration(); // Créer une instance de la classe CommunauteAgglomeration
        communaute.chargerCommunaute(cheminFichier); // Charger la communauté à partir du fichier spécifié
        int choixMenu;
        do {
            // Afficher le score actuel et les informations sur les zones de recharge
            System.out.println("\nScore : "+communaute.score());
            communaute.afficherVillesAvecOuSansRecharge();
            afficherMenuPrincipal();
            choixMenu = lireEntier(scanner);
            // Exécuter l'action en fonction du choix de l'utilisateur
            switch (choixMenu) {
                case 1:
                    // Option 1 : Résoudre manuellement
                    communaute.trouverSolutionManuelle();
                    break;
                case 2:
                    // Option 2 : Résoudre automatiquement avec l'Algorithme 2
                    communaute.resoudreAutomatiquement();
                    break;
                case 3:
                    // Option 3 : Sauvegarder
                    System.out.println("\nVeuillez entrer le chemin vers le fichier de sauvegarde :");
                    String cheminSauvegarde = scanner.nextLine();
                    communaute.sauvegarderSolution(cheminSauvegarde);
                    break;
                case 4:
                    // Option 4 : Fin
                    System.out.println("\nFin du programme.");
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 4); // Continuer jusqu'à ce que l'utilisateur choisisse de quitter
        scanner.close(); // Fermer le flux
    }

    /**
     * Affiche le menu principal du programme
     */
    public static void afficherMenuPrincipal() {
        System.out.println("\nMenu :");
        System.out.println("1) Résoudre manuellement");
        System.out.println("2) Résoudre automatiquement");
        System.out.println("3) Sauvegarder");
        System.out.println("4) Fin");
        System.out.print("\nVotre choix :\n");
    }

    /**
     * Lit un entier depuis l'entrée utilisateur en gérant les erreurs
     *
     * @param scanner Scanner pour lire l'entrée utilisateur
     * @return L'entier lu depuis l'entrée
     */
    public static int lireEntier(Scanner scanner) {
        // Boucle infinie pour gérer les tentatives de lecture jusqu'à ce qu'un entier valide soit fourni
        while (true) {
            try {
                int result = scanner.nextInt(); // Tenter de lire un entier
                scanner.nextLine(); // Consommer le caractère de nouvelle ligne
                return result; // Retourner l'entier lu avec succès
            } catch (java.util.InputMismatchException e) {
                // En cas d'erreur de type, affiche un message et consomme l'entrée restante
                System.out.println("\nVeuillez entrer un nombre entier.");
                scanner.nextLine(); // Pour consommer l'entrée restante
            }
        }
    } 
}
