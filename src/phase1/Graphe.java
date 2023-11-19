package phase1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe représentant un graphe utilisant une liste d'adjacence
 */
public class Graphe {
    private Map<String, Set<String>> adjacencyList; // Représentation du graphe sous forme de liste d'adjacence

    /**
     * Constructeur de la classe Graphe
     * Initialise le graphe en créant une liste d'adjacence vide
     */
    public Graphe() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Ajoute un sommet (vertex) au graphe
     *
     * @param vertex Le sommet à ajouter
     */
    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new HashSet<>());
    }

    /**
     * Ajoute une arête (edge) entre deux sommets (vertices) au graphe
     *
     * @param vertex1 Le premier sommet
     * @param vertex2 Le deuxième sommet
     */
    public void addEdge(String vertex1, String vertex2) {
        adjacencyList.get(vertex1).add(vertex2);
        adjacencyList.get(vertex2).add(vertex1);
    }

    /**
     * Obtient les voisins d'un sommet spécifié
     *
     * @param vertex Le sommet pour lequel récupérer les voisins
     * @return Un ensemble de sommets voisins
     */
    public Set<String> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new HashSet<>());
    }

    /**
     * Obtient l'ensemble de tous les sommets du graphe
     *
     * @return Un ensemble de tous les sommets du graphe
     */
    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    /**
     * Supprime une arête entre deux sommets du graphe
     *
     * @param vertex1 Le premier sommet
     * @param vertex2 Le deuxième sommet
     */
    public void removeEdge(String vertex1, String vertex2) {
        adjacencyList.get(vertex1).remove(vertex2);
        adjacencyList.get(vertex2).remove(vertex1);
    }
}
