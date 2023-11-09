package main;


public class Ville {

    private NomVille nom;
    private boolean sourceVille = false;

    public Ville(NomVille nom2) {
        this.nom = nom2;
    }
    
    public void setSourceVilleTrue() {
    	sourceVille = true;
    }

    public void setSourceVilleFalse() {
    	sourceVille = false;
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
