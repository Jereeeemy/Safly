package welshpowell;
import org.graphstream.graph.*;

import java.util.ArrayList;

public class welshpowell {
    Graph graph;
    ArrayList<Node> noeudsordonnes = new ArrayList<>() ;

    public welshpowell(Graph graph) {
        this.graph = graph;
        this.noeudsordonnes = new ArrayList<>();
        // Implémente tous les noeuds du graph dans la liste
        for (Node node : graph) {
            this.noeudsordonnes.add(node);
        }
    }

    private void RangerNoeudsOrdreDecroissant(Graph graph){
        for (int i = 1; i < noeudsordonnes.size(); i++) {
            Node key = noeudsordonnes.get(i);
            int keyValue = key.getDegree();
            int left = 0;
            int right = i;

            // Find the position to insert the current node (descending order)
            while (left < right) {
                int mid = (left + right) / 2;
                if (keyValue <= noeudsordonnes.get(mid).getDegree()) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // Shift nodes to the right to make space for the current node
            for (int j = i; j > left; j--) {
                noeudsordonnes.set(j, noeudsordonnes.get(j - 1));
            }

            // Insert the current node at the found position
            noeudsordonnes.set(left, key);
        }
    }
}
