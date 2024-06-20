package tests;

import applicationihm.MenuPrincipal;
import collisions.Carte;
import collisions.Collision;
import collisions.ExceptionNoFlight;
import collisions.ExceptionOrientation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import coloration.WelshPowell;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class TestCollisionAvecChoix {
    public static Carte test;

    static {
        try {
            test = new Carte();
        }
        catch (IOException | ExceptionNoFlight | ExceptionOrientation e) {
            System.out.println(e.getMessage());
        }
        catch (InputMismatchException e) {
            System.out.println("Saisie incorrecte");
        }

    }

    public static void main(String[] args) {
        //MenuPrincipal fenetrePrincipale = new MenuPrincipal();
        //fenetrePrincipale.setVisible(true);

        WelshPowell algowelsh = new WelshPowell(test.getGraph_vol());
        algowelsh.colorierNoeudsWelsh(2);
        countAndPrintConflicts(test.getGraph_vol());
        test.getGraph_vol().display();







    }
    // Méthode pour compter et afficher les conflits
    public static void countAndPrintConflicts(Graph graph) {
        int conflictCount = 0;

        for (Node node : graph) {
            List<Node> adjacentNodes = getAdjacentNodes(node);

            for (Node neighbor : adjacentNodes) {
                if (node.hasAttribute("ui.style") && neighbor.hasAttribute("ui.style")) {
                    String color1 = node.getAttribute("ui.style");
                    String color2 = neighbor.getAttribute("ui.style");

                    if (color1.equals(color2)) {
                        System.out.printf("Conflit trouvé entre noeud %s et noeud %s avec la couleur %s\n", node.getId(), neighbor.getId(), color1);
                        conflictCount++;
                    }
                }
            }
        }

        System.out.println("Nombre total de conflits : " + conflictCount);
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