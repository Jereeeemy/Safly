package dsature;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DsaturAlgorithm {

    // Classe interne pour représenter un nœud dans le graphe
    static class GraphNode implements Comparable<GraphNode> {
        int nodeId; // Identifiant unique du nœud
        int degree; // Degré du nœud (nombre de voisins)
        int saturationDegree; // Degré de saturation du nœud (nombre de couleurs uniques parmi ses voisins déjà colorés)

        // Constructeur de la classe GraphNode
        GraphNode(int nodeId, int degree) {
            this.nodeId = nodeId; // Initialise l'identifiant du nœud
            this.degree = degree; // Initialise le degré du nœud
            this.saturationDegree = 0; // Initialise le degré de saturation à 0
        }

        @Override
        public int compareTo(GraphNode other) {
            // Compare les nœuds en fonction de leur degré de saturation, puis de leur degré s'ils ont le même degré de saturation
            if (this.saturationDegree != other.saturationDegree) {
                return other.saturationDegree - this.saturationDegree; // Trie par degré de saturation décroissant
            }
            return other.degree - this.degree; // En cas d'égalité, trie par degré décroissant
        }
    }

    // Algorithme principal pour colorier le graphe avec un Kmax donné
    public static int[] dsaturColoring(Graph graph, int Kmax) {
        int numberOfVertices = graph.getNodeCount(); // Récupère le nombre de sommets du graphe
        int[] colors = new int[numberOfVertices]; // Tableau pour stocker les couleurs des sommets
        Arrays.fill(colors, -1); // Initialise toutes les couleurs à -1 (aucune couleur assignée)

        GraphNode[] nodes = new GraphNode[numberOfVertices]; // Tableau pour stocker les nœuds du graphe
        int index = 0;
        // Crée des objets GraphNode pour chaque nœud du graphe et les stocke dans le tableau nodes
        for (Node node : graph) {
            nodes[index] = new GraphNode(node.getIndex(), node.getDegree());
            index++;
        }

        TreeSet<GraphNode> nodeSet = new TreeSet<>();
        for (GraphNode node : nodes) {
            nodeSet.add(node);
        }

        // Boucle principale pour colorier les nœuds
        while (!nodeSet.isEmpty()) {
            GraphNode currentNode = nodeSet.pollFirst(); // Prend le nœud avec le plus haut degré de saturation
            boolean[] availableColors = new boolean[Kmax]; // Tableau pour suivre les couleurs disponibles
            Arrays.fill(availableColors, true); // Initialise toutes les couleurs comme disponibles

            Node graphNode = graph.getNode(currentNode.nodeId); // Récupère le nœud correspondant dans le graphe
            // Parcourt tous les voisins du nœud actuel pour marquer les couleurs déjà utilisées
            for (Edge edge : graphNode.getEachEdge()) {
                Node neighbor = edge.getOpposite(graphNode);
                if (colors[neighbor.getIndex()] != -1 && colors[neighbor.getIndex()] < Kmax) {
                    availableColors[colors[neighbor.getIndex()]] = false; // Marque la couleur utilisée comme indisponible
                }
            }

            int chosenColor = -1; // Initialisation de la couleur choisie à -1, représentant l'absence de couleur disponible
            for (int color = 0; color < Kmax; color++) {
                if (availableColors[color]) {
                    chosenColor = color; // Trouve la première couleur disponible
                    break; // Sort de la boucle dès qu'une couleur est trouvée
                }
            }

            if (chosenColor == -1) {
                // Si aucune couleur n'est disponible dans la limite de Kmax, trouve la couleur avec le moins de conflits
                chosenColor = findMinConflictColor(graph, colors, currentNode.nodeId, Kmax);
            }

            colors[currentNode.nodeId] = chosenColor; // Assigne la couleur choisie au nœud actuel

            // Met à jour les degrés de saturation des voisins du nœud actuel
            for (Edge edge : graphNode.getEachEdge()) {
                Node neighbor = edge.getOpposite(graphNode);
                if (colors[neighbor.getIndex()] == -1) {
                    GraphNode neighborNode = nodes[neighbor.getIndex()];
                    nodeSet.remove(neighborNode);
                    neighborNode.saturationDegree = calculateSaturationDegree(graph, colors, neighbor.getIndex());
                    nodeSet.add(neighborNode);
                }
            }
        }

        // Si certains sommets n'ont pas pu être coloriés avec Kmax, minimise les conflits restants
        for (int i = 0; i < numberOfVertices; i++) {
            if (colors[i] == -1) {
                colors[i] = findMinConflictColor(graph, colors, i, Kmax);
            }
        }

        return colors; // Retourne les couleurs des sommets
    }

    // Méthode pour trouver une couleur minimisant les conflits pour un nœud qui n'est pas déjà utilisée par ses voisins
    private static int findMinConflictColor(Graph graph, int[] colors, int nodeId, int Kmax) {
        int minConflicts = Integer.MAX_VALUE; // Initialise le nombre minimum de conflits à une valeur maximale
        int minConflictColor = 0; // Initialise la couleur minimisant les conflits

        Node node = graph.getNode(nodeId);
        for (int color = 0; color < Kmax; color++) {
            int conflicts = countConflicts(graph, colors, node, color);
            if (conflicts < minConflicts) {
                minConflicts = conflicts;
                minConflictColor = color;
            }
        }

        return minConflictColor; // Retourne la couleur minimisant les conflits
    }

    // Méthode pour compter le nombre de conflits d'un nœud donné s'il est coloré avec une couleur spécifique
    private static int countConflicts(Graph graph, int[] colors, Node node, int color) {
        int conflictCount = 0;
        for (Edge edge : node.getEachEdge()) {
            Node neighbor = edge.getOpposite(node);
            if (colors[neighbor.getIndex()] == color) {
                conflictCount++;
            }
        }
        return conflictCount;
    }

    // Calcule le degré de saturation d'un nœud donné dans le graphe
    private static int calculateSaturationDegree(Graph graph, int[] colors, int nodeId) {
        Set<Integer> uniqueColors = new HashSet<>();
        Node node = graph.getNode(nodeId);
        for (Edge edge : node.getEachEdge()) {
            Node neighbor = edge.getOpposite(node);
            if (colors[neighbor.getIndex()] != -1) {
                uniqueColors.add(colors[neighbor.getIndex()]);
            }
        }
        return uniqueColors.size(); // Retourne le nombre de couleurs uniques (le degré de saturation)
    }

    // Méthode pour colorier un graphe donné et afficher le résultat
    public static void colorAndDisplayGraph(Graph graph, int Kmax) {
        int[] result = dsaturColoring(graph, Kmax); // Applique l'algorithme Dsatur pour colorier le graphe

        // Affiche le nombre de sommets
        int numberOfVertices = graph.getNodeCount();
        System.out.println("Nombre de sommets: " + numberOfVertices);

        // Affiche chaque sommet avec sa couleur
        System.out.println("Sommet; Couleur");
        for (int i = 0; i < numberOfVertices; i++) {
            System.out.println(i + "; " + result[i]);
        }

        // Compte le nombre de couleurs utilisées
        Set<Integer> uniqueColors = new HashSet<>();
        for (int color : result) {
            uniqueColors.add(color);
        }
        System.out.println("Nombre de couleurs utilisées: " + uniqueColors.size());

        // Modifie la couleur des nœuds dans le graphe pour refléter les couleurs attribuées
        for (Node node : graph) {
            // Utilise les couleurs attribuées pour modifier l'aspect visuel des nœuds dans le graphe
            node.setAttribute("ui.style", "fill-color: rgb(" + (result[node.getIndex()] * 70 % 255) + "," + (result[node.getIndex()] * 130 % 255) + "," + (result[node.getIndex()] * 200 % 255) + ");");
        }

        //graph.display(); // Affiche le graphe colorié

        // Vérifie que chaque nœud a une couleur différente de ses voisins
        boolean allNodesCorrectlyColored = true;
        int totalConflicts = 0;
        for (Node node : graph) {
            int nodeId = node.getIndex();
            int nodeColor = result[nodeId];
            for (Edge edge : node.getEachEdge()) {
                Node neighbor = edge.getOpposite(node);
                int neighborColor = result[neighbor.getIndex()];
                if (nodeColor == neighborColor) {
                    System.out.println("Erreur: Le nœud " + nodeId + " et le nœud " + neighbor.getIndex() + " ont la même couleur.");
                    allNodesCorrectlyColored = false;
                    totalConflicts++;
                }
            }
        }

        if (allNodesCorrectlyColored) {
            System.out.println("Tous les nœuds ont des couleurs différentes de leurs voisins.");
        } else {
            System.out.println("Nombre total de conflits: " + totalConflicts);
        }
    }

    // Méthode principale
    public static void main(String[] args) {
        // Crée un nouveau graphe avec GraphStream
        Graph graph = new SingleGraph("DSATUR Graph");

        // Ajoute des nœuds au graphe
        graph.addNode("0");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addNode("4");
        graph.addNode("5");
        graph.addNode("6");
        graph.addNode("7");
        graph.addNode("8");
        graph.addNode("9");
        graph.addNode("10");

        // Ajoute des arêtes entre les nœuds
        graph.addEdge("0-1", "0", "1");
        graph.addEdge("0-2", "0", "2");
        graph.addEdge("1-2", "1", "2");
        graph.addEdge("1-3", "1", "3");
        graph.addEdge("2-3", "2", "3");
        graph.addEdge("3-4", "3", "4");
        graph.addEdge("4-5", "4", "5");
        graph.addEdge("5-6", "5", "6");
        graph.addEdge("6-7", "6", "7");
        graph.addEdge("7-8", "7", "8");
        graph.addEdge("8-9", "8", "9");
        graph.addEdge("9-10", "9", "10");

        // Appelle la méthode pour colorier le graphe et l'afficher
        colorAndDisplayGraph(graph, 1);
    }
}