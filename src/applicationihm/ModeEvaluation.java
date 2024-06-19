package applicationihm;

import coloration.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import graphvol.CreateurGraph;
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

public class ModeEvaluation {
    private final JPanel panelEvaluation;
    private CreateurGraph graph;
    public File selectedFile;
    public File fileToDownload;
    int kmax;
    Color couleurPrincipale = Color.decode("#D9D9D9");
    Color couleurSecondaire = Color.decode("#2C5789");
    Color couleurTertaire = Color.decode("#122A47");

    public ModeEvaluation(MenuPrincipal menuPrincipal) {

        panelEvaluation = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Récupérer l'image de fond actuelle à chaque fois que le panneau est redessiné
                Image background = menuPrincipal.getBackgroundImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panelEvaluation.setLayout(new BorderLayout(200, 75));

        JPanel panelChargerHaut = new JPanel();
        panelChargerHaut.setOpaque(false);
        RoundedPanel panelChargerCentre = new RoundedPanel(40);

        panelChargerCentre.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        panelChargerCentre.setBackground(couleurPrincipale);
        panelChargerCentre.setOpaque(false);
        JPanel panelChargerBas = new JPanel();
        panelChargerBas.setOpaque(false);
        JLabel nomAppli = new JLabel("SAFLY");
        nomAppli.setFont(new Font("Lucida Sans", Font.BOLD, 60));
        panelChargerBas.add(nomAppli);

        RoundedButton boutonAccueil = new RoundedButton("Accueil", 25);
        boutonAccueil.setFocusable(false);
        boutonAccueil.setFont(new Font("Lucida Sans", Font.PLAIN, 15));
        boutonAccueil.setBackground(couleurPrincipale);

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

        boutonFichierGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelEvaluation);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    try {
                        // Essayer de créer le graphe avec le fichier sélectionné
                        graph = new CreateurGraph(selectedFile);
                        kmax = graph.getGraph().getAttribute("kmax");

                        // Mettre à jour l'affichage si la création du graphe réussit
                        labelNomFichier.setText(fileName);
                        labelNomFichier.setForeground(Color.BLUE);
                        labelNomFichier.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                        boutonFichierGraphe.setForeground(Color.decode("#77E59B"));
                        panelEvaluation.revalidate();
                        panelEvaluation.repaint();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(panelEvaluation, "Erreur lors de la lecture du fichier ! Vérifiez que le fichier fourni est correct.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace(); // Afficher l'erreur dans le terminal pour un débogage ultérieur
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

        boutonFichierGraphe.setFocusable(false);
        boutonFichierGraphe.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonFichierGraphe.setForeground(Color.WHITE);
        boutonFichierGraphe.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        boutonFichierGraphe.setBackground(couleurTertaire);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Revenir à une seule colonne
        panelChargerCentre.add(boutonFichierGraphe, gbc);


        RoundedButton boutonColoration = new RoundedButton("Effectuer la coloration et telecharger", 50);
        boutonColoration.setFocusable(false);
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonColoration.setPreferredSize(new Dimension(220, 50));
        boutonColoration.setBorder(new EmptyBorder(15,15,15,15));
        boutonColoration.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonColoration.setBackground(couleurTertaire);
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelEvaluation, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
        panelEvaluation.add(panelChargerHaut, BorderLayout.NORTH);
        panelEvaluation.add(panelgauche, BorderLayout.WEST);
        panelEvaluation.add(panelChargerCentre, BorderLayout.CENTER);
        panelEvaluation.add(paneldroite, BorderLayout.EAST);
        panelEvaluation.add(panelChargerBas, BorderLayout.SOUTH);
    }

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
                    // Copier le contenu du fichier temporaire vers le fichier choisi par l'utilisateur
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

    public JPanel getPanel() {
        return panelEvaluation;
    }
}