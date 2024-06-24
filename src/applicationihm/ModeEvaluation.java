package applicationihm;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import coloration.DsaturAlgorithm;
import coloration.WelshPowell;
import graphvol.CreateurGraph;
import graphvol.ExceptionFormatIncorrect;
import graphvol.ExceptionLigneIncorrect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import static graphvol.CreateurGraph.*;

/**
 * Classe pour le mode d'évaluation de l'application.
 */
public class ModeEvaluation {

    /**
     * Panneau principal pour l'évaluation.
     */
    private final JPanel panelEvaluation;

    /**
     * Objet CreateurGraph pour gérer les graphes.
     */
    private CreateurGraph graph;

    /**
     * Fichier sélectionné par l'utilisateur.
     */
    public File selectedFile;

    /**
     * Fichier à télécharger.
     */
    public File fileToDownload;

    /**
     * Valeur maximale de k pour la coloration du graphe.
     */
    int kmax;

    /**
     * Couleur principale de l'interface.
     */
    Color couleurPrincipale = Color.decode("#D9D9D9");

    /**
     * Couleur secondaire de l'interface.
     */
    Color couleurSecondaire = Color.decode("#2C5789");

    /**
     * Couleur tertiaire de l'interface.
     */
    Color couleurTertaire = Color.decode("#122A47");

    /**
     * Constructeur de la classe ModeEvaluation.
     *
     * @param menuPrincipal Le menu principal de l'application.
     */
    public ModeEvaluation(MenuPrincipal menuPrincipal) {

        // Initialisation du panneau principal avec un fond personnalisé
        panelEvaluation = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = menuPrincipal.getBackgroundImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panelEvaluation.setLayout(new BorderLayout(200, 75));

        // Mise en place des panels principaux de la page
        JPanel panelChargerHaut = new JPanel(); // Panneau supérieur pour les boutons
        panelChargerHaut.setOpaque(false);
        RoundedPanel panelChargerCentre = new RoundedPanel(40); // Panneau central avec coins arrondis

        panelChargerCentre.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        panelChargerCentre.setBackground(couleurPrincipale);
        panelChargerCentre.setOpaque(false);
        JPanel panelChargerBas = new JPanel(); // Panneau inférieur pour le nom de l'application
        panelChargerBas.setOpaque(false);
        JLabel nomAppli = new JLabel("SAFLY"); // Nom de l'application
        nomAppli.setFont(new Font("Lucida Sans", Font.BOLD, 60));
        panelChargerBas.add(nomAppli);

        // Mise en place du bouton d'accueil
        RoundedButton boutonAccueil = new RoundedButton("Accueil", 25); // Bouton d'accueil
        boutonAccueil.setFocusable(false);
        boutonAccueil.setFont(new Font("Lucida Sans", Font.PLAIN, 15));
        boutonAccueil.setBackground(couleurPrincipale);

        // Ajout d'un écouteur pour le bouton d'accueil
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

        panelChargerCentre.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Ajouter le label avec les consignes
        JLabel labelDeposeFichier = new JLabel("<html><div style='text-align: center'>Bienvenue dans le mode Evaluation,<br> Pour charger un graphe, veuillez d'abord fournir les fichiers nécessaires, puis ensuite vous pourrez télécharger les resultats de la coloration de ce graphe.</div></html>");
        labelDeposeFichier.setFont(new Font("Lucida Sans", Font.ITALIC, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4; // Étendre sur les deux colonnes
        panelChargerCentre.add(labelDeposeFichier, gbc);

        final JLabel labelNomFichier = new JLabel();
        RoundedButton boutonFichierGraphe = new RoundedButton("Déposer un fichier de graphe", 70);
        ArrayList<File> fichiers_choisi_repertoire = new ArrayList<>();
        boutonFichierGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Sélection de répertoire
                int returnValue = fileChooser.showOpenDialog(panelEvaluation);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();

                    File[] files = selectedDirectory.listFiles();
                    for (File fichier : files) {
                        if (fichier.getName().endsWith(".txt") && fichier.getName().startsWith("graph")) {
                            fichiers_choisi_repertoire.add(fichier);
                        }
                    }
                    if (!fichiers_choisi_repertoire.isEmpty()) {
                        boutonFichierGraphe.removeMouseListener(this);
                        boutonFichierGraphe.setBackground(Color.DARK_GRAY);
                        boutonFichierGraphe.setForeground(Color.decode("#77E59B"));
                    } else {
                        JOptionPane.showMessageDialog(panelEvaluation, "Ce dossier ne contient aucun fichier de graph, veuillez réessayer", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }

                    // Tri des fichiers selon l'ordre numérique dans les noms de fichier
                    fichiers_choisi_repertoire.sort(Comparator.comparing(f -> {
                        String name = f.getName();
                        String number = name.substring(10, name.length() - 4);
                        return Integer.parseInt(number);
                    }));

                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile() && file.getName().endsWith(".graph")) {
                                String fileName = file.getName();
                                try {
                                    graph = new CreateurGraph(file);
                                    kmax = graph.getGraph().getAttribute("kmax");

                                    // Mettre à jour l'affichage si la création du graphe réussit
                                    labelNomFichier.setText(fileName);
                                    labelNomFichier.setForeground(Color.BLUE);
                                    labelNomFichier.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                                    boutonFichierGraphe.setForeground(Color.decode("#77E59B"));
                                    panelEvaluation.revalidate();
                                    panelEvaluation.repaint();
                                } catch (IOException | ExceptionFormatIncorrect | ExceptionLigneIncorrect ex) {
                                    JOptionPane.showMessageDialog(panelEvaluation, "Erreur lors de la lecture du fichier " + fileName + "! Vérifiez que le fichier fourni est correct.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                    ex.printStackTrace(); // Afficher l'erreur dans le terminal pour un débogage ultérieur
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boutonFichierGraphe.setBackground(couleurSecondaire);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boutonFichierGraphe.setBackground(couleurTertaire);
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

        // Décoration du bouton pour deposer le dossier de graphes
        boutonFichierGraphe.setFocusable(false);
        boutonFichierGraphe.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonFichierGraphe.setForeground(Color.WHITE);
        boutonFichierGraphe.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        boutonFichierGraphe.setBackground(couleurTertaire);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Revenir à une seule colonne
        panelChargerCentre.add(boutonFichierGraphe, gbc);

        // Mise en place et décoration du bouton pour colorier les graphes
        RoundedButton boutonColoration = new RoundedButton("Effectuer la coloration et telecharger", 50);
        boutonColoration.setFocusable(false);
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonColoration.setPreferredSize(new Dimension(220, 50));
        boutonColoration.setBorder(new EmptyBorder(15, 15, 15, 15));
        boutonColoration.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonColoration.setBackground(couleurTertaire);
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fichiers_choisi_repertoire.isEmpty()) {
                    JOptionPane.showMessageDialog(panelEvaluation, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    for (File fichier : fichiers_choisi_repertoire) {
                        int conflitwelsh = -1;
                        int conflitdsat = -1;
                        int[] colorationdsat;
                        int[] colorationwelsh;
                        CreateurGraph graphwelsh;
                        CreateurGraph graphdsat;
                        // Pour chaque fichier de graphe, effectue les 2 colorations et les met en resultats
                        try {
                            graphwelsh = new CreateurGraph(fichier);
                            WelshPowell algowelsh = new WelshPowell(graphwelsh.getGraph());
                            colorationwelsh = algowelsh.colorierNoeudsWelsh(graphwelsh.getGraph().getAttribute("kmax"));
                            conflitwelsh = graphwelsh.CompterConflits(algowelsh.getGraph());
                        } catch (IOException | ExceptionFormatIncorrect | ExceptionLigneIncorrect ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            graphdsat = new CreateurGraph(fichier);
                            colorationdsat = DsaturAlgorithm.dsaturColoring(graphdsat.getGraph(), graphdsat.getGraph().getAttribute("kmax"));
                            DsaturAlgorithm.modifyNodeColors(graphdsat.getGraph(), colorationdsat);
                            conflitdsat = graphdsat.CompterConflits(graphdsat.getGraph());
                        } catch (IOException | ExceptionFormatIncorrect | ExceptionLigneIncorrect ex) {
                            throw new RuntimeException(ex);
                        }
                        // Ecrit les fichier de resultats csv et les txt de chaque graphe en fonction de la meilleure coloration obtenue
                        if (conflitwelsh <= conflitdsat) {
                            writeCSVFile(fichier.getName(), conflitwelsh, "7");
                            writeTxtFile(graphwelsh.getGraph(), colorationwelsh, "7");
                        } else {
                            writeCSVFile(fichier.getName(), conflitdsat, "7");
                            writeTxtFile(graphdsat.getGraph(), colorationdsat, "7");
                        }
                    }
                    try {
                        JOptionPane.showMessageDialog(panelEvaluation, "Votre fichier zippé a bien été créé", "Challenge coloration", JOptionPane.INFORMATION_MESSAGE);
                        // transforme le dossier de resultats en zip
                        zipOutputDirectory("7");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boutonColoration.setBackground(couleurSecondaire);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boutonColoration.setBackground(couleurTertaire);
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

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridheight = 1; // Revenir à une seule ligne
        gbc.gridwidth = 1; // Revenir à une seule colonne
        panelChargerCentre.add(boutonColoration, gbc);

        panelChargerBas.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel panelgauche = new JPanel();
        panelgauche.setOpaque(false);
        JPanel paneldroite = new JPanel();
        paneldroite.setOpaque(false);

        // ajoute tout les panels principaux dans le panel de la page
        panelEvaluation.add(panelChargerHaut, BorderLayout.NORTH);
        panelEvaluation.add(panelgauche, BorderLayout.WEST);
        panelEvaluation.add(panelChargerCentre, BorderLayout.CENTER);
        panelEvaluation.add(paneldroite, BorderLayout.EAST);
        panelEvaluation.add(panelChargerBas, BorderLayout.SOUTH);
    }

    // Méthode pour sauvegarder un fichier
    private void saveFile() {
        if (fileToDownload != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le fichier");

            // Configurer le nom de fichier par défaut
            fileChooser.setSelectedFile(new File("example.txt"));

            int userSelection = fileChooser.showSaveDialog(panelEvaluation);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                        writer.write("Ceci est un exemple de fichier texte avec des informations.");
                    }
                    JOptionPane.showMessageDialog(panelEvaluation, "Fichier enregistré avec succès.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panelEvaluation, "Erreur lors de l'enregistrement du fichier : " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(panelEvaluation, "Le fichier à télécharger n'a pas été créé.");
        }
    }

    /**
     * Renvoie le panelEvalutaion
     * @return le JPanel panelEvaluation
     */
    public JPanel getPanel() {
        return panelEvaluation;
    }
}