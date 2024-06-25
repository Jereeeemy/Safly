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

public class RLF {
    private Graph graph;
    private int kmax;
    private int[] nodeColors;

    public RLF(Graph graph, int kmax) {
        this.graph = graph;
        this.kmax = kmax;
        this.nodeColors = new int[graph.getNodeCount()];
    }

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

    private Node selectNodeWithHighestDegree(List<Node> uncoloredNodes) {
        return Collections.max(uncoloredNodes, Comparator.comparingInt(Node::getDegree));
    }

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

    private void colorNode(Node node, int color) {
        nodeColors[node.getIndex()] = color;
        node.setAttribute("ui.style", "fill-color: rgb(" + getColorRGB(color) + ");");
    }

    private String getColorRGB(int color) {
        // Générer des couleurs différentes pour chaque entier de 0 à kmax-1
        int r = (color * 70) % 256;
        int g = (color * 130) % 256;
        int b = (color * 200) % 256;
        return r + "," + g + "," + b;
    }

    public int[] getNodeColors() {
        return nodeColors;
    }

}
