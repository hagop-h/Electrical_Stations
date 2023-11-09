package main;

public class Parking {

    private Ville ville; // La ville associée à une zone de recharge
    
    public Parking(Ville ville) {
        this.ville = ville; // Initialiser le parking avec une ville spécifiée
    }
   
    public Ville getVille() {
    	return ville;
    }
    
    public boolean estSourceRecharge() {
        return ville.getSourceVille();
    }

}
