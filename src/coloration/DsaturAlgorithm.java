package coloration;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static graphvol.CreateurGraph.writeCSVFile;
import static graphvol.CreateurGraph.writeTxtFile;


/**
 * Classe implémentant l'algorithme DSATUR pour colorier les graphes et générer des résultats.
 */
public class DsaturAlgorithm {
    /**
     * Classe interne représentant un nœud dans le graphe avec des informations supplémentaires.
     * Comparable permet de comparer les nœuds basés sur leur degré de saturation et leur degré.
     */
    static class GraphNode implements Comparable<GraphNode> {
        private int nodeId; // Identifiant du nœud
        private int degree; // Degré du nœud
        private int saturationDegree; // Degré de saturation du nœud

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
     * Cet algorithme utilise DSATUR pour attribuer des couleurs aux nœuds
     * d'un graphe de manière à minimiser les conflits entre les nœuds adjacents.
     *
     * @param graph Le graph à colorier.
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

        // Assure qu'aucun nœud n'est laissé sans couleur
        for (int i = 0; i < numberOfVertices; i++) {
            if (colors[i] == 0) {
                colors[i] = findMinConflictColor(graph, colors, i, Kmax);
            }
        }

        return colors;
    }

    /**
     * Trouve la couleur avec le moins de conflits pour un nœud donné.
     *
     * Cette méthode parcourt toutes les couleurs possibles pour un nœud
     * donné et détermine celle qui entraîne le moins de conflits avec ses voisins.
     *
     * @param graph Le graphe dans lequel le nœud est situé.
     * @param colors Tableau des couleurs attribuées aux nœuds du graphe.
     * @param nodeId L'identifiant du nœud pour lequel trouver la couleur avec le moins de conflits.
     * @param Kmax Le nombre maximum de couleurs à utiliser.
     * @return La couleur avec le moins de conflits pour le nœud spécifié.
     */
    public static int findMinConflictColor(Graph graph, int[] colors, int nodeId, int Kmax) {
        // Initialise le nombre minimum de conflits à une valeur maximale possible
        int minConflicts = Integer.MAX_VALUE;
        // Initialise la couleur avec le moins de conflits à la première couleur possible (1)
        int minConflictColor = 1;

        // Récupère le nœud spécifié par son identifiant dans le graphe
        Node node = graph.getNode(nodeId);

        // Parcourt toutes les couleurs possibles de 1 à Kmax
        for (int color = 1; color <= Kmax; color++) {
            // Compte le nombre de conflits pour le nœud avec la couleur actuelle
            int conflicts = countConflicts(colors, node, color); // Compte les conflits pour chaque couleur

            // Met à jour la couleur avec le moins de conflits si la couleur actuelle a moins de conflits
            if (conflicts < minConflicts) {
                minConflicts = conflicts;
                minConflictColor = color; // Choisit la couleur avec le moins de conflits
            }
        }

        // Retourne la couleur avec le moins de conflits pour le nœud spécifié
        return minConflictColor;
    }


    /**
     * Compte les conflits de couleurs pour un nœud donné.
     * Un conflit de couleur pour un nœud se produit lorsque ce nœud a un voisin
     * qui a la même couleur.
     *
     * @param colors Tableau des couleurs attribuées aux nœuds du graphe.
     * @param node Le nœud pour lequel compter les conflits.
     * @param color La couleur à tester pour les conflits.
     * @return Le nombre de conflits de couleur pour le nœud spécifié avec la couleur donnée.
     */
    private static int countConflicts(int[] colors, Node node, int color) {
        // Initialise un compteur pour le nombre de conflits
        int conflictCount = 0;

        // Parcourt toutes les arêtes (edges) adjacentes au nœud spécifié
        for (Edge edge : node.getEachEdge()) {
            // Récupère le voisin opposé au nœud actuel le long de cette arête
            Node neighbor = edge.getOpposite(node);

            // Vérifie si le voisin a la même couleur que celle testée
            if (colors[neighbor.getIndex()] == color) {
                // Incrémente le compteur de conflits si un voisin a la même couleur
                conflictCount++; // Incrémente le nombre de conflits si un voisin a la même couleur
            }
        }

        // Retourne le nombre total de conflits pour le nœud spécifié avec la couleur donnée
        return conflictCount;
    }


    /**
     * Calcule le degré de saturation pour un nœud donné.
     *
     * Le degré de saturation d'un nœud est défini comme le nombre de couleurs différentes
     * utilisées par ses voisins dans le graphe.
     *
     * @param graph Le graphe dans lequel le nœud est situé.
     * @param colors Tableau des couleurs attribuées aux nœuds du graphe.
     * @param nodeId L'identifiant du nœud pour lequel calculer le degré de saturation.
     * @return Le nombre de couleurs uniques utilisées par les voisins du nœud spécifié.
     */
    public static int calculateSaturationDegree(Graph graph, int[] colors, int nodeId) {
        // Initialise un ensemble pour stocker les couleurs uniques des voisins
        Set<Integer> uniqueColors = new HashSet<>();

        // Récupère le nœud spécifié dans le graphe
        Node node = graph.getNode(nodeId);

        // Parcourt toutes les arêtes (edges) adjacentes au nœud spécifié
        for (Edge edge : node.getEachEdge()) {
            // Récupère le voisin opposé au nœud actuel le long de cette arête
            Node neighbor = edge.getOpposite(node);

            // Vérifie si le voisin a déjà été coloré (sa couleur n'est pas 0)
            if (colors[neighbor.getIndex()] != 0) {
                // Ajoute la couleur du voisin à l'ensemble des couleurs uniques
                uniqueColors.add(colors[neighbor.getIndex()]); // Ajoute les couleurs uniques des voisins
            }
        }

        // Retourne le nombre de couleurs uniques utilisées par les voisins du nœud spécifié
        return uniqueColors.size();
    }


    /**
     * Modifie les couleurs des nœuds dans le graphe pour l'affichage.
     * Cette méthode assigne une couleur à chaque nœud en fonction de son index dans le tableau de résultats.
     * Les couleurs sont générées en utilisant une formule basée sur l'index pour obtenir des valeurs RGB.
     *
     * @param graph Le graphe dont les nœuds doivent être colorés.
     * @param result Tableau des couleurs des nœuds. Chaque index correspond à un nœud, et la valeur à cet index représente la couleur.
     */
    public static void modifyNodeColors(Graph graph, int[] result) {
        // Parcourt tous les nœuds du graphe
        for (Node node : graph) {
            // Calcule les valeurs RGB basées sur l'index et la couleur du nœud
            int red = (result[node.getIndex()] * 70 % 255);
            int green = (result[node.getIndex()] * 130 % 255);
            int blue = (result[node.getIndex()] * 200 % 255);

            // Définit l'attribut de style "ui.style" du nœud pour changer sa couleur
            node.setAttribute("ui.style", "fill-color: rgb(" + red + "," + green + "," + blue + ");");
        }
    }

    /**
     * Colorie le graphe, affiche les informations et enregistre les résultats.
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
     * Vérifie les conflits de couleurs dans le graphe.
     * @param graph Le graphe.
     * @param result Tableau des couleurs des nœuds.
     * @return Le nombre total de conflits de couleurs.
     */
    public static int checkColorConflicts(Graph graph, int[] result) {
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


}