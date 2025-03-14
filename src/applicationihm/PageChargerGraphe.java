package applicationihm;

import coloration.DsaturAlgorithm;
import coloration.RLF;
import coloration.WelshPowell;
import graphvol.CreateurGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static graphvol.CreateurGraph.InfoscolorOneGraph;
import static coloration.DsaturAlgorithm.dsaturColoring;
import org.graphstream.ui.view.ViewerPipe;

/**
 * Classe de l'application qui propose de charger un graphe à partir d'un fichier et de colorier ce graphe grâce à différents algorithmes
 */
public class PageChargerGraphe {
    /**
     * Bouton "Accueil" pour retourner à la page d'accueil.
     */
    private final JPanel panelCharger;

    /**
     * Nom du fichier graph
     */
    private String fileName;

    /**
     * Nom du fichier txt de sortie qui va contenir les infos du graph
     */
    private String outputFileName;

    /**
     * Cette variable va permettre de stocker les résultats de couleur de Dsatur
     */
    private int resultcolorDsat[];

    /**
     * Cette variable va permettre de stocker les résultats de couleur de Welsh&Powel
     */
    private int resultcolorWelsh[];

    /**
     * Cette variable va permettre de stocker les résultats de couleur de RLF
     */
    private int[] resultcolorRLF;

    /**
     * Fichier de sortie qui va contenir les informations de couleurs pour chaque sommet
     */
    File OutPuttxtFile;

    /**
     * Bouton "Accueil" pour retourner à la page d'accueil.
     */
    private RoundedButton boutonAccueil;

    /**
     * Couleur principale utilisée dans l'interface.
     * Par défaut : #D9D9D9 (Gris clair).
     */
    private Color couleurPrincipale = Color.decode("#D9D9D9");

    /**
     * Couleur secondaire utilisée dans l'interface.
     * Par défaut : #2C5789 (Bleu foncé).
     */
    private Color couleurSecondaire = Color.decode("#2C5789");

    /**
     * Couleur tertiaire utilisée dans l'interface.
     * Par défaut : #122A47 (Bleu marine).
     */
    private Color couleurTertiaire = Color.decode("#122A47");

    /**
     * Instance du créateur de graphe utilisé pour la manipulation de graphes.
     */
    private CreateurGraph graph;

    /**
     * Instance de l'algorithme de coloration de graphes Welsh-Powell.
     */
    private WelshPowell graphWelsh;

    /**
     * Fichier de graphe sélectionné par l'utilisateur.
     */
    public File selectedFile;

    /**
     * Valeur de la marge de sécurité pour la gestion des conflits.
     */
    int kmax;

    /**
     * Nombre de nœuds dans le graphe chargé.
     */
    int noeuds = 0;

    /**
     * Nombre d'arêtes dans le graphe chargé.
     */
    int aretes = 0;

    /**
     * Degré moyen des nœuds dans le graphe chargé.
     */
    double degre = 0;

    /**
     * Nombre de composantes connexes dans le graphe chargé.
     */
    int composantes = 0;

    /**
     * Diamètre du graphe chargé.
     */
    int diametre = 0;

    /**
     * Nombre de conflits du graphes
     */
    int nbConflit;

    /**
     * Tableau utilisé pour afficher les informations du graphe.
     */
    private JTable tableauInfoGraphe;

    /**
     * Modèle de données utilisé par le tableau d'informations du graphe.
     */
    private DefaultTableModel model;

    /**
     * Bouton pour lancer l'algorithme de coloration du graphe.
     */
    private RoundedButton boutonColoration;

    /**
     * Bouton pour télécharger le graphe ou les résultats.
     */
    private RoundedButton boutonTelecharger;

    /**
     * Bouton radio pour choisir l'algorithme de coloration Welsh-Powell.
     */
    private JRadioButton WelshBouton;

    /**
     * Bouton radio pour choisir l'algorithme de coloration DSatur.
     */
    private JRadioButton DsaturBouton;

    /**
     * Bouton radio pour choisir l'algorithme de coloration RLF.
     */
    private JRadioButton RLFBouton;

    /**
     * Bouton pour afficher le graphe dans une vue graphique.
     */
    private RoundedButton boutonAfficherGraphe;

    /**
     * Visionneuse pour afficher le graphe graphiquement.
     */
    private Viewer vue;

    /**
     * Tube de visionneuse pour interagir avec la vue du graphe.
     */
    private ViewerPipe viewerPipe;



    /**
     * Constructeur de la classe PageChargerGraphe.
     * @param menuPrincipal Instance de la classe MenuPrincipal pour la navigation et la gestion de l'interface.
     */
    public PageChargerGraphe(MenuPrincipal menuPrincipal) {

        // Créer un JPanel avec une méthode paintComponent personnalisée pour dessiner l'image de fond
        panelCharger = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Récupérer l'image de fond actuelle à chaque fois que le panneau est redessiné
                Image background = menuPrincipal.getBackgroundImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Créer des séparateurs verticaux avec un fond gris
        JSeparator sepa1 = new JSeparator(SwingConstants.VERTICAL);
        sepa1.setBackground(Color.GRAY);
        JSeparator sepa2 = new JSeparator(SwingConstants.VERTICAL);
        sepa2.setBackground(Color.GRAY);

        // Définir la disposition du panneau principal avec des écarts horizontaux et verticaux
        panelCharger.setLayout(new BorderLayout(60,45));

        // Créer et configurer le panneau du haut
        JPanel panelChargerHaut = new JPanel();
        panelChargerHaut.setOpaque(false);
        RoundedPanel panelChargerCentre = new RoundedPanel(40);

        // Créer et configurer le panneau central arrondi
        panelChargerCentre.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        panelChargerCentre.setBackground(couleurPrincipale);
        panelChargerCentre.setLayout(new BorderLayout(0,0));
        panelChargerCentre.setOpaque(false);

        // Créer et configurer le panneau du bas
        JPanel panelChargerBas = new JPanel();
        panelChargerBas.setOpaque(false);

        // Définir les propriétés du bouton d'accueil
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

        // Créer et configurer le premier panneau central
        JPanel centrePanel1 = new JPanel();
        centrePanel1.setLayout(new BoxLayout(centrePanel1, BoxLayout.PAGE_AXIS));
        centrePanel1.setBackground(couleurPrincipale);

        // Ajouter une étiquette avec des instructions pour déposer un fichier de graphe
        JLabel labelDeposeFichier = new JLabel("<html><div style='text-align: left'> Pour charger un graphe,<br> veuillez d'abord fournir <br>les fichiers nécessaires</div></html>");
        labelDeposeFichier.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        labelDeposeFichier.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        centrePanel1.add(labelDeposeFichier);

        // Étiquette pour afficher le nom du fichier sélectionné
        final JLabel labelNomFichier = new JLabel();

        // Bouton pour déposer un fichier de graphe
        RoundedButton boutonFichierGraphe = new RoundedButton("Déposer un fichier de graphe", 70);

        // Spinner pour sélectionner la valeur de k-max
        JSpinner spinnerKMax = getSpinnerKMax();
        spinnerKMax.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                kmax = (int) spinnerKMax.getValue();
                resultcolorDsat = null;
                resultcolorWelsh = null;
                resultcolorRLF = null;
            }
        });

        // Ajouter un écouteur au bouton pour sélectionner un fichier de graphe
        boutonFichierGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelCharger);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    fileName = selectedFile.getName();
                    try {
                        // Essayer de créer le graphe avec le fichier sélectionné
                        graph = new CreateurGraph(selectedFile);
                        graphWelsh = new WelshPowell(graph.getGraph());
                        resultcolorWelsh=null;
                        resultcolorDsat=null;
                        resultcolorRLF = null;
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

        // Ajouter une étiquette avec des instructions pour télécharger le fichier du graphe colorié
        JLabel labelTelechargerFichier = new JLabel("<html><div style='text-align: left'> Cliquez ici si vous souhaitez <br>télécharger le fichier qui <br>contient les informations <br>du graphe colorié </div></html>");
        labelTelechargerFichier.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        labelTelechargerFichier.setFont(new Font("Lucida Sans",Font.PLAIN,20));

        // Définir les propriétés du bouton de téléchargement
        decoBoutonTelecharger();

        // Ajouter les composants au premier panneau central
        centrePanel1.add(boutonFichierGraphe);
        centrePanel1.add(labelNomFichier);
        centrePanel1.add(labelTelechargerFichier);
        centrePanel1.add(boutonTelecharger);

        // Créer et configurer le deuxième panneau central
        JPanel centrePanel2 = new JPanel();
        centrePanel2.setLayout(new GridBagLayout());
        centrePanel2.setBackground(couleurPrincipale);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ajouter une étiquette principale avec des instructions
        JLabel labelChoixParam = new JLabel("<html><div style='text-align: left'> Après avoir chargé votre graphe,<br> vous pouvez personnaliser la <br>coloration du graphe : </div></html>");
        labelChoixParam.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        labelChoixParam.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        centrePanel2.add(labelChoixParam, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Ajouter le panneau central miniature
        RoundedPanel miniPanelCentral = new RoundedPanel(25);
        miniPanelCentral.setBackground(couleurPrincipale);
        miniPanelCentral.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        miniPanelCentral.setLayout(new BoxLayout(miniPanelCentral, BoxLayout.PAGE_AXIS));
        miniPanelCentral.setBackground(Color.decode("#696767"));

        // Ajouter une étiquette et des boutons pour le choix de l'algorithme de coloration
        JLabel labelChoixAlgo = new JLabel("Choix de l'Algo de coloration :");
        labelChoixAlgo.setForeground(Color.WHITE);
        labelChoixAlgo.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        miniPanelCentral.add(labelChoixAlgo);

        ButtonGroup groupAlgos = new ButtonGroup();
        decoBoutonWelsh(); // methode pour decorer et instancier le bouton Welsh
        decoBoutonDsatur(); // methode pour decorer et instancier le bouton DSatur
        decoBoutonRFL(); // methode pour decorer et instancier le bouton RFL

        groupAlgos.add(WelshBouton);
        groupAlgos.add(DsaturBouton);
        groupAlgos.add(RLFBouton);

        // Ajouter une étiquette et un spinner pour le choix de K-max
        JLabel labelKmax = new JLabel("Choix de K-max :");
        labelKmax.setForeground(Color.WHITE);
        labelKmax.setFont(new Font("Lucida Sans", Font.PLAIN, 20));


        miniPanelCentral.add(WelshBouton);
        miniPanelCentral.add(DsaturBouton);
        miniPanelCentral.add(RLFBouton);
        miniPanelCentral.add(labelKmax);
        miniPanelCentral.add(spinnerKMax);

        centrePanel2.add(miniPanelCentral, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 0, 0, 0);

        // Ajout du bouton de coloration
        decoBoutonColoration(); // Méthode que vous avez mentionnée pour instancier et personnaliser boutonColoration

        centrePanel2.add(boutonColoration, gbc);


        // Créer et configurer le troisième panneau central
        JPanel centrePanel3 = new JPanel();
        centrePanel3.setBackground(couleurPrincipale);

        // Initialiser la table et définir ses propriétés
        initializeTable();
        decoTab();

        // Ajouter le tableau d'informations sur le graphe au troisième panneau central
        centrePanel3.add(tableauInfoGraphe);
        centrePanel3.setBorder(BorderFactory.createEmptyBorder(0,30,0,10));

        // Créer et configurer le conteneur principal pour les panneaux centraux
        JPanel centrePanelContainer = new JPanel();
        centrePanelContainer.setLayout(new BoxLayout(centrePanelContainer, BoxLayout.X_AXIS));
        centrePanelContainer.setBackground(couleurPrincipale);

        // Ajouter des bordures au deuxième panneau central
        centrePanel2.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        // Ajouter les panneaux centraux et les séparateurs au conteneur principal
        centrePanelContainer.add(centrePanel1);
        centrePanelContainer.add(sepa1);
        centrePanelContainer.add(centrePanel2);
        centrePanelContainer.add(sepa2);
        centrePanelContainer.add(centrePanel3);

        // Ajouter le conteneur principal au centre du panneau principal
        panelChargerCentre.add(centrePanelContainer, BorderLayout.CENTER);

        // Définir les propriétés du bouton pour afficher le graphe
        decoBoutonGraphe();

        // Ajouter le bouton pour afficher le graphe au panneau inférieur
        panelChargerBas.add(boutonAfficherGraphe);
        panelChargerBas.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Créer et configurer les panneaux de gauche et de droite
        JPanel panelgauche = new JPanel();
        panelgauche.setOpaque(false);
        JPanel paneldroite = new JPanel();
        paneldroite.setOpaque(false);

        // Ajouter les différents panneaux au panneau principal
        panelCharger.add(panelChargerHaut,BorderLayout.NORTH);
        panelCharger.add(panelgauche,BorderLayout.WEST);
        panelCharger.add(panelChargerCentre,BorderLayout.CENTER);
        panelCharger.add(paneldroite, BorderLayout.EAST);
        panelCharger.add(panelChargerBas,BorderLayout.SOUTH);
    }


    /**
     * Méthode pour configurer et retourner un JSpinner pour la sélection de k-max.
     *
     * @return Un JSpinner configuré pour la sélection de k-max.
     */
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

    /**
     * Méthode pour enregistrer les informations du graphe colorié dans un fichier.
     * Ouvre un JFileChooser pour sélectionner l'emplacement de sauvegarde et enregistre
     * le fichier en fonction de l'algorithme de coloration sélectionné.
     */
    private void saveFile() {
        if (resultcolorDsat != null || resultcolorWelsh != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le fichier");

            // Configurer le nom de fichier par défaut
            outputFileName = "Infos-Coloration-" + fileName;
            fileChooser.setSelectedFile(new File(outputFileName));

            int userSelection = fileChooser.showSaveDialog(panelCharger);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String chosenFilePath = fileToSave.getAbsolutePath(); // Obtenir le chemin complet choisi par l'utilisateur

                if (DsaturBouton.isSelected()) {
                    try {
                        // Utiliser le chemin complet pour enregistrer le fichier
                        OutPuttxtFile = InfoscolorOneGraph(graph.getGraph(), chosenFilePath, resultcolorDsat);
                        JOptionPane.showMessageDialog(panelCharger, "Fichier enregistré avec succès.");
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(panelCharger, "Erreur lors de l'enregistrement du fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // Vous pouvez choisir de logger l'erreur plutôt que de l'afficher à l'utilisateur
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(panelCharger, "Une erreur inattendue s'est produite : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // Vous pouvez choisir de logger l'erreur plutôt que de l'afficher à l'utilisateur
                    }

                } else if (WelshBouton.isSelected()) {
                    try {
                        // Utiliser le chemin complet pour enregistrer le fichier
                        OutPuttxtFile = InfoscolorOneGraph(graph.getGraph(), chosenFilePath, resultcolorWelsh);
                        JOptionPane.showMessageDialog(panelCharger, "Fichier enregistré avec succès.");
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(panelCharger, "Erreur lors de l'enregistrement du fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // Vous pouvez choisir de logger l'erreur plutôt que de l'afficher à l'utilisateur
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(panelCharger, "Une erreur inattendue s'est produite : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // logger l'erreur plutôt que de l'afficher à l'utilisateur
                    }
                } else if (RLFBouton.isSelected()) {
                    try {
                        // Utiliser le chemin complet pour enregistrer le fichier
                        OutPuttxtFile = InfoscolorOneGraph(graph.getGraph(), chosenFilePath, resultcolorRLF);
                        JOptionPane.showMessageDialog(panelCharger, "Fichier enregistré avec succès.");
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(panelCharger, "Erreur lors de l'enregistrement du fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // Vous pouvez choisir de logger l'erreur plutôt que de l'afficher à l'utilisateur
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(panelCharger, "Une erreur inattendue s'est produite : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // logger l'erreur plutôt que de l'afficher à l'utilisateur
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(panelCharger, "Le fichier à télécharger n'a pas été créé.");
        }
    }

    /**
     * Méthode pour initialiser la table d'informations sur le graphe.
     * Configure les colonnes et les lignes de la table avec les valeurs initiales.
     */
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
                        {"Diamètre :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Nb conflits :", " " + " " + 0}
                },
                new String[]{"Informations Graphe :", null}
        );

        tableauInfoGraphe = new JTable(model);

        JButton updateButton = new JButton("Mettre à jour les données");

    }

    /**
     * Méthode pour mettre à jour les données de la table d'informations sur le graphe.
     *
     * @param noeuds       Nombre de noeuds du graphe.
     * @param aretes       Nombre d'arêtes du graphe.
     * @param degre        Degré moyen du graphe.
     * @param composantes  Nombre de composantes du graphe.
     * @param diametre     Diamètre du graphe.
     * @param nbConflit    Nombre de conflits dans la coloration du graphe.
     */
    public void updateTableData(int noeuds, int aretes, double degre, int composantes, int diametre, int nbConflit) {
        model.setValueAt(" " + " " + noeuds, 2, 1);
        model.setValueAt(" " + " " + aretes, 4, 1);
        model.setValueAt(" " + " " + degre, 6, 1);
        model.setValueAt(" " + " " + composantes, 8, 1);
        model.setValueAt(" " + " " + diametre, 10, 1);
        model.setValueAt(" " + " " + nbConflit, 12, 1);
    }

    /**
     * Méthode pour définir les propriétés du bouton d'accueil.
     */
    private void decoBoutonAcceuil(){
        boutonAccueil = new RoundedButton("Accueil", 25);
        boutonAccueil.setFocusable(false);
        boutonAccueil.setFont(new Font("Lucida Sans",Font.PLAIN,15));
        boutonAccueil.setBackground(couleurPrincipale);
    }

    /**
     * Méthode pour définir les propriétés de la table d'informations sur le graphe.
     */
    private void decoTab(){
        tableauInfoGraphe.setGridColor(Color.DARK_GRAY);
        tableauInfoGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,18));
        tableauInfoGraphe.setForeground(Color.WHITE);
        tableauInfoGraphe.setBackground(Color.decode("#696767"));
        tableauInfoGraphe.setShowVerticalLines(true);
        tableauInfoGraphe.setShowHorizontalLines(false);
        tableauInfoGraphe.setPreferredSize(new Dimension(260, 350));
        tableauInfoGraphe.setRowHeight(tableauInfoGraphe.getRowHeight() + 10);
        tableauInfoGraphe.setRowMargin(5);
        tableauInfoGraphe.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableauInfoGraphe.setEnabled(false);
        TableColumn colonneValeur = tableauInfoGraphe.getColumnModel().getColumn(1);
        colonneValeur.setPreferredWidth(125);
    }

    /**
     * Méthode pour définir les propriétés du bouton de coloration du graphe.
     */
    private void decoBoutonColoration(){
        boutonColoration = new RoundedButton("Effectuer la coloration", 50);
        boutonColoration.setFocusable(false);
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonColoration.setPreferredSize(new Dimension(280, 50));
        boutonColoration.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonColoration.setBackground(couleurTertiaire);
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelCharger, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else if (DsaturBouton.isSelected()){
                    resultcolorWelsh = null;
                    resultcolorRLF = null;
                    if (resultcolorDsat != null){
                        JOptionPane.showMessageDialog(panelCharger, "Coloration déjà effectuée avec cet algorithme !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        resultcolorDsat = dsaturColoring(graph.getGraph(), kmax);
                        statGrapheDsat();
                        updateTableData(noeuds, aretes, degre, composantes, diametre, nbConflit);
                    }
                }
                else if (WelshBouton.isSelected()) {
                    resultcolorDsat = null;
                    resultcolorRLF = null;
                    if (resultcolorWelsh != null){
                        JOptionPane.showMessageDialog(panelCharger, "Coloration déjà effectuée avec cet algorithme !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        resultcolorWelsh = graphWelsh.colorierNoeudsWelsh(kmax);
                        statGrapheWelsh();
                        updateTableData(noeuds, aretes, degre, composantes, diametre, nbConflit);
                    }
                }
                else if (RLFBouton.isSelected()){
                    resultcolorDsat = null;
                    resultcolorWelsh = null;
                    if (resultcolorRLF != null){
                        JOptionPane.showMessageDialog(panelCharger, "Coloration déjà effectuée avec cet algorithme !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        RLF rlf = new RLF(graph.getGraph(),graph.getGraph().getAttribute("kmax"));
                        rlf.colorGraph();
                        resultcolorRLF = rlf.getNodeColors();
                        nbConflit = graph.CompterConflits(graph.getGraph());
                        statGrapheWelsh();
                        updateTableData(noeuds, aretes, degre, composantes, diametre, nbConflit);
                    }

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

    /**
     * Méthode pour définir les propriétés du bouton pour l'algorithme Welsh-Powell.
     */
    private void decoBoutonWelsh() {
        WelshBouton = new JRadioButton("Welsh & Powell");
        WelshBouton.setForeground(Color.WHITE);
        WelshBouton.setFocusable(false);
        WelshBouton.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
        WelshBouton.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        WelshBouton.setBackground(Color.decode("#696767"));
        WelshBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        WelshBouton.setSelected(true);
    }

    /**
     * Méthode pour définir les propriétés du bouton pour l'algorithme D-Satur.
     */
    private void decoBoutonDsatur() {
        DsaturBouton = new JRadioButton("D-Satur");
        DsaturBouton.setForeground(Color.WHITE);
        DsaturBouton.setFocusable(false);
        DsaturBouton.setFont(new Font("Lucida Sans", Font.PLAIN, 19));
        DsaturBouton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        DsaturBouton.setBackground(Color.decode("#696767"));
        DsaturBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Méthode pour définir les propriétés du bouton pour l'algorithme RFL.
     */
    private void decoBoutonRFL() {
        RLFBouton = new JRadioButton("RFL");
        RLFBouton.setForeground(Color.WHITE);
        RLFBouton.setFocusable(false);
        RLFBouton.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
        RLFBouton.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        RLFBouton.setBackground(Color.decode("#696767"));
        RLFBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Méthode pour configurer et ajouter des écouteurs au bouton d'affichage du graphe.
     */
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
                } else if (DsaturBouton.isSelected()) {
                    vue = graph.getGraph().display();
                    viewerPipe = vue.newViewerPipe();
                    vue.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
                    ajouterGrapheListener(vue);
                }
                else if (WelshBouton.isSelected()){
                    vue = graphWelsh.getGraph().display();
                    viewerPipe = vue.newViewerPipe();
                    ajouterGrapheListener(vue);
                    vue.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
                }
                else if (RLFBouton.isSelected()){
                    vue = graph.getGraph().display();
                    viewerPipe = vue.newViewerPipe();
                    vue.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
                    ajouterGrapheListener(vue);
                }
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

    /**
     * Méthode pour ajouter un écouteur de zoom au Viewer du graphe.
     *
     * @param vue Le Viewer du graphe.
     */
    private void ajouterGrapheListener(Viewer vue){
        vue.getDefaultView().addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoomFactor = e.getWheelRotation() > 0 ? 1.1 : 0.9;
                double zoomPercent = vue.getDefaultView().getCamera().getViewPercent() * zoomFactor;

                // Calculez les coordonnées du point de zoom
                double mouseX = e.getX();
                double mouseY = e.getY();

                // Calculez le nouveau centre de vue en fonction de la position de la souris
                double currentZoom = vue.getDefaultView().getCamera().getViewPercent();
                double centerX = vue.getDefaultView().getCamera().getViewCenter().x;
                double centerY = vue.getDefaultView().getCamera().getViewCenter().y;

                double newX = centerX - ((mouseX / vue.getDefaultView().getWidth()) * (zoomPercent - currentZoom));
                double newY = centerY + (((vue.getDefaultView().getHeight() - mouseY) / vue.getDefaultView().getHeight()) * (zoomPercent - currentZoom));

                // Appliquez le zoom et le nouvel emplacement du centre de vue
                vue.getDefaultView().getCamera().setViewPercent(zoomPercent);
                vue.getDefaultView().getCamera().setViewCenter(newX, newY, 0);
            }
        });

    }

    /**
     * Méthode pour configurer et ajouter des écouteurs au bouton de téléchargement.
     */
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
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelCharger, "Veuillez d'abord charger un fichier de graphe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else if (resultcolorDsat==null && resultcolorWelsh==null){
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



    /**
     * Fonction pour explorer une composante connexe du graphe.
     *
     * @param node    Le noeud de départ pour l'exploration.
     * @param visited Ensemble des noeuds déjà visités.
     */
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

    /**
     * Fonction pour calculer le diamètre du graphe.
     *
     * @param graph Le graphe dont le diamètre est à calculer.
     * @return Le diamètre du graphe.
     */
    private static int calculateDiameter(Graph graph) {
        int diameter = 0;
        for (Node node : graph) {
            int maxDistance = bfs(node);
            diameter = Math.max(diameter, maxDistance);
        }
        return diameter;
    }

    /**
     * Fonction pour effectuer une recherche en largeur (BFS) et retourner la distance maximale.
     *
     * @param start Le noeud de départ pour la recherche en largeur.
     * @return La distance maximale trouvée dans le graphe.
     */
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

    /**
     * Calculer les statistiques du graphe pour l'algorithme DSATUR.
     */
    private void statGrapheDsat(){
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

        //Colorie le graph puis compte le nombre de conflits
        int [] colorationdsat = DsaturAlgorithm.dsaturColoring(graph.getGraph(),kmax);
        DsaturAlgorithm.modifyNodeColors(graph.getGraph(), colorationdsat);
        nbConflit = graph.CompterConflits(graph.getGraph());
    }

    /**
     * Calculer les statistiques du graphe pour l'algorithme Welsh-Powell.
     */
    private void statGrapheWelsh(){
        noeuds = graphWelsh.getGraph().getNodeCount();
        aretes = graphWelsh.getGraph().getEdgeCount();


        // Calcul du degré moyen
        double totalDegree = 0;
        for (Node node : graphWelsh.getGraph()) {
            totalDegree += node.getDegree();
        }
        degre = totalDegree / noeuds;

        // Calcul des composantes connexes
        Set<Node> visited = new HashSet<>();
        composantes = 0;
        for (Node node : graphWelsh.getGraph()) {
            if (!visited.contains(node)) {
                composantes++;
                exploreComponent(node, visited);
            }
        }
        // Calcul du diamètre du graphe
        diametre = calculateDiameter(graphWelsh.getGraph());

        //Récupère le nombre de conflit du graph
        nbConflit = graph.CompterConflits(graphWelsh.getGraph());
    }

    /**
     * Calculer les statistiques du graphe pour l'algorithme RLF.
     */
    private void statGrapheRLF(){
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

        //Récupère le nombre de conflit du graph
        nbConflit = graph.CompterConflits(graph.getGraph());
    }

    /**
     * Obtenir le panneau principal.
     *
     * @return Le panneau principal.
     */
    public JPanel getPanel() {
        return panelCharger;
    }
}