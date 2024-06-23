package graphvol;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static coloration.Couleur.getAdjacentNodes;
import static java.lang.System.in;

public class CreateurGraph {
    private static int numAfterNameFichierTxT = 0;
    Graph graph;

    public CreateurGraph(File fichier) throws IOException, ExceptionFormatIncorrect, ExceptionLigneIncorrect {
        this.graph = ChargerGraphDepuisFichier(fichier);
    }

    public CreateurGraph() throws IOException, ExceptionFormatIncorrect, ExceptionLigneIncorrect {
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

    public Graph ChargerGraphDepuisFichier(File fichier) throws IOException, ExceptionFormatIncorrect, ExceptionLigneIncorrect {
        Graph new_graph = new SingleGraph(fichier.getCanonicalPath());

        BufferedReader br = new BufferedReader(new FileReader(fichier));

        // Lire le nombre maximum de couleurs (kmax)
        String line = br.readLine();
        if (line == null || !line.matches("\\d+")) {
            throw new ExceptionFormatIncorrect();
        }
        int kmax = Integer.parseInt(line);
        new_graph.setAttribute("kmax", kmax);

        // Lire le nombre de sommets
        line = br.readLine();
        if (line == null || !line.matches("\\d+")) {
            throw new ExceptionFormatIncorrect();
        }
        int nb_sommets = Integer.parseInt(line);

        // Ajouter les sommets au graphe
        for (int i = 1; i <= nb_sommets; i++) {
            new_graph.addNode(String.valueOf(i));
        }

        // Lire les arêtes et les ajouter au graphe
        while ((line = br.readLine()) != null) {
            if (!line.matches("\\d+ \\d+")) {
                // Lancer une exception si la ligne n'est pas valide
                throw new ExceptionLigneIncorrect(line);
            }
            String[] noeuds = line.split(" ");
            String noeud1 = noeuds[0];
            String noeud2 = noeuds[1];

            // Vérifier que les noeuds sont dans la plage valide
            int noeud1Int = Integer.parseInt(noeud1);
            int noeud2Int = Integer.parseInt(noeud2);
            if (noeud1Int < 1 || noeud1Int > nb_sommets || noeud2Int < 1 || noeud2Int > nb_sommets) {
                // Lancer une exception si les noeuds ne sont pas valides
                throw new ExceptionLigneIncorrect(line);
            }

            // Ajouter l'arête si elle n'existe pas déjà
            String edgeId = noeud1 + "-" + noeud2;
            String edgeIdReverse = noeud2 + "-" + noeud1;
            if (new_graph.getEdge(edgeId) == null && new_graph.getEdge(edgeIdReverse) == null) {
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
                        } catch (ExceptionFormatIncorrect | ExceptionLigneIncorrect e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Le chemin fourni n'est pas un dossier.");
        }

        return graphs;
    }


    /**
     * Écrit les sommets et leurs couleurs dans un fichier texte.
     * @param graph Le graphe.
     * @param colors Tableau des couleurs des nœuds.
     * @param filename Le nom du fichier texte de sortie.
     */
    public static void writeColorsToTxtFile(Graph graph, int[] colors, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Sommet; Couleur\n");
            for (Node node : graph) {
                int nodeId = node.getIndex();
                writer.write((nodeId + 1) + "; " + colors[nodeId] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * Écrit les résultats dans un fichier CSV.
     * @param nomFichier Le nom du fichier CSV.
     * @param totalConflicts Le nombre total de conflits de couleurs.
     * @param outputDirectory Le répertoire de sortie pour le fichier.
     * @return Le fichier CSV généré.
     */
    public static File writeCSVFile(String nomFichier, int totalConflicts, String outputDirectory) {
        // Vérifie si le répertoire de sortie existe, sinon le crée
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Construit le chemin complet et le nom du fichier CSV à créer
        String csvFileName = outputDirectory + File.separator + "coloration-groupe3.7.csv";

        // Crée un objet File pour le fichier CSV
        File csvFile = new File(csvFileName);

        // Compte le nombre de lignes dans le fichier avant d'ajouter le nouveau contenu
        int lineCount = countLines(csvFile);

        // Si le fichier contient moins de 20 lignes, ajoute de nouvelles données
        if (lineCount < 20) {
            // Prépare le contenu à écrire dans le fichier CSV (nom du fichier et nombre total de conflits)
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("graph-eval"+nomFichier.substring(10)).append("; ").append(totalConflicts).append("\r\n");

            // Utilisation de BufferedWriter pour écrire dans le fichier CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
                writer.write(csvContent.toString()); // Écrit le contenu dans le fichier CSV
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Prépare le contenu à écrire dans le fichier CSV (nom du fichier et nombre total de conflits)
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("graph-eval"+nomFichier.substring(10)).append("; ").append(totalConflicts).append("\r\n");

            // Utilisation de BufferedWriter pour écrire dans le fichier CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
                writer.write(csvContent.toString()); // Écrit le contenu dans le fichier CSV
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return csvFile; // Retourne le fichier CSV généré
    }



    // Méthode pour compter le nombre de lignes dans un fichier
    private static int countLines(File file) {
        int lines = 0;
        if (!file.exists()) {
            return lines; // Si le fichier n'existe pas, retourne 0 lignes
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
        return lines;
    }

    /**
     * Écrit les résultats dans un fichier texte.
     * @param graph Le graphe.
     * @param result Tableau des couleurs des nœuds.
     * @param outputDirectory Le répertoire de sortie pour le fichier.
     * @return Le fichier texte généré.
     */
    public static File writeTxtFile(Graph graph, int[] result, String outputDirectory) {
        // Vérifie si le répertoire de sortie existe, sinon le crée
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }



        // Construit le nom complet du fichier texte à créer avec un compteur pour garantir l'unicité des noms
        if (numAfterNameFichierTxT==20){
            numAfterNameFichierTxT=0;
        }
        String txtFileName = outputDirectory + File.separator + "colo-eval" + numAfterNameFichierTxT + ".txt";
        numAfterNameFichierTxT++;

        // Prépare le contenu à écrire dans le fichier texte (identifiant du nœud et sa couleur)
        StringBuilder txtContent = new StringBuilder();
        for (Node node : graph) {
            txtContent.append(node.getIndex() + 1).append(" ; ").append(result[node.getIndex()]).append("\r\n");
        }

        // Crée un objet File pour le fichier texte
        File txtFile = new File(txtFileName);

        // Utilisation de BufferedWriter pour écrire dans le fichier texte
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
            writer.write(txtContent.toString()); // Écrit le contenu dans le fichier texte
        } catch (IOException e) {
            e.printStackTrace();
        }

        return txtFile; // Retourne le fichier texte généré
    }


    /**
     * Zippe le contenu d'un répertoire dans un fichier ZIP.
     * @param directoryPath Chemin du répertoire à zipper.
     * @return Le fichier ZIP créé.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    public static File zipOutputDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath); // Crée un objet File à partir du chemin du répertoire
        File zipFile = new File(directory.getParent(), directory.getName() + ".zip"); // Crée un objet File pour le fichier ZIP de sortie

        // Utilisation de try-with-resources pour gérer automatiquement la fermeture des flux
        try (FileOutputStream fos = new FileOutputStream(zipFile); // Crée un flux de sortie vers le fichier ZIP
             ZipOutputStream zos = new ZipOutputStream(fos)) { // Crée un flux ZIP sur le flux de sortie

            zipDirectory(directory, directory, zos); // Appelle la méthode pour ajouter les fichiers du répertoire au fichier ZIP
        }

        return zipFile; // Retourne le fichier ZIP créé
    }


    /**
     * Récursivement ajoute des fichiers d'un répertoire à un fichier ZIP.
     * @param root Répertoire racine initial.
     * @param directory Répertoire actuel.
     * @param zos Flux de sortie ZIP.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    private static void zipDirectory(File root, File directory, ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles(); // Récupère la liste des fichiers et répertoires dans le répertoire actuel
        byte[] buffer = new byte[1024]; // Tampon pour lire les données des fichiers

        for (File file : files) { // Parcourt tous les fichiers et répertoires du répertoire actuel
            if (file.isDirectory()) { // Vérifie si c'est un répertoire
                zipDirectory(root, file, zos); // Appelle récursivement la méthode pour ajouter les fichiers du sous-répertoire au fichier ZIP
            } else {
                // Construit le chemin relatif du fichier par rapport au répertoire racine
                String zipEntryName = root.toURI().relativize(file.toURI()).getPath();
                ZipEntry zipEntry = new ZipEntry(zipEntryName); // Crée une nouvelle entrée ZIP pour le fichier

                zos.putNextEntry(zipEntry); // Ajoute l'entrée ZIP au flux ZIP

                try (FileInputStream fis = new FileInputStream(file)) { // Crée un flux d'entrée pour lire le contenu du fichier
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length); // Écrit les données du fichier dans le flux ZIP
                    }
                }

                zos.closeEntry(); // Ferme l'entrée ZIP pour ce fichier
            }
        }
    }

    /**
     * Cette méthode est similaire à writeTxtFile, elle est déstiner à la page charger graphe
     * qui va permettre de donner les informations d'un graphe à la fois.
     *
     * @param graph    Le graphe.
     * @param colors   Tableau des couleurs des nœuds.
     * @param filename Le nom du fichier texte.
     * @return
     */
    public static File writeColorsForOneGraphToTxtFile(Graph graph, int[] colors, String filename) {

        // Prépare le contenu à écrire dans le fichier texte (identifiant du nœud et sa couleur)
        StringBuilder txtContent = new StringBuilder();
        for (Node node : graph) {
            txtContent.append(node.getIndex() + 1).append(" ; ").append(colors[node.getIndex()]).append("\r\n");
        }

        // Crée un objet File pour le fichier texte
        File txtFile = new File(filename);

        // Utilisation de BufferedWriter pour écrire dans le fichier texte
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
            writer.write(txtContent.toString()); // Écrit le contenu dans le fichier texte
        } catch (IOException e) {
            e.printStackTrace();
        }

        return txtFile;
    }

    /**
     * Génère un fichier texte contenant les résultats de coloration d'un graphe donné.
     *
     * @param graph Le graphe pour lequel les résultats de coloration sont générés.
     * @param filename Le nom de fichier à utiliser pour enregistrer les résultats.
     * @param colorResult Le tableau contenant les résultats de coloration pour chaque sommet du graphe.
     * @return Le fichier texte généré contenant les résultats de coloration.
     * @throws IOException Si une erreur d'entrée/sortie survient lors de l'écriture dans le fichier.
     */
    public static File InfoscolorOneGraph(Graph graph, String filename, int colorResult[]) throws IOException {
        // Écrit les résultats dans un fichier texte
        File txtFile = writeColorsForOneGraphToTxtFile(graph, colorResult, filename);

        // Retourne les fichiers générés
        return txtFile;
    }

    // Méthode pour compter et afficher les conflits
    public int CompterConflits(Graph graph) {
        int conflictCount = 0;

        for (Node node : graph) {
            List<Node> adjacentNodes = getAdjacentNodes(node);

            for (Node neighbor : adjacentNodes) {
                if (node.hasAttribute("ui.style") && neighbor.hasAttribute("ui.style")) {
                    String color1 = node.getAttribute("ui.style");
                    String color2 = neighbor.getAttribute("ui.style");
                    if (color1.equals(color2)) {
                        conflictCount++;
                    }
                }
            }
        }
        conflictCount = conflictCount / 2;
        return conflictCount;
    }

}