package tests;

import coloration.DsaturAlgorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de test pour l'algorithme DSATUR.
 */
public class TestDsatur {

    /**
     * Teste la méthode dsaturColoring.
     */
    @Test
    public void testDsaturColoring() {
        // Crée un nouveau graphe avec GraphStream
        Graph graph = new SingleGraph("TestGraph");

        // Ajoute des nœuds au graphe
        for (int i = 0; i < 11; i++) {
            graph.addNode(String.valueOf(i));
        }

        // Ajoute des arêtes entre les nœuds
        graph.addEdge("0-1", "0", "1");
        graph.addEdge("0-2", "0", "2");
        graph.addEdge("1-2", "1", "2");
        graph.addEdge("1-3", "1", "3");
        graph.addEdge("2-3", "2", "3");
        graph.addEdge("3-4", "3", "4");
        graph.addEdge("4-5", "4", "5");
        graph.addEdge("5-6", "5", "6");
        graph.addEdge("6-7", "6", "7");
        graph.addEdge("7-8", "7", "8");
        graph.addEdge("8-9", "8", "9");
        graph.addEdge("9-10", "9", "10");

        int Kmax = 5;
        // Appelle la méthode dsaturColoring pour colorier le graphe
        int[] colors = DsaturAlgorithm.dsaturColoring(graph, Kmax);

        // Vérifie que toutes les couleurs sont dans l'intervalle [1, Kmax]
        for (int color : colors) {
            assertTrue(color >= 1 && color <= Kmax, "La couleur doit être entre 1 et " + Kmax);
        }

        // Vérifie qu'il n'y a pas de conflits de couleurs
        int totalConflicts = DsaturAlgorithm.checkColorConflicts(graph, colors);
        assertEquals(0, totalConflicts, "Il ne doit pas y avoir de conflits de couleurs.");
    }

    /**
     * Teste la méthode findMinConflictColor.
     */
    @Test
    public void testFindMinConflictColor() {
        Graph graph = new SingleGraph("TestGraph");

        // Ajoute des nœuds et des arêtes au graphe
        for (int i = 0; i < 4; i++) {
            graph.addNode(String.valueOf(i));
        }
        graph.addEdge("0-1", "0", "1");
        graph.addEdge("0-2", "0", "2");
        graph.addEdge("1-3", "1", "3");
        graph.addEdge("2-3", "2", "3");

        int[] colors = new int[4];
        int Kmax = 3;

        // Colorie le nœud 0 avec la couleur 1
        colors[0] = 1;
        colors[1] = 2;
        colors[2] = 3;
        colors[3] = 4;


        // Teste la méthode findMinConflictColor
        int minConflictColor = DsaturAlgorithm.findMinConflictColor(graph, colors, 1, Kmax);
        assertEquals(2, minConflictColor, "La couleur avec le moins de conflits doit être 2.");
    }

    /**
     * Teste la méthode calculateSaturationDegree.
     */
    @Test
    public void testCalculateSaturationDegree() {
        Graph graph = new SingleGraph("TestGraph");

        // Ajoute des nœuds et des arêtes au graphe
        for (int i = 0; i < 4; i++) {
            graph.addNode(String.valueOf(i));
        }
        graph.addEdge("0-1", "0", "1");
        graph.addEdge("0-2", "0", "2");
        graph.addEdge("1-3", "1", "3");
        graph.addEdge("2-3", "2", "3");

        int[] colors = new int[4];
        colors[0] = 1;
        colors[1] = 2;
        colors[2] = 1;
        colors[3] = 2;

        // Teste la méthode calculateSaturationDegree
        int saturationDegree = DsaturAlgorithm.calculateSaturationDegree(graph, colors, 2);
        assertEquals(2, saturationDegree, "Le degré de saturation doit être 2.");
    }

    /**
     * Teste la méthode checkColorConflicts.
     */
    @Test
    public void testCheckColorConflicts() {
        Graph graph = new SingleGraph("TestGraph");

        // Ajoute des nœuds et des arêtes au graphe
        for (int i = 0; i < 4; i++) {
            graph.addNode(String.valueOf(i));
        }
        graph.addEdge("0-1", "0", "1");
        graph.addEdge("0-2", "0", "2");
        graph.addEdge("1-3", "1", "3");
        graph.addEdge("2-3", "2", "3");

        int[] colors = {1, 1, 2, 3};

        // Teste la méthode checkColorConflicts
        int totalConflicts = DsaturAlgorithm.checkColorConflicts(graph, colors);
        assertEquals(1, totalConflicts, "Le nombre total de conflits doit être 1.");
    }

    /**
     * Teste la méthode modifyNodeColors.
     */
    @Test
    public void testModifyNodeColors() {
        Graph graph = new SingleGraph("TestGraph");

        // Ajoute des nœuds au graphe
        for (int i = 0; i < 4; i++) {
            graph.addNode(String.valueOf(i));
        }

        int[] colors = {1, 2, 3, 4};

        // Teste la méthode modifyNodeColors
        DsaturAlgorithm.modifyNodeColors(graph, colors);

        for (Node node : graph) {
            String color = node.getAttribute("ui.style").toString();
            assertNotNull(color, "La couleur ne doit pas être nulle.");
            assertTrue(color.contains("fill-color: rgb("), "La couleur doit être définie correctement.");
        }
    }

    /**
     * Teste la méthode colorAndDisplayGraph.
     */
    @Test
    public void testColorAndDisplayGraph() {
        Graph graph = new SingleGraph("TestGraph");

        // Ajoute des nœuds au graphe
        for (int i = 0; i < 4; i++) {
            graph.addNode(String.valueOf(i));
        }
        graph.addEdge("0-1", "0", "1");
        graph.addEdge("0-2", "0", "2");
        graph.addEdge("1-3", "1", "3");
        graph.addEdge("2-3", "2", "3");

        int Kmax = 3;
        String nomFichier = "test_file";
        String outputDirectory = "test_output";

        // Crée le répertoire de sortie s'il n'existe pas
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Appelle la méthode colorAndDisplayGraph pour colorier et afficher le graphe
        File[] files = DsaturAlgorithm.colorAndDisplayGraph(graph, Kmax, nomFichier, outputDirectory);

        assertNotNull(files, "Les fichiers ne doivent pas être nuls.");
        assertEquals(2, files.length, "Il doit y avoir deux fichiers générés.");
        // Vérifie que les fichiers existent
        boolean csvExists = files[0].exists();
        boolean txtExists = files[1].exists();
        System.out.println("CSV file exists: " + csvExists);
        System.out.println("TXT file exists: " + txtExists);

        assertTrue(csvExists, "Le fichier CSV doit exister.");
        assertTrue(txtExists, "Le fichier TXT doit exister.");
    }
}