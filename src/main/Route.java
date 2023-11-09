package main;

public class Route {

    private Ville villeA; // La première ville reliée par la route
    private Ville villeB; // La deuxième ville reliée par la route

    public Route(Ville villeA, Ville villeB) {
        // Initialiser la route avec deux villes spécifiées
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
