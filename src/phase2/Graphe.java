package phase2;

import java.util.*;

/**
 * Classe représentant un graphe sous forme de liste d'adjacence
 * Les méthodes de cette classe permettent d'ajouter des sommets, des arêtes, de récupérer les voisins et les sommets du graphe
 */
public class Graphe {
    private Map<String, Set<String>> adjacencyList; // Représentation d'un graphe sous forme de liste d'adjacence

    /**
     * Constructeur pour créer un graphe vide
     */
    public Graphe() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Ajoute un sommet au graphe s'il n'existe pas déjà
     *
     * @param vertex Le sommet à ajouter
     */
    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new HashSet<>());
    }

    /**
     * Ajoute une arête entre deux sommets dans le graphe
     *
     * @param vertex1 Le premier sommet
     * @param vertex2 Le deuxième sommet
     */
    public void addEdge(String vertex1, String vertex2) {
        adjacencyList.get(vertex1).add(vertex2);
        adjacencyList.get(vertex2).add(vertex1);
    }

    /**
     * Récupère l'ensemble des voisins d'un sommet donné
     *
     * @param vertex Le sommet pour lequel récupérer les voisins
     * @return L'ensemble des voisins du sommet
     */
    public Set<String> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new HashSet<>());
    }

    /**
     * Récupère l'ensemble de tous les sommets du graphe
     *
     * @return L'ensemble des sommets du graphe
     */
    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    /**
     * Supprime une arête entre deux sommets dans le graphe
     *
     * @param vertex1 Le premier sommet
     * @param vertex2 Le deuxième sommet
     */
    public void removeEdge(String vertex1, String vertex2) {
        adjacencyList.get(vertex1).remove(vertex2);
        adjacencyList.get(vertex2).remove(vertex1);
    }
}
