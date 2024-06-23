package coloration;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;


public class Couleur {
    private String couleur;
    private int count;

    public Couleur(String color) {
        this.couleur = color;
        this.count = 1;
    }

    public Couleur() {
    }

    public String getCouleur() {
        return couleur;
    }

    public int getCount() {
        return count;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public void incrementCount() {
        this.count++;
    }

    // Méthode pour obtenir les nœuds adjacents
    public static List<Node> getAdjacentNodes(Node node) {
        List<Node> adjacentNodes = new ArrayList<>();
        for (Edge edge : node.getEachEdge()) {
            Node oppositeNode = edge.getOpposite(node);
            adjacentNodes.add(oppositeNode);
        }
        return adjacentNodes;
    }

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

