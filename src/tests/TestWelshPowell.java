package tests;

import coloration.WelshPowell;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class de test pour l'algorithme de coloration Welsh et Powell
 */
public class TestWelshPowell {

    private Graph graph;
    private WelshPowell welshPowell;

    /**
     * Initialise un graph pour les tests
     */
    @BeforeEach
    public void setUp() {
        // Création d'un graphe pour les tests
        graph = new SingleGraph("TestGraph");

        // Ajout des noeuds
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");

        // Ajout des arêtes
        graph.addEdge("AB", "A", "B");
        graph.addEdge("AC", "A", "C");
        graph.addEdge("BD", "B", "D");
        graph.addEdge("CD", "C", "D");

        // Initialisation de WelshPowell avec le graphe de test
        welshPowell = new WelshPowell(graph);
    }

    /**
     * Test la coloration des nœuds
     */
    @Test
    public void testColorierNoeudsWelsh() {
        // Appliquer la coloration
        int kmax = 3;
        int[] coloration = welshPowell.colorierNoeudsWelsh(kmax);

        // Vérification que chaque noeud a une couleur attribuée
        for (int color : coloration) {
            assertTrue(color > 0, "Chaque noeud doit avoir une couleur attribuée.");
        }

        // Vérification que les noeuds adjacents n'ont pas la même couleur
        for (int i = 0; i < graph.getNodeCount(); i++) {
            for (int j = i + 1; j < graph.getNodeCount(); j++) {
                if (graph.getNode(i).hasEdgeBetween((Node) graph.getNode(j))) {
                    assertNotEquals(coloration[i], coloration[j], "Les noeuds adjacents ne doivent pas avoir la même couleur.");
                }
            }
        }
    }

    /**
     * Test vérifiant qu'on ne dépasse pas kmax.
     */
    @Test
    public void testKmax() {
        // Appliquer la coloration
        int kmax = 3;
        welshPowell.colorierNoeudsWelsh(kmax);

        // Compter les couleurs utilisées
        int nombreDeCouleurs = welshPowell.CompteCouleurs();

        // Vérification du nombre de couleurs
        assertTrue(nombreDeCouleurs <= kmax, "Le nombre de couleurs utilisées doit être inférieur ou égal à kmax.");
    }
}
