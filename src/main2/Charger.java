package main2;

public class Charger {
    private Ville ville;
    
    public Charger(Ville ville) {
        this.ville = ville;
    }
   
    public Ville getVille() {
    	return ville;
    }
    
    public boolean estSourceRecharge() {
        return ville.getzoneDeRecharge();
    }
}
