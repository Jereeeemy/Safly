package graphvol;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.in;

public class CreateurGraph {
    Graph graph;

    public CreateurGraph(File fichier) throws IOException {
        this.graph = ChargerGraphDepuisFichier(fichier);
    }

    public CreateurGraph() throws IOException {
        this.graph = ChargerGraphDepuisFichier(new File("data/graph-test0.txt"));
    }

    public Graph getGraph() {
        return graph;
    }

    public File ChoixGraph() throws ExceptionNoGraphVol {
        Scanner scanner = new Scanner(in);
        File repertoire = new File("data");//Dossier contenant tous les fichiers nécessaires

        File[] files = repertoire.listFiles((dir, name) -> name.endsWith(".txt") && name.startsWith("graph-test"));// Liste contenant tous les fichiers de vols (qui commencent par "vol-test" et du type ".csv"

        if (files == null || files.length == 0) {// Vérifie qu'il y a bien au moins un fichier de vol dans le répertoire
            throw new ExceptionNoGraphVol();
        }

        System.out.println("Liste fichiers de vols disponibles :");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i) + ". " + files[i].getName());
        }


        int choix;
        while (true) {//Tant que l'utilisateur n'a pas entrée quelque chose de proposé, on boucle à l'infinie
            System.out.print("Veuillez saisir le numéro du fichier souhaité : ");
            try {// Test si l'utilisateur a bien entré un entier
                choix = scanner.nextInt();
                if (choix >= 0 && choix < files.length) {
                    break;
                }// Test si l'utilisateur a bien entré une des propositions
                else {
                    System.out.println("Saisie incorrecte, entrez un numéro valide");
                }
            } catch (InputMismatchException e) {
                System.out.println("Saisie incorrecte, entrez un entier");
                scanner.next();
            }
        }
        return files[choix];
    }

    public Graph ChargerGraphDepuisFichier(File fichier) throws IOException {
        Graph new_graph = new SingleGraph(fichier.getCanonicalPath());

        BufferedReader br = new BufferedReader(new FileReader(fichier));

        // Lire le nombre maximum de couleurs (kmax)
        String line = br.readLine();
        int kmax = Integer.parseInt(line);
        new_graph.setAttribute("kmax",kmax);

        // Lire le nombre de sommets
        line = br.readLine();
        int nb_sommets = Integer.parseInt(line);

        // Ajouter les sommets au graphe
        for (int i = 1; i <= nb_sommets; i++) {
            new_graph.addNode(String.valueOf(i));
        }

        // Lire les arêtes et les ajouter au graphe
        while ((line = br.readLine()) != null) {
            String[] noeuds = line.split(" ");
            String noeud1 = noeuds[0];
            String noeud2 = noeuds[1];
            String edgeId = noeud1 + "-" + noeud2;
            String edgeIdReverse = noeud2 + "-" + noeud1;
            if(new_graph.getEdge(edgeId) == null && new_graph.getEdge(edgeIdReverse) == null){
                new_graph.addEdge(edgeId, noeud1, noeud2);
            }
        }
        return new_graph;
    }

    public ArrayList<Graph> ChargerGraphsDepuisDossier(File dossier) throws IOException {
        ArrayList<Graph> graphs = new ArrayList<>();

        if (dossier.isDirectory()) {
            File[] files = dossier.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            Graph graph = ChargerGraphDepuisFichier(file);
                            graphs.add(graph);
                        } catch (IOException e) {
                            throw new IOException();
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Le chemin fourni n'est pas un dossier.");
        }

        return graphs;
    }

}