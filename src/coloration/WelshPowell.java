package coloration;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;

import static coloration.Couleur.getAdjacentNodes;

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

    public int[] colorierNoeudsWelsh(int kmax) {
        ArrayList<Integer> coloration = new ArrayList<>();
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
            coloration.add(colorIndex+1);
            // Réinitialiser colorIndex pour la prochaine itération
            colorIndex = 0;
        }
        int[] colorationtab = new int[coloration.size()];
        for (int i = 0; i<coloration.size(); i++) {
            colorationtab[i] = coloration.get(i);
        }
        return colorationtab;
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

    // Fonction pour générer une couleur RGB basée sur un index
    private String generateColor(int index) {
        int r = ((index * 67) % 256);
        int g = ((index * 137) % 256);
        int b = ((index * 223) % 256);
        return "rgb(" + r + "," + g + "," + b + ")";
    }



    public int CompteCouleurs(){
        int compte = 0;
        ArrayList<String> ListeCouleurs = new ArrayList<>();
        for (Node node : this.getGraph()) {
            String couleur = node.getAttribute("ui.style");
            if (couleur !=null) {
                if (!ListeCouleurs.contains(couleur)) {
                    ListeCouleurs.add(couleur);
                }
            }
        }
        compte = ListeCouleurs.size();
        return compte;
    }

}