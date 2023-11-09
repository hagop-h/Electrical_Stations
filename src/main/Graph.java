package main;
import java.util.*;

public class Graph {

    private Map<String, Set<String>> adjacencyList; // Représenter le graphe sous forme de liste d'adjacence

    public Graph() {
        adjacencyList = new HashMap<>(); // Initialiser le graphe en créant une liste d'adjacence vide

    }

    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new HashSet<>());
    }

    public void addEdge(String vertex1, String vertex2) {
        adjacencyList.get(vertex1).add(vertex2);
        adjacencyList.get(vertex2).add(vertex1);
    }

    public Set<String> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new HashSet<>());
    }

    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    public void removeEdge(String vertex1, String vertex2) {
        adjacencyList.get(vertex1).remove(vertex2);
        adjacencyList.get(vertex2).remove(vertex1);
    }

}
