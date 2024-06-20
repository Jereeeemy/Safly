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
    private static List<Node> getAdjacentNodes(Node node) {
        List<Node> adjacentNodes = new ArrayList<>();
        for (Edge edge : node.getEachEdge()) {
            Node oppositeNode = edge.getOpposite(node);
            adjacentNodes.add(oppositeNode);
        }
        return adjacentNodes;
    }


}

