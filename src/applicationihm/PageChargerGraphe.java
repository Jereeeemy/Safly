package applicationihm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import graphvol.CreateurGraph;
import org.graphstream.ui.view.Viewer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static coloration.DsaturAlgorithm.colorAndDisplayGraph;

public class PageChargerGraphe {
    private final JPanel panelCharger;
    private CreateurGraph test;
    public File selectedFile;
    public File fileToDownload;
    int kmax;
    int noeuds = 0;
    int aretes = 0;
    int degre = 0;
    int composantes = 0;
    int diametre = 0;
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
        panelChargerCentre.setBackground(Color.decode("#D9D9D9"));
        panelChargerCentre.setOpaque(false);
        JPanel panelChargerBas = new JPanel();
        panelChargerBas.setOpaque(false);

        RoundedButton boutonAccueil = new RoundedButton("Accueil", 25);
        boutonAccueil.setFocusable(false);
        boutonAccueil.setFont(new Font("Lucida Sans",Font.PLAIN,15));
        boutonAccueil.setBackground(Color.decode("#D9D9D9"));

        boutonAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.setContentPane(menuPrincipal.getPanelAccueil());
                menuPrincipal.revalidate();
                menuPrincipal.repaint();
            }
        });

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
                boutonAccueil.setBackground(Color.decode("#D9D9D9"));
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

        panelChargerCentre.setLayout(new BorderLayout(0,0));
        JPanel centrePanel1 = new JPanel();
        centrePanel1.setLayout(new BoxLayout(centrePanel1, BoxLayout.PAGE_AXIS));
        centrePanel1.setBackground(Color.decode("#D9D9D9"));
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
                        test = new CreateurGraph(selectedFile);
                        kmax = test.getGraph().getAttribute("kmax");
                        spinnerKMax.setValue(kmax);

                        // Mettre à jour l'affichage si la création du graphe réussit
                        labelNomFichier.setText(fileName);
                        labelNomFichier.setForeground(Color.BLUE);
                        labelNomFichier.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                        boutonFichierGraphe.setForeground(Color.decode("#77E59B"));
                        panelCharger.revalidate();
                        panelCharger.repaint();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(panelCharger, "Erreur lors de la lecture du fichier ! Vérifiez que le fichier fourni est correct.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace(); // Afficher l'erreur dans le terminal pour un débogage ultérieur
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
        boutonFichierGraphe.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        boutonFichierGraphe.setBackground(Color.decode("#696767"));
        centrePanel1.add(boutonFichierGraphe);
        centrePanel1.add(labelNomFichier);
        JLabel labelTelechargerFichier = new JLabel("<html><div style='text-align: left'> Cliquez ici si vous souhaitez <br>télécharger le fichier qui <br>contient les informations <br>du graphe colorié </div></html>");
        labelTelechargerFichier.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        labelTelechargerFichier.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        centrePanel1.add(labelTelechargerFichier);
        RoundedButton boutonTelecharger = new RoundedButton("Télécharger", 50);
        boutonTelecharger.setFocusable(false);
        boutonTelecharger.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        boutonTelecharger.setBackground(Color.decode("#696767"));
        boutonTelecharger.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonTelecharger.setForeground(Color.WHITE);
        boutonTelecharger.setCursor(new Cursor(Cursor.HAND_CURSOR));



        centrePanel1.add(boutonTelecharger);

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

        JPanel centrePanel2 = new JPanel();
        centrePanel2.setLayout(new BoxLayout(centrePanel2, BoxLayout.PAGE_AXIS));
        centrePanel2.setBackground(Color.decode("#D9D9D9"));
        JLabel labelChoixParam = new JLabel("<html><div style='text-align: left'> Après avoir chargé votre graphe,<br> vous pouvez personnaliser la <br>coloration du graphe : </div></html>");
        labelChoixParam.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        labelChoixParam.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        centrePanel2.add(labelChoixParam);
        RoundedPanel miniPanelCentral = new RoundedPanel(25);
        miniPanelCentral.setBackground(Color.decode("#D9D9D9"));
        miniPanelCentral.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        miniPanelCentral.setLayout(new BoxLayout(miniPanelCentral, BoxLayout.PAGE_AXIS));
        JLabel labelChoixAlgo = new JLabel("Choix de l'Algo de coloration :");
        labelChoixAlgo.setForeground(Color.WHITE);
        miniPanelCentral.add(labelChoixAlgo);
        labelChoixAlgo.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        ButtonGroup groupAlgos = new ButtonGroup();
        JRadioButton WelshBouton = new JRadioButton("Welsh & Powell");
        WelshBouton.setForeground(Color.WHITE);
        WelshBouton.setFocusable(false);
        WelshBouton.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        WelshBouton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JRadioButton DsaturBouton = new JRadioButton("D-Satur");
        DsaturBouton.setForeground(Color.WHITE);
        DsaturBouton.setFocusable(false);
        DsaturBouton.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        DsaturBouton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        groupAlgos.add(WelshBouton);
        groupAlgos.add(DsaturBouton);
        WelshBouton.setBackground(Color.decode("#696767"));
        WelshBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        DsaturBouton.setBackground(Color.decode("#696767"));
        DsaturBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        WelshBouton.setSelected(true);
        miniPanelCentral.add(WelshBouton);
        miniPanelCentral.add(DsaturBouton);
        JLabel labelKmax = new JLabel("Choix de K-max :");
        labelKmax.setForeground(Color.WHITE);
        miniPanelCentral.add(labelKmax);
        labelKmax.setFont(new Font("Lucida Sans",Font.PLAIN,20));

        miniPanelCentral.add(spinnerKMax);

        miniPanelCentral.setBackground(Color.decode("#696767"));
        centrePanel2.add(miniPanelCentral);
        RoundedButton boutonColoration = new RoundedButton("Effectuer la coloration", 50);
        boutonColoration.setFocusable(false);
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonColoration.setPreferredSize(new Dimension(220, 50));
        boutonColoration.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boutonColoration.setBackground(Color.decode("#122A47"));
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelCharger, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else if (DsaturBouton.isSelected()){
                    colorAndDisplayGraph(test.getGraph(),kmax);
                } else if (WelshBouton.isSelected()) {

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boutonColoration.setBackground(Color.decode("#2C5789"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boutonColoration.setBackground(Color.decode("#122A47"));
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

        centrePanel2.add(boutonColoration);



        JPanel centrePanel3 = new JPanel();
        centrePanel3.setBackground(Color.decode("#D9D9D9"));
        JTable tableauInfoGraphe = new JTable();
        tableauInfoGraphe.setModel(new DefaultTableModel(
                new Object[][]{
                        {"Informations : ", null},
                        {null, null},
                        {"Noeuds :", " "+ " "+ noeuds},
                        {"",null},
                        {"Arêtes :", " "+ " "+ aretes},
                        {"",null},
                        {"Degré Moyen :", " "+ " "+ degre},
                        {"",null},
                        {"Nb Composantes :", " "+ " "+ composantes},
                        {"",null},
                        {"Diamètre", " "+ " "+ diametre},
                        {"",null},
                },
                new String[]{"Informations Graphe :", null}
        ));

        tableauInfoGraphe.setGridColor(Color.DARK_GRAY);
        tableauInfoGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,18));
        tableauInfoGraphe.setForeground(Color.WHITE);
        tableauInfoGraphe.setBackground(Color.decode("#696767"));
        tableauInfoGraphe.setShowVerticalLines(true);
        tableauInfoGraphe.setShowHorizontalLines(false);
        tableauInfoGraphe.setPreferredSize(new Dimension(250, 300));
        tableauInfoGraphe.setRowHeight(tableauInfoGraphe.getRowHeight() + 10);
        tableauInfoGraphe.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableauInfoGraphe.setEnabled(false);

        centrePanel3.add(tableauInfoGraphe);
        centrePanel3.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        JPanel centrePanelContainer = new JPanel();
        centrePanelContainer.setLayout(new BoxLayout(centrePanelContainer, BoxLayout.X_AXIS));
        centrePanelContainer.setBackground(Color.decode("#D9D9D9"));

        centrePanel2.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

        // Add centrePanel1, sepa1, centrePanel2, sepa2, centrePanel3 to the container
        centrePanelContainer.add(centrePanel1);
        centrePanelContainer.add(sepa1);
        centrePanelContainer.add(centrePanel2);
        centrePanelContainer.add(sepa2);
        centrePanelContainer.add(centrePanel3);

        // Add the container to the center of panelChargerCentre
        panelChargerCentre.add(centrePanelContainer, BorderLayout.CENTER);

        RoundedButton boutonAfficherGraphe = new RoundedButton("Afficher le Graphe", 90);
        boutonAfficherGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelCharger, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Viewer vue = test.getGraph().display();
                vue.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boutonAfficherGraphe.setBackground(Color.decode("#2C5789"));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boutonAfficherGraphe.setBackground(Color.decode("#122A47"));
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

        boutonAfficherGraphe.setFocusable(false);
        boutonAfficherGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,25));
        boutonAfficherGraphe.setForeground(Color.WHITE);
        boutonAfficherGraphe.setPreferredSize(new Dimension(320, 80));
        boutonAfficherGraphe.setBackground(Color.decode("#122A47"));
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
        spinnerKMax.setModel(new SpinnerNumberModel(10, 0, 200, 1));
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

    public JPanel getPanel() {
        return panelCharger;
    }
}