package phase1;

/**
 * Classe représentant une zone de recharge associée à une ville
 */
public class ZoneRecharge {
    private final Ville ville; // La ville associée à la zone de recharge

    /**
     * Constructeur de la classe ZoneRecharge
     *
     * @param ville La ville à laquelle la zone de recharge est associée
     */
    public ZoneRecharge(Ville ville) {
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
        return ville.getSourceVille();
    }
}
