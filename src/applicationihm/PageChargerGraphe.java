package applicationihm;

import coloration.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import graphvol.CreateurGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;
import org.graphstream.graph.implementations.*;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static coloration.DsaturAlgorithm.colorAndDisplayGraph;

public class PageChargerGraphe {
    private final JPanel panelCharger;

    private RoundedButton boutonAccueil;
    private Color couleurPrincipale = Color.decode("#D9D9D9");
    private Color couleurSecondaire = Color.decode("#2C5789");
    private Color couleurTertiaire = Color.decode("#122A47");
    private CreateurGraph graph;
    public File selectedFile;
    public File fileToDownload;
    int kmax;
    int noeuds = 0;
    int aretes = 0;
    double degre = 0;
    int composantes = 0;
    int diametre = 0;
    private JTable tableauInfoGraphe;
    private DefaultTableModel model;
    private RoundedButton boutonColoration;
    private RoundedButton boutonTelecharger;
    private JRadioButton WelshBouton;
    private JRadioButton DsaturBouton;
    private RoundedButton boutonAfficherGraphe;
    public PageChargerGraphe(MenuPrincipal menuPrincipal) {

        panelCharger = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Récupérer l'image de fond actuelle à chaque fois que le panneau est redessiné
                Image background = menuPrincipal.getBackgroundImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        JSeparator sepa1 = new JSeparator(SwingConstants.VERTICAL);
        sepa1.setBackground(Color.GRAY);

        JSeparator sepa2 = new JSeparator(SwingConstants.VERTICAL);
        sepa2.setBackground(Color.GRAY);


        panelCharger.setLayout(new BorderLayout(60,45));

        JPanel panelChargerHaut = new JPanel();
        panelChargerHaut.setOpaque(false);
        RoundedPanel panelChargerCentre = new RoundedPanel(40);

        panelChargerCentre.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        panelChargerCentre.setBackground(couleurPrincipale);
        panelChargerCentre.setLayout(new BorderLayout(0,0));
        panelChargerCentre.setOpaque(false);

        JPanel panelChargerBas = new JPanel();
        panelChargerBas.setOpaque(false);

        decoBoutonAcceuil();
        boutonAccueil.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuPrincipal.setContentPane(menuPrincipal.getPanelAccueil());
                menuPrincipal.revalidate();
                menuPrincipal.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boutonAccueil.setBackground(Color.GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boutonAccueil.setBackground(couleurPrincipale);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boutonAccueil.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boutonAccueil.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        panelChargerHaut.add(boutonAccueil, BorderLayout.CENTER);
        panelChargerHaut.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel centrePanel1 = new JPanel();
        centrePanel1.setLayout(new BoxLayout(centrePanel1, BoxLayout.PAGE_AXIS));
        centrePanel1.setBackground(couleurPrincipale);

        JLabel labelDeposeFichier = new JLabel("<html><div style='text-align: left'> Pour charger un graphe,<br> veuillez d'abord fournir <br>les fichiers nécessaires</div></html>");
        labelDeposeFichier.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        labelDeposeFichier.setFont(new Font("Lucida Sans",Font.PLAIN,20));

        centrePanel1.add(labelDeposeFichier);
        final JLabel labelNomFichier = new JLabel();

        RoundedButton boutonFichierGraphe = new RoundedButton("Déposer un fichier de graphe", 70);

        JSpinner spinnerKMax = getSpinnerKMax();
        spinnerKMax.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                kmax = (int) spinnerKMax.getValue();
            }
        });


        boutonFichierGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelCharger);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    try {
                        // Essayer de créer le graphe avec le fichier sélectionné
                        graph = new CreateurGraph(selectedFile);
                        kmax = graph.getGraph().getAttribute("kmax");
                        spinnerKMax.setValue(kmax);

                        // Mettre à jour l'affichage si la création du graphe réussit
                        labelNomFichier.setText(fileName);
                        labelNomFichier.setForeground(Color.BLUE);
                        labelNomFichier.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                        boutonFichierGraphe.setForeground(Color.decode("#77E59B"));
                        panelCharger.revalidate();
                        panelCharger.repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panelCharger, "Erreur lors de la lecture du fichier ! Vérifiez que le fichier fourni est correct.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boutonFichierGraphe.setBackground(Color.DARK_GRAY);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boutonFichierGraphe.setBackground(Color.decode("#696767"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boutonFichierGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boutonFichierGraphe.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        boutonFichierGraphe.setFocusable(false);
        boutonFichierGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonFichierGraphe.setForeground(Color.WHITE);
        boutonFichierGraphe.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        boutonFichierGraphe.setBackground(Color.decode("#696767"));

        JLabel labelTelechargerFichier = new JLabel("<html><div style='text-align: left'> Cliquez ici si vous souhaitez <br>télécharger le fichier qui <br>contient les informations <br>du graphe colorié </div></html>");
        labelTelechargerFichier.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        labelTelechargerFichier.setFont(new Font("Lucida Sans",Font.PLAIN,20));

        decoBoutonTelecharger();

        centrePanel1.add(boutonFichierGraphe);
        centrePanel1.add(labelNomFichier);
        centrePanel1.add(labelTelechargerFichier);
        centrePanel1.add(boutonTelecharger);


        JPanel centrePanel2 = new JPanel();
        centrePanel2.setLayout(new BoxLayout(centrePanel2, BoxLayout.PAGE_AXIS));
        centrePanel2.setBackground(couleurPrincipale);

        JLabel labelChoixParam = new JLabel("<html><div style='text-align: left'> Après avoir chargé votre graphe,<br> vous pouvez personnaliser la <br>coloration du graphe : </div></html>");
        labelChoixParam.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        labelChoixParam.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        centrePanel2.add(labelChoixParam);

        RoundedPanel miniPanelCentral = new RoundedPanel(25);
        miniPanelCentral.setBackground(couleurPrincipale);
        miniPanelCentral.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        miniPanelCentral.setLayout(new BoxLayout(miniPanelCentral, BoxLayout.PAGE_AXIS));
        JLabel labelChoixAlgo = new JLabel("Choix de l'Algo de coloration :");
        labelChoixAlgo.setForeground(Color.WHITE);
        labelChoixAlgo.setFont(new Font("Lucida Sans",Font.PLAIN,20));

        ButtonGroup groupAlgos = new ButtonGroup();
        decoBoutonWelsh();
        decoBoutonDsatur();
        groupAlgos.add(WelshBouton);
        groupAlgos.add(DsaturBouton);

        JLabel labelKmax = new JLabel("Choix de K-max :");
        labelKmax.setForeground(Color.WHITE);
        labelKmax.setFont(new Font("Lucida Sans",Font.PLAIN,20));

        miniPanelCentral.add(labelChoixAlgo);
        miniPanelCentral.add(WelshBouton);
        miniPanelCentral.add(DsaturBouton);
        miniPanelCentral.add(labelKmax);
        miniPanelCentral.add(spinnerKMax);
        miniPanelCentral.setBackground(Color.decode("#696767"));

        centrePanel2.add(miniPanelCentral);

        decoBoutonColoration();


        centrePanel2.add(boutonColoration);

        JPanel centrePanel3 = new JPanel();
        centrePanel3.setBackground(couleurPrincipale);

        initializeTable();
        decoTab();


        centrePanel3.add(tableauInfoGraphe);
        centrePanel3.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        JPanel centrePanelContainer = new JPanel();
        centrePanelContainer.setLayout(new BoxLayout(centrePanelContainer, BoxLayout.X_AXIS));
        centrePanelContainer.setBackground(couleurPrincipale);

        centrePanel2.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

        // Add centrePanel1, sepa1, centrePanel2, sepa2, centrePanel3 to the container
        centrePanelContainer.add(centrePanel1);
        centrePanelContainer.add(sepa1);
        centrePanelContainer.add(centrePanel2);
        centrePanelContainer.add(sepa2);
        centrePanelContainer.add(centrePanel3);

        // Add the container to the center of panelChargerCentre
        panelChargerCentre.add(centrePanelContainer, BorderLayout.CENTER);


        decoBoutonGraphe();

        panelChargerBas.add(boutonAfficherGraphe);
        panelChargerBas.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel panelgauche = new JPanel();
        panelgauche.setOpaque(false);
        JPanel paneldroite = new JPanel();
        paneldroite.setOpaque(false);
        panelCharger.add(panelChargerHaut,BorderLayout.NORTH);
        panelCharger.add(panelgauche,BorderLayout.WEST);
        panelCharger.add(panelChargerCentre,BorderLayout.CENTER);
        panelCharger.add(paneldroite, BorderLayout.EAST);
        panelCharger.add(panelChargerBas,BorderLayout.SOUTH);
    }




    private static JSpinner getSpinnerKMax() {
        JSpinner spinnerKMax = new JSpinner();
        spinnerKMax.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        spinnerKMax.setModel(new SpinnerNumberModel(10, 1, 200, 1));
        spinnerKMax.setFocusable(false);
        Dimension preferredSize = new Dimension(75, spinnerKMax.getPreferredSize().height + 5);
        spinnerKMax.setPreferredSize(preferredSize);
        spinnerKMax.setMaximumSize(preferredSize);
        JComponent editor = spinnerKMax.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setEditable(false);
            textField.setBackground(Color.LIGHT_GRAY);
        }
        spinnerKMax.setBackground(Color.decode("#696767"));
        return spinnerKMax;
    }

    private void saveFile() {
        if (fileToDownload != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le fichier");

            // Configurer le nom de fichier par défaut
            fileChooser.setSelectedFile(new File("example.txt"));

            int userSelection = fileChooser.showSaveDialog(panelCharger);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    // Copier le contenu du fichier temporaire vers le fichier choisi par l'utilisateur
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                        writer.write("Ceci est un exemple de fichier texte avec des informations.");
                    }
                    JOptionPane.showMessageDialog(panelCharger, "Fichier enregistré avec succès.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panelCharger, "Erreur lors de l'enregistrement du fichier : " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(panelCharger, "Le fichier à télécharger n'a pas été créé.");
        }
    }

    private void initializeTable() {
        model = new DefaultTableModel(
                new Object[][]{
                        {"Informations : ", null},
                        {null, null},
                        {"Noeuds :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Arêtes :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Degré Moyen :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Composantes  :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Diamètre", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                },
                new String[]{"Informations Graphe :", null}
        );

        tableauInfoGraphe = new JTable(model);

        JButton updateButton = new JButton("Mettre à jour les données");

    }

    // Méthode pour mettre à jour les données du tableau
    public void updateTableData(int noeuds, int aretes, double degre, int composantes, int diametre) {
        model.setValueAt(" " + " " + noeuds, 2, 1);
        model.setValueAt(" " + " " + aretes, 4, 1);
        model.setValueAt(" " + " " + degre, 6, 1);
        model.setValueAt(" " + " " + composantes, 8, 1);
        model.setValueAt(" " + " " + diametre, 10, 1);
    }

    private void decoBoutonAcceuil(){
        boutonAccueil = new RoundedButton("Accueil", 25);
        boutonAccueil.setFocusable(false);
        boutonAccueil.setFont(new Font("Lucida Sans",Font.PLAIN,15));
        boutonAccueil.setBackground(couleurPrincipale);
    }
    private void decoTab(){
        tableauInfoGraphe.setGridColor(Color.DARK_GRAY);
        tableauInfoGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,18));
        tableauInfoGraphe.setForeground(Color.WHITE);
        tableauInfoGraphe.setBackground(Color.decode("#696767"));
        tableauInfoGraphe.setShowVerticalLines(true);
        tableauInfoGraphe.setShowHorizontalLines(false);
        tableauInfoGraphe.setPreferredSize(new Dimension(260, 300));
        tableauInfoGraphe.setRowHeight(tableauInfoGraphe.getRowHeight() + 10);
        tableauInfoGraphe.setRowMargin(5);
        tableauInfoGraphe.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableauInfoGraphe.setEnabled(false);
        TableColumn colonneValeur = tableauInfoGraphe.getColumnModel().getColumn(1);
        colonneValeur.setPreferredWidth(125);
    }

    private void decoBoutonColoration(){
        boutonColoration = new RoundedButton("Effectuer la coloration", 50);
        boutonColoration.setFocusable(false);
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonColoration.setPreferredSize(new Dimension(220, 50));
        boutonColoration.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonColoration.setBackground(couleurTertiaire);
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelCharger, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else if (DsaturBouton.isSelected()){
                    colorAndDisplayGraph(graph.getGraph(),kmax);
                    statGraphe();
                    updateTableData(noeuds, aretes, degre, composantes,diametre);

                } else if (WelshBouton.isSelected()) {

                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boutonColoration.setBackground(couleurSecondaire);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boutonColoration.setBackground(couleurTertiaire);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boutonColoration.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boutonColoration.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    private void decoBoutonWelsh() {
        WelshBouton = new JRadioButton("Welsh & Powell");
        WelshBouton.setForeground(Color.WHITE);
        WelshBouton.setFocusable(false);
        WelshBouton.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        WelshBouton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        WelshBouton.setBackground(Color.decode("#696767"));
        WelshBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        WelshBouton.setSelected(true);
    }

    private void decoBoutonDsatur() {
        DsaturBouton = new JRadioButton("D-Satur");
        DsaturBouton.setForeground(Color.WHITE);
        DsaturBouton.setFocusable(false);
        DsaturBouton.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        DsaturBouton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        DsaturBouton.setBackground(Color.decode("#696767"));
        DsaturBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void decoBoutonGraphe(){
        boutonAfficherGraphe = new RoundedButton("Afficher le Graphe", 90);
        boutonAfficherGraphe.setFocusable(false);
        boutonAfficherGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,25));
        boutonAfficherGraphe.setForeground(Color.WHITE);
        boutonAfficherGraphe.setPreferredSize(new Dimension(320, 80));
        boutonAfficherGraphe.setBackground(couleurTertiaire);
        boutonAfficherGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelCharger, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Viewer vue = graph.getGraph().display();
                vue.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boutonAfficherGraphe.setBackground(couleurSecondaire);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boutonAfficherGraphe.setBackground(couleurTertiaire);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                boutonAfficherGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boutonAfficherGraphe.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void decoBoutonTelecharger(){
        boutonTelecharger = new RoundedButton("Télécharger", 50);
        boutonTelecharger.setFocusable(false);
        boutonTelecharger.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        boutonTelecharger.setBackground(Color.decode("#696767"));
        boutonTelecharger.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonTelecharger.setForeground(Color.WHITE);
        boutonTelecharger.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonTelecharger.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileToDownload = new File("hehehehhe");
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelCharger, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else if (fileToDownload == null){
                    JOptionPane.showMessageDialog(panelCharger, "Attention ! Vous n'avez pas encore effectué la coloration.", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    saveFile();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boutonTelecharger.setBackground(Color.DARK_GRAY);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boutonTelecharger.setBackground(Color.decode("#696767"));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                boutonTelecharger.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boutonTelecharger.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }



    // Fonction pour explorer une composante connexe
    private static void exploreComponent(Node node, Set<Node> visited) {
        List<Node> queue = new LinkedList<>();
        queue.add(node);
        visited.add(node);

        while (!queue.isEmpty()) {
            Node current = ((LinkedList<Node>) queue).poll();
            for (Edge edge : current.getEachEdge()) {
                Node neighbor = edge.getOpposite(current);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }

    // Fonction pour calculer le diamètre du graphe
    private static int calculateDiameter(Graph graph) {
        int diameter = 0;
        for (Node node : graph) {
            int maxDistance = bfs(node);
            diameter = Math.max(diameter, maxDistance);
        }
        return diameter;
    }

    // Fonction pour effectuer une recherche en largeur (BFS) et retourner la distance maximale
    private static int bfs(Node start) {
        Map<Node, Integer> distances = new HashMap<>();
        List<Node> queue = new LinkedList<>();
        queue.add(start);
        distances.put(start, 0);

        int maxDistance = 0;
        while (!queue.isEmpty()) {
            Node current = ((LinkedList<Node>) queue).poll();
            int currentDistance = distances.get(current);

            for (Edge edge : current.getEachEdge()) {
                Node neighbor = edge.getOpposite(current);
                if (!distances.containsKey(neighbor)) {
                    int newDistance = currentDistance + 1;
                    distances.put(neighbor, newDistance);
                    maxDistance = Math.max(maxDistance, newDistance);
                    queue.add(neighbor);
                }
            }
        }
        return maxDistance;
    }

    private void statGraphe(){
        noeuds = graph.getGraph().getNodeCount();
        aretes = graph.getGraph().getEdgeCount();


        // Calcul du degré moyen
        double totalDegree = 0;
        for (Node node : graph.getGraph()) {
            totalDegree += node.getDegree();
        }
        degre = totalDegree / noeuds;

        // Calcul des composantes connexes
        Set<Node> visited = new HashSet<>();
        composantes = 0;
        for (Node node : graph.getGraph()) {
            if (!visited.contains(node)) {
                composantes++;
                exploreComponent(node, visited);
            }
        }
        // Calcul du diamètre du graphe
        diametre = calculateDiameter(graph.getGraph());
    }

    public JPanel getPanel() {
        return panelCharger;
    }
}