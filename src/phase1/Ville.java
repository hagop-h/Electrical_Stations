package phase1;

/**
 * Classe représentant une ville
 */
public class Ville {
    private final NomVille nom; // Le nom de la ville
    private boolean sourceVille = false; // Indique si la ville est une source de recharge

    /**
     * Constructeur de la classe Ville
     *
     * @param nom Le nom de la ville, utilisant l'énumération NomVille
     */
    public Ville(NomVille nom) {
        this.nom = nom; // Initialiser la ville avec un nom spécifié
    }

    /**
     * Définit la ville comme une source de recharge en mettant sourceVille à vrai
     */
    public void setSourceVilleTrue() {
        sourceVille = true;
    }

    /**
     * Définit la ville comme non une source de recharge en mettant sourceVille à faux
     */
    public void setSourceVilleFalse() {
        sourceVille = false;
    }

    /**
     * Définit l'état de sourceVille avec la valeur spécifiée
     *
     * @param etat La nouvelle valeur de sourceVille
     */
    public void setSourceVille(boolean etat) {
        sourceVille = etat;
    }

    /**
     * Obtient l'état de sourceVille
     *
     * @return vrai si la ville est une source de recharge, sinon faux
     */
    public boolean getSourceVille() {
        return sourceVille;
    }

    /**
     * Obtient le nom de la ville
     *
     * @return Le nom de la ville
     */
    public NomVille getNom() {
        return nom;
    }
}
