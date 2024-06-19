package coloration;
import org.graphstream.graph.*;

import java.util.ArrayList;
import java.util.List;

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

    }

    private void rangerNoeudsOrdreDecroissant(){
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


    public void colorierNoeudsWelsh(int kmax) {
        //Range les noeuds dans l'ordre décroissant de degré
        this.rangerNoeudsOrdreDecroissant();
        // Fonction pour générer des couleurs RGB de manière dynamique
        int colorIndex = 0;

        for (Node node : noeuds) {
            ArrayList<String> usedColors = new ArrayList<>();
            ArrayList<Couleur> colorCounts = new ArrayList<>();
            List<Node> adjacentNodes = getAdjacentNodes(node);

            // Collecter les couleurs utilisées par les nœuds adjacents
            for (Node neighbor : adjacentNodes) {
                if (neighbor.hasAttribute("ui.style")) {
                    String color = neighbor.getAttribute("ui.style");
                    usedColors.add(color);
                    ajoutColorCount(colorCounts, color);
                }
            }

            // Trouver la première couleur disponible qui n'est pas utilisée par les nœuds adjacents
            while (true) {
                String color = generateColor(colorIndex);
                if (!usedColors.contains("fill-color: " + color + ";")) {
                    if (colorIndex < kmax) {
                        node.setAttribute("ui.style", "fill-color: " + color + ";");
                        break;
                    }
                    else {
                        // Trouver la couleur la moins fréquente parmi les nœuds adjacents
                        String leastUsedColor = trouveCouleurMini(colorCounts);
                        if (leastUsedColor != null) {
                            node.setAttribute("ui.style", leastUsedColor);
                            break;
                        }
                    }
                }
                colorIndex++;
            }
        }
    }


    // Ajouter une couleur à la liste de comptage des couleurs
    private void ajoutColorCount(ArrayList<Couleur> colorCounts, String color) {
        for (Couleur cc : colorCounts) {
            if (cc.getCouleur().equals(color)) {
                cc.incrementCount();
                return;
            }
        }
        colorCounts.add(new Couleur(color));
    }

    // Trouver la couleur la moins utilisée
    private String trouveCouleurMini(ArrayList<Couleur> colorCounts) {
        String leastUsedColor = null;
        int minCount = Integer.MAX_VALUE;
        for (Couleur cc : colorCounts) {
            if (cc.getCount() < minCount) {
                minCount = cc.getCount();
                leastUsedColor = cc.getCouleur();
            }
        }
        return leastUsedColor;
    }

    // Obtenir les nœuds adjacents
    private List<Node> getAdjacentNodes(Node node) {
        List<Node> adjacentNodes = new ArrayList<>();
        for (Edge edge : node.getEachEdge()) {
            Node oppositeNode = edge.getOpposite(node);
            adjacentNodes.add(oppositeNode);
        }
        return adjacentNodes;
    }

    // Fonction pour générer une couleur RGB basée sur un index
    private String generateColor(int index) {
        int r = (index * 67) % 256;
        int g = (index * 137) % 256;
        int b = (index * 223) % 256;
        return "rgb(" + r + "," + g + "," + b + ")";
    }





}