package coloration;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;

import static graphvol.CreateurGraph.getAdjacentNodes;

/**
 * Class représentant l'algorithme de Welsh et Powell ainsi que différentes opérations liées
 */
public class WelshPowell {
    /**
     * Graph auquel on applique la coloration
     */
    private Graph graph;

    /**
     * Liste des noeuds du graph
     */
    private ArrayList<Node> noeuds = new ArrayList<>();

    public ArrayList<Node> getNoeuds() {
        return noeuds;
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Constructeur de la class
     * @param graph Graph auquel on veut appliquer l'algorithme de Welsh & Powell
     */
    public WelshPowell(Graph graph) {
        this.graph = graph;
        this.noeuds = new ArrayList<>();
        // Implémente tous les nœuds du graph dans la liste des noeuds
        for (Node node : graph) {
            this.noeuds.add(node);
        }
    }

    /**
     * Range les nœuds dans l'ordre décroissant de leurs degrée
     */
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

    /**
     * Colorie le graph qui est en attribut
     * @param kmax Nombre maximum de couleurs
     * @return Tableau contenant la coloration effectuée, à chaque élément on a un entier représentant la couleur du nœud de l'indice
     */
    public int[] colorierNoeudsWelsh(int kmax) {
        for (Node noeud : getGraph()){
            noeud.removeAttribute("ui-style");
        }
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

    /**
     * Ajoute une couleur à la liste des couleurs si elle n'existe pas déjà, ou incrémente le compteur si elle existe.
     * @param colorCounts La liste des objets Couleur représentant les couleurs et leurs occurrences.
     * @param color       La couleur à ajouter ou à incrémenter.
     */
    private void ajoutColorCount(ArrayList<Couleur> colorCounts, String color) {
        for (Couleur cc : colorCounts) {
            if (cc.getCouleur().equals(color)) {
                cc.incrementCount();
                return;
            }
        }
        colorCounts.add(new Couleur(color));
    }


    /**
     * Trouve la couleur la moins utilisée dans la liste des couleurs.
     *
     * @param colorCounts La liste des objets Couleur représentant les couleurs et leurs occurrences.
     * @return La couleur la moins utilisée sous forme de chaîne de caractères.
     */
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

    /**
     * Génère une couleur RGB basée sur un index.
     *
     * @param index L'index utilisé pour générer la couleur.
     * @return La couleur générée sous forme de chaîne de caractères au format "rgb(r,g,b)".
     */
    private String generateColor(int index) {
        int r = ((index * 67) % 256);
        int g = ((index * 137) % 256);
        int b = ((index * 223) % 256);
        return "rgb(" + r + "," + g + "," + b + ")";
    }


    /**
     * Compte le nombre de couleurs uniques utilisées dans le graph.
     * @return Le nombre de couleurs uniques utilisées.
     */
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