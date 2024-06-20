package coloration;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;

public class WelshPowell {
    Graph graph;
    ArrayList<Node> noeuds = new ArrayList<>();

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

    private void rangerNoeudsOrdreDecroissant() {
        for (int i = 1; i < noeuds.size(); i++) {
            Node key = noeuds.get(i);
            int keyValue = key.getDegree();
            int left = 0;
            int right = i;

            // Trouver la position pour insérer le nœud actuel (ordre décroissant)
            while (left < right) {
                int mid = (left + right) / 2;
                if (keyValue <= noeuds.get(mid).getDegree()) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // Décaler les nœuds vers la droite pour faire de la place pour le nœud actuel
            for (int j = i; j > left; j--) {
                noeuds.set(j, noeuds.get(j - 1));
            }

            // Insérer le nœud actuel à la position trouvée
            noeuds.set(left, key);
        }
    }

    public void colorierNoeudsWelsh(int kmax) {
        // Range les nœuds dans l'ordre décroissant de degré
        this.rangerNoeudsOrdreDecroissant();
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
            boolean colorSet = false;
            while (!colorSet) {
                String color = generateColor(colorIndex);
                if (!usedColors.contains("fill-color: " + color + ";")) {
                    node.setAttribute("ui.style", "fill-color: " + color + ";");
                    colorSet = true;
                } else {
                    if (colorIndex < kmax-1) {
                        colorIndex++;
                    } else {
                        // Trouver la couleur la moins fréquente parmi les nœuds adjacents
                        String leastUsedColor = trouveCouleurMini(colorCounts);
                        if (leastUsedColor != null) {
                            node.setAttribute("ui.style", leastUsedColor);
                            colorSet = true;
                        } else {
                            // Si toutes les couleurs sont utilisées également, on sort pour éviter une boucle infinie
                            break;
                        }
                    }
                }
            }

            // Réinitialiser colorIndex pour la prochaine itération
            colorIndex = 0;
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
        int r = ((index * 67) % 256);
        int g = ((index * 137) % 256);
        int b = ((index * 223) % 256);
        return "rgb(" + r + "," + g + "," + b + ")";
    }
}