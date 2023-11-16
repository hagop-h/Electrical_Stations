package main2;

/**
 * La classe Ville représente une ville dans une communauté d'agglomération
 * Chaque ville a un nom et peut être associée à une zone de recharge
 */
public class Ville {
    private String nom; // Nom d'une ville
    private boolean zoneDeRecharge = false; // Pour indiquer si une ville possède une zone de recharge

    /**
     * Constructeur pour créer une nouvelle ville avec un nom donné
     *
     * @param nom Le nom de la ville
     */
    public Ville(String nom) {
        this.nom = nom;
    }


    /**
     * Active la zone de recharge d'une ville
     */
    public void setzoneDeRechargeTrue() {
    	zoneDeRecharge = true;
    }

    /**
     * Désactive la zone de recharge d'une ville
     */
    public void setzoneDeRechargeFalse() {
    	zoneDeRecharge = false;
    }

    /**
     * Définit l'état initial de la zone de recharge d'une ville
     *
     * @param etatInitial L'état initial de la zone de recharge
     */
    public void setzoneDeRecharge(boolean etatInitial) {
        zoneDeRecharge = etatInitial;
    }

    /**
     * Obtient l'état de la zone de recharge d'une ville
     *
     * @return vrai si la ville possède une zone de recharge, sinon faux
     */
    public boolean getzoneDeRecharge() {
    	return zoneDeRecharge;
    }

    /**
     * Obtient le nom d'une ville
     *
     * @return Le nom de la ville
     */
    public String getNom() {
    	return nom;
    }


    /**
     * Surcharge de la méthode toString pour obtenir une représentation textuelle de la ville
     *
     * @return Une chaîne représentant la ville
     */
    @Override
    public String toString() {
        return "Ville {" + "nom = " + nom + ", zoneDeRecharge = " + zoneDeRecharge + '}';
    }
}
