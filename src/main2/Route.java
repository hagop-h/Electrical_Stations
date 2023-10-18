package main2;

public class Route {
    private Ville villeA;
    private Ville villeB;

    public Route(Ville villeA, Ville villeB) {
        this.villeA = villeA;
        this.villeB = villeB;
    }
    
    public Ville getVilleA() {
    	return villeA;
    }
    
    public Ville getVilleB() {
    	return villeB;
    }
}
