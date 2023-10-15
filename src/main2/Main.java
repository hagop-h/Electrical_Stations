package main2;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String cheminFichier = "";
        Scanner scanner = new Scanner(System.in);

        if (args.length != 1) {
            System.out.print("Veuillez entrer le chemin du fichier : ");
            cheminFichier = scanner.nextLine();
            
        } else {
            cheminFichier = args[0];
        }

        CommunauteAgglomeration communaute = new CommunauteAgglomeration();

        // Charger la communauté depuis le fichier
        communaute.chargerCommunaute(cheminFichier);

        // Menu principal
        int choixMenu;

        do {
            afficherMenuPrincipal();
            choixMenu = lireEntier(scanner);

            switch (choixMenu) {
                case 1:
                    // Option 1 : résoudre manuellement
                    communaute.trouverSolutionManuelle();
                    break;
                case 2:
                    // Option 2 : résoudre automatiquement avec Algorithme 2
                    System.out.print("Veuillez entrer le nombre d'itérations : ");
                    int nombreIterations = lireEntier(scanner);
                    communaute.resoudreAutomatiquementAlgo2(nombreIterations);
                    break;
                case 3:
                    // Option 3 : sauvegarder
                    System.out.print("Veuillez entrer le chemin vers le fichier de sauvegarde : ");
                    String cheminSauvegarde = scanner.nextLine();
                    communaute.sauvegarderSolution(cheminSauvegarde);
                    break;
                case 4:
                    // Option 4 : fin
                    System.out.println("Fin du programme.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choixMenu != 4);

        scanner.close();
    }

    // Autres méthodes restent inchangées

    public static void afficherMenuPrincipal() {
        System.out.println("\nMenu :");
        System.out.println("1) Résoudre manuellement");
        System.out.println("2) Résoudre automatiquement");
        System.out.println("3) Sauvegarder");
        System.out.println("4) Fin");
        System.out.print("Votre choix : ");
    }

    public static int lireEntier(Scanner scanner) {
        while (true) {
            try {
                int result = scanner.nextInt();
                scanner.nextLine(); // Consume le caractère de nouvelle ligne
                return result;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre entier.");
                scanner.nextLine(); // Pour consommer l'entrée restante
            }
        }
    }
}
