package applicationihm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PageChargerGraphe {
    private final JPanel panelCharger;

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
        boutonAccueil.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boutonAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.setContentPane(menuPrincipal.getPanelAccueil());
                menuPrincipal.revalidate();
                menuPrincipal.repaint();
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
        RoundedButton boutonFichierGraphe = new RoundedButton("Déposer un fichier de graphe", 90);
        boutonFichierGraphe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelCharger);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    labelNomFichier.setText(fileName);
                    labelNomFichier.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
                    boutonFichierGraphe.setForeground(Color.GREEN);
                    panelCharger.revalidate();
                    panelCharger.repaint();
                }
            }
        });
        boutonFichierGraphe.setFocusable(false);
        boutonFichierGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        RoundedButton boutonTelecharger = new RoundedButton("Télécharger", 70);
        boutonTelecharger.setFocusable(false);
        boutonTelecharger.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        boutonTelecharger.setBackground(Color.decode("#696767"));
        boutonTelecharger.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonTelecharger.setForeground(Color.WHITE);
        boutonTelecharger.setCursor(new Cursor(Cursor.HAND_CURSOR));
        centrePanel1.add(boutonTelecharger);


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
        DsaturBouton.setBackground(Color.decode("#696767"));
        WelshBouton.setSelected(true);
        miniPanelCentral.add(WelshBouton);
        miniPanelCentral.add(DsaturBouton);
        JLabel labelKmax = new JLabel("Choix de K-max :");
        labelKmax.setForeground(Color.WHITE);
        miniPanelCentral.add(labelKmax);
        labelKmax.setFont(new Font("Lucida Sans",Font.PLAIN,20));

        JSpinner spinnerKMax = getSpinnerKMax();
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
        centrePanel2.add(boutonColoration);

        JPanel centrePanel3 = new JPanel();
        centrePanel3.setBackground(Color.decode("#D9D9D9"));
        JTable tableauInfoGraphe = new JTable();
        tableauInfoGraphe.setModel(new DefaultTableModel(
                new Object[][]{
                        {"Noeuds :", null},
                        {"Arêtes :", null},
                        {"Degré Moyen :", null},
                        {"Nb Composantes :", null},
                        {"Diamètre", null}
                },
                new String[]{"Informations Graphe :", null}
        ));
        tableauInfoGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,10));
        tableauInfoGraphe.setForeground(Color.WHITE);
        tableauInfoGraphe.setBackground(Color.decode("#696767"));
        tableauInfoGraphe.setShowGrid(false);
        tableauInfoGraphe.setPreferredSize(new Dimension(150, 200));
        tableauInfoGraphe.setRowHeight(tableauInfoGraphe.getRowHeight() + 10);
        tableauInfoGraphe.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableauInfoGraphe.setEnabled(false);

        centrePanel3.add(tableauInfoGraphe);

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
        boutonAfficherGraphe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame fenetreGraphe = new JFrame();
                fenetreGraphe.setTitle("Graphe");
                fenetreGraphe.setSize(750, 700);
                fenetreGraphe.setLocationRelativeTo(null);
                fenetreGraphe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fenetreGraphe.setVisible(true);

            }
        });
        boutonAfficherGraphe.setFocusable(false);
        boutonAfficherGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    public JPanel getPanel() {
        return panelCharger;
    }
}
