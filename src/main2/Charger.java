package main2;

/**
 * La classe Charger représente une zone de recharge associée à une ville dans une communauté d'agglomération
 */
public class Charger {
    private Ville ville; // La ville associée à une zone de recharge

    /**
     * Constructeur de la classe Charger.
     *
     * @param ville La ville à associer à la zone de recharge
     */
    public Charger(Ville ville) {
        this.ville = ville;
    }

    /**
     * Obtient la ville associée à la zone de recharge
     *
     * @return La ville associée à la zone de recharge
     */
    public Ville getVille() {
    	return ville;
    }

    /**
     * Vérifie si la ville associée à la zone de recharge est une source de recharge
     *
     * @return vrai si la ville est une source de recharge, sinon faux
     */
    public boolean estSourceRecharge() {
        return ville.getzoneDeRecharge();
    }
}
