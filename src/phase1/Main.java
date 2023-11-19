package phase1;

/**
 * Classe principale contenant la méthode main
 */
public class Main {

    /**
     * Méthode principale du programme
     * Crée une instance de CommunauteAgglomeration, configure la communauté, et gère le menu des zones de recharge
     *
     * @param args Les arguments de la ligne de commande (non utilisés dans cet exemple)
     */
    public static void main(String[] args) {
        CommunauteAgglomeration communaute = new CommunauteAgglomeration(); // Créer une instance de CommunauteAgglomeration
        communaute.configurerCommunaute(); // Configurer la communauté
        communaute.gererMenuZonesRecharge(); // Gérer le menu des zones de recharge
    }
}
