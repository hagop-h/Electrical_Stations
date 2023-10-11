package projet_java;

import projet_java.NomVille.Nom;

public class Ville {
    Nom nom;
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
}
