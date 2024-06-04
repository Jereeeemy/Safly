package applicationihm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PageConstruireGraphe {
    private final JPanel panelConstruire;

    public PageConstruireGraphe(MenuPrincipal menuPrincipal) {
        Image background = menuPrincipal.getBackgroundImage();

        panelConstruire = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panelConstruire.setLayout(new BorderLayout(60,45));
        JPanel panelConstruireHaut = new JPanel();
        panelConstruireHaut.setOpaque(false);
        RoundedPanel panelConstruireCentre = new RoundedPanel(40);
        panelConstruireCentre.setBackground(Color.decode("#D9D9D9"));
        panelConstruireCentre.setOpaque(false);
        panelConstruireCentre.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));

        JPanel panelConstruireBas = new JPanel();
        panelConstruireBas.setOpaque(false);

        RoundedButton boutonAccueil = new RoundedButton("Accueil",25);
        boutonAccueil.setFocusable(false);
        boutonAccueil.setFont(new Font("Lucida Sans",Font.PLAIN,15));
        boutonAccueil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonAccueil.setBackground(Color.decode("#D9D9D9"));
        boutonAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.setContentPane(menuPrincipal.getPanelAccueil());
                menuPrincipal.revalidate();
                menuPrincipal.repaint();
            }
        });
        panelConstruireHaut.add(boutonAccueil, BorderLayout.CENTER);
        panelConstruireHaut.setLayout(new FlowLayout(FlowLayout.CENTER));

        panelConstruireCentre.setLayout(new BoxLayout(panelConstruireCentre, BoxLayout.LINE_AXIS));
        JPanel centrePanel1 = new JPanel();
        centrePanel1.setBackground(Color.decode("#D9D9D9"));
        centrePanel1.setLayout(new BoxLayout(centrePanel1, BoxLayout.PAGE_AXIS));
        JLabel labelDeposeFichiers = new JLabel("<html><div style='text-align: left'> Pour construire un graphe,<br> veuillez d'abord fournir <br>les fichiers nécessaires</div></html>");
        labelDeposeFichiers.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        labelDeposeFichiers.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        centrePanel1.add(labelDeposeFichiers);
        JLabel labelNomFichier = new JLabel();
        JLabel labelListeVol = new JLabel();
        RoundedButton boutonFichierAeroport = new RoundedButton("Importer une liste d'aéroports",50);
        boutonFichierAeroport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelConstruire);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    labelNomFichier.setText(fileName);
                    labelNomFichier.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
                    boutonFichierAeroport.setForeground(Color.GREEN);
                    panelConstruire.revalidate();
                    panelConstruire.repaint();
                }
            }
        });
        boutonFichierAeroport.setFocusable(false);
        boutonFichierAeroport.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonFichierAeroport.setForeground(Color.WHITE);
        boutonFichierAeroport.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        boutonFichierAeroport.setBackground(Color.decode("#696767"));
        boutonFichierAeroport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        centrePanel1.add(boutonFichierAeroport);
        centrePanel1.add(labelNomFichier);
        RoundedButton boutonFichierVol = new RoundedButton("Importer une liste de vols",50);
        boutonFichierVol.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               JFileChooser fileChooser = new JFileChooser();
               int returnValue = fileChooser.showOpenDialog(panelConstruire);
               if (returnValue == JFileChooser.APPROVE_OPTION) {
                   File selectedFile = fileChooser.getSelectedFile();
                   String fileName = selectedFile.getName();
                   labelListeVol.setText(fileName);
                   labelListeVol.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
                   boutonFichierVol.setForeground(Color.GREEN);
                   panelConstruire.revalidate();
                   panelConstruire.repaint();

               }
           }
        });
        boutonFichierVol.setFocusable(false);
        boutonFichierVol.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        boutonFichierVol.setForeground(Color.WHITE);
        boutonFichierAeroport.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonFichierVol.setBackground(Color.decode("#696767"));
        boutonFichierVol.setCursor(new Cursor(Cursor.HAND_CURSOR));

        centrePanel1.add(boutonFichierVol);
        centrePanel1.add(labelListeVol);

        JPanel centrePanel2 = new JPanel();
        centrePanel2.setBackground(Color.decode("#D9D9D9"));
        centrePanel2.setLayout(new BoxLayout(centrePanel2,BoxLayout.PAGE_AXIS));
        JLabel labelChoixParam = new JLabel("<html><div style='text-align: left'> Après avoir chargé votre graphe,<br> vous pouvez personnaliser <br>la coloration du graphe : </div></html>");
        labelChoixParam.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        labelChoixParam.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        centrePanel2.add(labelChoixParam);
        RoundedPanel miniPanelCentral = new RoundedPanel(25);
        miniPanelCentral.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        miniPanelCentral.setLayout(new BoxLayout(miniPanelCentral,BoxLayout.PAGE_AXIS));
        JLabel labelChoixAlgo = new JLabel("Choix Algorithme de coloration :");
        labelChoixAlgo.setForeground(Color.WHITE);
        labelChoixAlgo.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        miniPanelCentral.add(labelChoixAlgo);
        labelChoixAlgo.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        ButtonGroup groupAlgos = new ButtonGroup();
        JRadioButton WelshBouton = new JRadioButton("Welsh & Powell");
        WelshBouton.setForeground(Color.WHITE);
        WelshBouton.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        WelshBouton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JRadioButton DsaturBouton = new JRadioButton("D-Satur");
        DsaturBouton.setForeground(Color.WHITE);
        DsaturBouton.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        DsaturBouton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        DsaturBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        groupAlgos.add(WelshBouton);
        groupAlgos.add(DsaturBouton);
        WelshBouton.setBackground(Color.decode("#696767"));
        DsaturBouton.setBackground(Color.decode("#696767"));
        WelshBouton.setSelected(true);
        WelshBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        miniPanelCentral.add(WelshBouton);
        miniPanelCentral.add(DsaturBouton);
        JLabel labelChoixKmax = new JLabel("Choix de K-max :");
        miniPanelCentral.add(labelChoixKmax);
        labelChoixKmax.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        labelChoixKmax.setForeground(Color.WHITE);
        JSpinner spinnerKMax = getSpinnerKMax();
        miniPanelCentral.add(spinnerKMax);

        miniPanelCentral.setBackground(Color.decode("#696767"));
        centrePanel2.add(miniPanelCentral);
        RoundedButton boutonColoration = new RoundedButton("Effectuer la coloration",50);
        boutonColoration.setFocusable(false);
        boutonColoration.setFont(new Font("Lucida Sans",Font.PLAIN,30));
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setPreferredSize(new Dimension(400,80));
        boutonColoration.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonColoration.setBackground(Color.decode("#122A47"));
        centrePanel2.add(boutonColoration);

        JPanel centrePanel3 = new JPanel();
        centrePanel3.setBackground(Color.decode("#D9D9D9"));
        JTable tableauInfoGraphe = new JTable();
        tableauInfoGraphe.setModel(new DefaultTableModel(
                new Object[][]{
                        {"Informations : ", null},
                        {null, null},
                        {"Noeuds :", 280},
                        {"",null},
                        {"Arêtes :", 7},
                        {"",null},
                        {"Degré Moyen :", 6},
                        {"",null},
                        {"Nb Composantes :", 12},
                        {"",null},
                        {"Diamètre", 6},
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
        JSeparator sepa1 = new JSeparator(SwingConstants.VERTICAL);
        sepa1.setBackground(Color.GRAY);
        JSeparator sepa2 = new JSeparator(SwingConstants.VERTICAL);
        sepa2.setBackground(Color.GRAY);


        panelConstruireCentre.add(centrePanel1);
        panelConstruireCentre.add(sepa1);
        panelConstruireCentre.add(centrePanel2);
        panelConstruireCentre.add(sepa2);
        panelConstruireCentre.add(centrePanel3);


        RoundedButton boutonAfficherGraphe = new RoundedButton("Afficher la carte",90);
        boutonAfficherGraphe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame fenetreGraphe = new JFrame();
                fenetreGraphe.setTitle("Carte");
                fenetreGraphe.setSize(750, 700);
                fenetreGraphe.setLocationRelativeTo(null);
                fenetreGraphe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fenetreGraphe.setVisible(true);
            }
        });
        boutonAfficherGraphe.setFocusable(false);
        boutonAfficherGraphe.setForeground(Color.WHITE);
        boutonAfficherGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,30));
        boutonAfficherGraphe.setPreferredSize(new Dimension(320,80));
        boutonAfficherGraphe.setBackground(Color.decode("#122A47"));
        boutonAfficherGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelConstruireBas.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        panelConstruireBas.add(boutonAfficherGraphe);

        JPanel panelgauche = new JPanel();
        panelgauche.setOpaque(false);
        JPanel paneldroite = new JPanel();
        paneldroite.setOpaque(false);
        panelConstruire.add(panelConstruireHaut,BorderLayout.NORTH);
        panelConstruire.add(panelgauche,BorderLayout.WEST);
        panelConstruire.add(panelConstruireCentre,BorderLayout.CENTER);
        panelConstruire.add(paneldroite,BorderLayout.EAST);
        panelConstruire.add(panelConstruireBas, BorderLayout.SOUTH);
    }

    private static JSpinner getSpinnerKMax() {
        JSpinner spinnerKMax = new JSpinner();
        spinnerKMax.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        spinnerKMax.setModel(new SpinnerNumberModel(10,0,200,1));
        spinnerKMax.setFocusable(false);
        Dimension preferredSize = new Dimension(75, spinnerKMax.getPreferredSize().height+5);
        spinnerKMax.setPreferredSize(preferredSize);
        spinnerKMax.setMaximumSize(preferredSize);
        JComponent editor = spinnerKMax.getEditor();
        if (editor instanceof JSpinner.DefaultEditor){
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setEditable(false);
            textField.setBackground(Color.LIGHT_GRAY);
        }
        spinnerKMax.setBackground(Color.decode("#696767"));
        return spinnerKMax;
    }

    public JPanel getPanel() {
        return panelConstruire;
    }
}
