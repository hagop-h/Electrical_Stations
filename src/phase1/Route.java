package phase1;

/**
 * Classe représentant une route entre deux villes
 */
public class Route {
    private final Ville villeA; // La première ville de la route
    private final Ville villeB; // La deuxième ville de la route

    /**
     * Constructeur de la classe Route.
     *
     * @param villeA La première ville de la route
     * @param villeB La deuxième ville de la route
     */
    public Route(Ville villeA, Ville villeB) {
        this.villeA = villeA;
        this.villeB = villeB;
    }

    /**
     * Obtient la première ville de la route
     *
     * @return La première ville de la route
     */
    public Ville getVilleA() {
        return villeA;
    }

    /**
     * Obtient la deuxième ville de la route
     *
     * @return La deuxième ville de la route
     */
    public Ville getVilleB() {
        return villeB;
    }
}
