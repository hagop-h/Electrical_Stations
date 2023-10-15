package main2;


public class Ville {
    private String nom;
    private boolean sourceVille=false;;
    
    public Ville(String nom2) {
        this.nom = nom2;
    }
    
    public void setSourceVilleTrue() {
    	sourceVille=true;
    }
    public void setSourceVilleFalse() {
    	sourceVille=false;
    }
    public boolean getSourceVille() {
    	return sourceVille;
    }
    
    
    public String getNom() {
    	return nom;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom=" + nom +
                ", sourceVille=" + sourceVille +
                '}';
    }

}