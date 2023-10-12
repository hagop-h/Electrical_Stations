package main;

import main.NomVille.Nom;

public class Ville {
    private Nom nom;
    private boolean sourceVille=false;;

    public Ville(Nom nom2) {
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
    public Nom getNom() {
    	return nom;
    }
}