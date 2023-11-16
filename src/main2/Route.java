package main2;

/**
 * La classe Route représente une connexion routière entre deux villes dans une communauté d'agglomération
 * Chaque route a une ville de départ (villeA) et une ville d'arrivée (villeB)
 */
public class Route {
    private Ville villeA; // Ville de départ de la route
    private Ville villeB; // Ville d'arrivée de la route

    /**
     * Constructeur pour créer une nouvelle route entre deux villes
     *
     * @param villeA Ville de départ de la route
     * @param villeB Ville d'arrivée de la route
     */
    public Route(Ville villeA, Ville villeB) {
        this.villeA = villeA;
        this.villeB = villeB;
    }

    /**
     * Retourne la ville de départ de la route
     *
     * @return La ville de départ
     */
    public Ville getVilleA() {
    	return villeA;
    }

    /**
     * Retourne la ville d'arrivée de la route
     *
     * @return La ville d'arrivée
     */
    public Ville getVilleB() {
    	return villeB;
    }
}
