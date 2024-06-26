package coloration;

import graphvol.CreateurGraph;
import graphvol.ExceptionFormatIncorrect;
import graphvol.ExceptionLigneIncorrect;
import graphvol.ExceptionNoGraphVol;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * La classe RLF (Recursive Largest First) implémente un algorithme de coloration de graphe.
 */
public class RLF {
    /**
     * Graph coloré
     */
    private Graph graph;

    /**
     * Nombre maximum de couleurs différentes
     */
    private int kmax;

    /**
     * Tableau contenant la coloration effectuée, à chaque élément on a un entier représentant la couleur du nœud de l'indice
     */
    private int[] nodeColors;


    /**
     * Constructeur de la classe RLF.
     *
     * @param graph le graphe à colorier
     * @param kmax  le nombre maximal de couleurs
     */
    public RLF(Graph graph, int kmax) {
        this.graph = graph;
        this.kmax = kmax;
        this.nodeColors = new int[graph.getNodeCount()];
    }

    public int[] getNodeColors() {
        return nodeColors;
    }

    /**
     * Colore le graphe en utilisant l'algorithme RLF.
     */
    public void colorGraph() {
        // Initialisation des couleurs de tous les nœuds à -1 (non colorié)
        Arrays.fill(nodeColors, -1);

        // Liste des nœuds non colorés
        List<Node> uncoloredNodes = new ArrayList<>(graph.getNodeSet());

        // Liste des nœuds déjà colorés dans la phase courante
        Set<Node> currentColorGroup = new HashSet<>();

        // Couleur actuelle
        int currentColor = 0;

        while (!uncoloredNodes.isEmpty()) {
            if (currentColorGroup.isEmpty()) {
                // Sélectionner le nœud avec le plus grand degré parmi les nœuds non colorés
                Node startNode = selectNodeWithHighestDegree(uncoloredNodes);
                colorNode(startNode, currentColor);
                currentColorGroup.add(startNode);
                uncoloredNodes.remove(startNode);
            }

            // Sélectionner le prochain nœud à colorier
            Node nextNode = selectNextNode(currentColorGroup, uncoloredNodes);

            if (nextNode != null) {
                int bestColor = selectBestColorForNode(nextNode);
                colorNode(nextNode, bestColor);
                currentColorGroup.add(nextNode);
                uncoloredNodes.remove(nextNode);
            } else {
                // Si aucun nœud n'est sélectionné, changer de couleur
                currentColorGroup.clear();
                currentColor++;
                if (currentColor >= kmax) {
                    currentColor = 0;
                }
            }
        }
    }


    /**
     * Sélectionne le nœud avec le plus grand degré parmi les nœuds non colorés.
     *
     * @param uncoloredNodes la liste des nœuds non colorés
     * @return le nœud avec le plus grand degré
     */
    private Node selectNodeWithHighestDegree(List<Node> uncoloredNodes) {
        return Collections.max(uncoloredNodes, Comparator.comparingInt(Node::getDegree));
    }

    /**
     * Sélectionne le prochain nœud à colorier.
     *
     * @param currentColorGroup le groupe des nœuds colorés dans la phase courante
     * @param uncoloredNodes    la liste des nœuds non colorés
     * @return le prochain nœud à colorier, ou null s'il n'y a pas de nœud approprié
     */
    private Node selectNextNode(Set<Node> currentColorGroup, List<Node> uncoloredNodes) {
        Node bestNode = null;
        int maxNonAdjacency = -1;

        for (Node node : uncoloredNodes) {
            int nonAdjacencyCount = 0;
            for (Node coloredNode : currentColorGroup) {
                if (!node.hasEdgeBetween(coloredNode)) {
                    nonAdjacencyCount++;
                }
            }

            if (nonAdjacencyCount > maxNonAdjacency) {
                maxNonAdjacency = nonAdjacencyCount;
                bestNode = node;
            }
        }

        return bestNode;
    }

    /**
     * Sélectionne la meilleure couleur pour un nœud donné.
     *
     * @param node le nœud à colorier
     * @return la meilleure couleur pour le nœud
     */
    private int selectBestColorForNode(Node node) {
        int[] conflictCount = new int[kmax];
        Arrays.fill(conflictCount, 0);

        for (Edge edge : node.getEachEdge()) {
            Node neighbor = edge.getOpposite(node);
            int neighborColor = nodeColors[neighbor.getIndex()];
            if (neighborColor != -1) {
                conflictCount[neighborColor]++;
            }
        }

        int bestColor = 0;
        int minConflicts = Integer.MAX_VALUE;
        for (int color = 0; color < kmax; color++) {
            if (conflictCount[color] < minConflicts) {
                minConflicts = conflictCount[color];
                bestColor = color;
            }
        }

        return bestColor;
    }

    /**
     * Colore un nœud avec une couleur spécifiée.
     *
     * @param node  le nœud à colorier
     * @param color la couleur à utiliser
     */
    private void colorNode(Node node, int color) {
        nodeColors[node.getIndex()] = color;
        node.setAttribute("ui.style", "fill-color: rgb(" + getColorRGB(color) + ");");
    }

    /**
     * Génère une couleur RGB pour une couleur donnée.
     *
     * @param color la couleur à convertir en RGB
     * @return la chaîne RGB correspondante
     */
    private String getColorRGB(int color) {
        // Générer des couleurs différentes pour chaque entier de 0 à kmax-1
        int r = (color * 70) % 256;
        int g = (color * 130) % 256;
        int b = (color * 200) % 256;
        return r + "," + g + "," + b;
    }



}
