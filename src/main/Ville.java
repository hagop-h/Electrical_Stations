package main;


public class Ville {

    private NomVille nom; // Le nom de la ville
    private boolean sourceVille = false; // Indiquer si la ville est une source de recharge ou non

    public Ville(NomVille nom2) {
        this.nom = nom2; // Initialiser la ville avec un nom spécifié
    }
    
    public void setSourceVilleTrue() {
    	sourceVille = true;
    }

    public void setSourceVilleFalse() {
    	sourceVille = false;
    }

    public void setSourceVille(boolean etat) {
        sourceVille = etat;
    }

    public boolean getSourceVille() {
    	return sourceVille;
    }
    
    public NomVille getNom() {
    	return nom;
    }

    @Override
    public String toString() {
        return "Ville {" + "nom = " + nom + ", sourceVille = " + sourceVille + " }";
    }

}
