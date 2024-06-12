package welshpowell;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WelshPowell {
    Graph graph;
    ArrayList<Node> noeuds = new ArrayList<>() ;

    public ArrayList<Node> getNoeuds() {
        return noeuds;
    }

    public Graph getGraph() {
        return graph;
    }

    public WelshPowell(Graph graph) {
        this.graph = graph;
        this.noeuds = new ArrayList<>();
        // Implémente tous les noeuds du graph dans la liste
        for (Node node : graph) {
            this.noeuds.add(node);
        }
        //Range les noeuds dans l'ordre décroissant de degré
        this.RangerNoeudsOrdreDecroissant();
        this.ColorierNoeuds();

    }

    private void RangerNoeudsOrdreDecroissant(){
        for (int i = 1; i < noeuds.size(); i++) {
            Node key = noeuds.get(i);
            int keyValue = key.getDegree();
            int left = 0;
            int right = i;

            // Find the position to insert the current node (descending order)
            while (left < right) {
                int mid = (left + right) / 2;
                if (keyValue <= noeuds.get(mid).getDegree()) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // Shift nodes to the right to make space for the current node
            for (int j = i; j > left; j--) {
                noeuds.set(j, noeuds.get(j - 1));
            }

            // Insert the current node at the found position
            noeuds.set(left, key);
        }
    }

    private void ColorierNoeuds() {
        // Fonction pour générer des couleurs RGB de manière dynamique
        int colorIndex = 0;

        for (Node node : noeuds) {
            Set<String> usedColors = new HashSet<>();

            // Collecter les couleurs utilisées par les nœuds adjacents
            for (Edge edge : node.getEachEdge()) {
                Node neighbor = edge.getOpposite(node);
                if (neighbor.hasAttribute("ui.style")) {
                    String color = neighbor.getAttribute("ui.style");
                    usedColors.add(color);
                }
            }

            // Trouver la première couleur disponible qui n'est pas utilisée par les nœuds adjacents
            while (true) {
                String color = generateColor(colorIndex);
                if (!usedColors.contains("fill-color: " + color + ";")) {
                    node.setAttribute("ui.style", "fill-color: " + color + ";");
                    break;
                }
                colorIndex++;
            }
        }
    }

    // Fonction pour générer une couleur RGB basée sur un index
    private String generateColor(int index) {
        int r = (index * 67) % 256;
        int g = (index * 137) % 256;
        int b = (index * 223) % 256;
        return "rgb(" + r + "," + g + "," + b + ")";
    }

}