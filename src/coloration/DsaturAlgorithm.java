package coloration;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;

public class DsaturAlgorithm {

    // Classe pour représenter un nœud dans le graphe
    static class GraphNode implements Comparable<GraphNode> {
        int nodeId; // Identifiant unique du nœud
        int degree; // Degré du nœud (nombre de voisins)
        int saturationDegree; // Degré de saturation du nœud (nombre de couleurs uniques parmi ses voisins déjà colorés)

        GraphNode(int nodeId, int degree) {
            this.nodeId = nodeId; // Initialise l'identifiant du nœud
            this.degree = degree; // Initialise le degré du nœud
            this.saturationDegree = 0; // Initialise le degré de saturation à 0
        }

        @Override
        public int compareTo(GraphNode other) {
            if (this.saturationDegree != other.saturationDegree) {
                return other.saturationDegree - this.saturationDegree; // Trie par degré de saturation décroissant
            }
            return other.degree - this.degree; // En cas d'égalité, trie par degré décroissant
        }
    }

    public static int[] dsaturColoring(Graph graph) {
        int numberOfVertices = graph.getNodeCount(); // Récupère le nombre de sommets du graphe
        int[] colors = new int[numberOfVertices]; // Tableau pour stocker les couleurs des sommets
        Arrays.fill(colors, -1); // Initialise toutes les couleurs à -1 (aucune couleur assignée)

        GraphNode[] nodes = new GraphNode[numberOfVertices]; // Tableau pour stocker les nœuds
        int index = 0;
        for (Node node : graph) {
            nodes[index] = new GraphNode(index, node.getDegree()); // Crée un nœud avec son degré
            index++;
        }

        TreeSet<GraphNode> nodeSet = new TreeSet<>(); // Crée un TreeSet pour trier les nœuds
        Collections.addAll(nodeSet, nodes); // Ajoute tous les nœuds dans le TreeSet

        colors[nodeSet.first().nodeId] = 0; // Colorie le nœud avec le plus haut degré avec la première couleur
        nodeSet.remove(nodeSet.first()); // Retire ce nœud du TreeSet

        while (!nodeSet.isEmpty()) {
            GraphNode currentNode = nodeSet.pollFirst(); // Prend le nœud avec le plus haut degré de saturation
            boolean[] availableColors = new boolean[numberOfVertices]; // Tableau pour suivre les couleurs disponibles
            Arrays.fill(availableColors, true); // Initialise toutes les couleurs comme disponibles

            Node graphNode = graph.getNode(currentNode.nodeId);
            for (Edge edge : graphNode.getEachEdge()) { // Boucle pour chaque voisin du nœud actuel
                Node neighbor = edge.getOpposite(graphNode);
                if (colors[neighbor.getIndex()] != -1) { // Vérifie si le voisin est colorié
                    availableColors[colors[neighbor.getIndex()]] = false; // Marque la couleur utilisée par le voisin comme non disponible
                }
            }

            int chosenColor;
            for (chosenColor = 0; chosenColor < numberOfVertices; chosenColor++) { // Boucle pour trouver la première couleur disponible
                if (availableColors[chosenColor]) { // Vérifie si la couleur est disponible
                    break; // Sort de la boucle lorsqu'une couleur disponible est trouvée
                }
            }

            colors[currentNode.nodeId] = chosenColor; // Assigne la couleur choisie au nœud actuel

            for (Edge edge : graphNode.getEachEdge()) { // Boucle pour chaque voisin du nœud actuel
                Node neighbor = edge.getOpposite(graphNode);
                if (colors[neighbor.getIndex()] == -1) { // Vérifie si le voisin n'est pas encore colorié
                    GraphNode neighborNode = nodes[neighbor.getIndex()]; // Récupère l'objet nœud du voisin
                    nodeSet.remove(neighborNode); // Retire le voisin du TreeSet
                    neighborNode.saturationDegree = calculateSaturationDegree(graph, colors, neighbor.getIndex()); // Recalcule le degré de saturation du voisin
                    nodeSet.add(neighborNode); // Réajoute le voisin au TreeSet avec le nouveau degré de saturation
                }
            }
        }

        return colors; // Retourne les couleurs des sommets
    }

    private static int calculateSaturationDegree(Graph graph, int[] colors, int nodeId) {
        Set<Integer> uniqueColors = new HashSet<>(); // Utilise un ensemble pour stocker les couleurs uniques des voisins
        Node node = graph.getNode(nodeId);
        for (Edge edge : node.getEachEdge()) { // Boucle pour chaque voisin du nœud
            Node neighbor = edge.getOpposite(node);
            if (colors[neighbor.getIndex()] != -1) { // Vérifie si le voisin est colorié
                uniqueColors.add(colors[neighbor.getIndex()]); // Ajoute la couleur du voisin à l'ensemble
            }
        }
        return uniqueColors.size(); // Retourne le nombre de couleurs uniques (le degré de saturation)
    }

    public static void colorAndDisplayGraph(Graph graph) {
        int[] result = dsaturColoring(graph);
        System.out.println("Node Colors: " + Arrays.toString(result));

        for (Node node : graph) {
            node.setAttribute("ui.style", "fill-color: rgb(" + (result[node.getIndex()] * 40 % 255) + "," + (result[node.getIndex()] * 80 % 255) + "," + (result[node.getIndex()] * 120 % 255) + ");");
        }

        graph.display();
    }

    public static void main(String[] args) {
        Graph graph = new SingleGraph("DSATUR Graph"); // Crée un graphe avec GraphStream
        graph.addNode("0");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addNode("4");
        graph.addNode("5");

        graph.addEdge("0-1", "0", "1");
        graph.addEdge("0-2", "0", "2");
        graph.addEdge("1-2", "1", "2");
        graph.addEdge("1-3", "1", "3");
        graph.addEdge("2-3", "2", "3");
        graph.addEdge("3-4", "3", "4");
        graph.addEdge("4-5", "4", "5");

        colorAndDisplayGraph(graph);
    }
}