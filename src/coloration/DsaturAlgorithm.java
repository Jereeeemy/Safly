package coloration;

import graphvol.CreateurGraph;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static graphvol.CreateurGraph.*;

/**
 * Classe implémentant l'algorithme DSATUR pour colorier les graphes et générer des résultats.
 */
public class DsaturAlgorithm {
    private static int numAfterNameFichierTxT = 0;
    /**
     * Classe interne représentant un nœud dans le graphe avec des informations supplémentaires.
     * Comparable permet de comparer les nœuds basés sur leur degré de saturation et leur degré.
     */
    static class GraphNode implements Comparable<GraphNode> {
        int nodeId; // Identifiant du nœud
        int degree; // Degré du nœud
        int saturationDegree; // Degré de saturation du nœud

        /**
         * Constructeur pour initialiser un nœud avec son identifiant et son degré.
         * @param nodeId Identifiant du nœud.
         * @param degree Degré du nœud.
         */
        GraphNode(int nodeId, int degree) {
            this.nodeId = nodeId;
            this.degree = degree;
            this.saturationDegree = 0;
        }

        /**
         * Méthode de comparaison pour trier les nœuds par degré de saturation puis par degré.
         * @param other Autre nœud à comparer.
         * @return Valeur négative si this est avant other, positive si this est après other, 0 si égaux.
         */
        @Override
        public int compareTo(GraphNode other) {
            if (this.saturationDegree != other.saturationDegree) {
                return other.saturationDegree - this.saturationDegree;
            }
            return other.degree - this.degree;
        }
    }

    /**
     * Applique l'algorithme DSATUR pour colorier un graphe.
     * @param graph Le graphe à colorier.
     * @param Kmax Le nombre maximum de couleurs à utiliser.
     * @return Un tableau des couleurs attribuées à chaque nœud.
     */
    public static int[] dsaturColoring(Graph graph, int Kmax) {
        int numberOfVertices = graph.getNodeCount(); // Nombre de nœuds dans le graphe
        int[] colors = new int[numberOfVertices]; // Tableau des couleurs des nœuds
        Arrays.fill(colors, 0); // Initialise toutes les couleurs à 0 (non colorié)

        GraphNode[] nodes = new GraphNode[numberOfVertices]; // Tableau des nœuds avec informations supplémentaires
        int index = 0;
        for (Node node : graph) {
            nodes[index] = new GraphNode(node.getIndex(), node.getDegree()); // Crée les objets GraphNode
            index++;
        }

        TreeSet<GraphNode> nodeSet = new TreeSet<>(); // Ensemble trié des nœuds par degré de saturation et degré
        for (GraphNode node : nodes) {
            nodeSet.add(node);
        }

        // Boucle principale de l'algorithme DSATUR
        while (!nodeSet.isEmpty()) {
            GraphNode currentNode = nodeSet.pollFirst(); // Récupère le nœud avec le degré de saturation le plus élevé
            boolean[] availableColors = new boolean[Kmax + 1]; // Tableau des couleurs disponibles
            Arrays.fill(availableColors, true);

            Node graphNode = graph.getNode(currentNode.nodeId); // Récupère le nœud dans le graphe
            for (Edge edge : graphNode.getEachEdge()) {
                Node neighbor = edge.getOpposite(graphNode);
                if (colors[neighbor.getIndex()] != 0 && colors[neighbor.getIndex()] <= Kmax) {
                    availableColors[colors[neighbor.getIndex()]] = false; // Marque les couleurs déjà utilisées par les voisins
                }
            }

            int chosenColor = 0;
            for (int color = 1; color <= Kmax; color++) {
                if (availableColors[color]) {
                    chosenColor = color; // Choisit la première couleur disponible
                    break;
                }
            }

            if (chosenColor == 0) {
                chosenColor = findMinConflictColor(graph, colors, currentNode.nodeId, Kmax); // Trouve la couleur avec le moins de conflits
            }

            colors[currentNode.nodeId] = chosenColor; // Assigne la couleur choisie au nœud

            for (Edge edge : graphNode.getEachEdge()) {
                Node neighbor = edge.getOpposite(graphNode);
                if (colors[neighbor.getIndex()] == 0) {
                    GraphNode neighborNode = nodes[neighbor.getIndex()];
                    nodeSet.remove(neighborNode); // Met à jour le degré de saturation des voisins non coloriés
                    neighborNode.saturationDegree = calculateSaturationDegree(graph, colors, neighbor.getIndex());
                    nodeSet.add(neighborNode);
                }
            }
        }

        for (int i = 0; i < numberOfVertices; i++) {
            if (colors[i] == 0) {
                colors[i] = findMinConflictColor(graph, colors, i, Kmax); // Assure qu'aucun nœud n'est laissé sans couleur
            }
        }

        return colors;
    }

    /**
     * Trouve la couleur avec le moins de conflits pour un nœud donné.
     * @param graph Le graphe.
     * @param colors Tableau des couleurs des nœuds.
     * @param nodeId L'identifiant du nœud.
     * @param Kmax Le nombre maximum de couleurs à utiliser.
     * @return La couleur avec le moins de conflits.
     */
    private static int findMinConflictColor(Graph graph, int[] colors, int nodeId, int Kmax) {
        int minConflicts = Integer.MAX_VALUE;
        int minConflictColor = 1;

        Node node = graph.getNode(nodeId);
        for (int color = 1; color <= Kmax; color++) {
            int conflicts = countConflicts(graph, colors, node, color); // Compte les conflits pour chaque couleur
            if (conflicts < minConflicts) {
                minConflicts = conflicts;
                minConflictColor = color; // Choisit la couleur avec le moins de conflits
            }
        }

        return minConflictColor;
    }

    /**
     * Compte les conflits de couleurs pour un nœud donné.
     * @param graph Le graphe.
     * @param colors Tableau des couleurs des nœuds.
     * @param node Le nœud.
     * @param color La couleur à tester.
     * @return Le nombre de conflits.
     */
    private static int countConflicts(Graph graph, int[] colors, Node node, int color) {
        int conflictCount = 0;
        for (Edge edge : node.getEachEdge()) {
            Node neighbor = edge.getOpposite(node);
            if (colors[neighbor.getIndex()] == color) {
                conflictCount++; // Incrémente le nombre de conflits si un voisin a la même couleur
            }
        }
        return conflictCount;
    }

    /**
     * Calcule le degré de saturation pour un nœud donné.
     * @param graph Le graphe.
     * @param colors Tableau des couleurs des nœuds.
     * @param nodeId L'identifiant du nœud.
     * @return Le degré de saturation.
     */
    private static int calculateSaturationDegree(Graph graph, int[] colors, int nodeId) {
        Set<Integer> uniqueColors = new HashSet<>();
        Node node = graph.getNode(nodeId);
        for (Edge edge : node.getEachEdge()) {
            Node neighbor = edge.getOpposite(node);
            if (colors[neighbor.getIndex()] != 0) {
                uniqueColors.add(colors[neighbor.getIndex()]); // Ajoute les couleurs uniques des voisins
            }
        }
        return uniqueColors.size();
    }


    /**
     * Colore le graphe, affiche les informations et enregistre les résultats.
     * @param graph Le graphe.
     * @param Kmax Le nombre maximum de couleurs à utiliser.
     * @param nomFichier Le nom du fichier pour les résultats.
     * @param outputDirectory Le répertoire de sortie pour les fichiers.
     * @return Un tableau des fichiers générés.
     */
    public static File[] colorAndDisplayGraph(Graph graph, int Kmax, String nomFichier, String outputDirectory) {
        int[] result = dsaturColoring(graph, Kmax); // Applique l'algorithme DSatur pour colorier le graphe
        displayGraphInfo(graph, result); // Affiche les informations sur le graphe et ses couleurs
        modifyNodeColors(graph, result); // Modifie les couleurs des nœuds pour l'affichage graphique

        int totalConflicts = checkColorConflicts(graph, result); // Vérifie les conflits de couleurs dans le graphe

        File csvFile = writeCSVFile(nomFichier, totalConflicts, outputDirectory); // Écrit les résultats dans un fichier CSV
        File txtFile = writeTxtFile(graph, result, outputDirectory); // Écrit les résultats dans un fichier texte

        return new File[]{csvFile, txtFile}; // Retourne les fichiers générés
    }


    /**
     * Affiche les informations du graphe.
     * @param graph Le graphe.
     * @param result Tableau des couleurs des nœuds.
     */
    private static void displayGraphInfo(Graph graph, int[] result) {
        int numberOfVertices = graph.getNodeCount();
        System.out.println("Nombre de sommets: " + numberOfVertices); // Affiche le nombre total de sommets dans le graphe
        System.out.println("Sommet; Couleur");
        for (int i = 0; i < numberOfVertices; i++) {
            System.out.println((i + 1) + " ; " + result[i]); // Affiche l'identifiant du nœud et sa couleur correspondante
        }

        Set<Integer> uniqueColors = new HashSet<>();
        for (int color : result) {
            uniqueColors.add(color);
        }
        System.out.println("Nombre de couleurs utilisées: " + uniqueColors.size()); // Affiche le nombre de couleurs différentes utilisées
    }


    /**
     * Modifie les couleurs des nœuds dans le graphe pour l'affichage.
     * @param graph Le graphe.
     * @param result Tableau des couleurs des nœuds.
     */
    public static void modifyNodeColors(Graph graph, int[] result) {
        for (Node node : graph) {
            node.setAttribute("ui.style", "fill-color: rgb(" +
                    (result[node.getIndex()] * 70 % 255) + "," +
                    (result[node.getIndex()] * 130 % 255) + "," +
                    (result[node.getIndex()] * 200 % 255) + ");"); // Modifie la couleur des nœuds pour l'affichage graphique
        }
    }

    /**
     * Vérifie les conflits de couleurs dans le graphe.
     * @param graph Le graphe.
     * @param result Tableau des couleurs des nœuds.
     * @return Le nombre total de conflits de couleurs.
     */
    private static int checkColorConflicts(Graph graph, int[] result) {
        Set<String> checkedPairs = new HashSet<>(); // Initialise un ensemble pour stocker les paires de nœuds déjà vérifiées
        int totalConflicts = 0; // Initialise le compteur de conflits à zéro
        for (Node node : graph) { // Parcourt tous les nœuds du graphe
            int nodeId = node.getIndex(); // Récupère l'identifiant du nœud actuel
            int nodeColor = result[nodeId]; // Récupère la couleur du nœud actuel à partir du tableau des résultats
            for (Edge edge : node.getEachEdge()) { // Parcourt toutes les arêtes du nœud actuel
                Node neighbor = edge.getOpposite(node); // Récupère le voisin connecté par cette arête
                int neighborId = neighbor.getIndex(); // Récupère l'identifiant du voisin
                int neighborColor = result[neighborId]; // Récupère la couleur du voisin à partir du tableau des résultats
                // Génère une clé pour la paire de nœuds afin de gérer les paires dans les deux sens
                String pairKey = nodeId < neighborId ? nodeId + "-" + neighborId : neighborId + "-" + nodeId;
                // Vérifie si les deux nœuds ont la même couleur et si cette paire n'a pas déjà été vérifiée
                if (nodeColor == neighborColor && !checkedPairs.contains(pairKey)) {
                    System.out.println("Erreur: Le nœud " + nodeId + " et le nœud " + neighborId + " ont la même couleur.");
                    checkedPairs.add(pairKey); // Ajoute la paire à l'ensemble des paires vérifiées
                    totalConflicts++; // Incrémente le compteur de conflits
                }
            }
        }
        if (totalConflicts == 0) {
            System.out.println("Tous les nœuds ont des couleurs différentes de leurs voisins.");
        } else {
            System.out.println("Nombre total de conflits: " + totalConflicts); // Affiche le nombre total de conflits détectés
        }
        return totalConflicts; // Retourne le nombre total de conflits de couleurs
    }




    /**
     * Méthode principale pour tester l'algorithme DSATUR.
     * @param args Arguments de la ligne de commande (non utilisés ici).
     */
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

        int Kmax = 5;
        String nomFichier = "nom_fichier.txt";
        String outputDirectory = "output_directory";
        colorAndDisplayGraph(graph, Kmax, nomFichier, outputDirectory);// Colore le graphe et affiche les résultats

        // Écriture des sommets et couleurs dans un fichier texte
        writeColorsToTxtFile(graph, dsaturColoring(graph, Kmax), "sommets_couleurs.txt");
    }

    private List<Node> getAdjacentNodes(Node node) {
        List<Node> adjacentNodes = new ArrayList<>();
        for (Edge edge : node.getEachEdge()) {
            Node oppositeNode = edge.getOpposite(node);
            adjacentNodes.add(oppositeNode);
        }
        return adjacentNodes;
    }

    // Méthode pour compter et afficher les conflits
    public int CompterConflits(Graph graph) {
        int conflictCount = 0;

        for (Node node : graph) {
            List<Node> adjacentNodes = getAdjacentNodes(node);

            for (Node neighbor : adjacentNodes) {
                if (node.hasAttribute("ui.style") && neighbor.hasAttribute("ui.style")) {
                    String color1 = node.getAttribute("ui.style");
                    String color2 = neighbor.getAttribute("ui.style");
                    if (color1.equals(color2)) {
                        conflictCount++;
                    }
                }
            }
        }
        conflictCount = conflictCount / 2;
        return conflictCount;
    }

}