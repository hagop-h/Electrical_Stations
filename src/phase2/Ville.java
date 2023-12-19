package phase2;

/**
 * La classe Ville représente une ville dans une communauté d'agglomération
 * Chaque ville a un nom et peut être associée à une zone de recharge
 */
public class Ville {
    private final String nom; // Nom d'une ville
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
    public void setZoneDeRechargeTrue() {
        zoneDeRecharge = true;
    }

    /**
     * Désactive la zone de recharge d'une ville
     */
    public void setZoneDeRechargeFalse() {
        zoneDeRecharge = false;
    }

    /**
     * Définit l'état initial de la zone de recharge d'une ville
     *
     * @param etatInitial L'état initial de la zone de recharge
     */
    public void setZoneDeRecharge(boolean etatInitial) {
        zoneDeRecharge = etatInitial;
    }

    /**
     * Obtient l'état de la zone de recharge d'une ville
     *
     * @return vrai si la ville possède une zone de recharge, sinon faux
     */
    public boolean getZoneDeRecharge() {
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
}
