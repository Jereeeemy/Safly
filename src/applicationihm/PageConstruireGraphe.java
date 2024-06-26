package applicationihm;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import collisions.Aeroport;
import collisions.Carte;
import collisions.ExceptionNoFlight;
import collisions.ExceptionOrientation;
import collisions.Vol;
import static collisions.Carte.setTemps_collision;
import coloration.WelshPowell;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

/**
 * Classe de l'application qui propose de construire un graphe d'intersection de vols et affiche une carte de ces vols en France
 */
public class PageConstruireGraphe {
    /**
     * Panel principal de la page de construction de graphe.
     */
    private final JPanel panelConstruire;

    /**
     * Couleur secondaire utilisée dans l'interface.
     * Par défaut : #2C5789 (Bleu foncé).
     */
    private final Color couleurSecondaire = Color.decode("#2C5789");

    /**
     * Couleur tertiaire utilisée dans l'interface.
     * Par défaut : #122A47 (Bleu marine).
     */
    private final Color couleurTertiaire = Color.decode("#122A47");

    /**
     * Fichier d'aéroports sélectionné par l'utilisateur.
     */
    public File selectedFile;

    /**
     * nombre de coloration maximum qu'il est possible d'implémenter
     */
    private int maxkmax=1000000000;
    /**
     * Fichier de vols sélectionné par l'utilisateur.
     */
    public File fichierVols;

    /**
     * Instance de la classe Carte utilisée pour la visualisation du graphe.
     */
    private Carte map;

    /**
     * Instance de l'algorithme de coloration de graphes Welsh-Powell.
     */
    private WelshPowell algowelsh;

    /**
     * Tableau utilisé pour afficher les informations du graphe.
     */
    private JTable tableauInfoGraphe;

    /**
     * Modèle de données utilisé par le tableau d'informations du graphe.
     */
    private DefaultTableModel model;

    /**
     * Constructeur de la page ConstruireGraphe de l'application
     * @param menuPrincipal Fenetre qui va prendre la pageConstruireGraphe est mise en place
     */
    public PageConstruireGraphe(MenuPrincipal menuPrincipal) {

        // Prend le fond d'ecran du menu principal pour garder le meme theme
        Image background = menuPrincipal.getBackgroundImage();

        // peind le panel avec le fond d'ecran
        panelConstruire = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Mise en place des panel pricincipaux
        panelConstruire.setLayout(new BorderLayout(60,45));
        JPanel panelConstruireHaut = new JPanel();
        panelConstruireHaut.setOpaque(false);

        RoundedPanel panelConstruireCentre = new RoundedPanel(40);
        /**
         * Couleur principale utilisée dans l'interface.
         * Par défaut : #D9D9D9 (Gris clair).
         */
        Color couleurPrincipale = Color.decode("#D9D9D9");
        panelConstruireCentre.setBackground(couleurPrincipale);
        panelConstruireCentre.setOpaque(false);
        panelConstruireCentre.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));

        JPanel panelConstruireBas = new JPanel();
        panelConstruireBas.setOpaque(false);

        // Mise en place du bouton d'accueil qui permet de revenir au menu principal
        RoundedButton boutonAccueil = new RoundedButton("Accueil",25);
        boutonAccueil.setFocusable(false);
        boutonAccueil.setFont(new Font("Lucida Sans",Font.PLAIN,15));
        boutonAccueil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonAccueil.setBackground(couleurPrincipale);
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

        // Création du centrePanel1 et ses composants
        JPanel centrePanel1 = new JPanel();
        centrePanel1.setMaximumSize(new Dimension(500,500));
        centrePanel1.setBackground(couleurPrincipale);
        centrePanel1.setLayout(new BoxLayout(centrePanel1, BoxLayout.PAGE_AXIS));
        centrePanel1.setBorder(new EmptyBorder(0, 0, 0, 10));

        // Ajout du label d'instructions
        JLabel labelDeposeFichiers = new JLabel("<html><div style='text-align: left'> Pour construire un graphe,<br> veuillez d'abord fournir <br>les fichiers nécessaires</div></html>");
        labelDeposeFichiers.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        labelDeposeFichiers.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        centrePanel1.add(labelDeposeFichiers);

        // Ajout du label pour afficher le nom du fichier sélectionné
        JLabel labelNomFichier = new JLabel();
        labelNomFichier.setBorder(new EmptyBorder(0, 0, 50, 0));

        // Ajout du bouton pour importer une liste d'aéroports
        RoundedButton boutonFichierAeroport = new RoundedButton("Importer une liste d'aéroports", 50);
        boutonFichierAeroport.setFocusable(false);
        boutonFichierAeroport.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonFichierAeroport.setForeground(Color.WHITE);
        boutonFichierAeroport.setMaximumSize(new Dimension(400, 50));
        boutonFichierAeroport.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        boutonFichierAeroport.setBackground(Color.decode("#696767"));
        centrePanel1.add(boutonFichierAeroport);
        centrePanel1.add(labelNomFichier);


        // Ajout du label pour afficher la liste des vols sélectionnée
        JLabel labelListeVol = new JLabel();
        centrePanel1.add(labelListeVol);

        // Ajout du bouton pour importer une liste de vols
        RoundedButton boutonFichierVol = new RoundedButton("<html><div>Importer une liste de vols</div></html>", 50);
        boutonFichierVol.setFocusable(false);
        boutonFichierVol.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonFichierVol.setForeground(Color.WHITE);
        boutonFichierVol.setMaximumSize(new Dimension(400, 50));
        boutonFichierVol.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        boutonFichierVol.setBackground(Color.decode("#696767"));
        centrePanel1.add(boutonFichierVol);

        // Ajout des listeners pour les boutons
        boutonFichierAeroport.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Mise en place d'un JFileChooser pour séléctionner un fichier d'aéroports
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelConstruire);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    try {
                        if (!selectedFile.getName().endsWith(".txt")) {
                            throw new Exception();
                        }
                        // Création d'un objet Carte qui permet de stocker les aéroports et les vols
                        map = new Carte(selectedFile);
                        boutonFichierAeroport.removeMouseListener(this);

                        //confirmation visuelle pour l'utilisateur que le fichier à bien été pris dans l'appli
                        labelNomFichier.setText(fileName);
                        labelNomFichier.setForeground(Color.BLUE);
                        labelNomFichier.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                        boutonFichierAeroport.setForeground(Color.decode("#77E59B"));
                        panelConstruire.revalidate();
                        panelConstruire.repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panelConstruire, "Attention ! votre fichier d'aeroports ne correspond pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boutonFichierAeroport.setBackground(Color.DARK_GRAY);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boutonFichierAeroport.setBackground(Color.decode("#696767"));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                boutonFichierAeroport.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boutonFichierAeroport.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Ajout du Listener pour le bouton de fichier de vols
        boutonFichierVol.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (map == null) {
                    JOptionPane.showMessageDialog(panelConstruire, "Attention ! Veuillez d'abord fournir un fichier d'aéroports", "Erreur", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelConstruire);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    fichierVols = fileChooser.getSelectedFile();
                    String fileName = fichierVols.getName();
                    try {
                        if ((fileName.toLowerCase().startsWith("aeroports")) || (fileName.toLowerCase().startsWith("aéroports"))
                                || (fileName.toLowerCase().startsWith("aeroport")) || (fileName.toLowerCase().startsWith("aéroport"))) {
                            throw new Exception();
                        }
                        // Création du graphe de collisions avec les vols grace au fichier de vol
                        map.setGraph_vol(new SingleGraph("Collisions_carte_"+fileName));
                        map.setListe_vols(fichierVols);

                        //confirmation visuelle pour l'utilisateur que le fichier à bien été pris dans l'appli
                        labelListeVol.setText(fileName);
                        labelListeVol.setForeground(Color.BLUE);
                        labelListeVol.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                        boutonFichierVol.setForeground(Color.decode("#77E59B"));
                        panelConstruire.revalidate();
                        panelConstruire.repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panelConstruire, "Attention ! votre fichier de vols ne correspond pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                boutonFichierVol.setBackground(Color.DARK_GRAY);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                boutonFichierVol.setBackground(Color.decode("#696767"));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                boutonFichierVol.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boutonFichierVol.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Personnalisation du bouton de fichier de vols
        boutonFichierVol.setFocusable(false);
        boutonFichierVol.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        boutonFichierVol.setForeground(Color.WHITE);
        boutonFichierVol.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonFichierVol.setBackground(Color.decode("#696767"));
        boutonFichierVol.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonFichierVol.setMaximumSize(new Dimension(400,50));

        centrePanel1.add(boutonFichierVol);
        centrePanel1.add(labelListeVol);

        // Création du panel central2 qui se situe au centre
        JPanel centrePanel2 = new JPanel();
        centrePanel2.setBackground(couleurPrincipale);
        centrePanel2.setLayout(new GridBagLayout());
        centrePanel2.setBorder(new EmptyBorder(0, 20, 0, 0));

        // Ajout du GridBagLayout afin d'aligner correctement les composants
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mise en place du label qui explique la création de graphe de gestion de conflits
        JLabel labelChoixParam = new JLabel("<html><div style='text-align: left'> Après avoir chargé vos fichiers,<br> vous pouvez personnaliser <br>la gestion des conflits : </div></html>");
        labelChoixParam.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        labelChoixParam.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        centrePanel2.add(labelChoixParam, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Mise en place du panel central qui permet de choisir l'intervalle de sécurité entre les vols
        RoundedPanel miniPanelCentral = new RoundedPanel(25);
        miniPanelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        miniPanelCentral.setLayout(new BoxLayout(miniPanelCentral, BoxLayout.PAGE_AXIS));
        miniPanelCentral.setBackground(Color.decode("#696767"));


        JLabel labelChoixKmax = new JLabel("Choix de la marge de sécurité :");
        labelChoixKmax.setForeground(Color.WHITE);
        labelChoixKmax.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
        miniPanelCentral.add(labelChoixKmax);

        // Mise en place du spinner pour choisir l'intervalle de sécurité des vols
        JSpinner SpinnerMargeSecu = getSpinnerMargeSecu();
        SpinnerMargeSecu.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setTemps_collision((int) SpinnerMargeSecu.getValue()) ;
            }
        });

        miniPanelCentral.add(SpinnerMargeSecu);
        centrePanel2.add(miniPanelCentral, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 0, 0, 0);

        // Mise en place du bouton Coloration qui permet d'afficher le graphe d'intersection des vols colorié
        RoundedButton boutonColoration = new RoundedButton("Afficher le graphe d'intersection", 80);
        boutonColoration.setFocusable(false);
        boutonColoration.setFont(new Font("Lucida Sans", Font.PLAIN, 22));
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setPreferredSize(new Dimension(400, 80));
        boutonColoration.setBackground(couleurTertiaire);
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int nbChroma = 0;
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelConstruire, "Veuillez d'abord charger un fichier de vols.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    map = new Carte(selectedFile, fichierVols);
                    // Coloration du graphe d'intersection
                    map.RechercheCollision();
                    algowelsh = new WelshPowell(map.getGraph_vol());
                    algowelsh.colorierNoeudsWelsh(1000000000);
                    nbChroma = algowelsh.CompteCouleurs();
                    map.setGraph_vol(algowelsh.getGraph());

                    // Mise à jour du tableau avec les nouvelles informations
                    updateTableData(map.getGraph_vol().getNodeCount(), map.getGraph_vol().getEdgeCount(), nbChroma);

                    // Visualisation du graphe avec un Viewer
                    Viewer vue = map.getGraph_vol().display();
                    ajouterGrapheListener(vue);
                    vue.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
                } catch (IOException | ExceptionNoFlight | ExceptionOrientation |NullPointerException  ex ) {
                    JOptionPane.showMessageDialog(panelConstruire, "Veuillez d'abord charger un fichier de vols.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

        centrePanel2.add(boutonColoration, gbc);
        centrePanel2.setBorder(new EmptyBorder(0,10,0,10));


        JPanel centrePanel3 = new JPanel();
        centrePanel3.setBackground(couleurPrincipale);

        // Mise en place du tableau qui donne divers informations sur le graphe de collisions
        initializeTable();
        tableauInfoGraphe.setGridColor(Color.DARK_GRAY);
        tableauInfoGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,18));
        tableauInfoGraphe.setForeground(Color.WHITE);
        tableauInfoGraphe.setBackground(Color.decode("#696767"));
        tableauInfoGraphe.setShowVerticalLines(true);
        tableauInfoGraphe.setShowHorizontalLines(false);
        tableauInfoGraphe.setPreferredSize(new Dimension(260, 200));
        tableauInfoGraphe.setRowHeight(tableauInfoGraphe.getRowHeight() + 10);
        tableauInfoGraphe.setRowMargin(5);
        tableauInfoGraphe.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableauInfoGraphe.setEnabled(false);
        TableColumn colonneValeur = tableauInfoGraphe.getColumnModel().getColumn(1);
        colonneValeur.setPreferredWidth(125);

        centrePanel3.add(tableauInfoGraphe);
        centrePanel3.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));


        centrePanel3.add(tableauInfoGraphe);
        JSeparator sepa1 = new JSeparator(SwingConstants.VERTICAL);
        sepa1.setBackground(Color.GRAY);
        JSeparator sepa2 = new JSeparator(SwingConstants.VERTICAL);
        sepa2.setBackground(Color.GRAY);

        // Ajout de tout les panels centraux utilisés dans le panel central principal
        panelConstruireCentre.add(centrePanel1);
        panelConstruireCentre.add(sepa1);
        panelConstruireCentre.add(centrePanel2);
        panelConstruireCentre.add(sepa2);
        panelConstruireCentre.add(centrePanel3);

        // Ajout du bouton qui permet d'afficher la carte de la France
        RoundedButton boutonAfficherGraphe = new RoundedButton("Afficher la carte",90);
        boutonAfficherGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fichierVols != null) {
                    demanderAffichageParHeure();
                }
                else{
                    afficherCarte();
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

        // Décoration du bouton afficherGraphe
        boutonAfficherGraphe.setFocusable(false);
        boutonAfficherGraphe.setForeground(Color.WHITE);
        boutonAfficherGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,30));
        boutonAfficherGraphe.setPreferredSize(new Dimension(320,80));
        boutonAfficherGraphe.setBackground(couleurTertiaire);
        panelConstruireBas.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        panelConstruireBas.add(boutonAfficherGraphe);

        JPanel panelgauche = new JPanel();
        panelgauche.setOpaque(false);
        JPanel paneldroite = new JPanel();
        paneldroite.setOpaque(false);

        // Ajout de tout les panels principaux dans panelConstruire pour avoir l'entiereté de la page regroupée
        panelConstruire.add(panelConstruireHaut,BorderLayout.NORTH);
        panelConstruire.add(panelgauche,BorderLayout.WEST);
        panelConstruire.add(panelConstruireCentre,BorderLayout.CENTER);
        panelConstruire.add(paneldroite,BorderLayout.EAST);
        panelConstruire.add(panelConstruireBas, BorderLayout.SOUTH);
    }

    /**
     * Méthode qui ajoute un Listener au viewer pour pouvoir zoomer sur le graphe
     * @param vue Viewer qui prend le graphe en cours afin de l'afficher
     */
    private void ajouterGrapheListener(Viewer vue) {
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
     * Getter qui permet d'avoir le spinner qui peut changer la valeur de la marge de sécurité entre les vols
     * @return le SpinnerMargeSecu qui est mis en place
     */
    private static JSpinner getSpinnerMargeSecu() {
        JSpinner SpinnerMargeSecu = new JSpinner();
        SpinnerMargeSecu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        SpinnerMargeSecu.setModel(new SpinnerNumberModel(15,1,120,1));
        SpinnerMargeSecu.setFocusable(false);
        SpinnerMargeSecu.setFont(new Font("Lucida Sans",Font.PLAIN,18));

        Dimension preferredSize = new Dimension(60, 40);
        SpinnerMargeSecu.setPreferredSize(preferredSize);
        SpinnerMargeSecu.setMaximumSize(preferredSize);
        JComponent editor = SpinnerMargeSecu.getEditor();
        if (editor instanceof JSpinner.DefaultEditor){
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setEditable(false);
            textField.setBackground(Color.LIGHT_GRAY);
        }
        SpinnerMargeSecu.setBackground(Color.decode("#696767"));
        return SpinnerMargeSecu;
    }

    /**
     * Methode qui permet d'afficher une carte de la France avec diverses informations et selon certain critères ( aéroports, vols )
     */
    private void afficherCarte() {
        JFrame fenetreCarte = initFenetre();
        JXMapViewer mapViewer = initMapViewer();
        fenetreCarte.getContentPane().add(mapViewer);
        fenetreCarte.setVisible(true);
        ajouterEcouteurs(mapViewer);

        if (map != null) {
            if (map.getListe_vols() == null) {
                // Affiche seulement les aéroports si aucun fichier de vol n'a été fourni
                WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
                Set<DefaultWaypoint> airportWaypoints = chargerAeroports();
                waypointPainter.setWaypoints(airportWaypoints);
                List<Painter<JXMapViewer>> painters = new ArrayList<>();
                painters.add(waypointPainter);
                CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
                mapViewer.setOverlayPainter(compoundPainter);
            } else {
                // si un fichier de vol à été fourni, affiche seulement les aéroports qui on un vol et leur vol
                Set<DefaultWaypoint> airportWaypoints = chargerAeroports();
                List<GeoPosition[]> routes = chargerRoutes();
                if (algowelsh == null) {
                    map.RechercheCollision();
                    algowelsh = new WelshPowell(map.getGraph_vol());
                    algowelsh.colorierNoeudsWelsh(10000);
                    map.setGraph_vol(algowelsh.getGraph());
                }
                else {
                    algowelsh = new WelshPowell(map.getGraph_vol());
                    algowelsh.colorierNoeudsWelsh(10000);
                    map.setGraph_vol(algowelsh.getGraph());
                }
                airportWaypoints = filtrerAeroportsAvecVols(airportWaypoints, routes);
                afficherMarqueursEtRoutes(mapViewer, airportWaypoints, routes);
            }
        }
    }

    /**
     * Initialise la fenetre qui va permettre de voir la carte de la France
     * @return La JFrame mise en place
     */
    private JFrame initFenetre() {
        JFrame fenetreCarte = new JFrame();
        fenetreCarte.setTitle("Carte de la France");
        fenetreCarte.setSize(800, 600);
        fenetreCarte.setLocationRelativeTo(null);
        fenetreCarte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return fenetreCarte;
    }

    /**
     * Initialise la Carte de la France grace à la librairies JXMapViewer
     * @return la carte de la france initialisée
     */
    private JXMapViewer initMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new TileFactoryInfo(1, 17, 11, 256, true, true,
                "http://tile.openstreetmap.org", "x", "y", "z") {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                int invZoom = getTotalMapZoom() - zoom;
                return this.baseURL + "/" + invZoom + "/" + x + "/" + y + ".png";
            }
        };
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        GeoPosition france = new GeoPosition(46.603354, 1.888334);
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(france);
        mapViewer.setToolTipText(""); // Enable tooltips
        return mapViewer;
    }

    /**
     * Methode qui permet de charger les aéroports à partir de la liste d'aéroports de l'objet Carte map
     * @return un Set de Waypoints qui ont les coordonnées de chaque aéroports
     */
    private Set<DefaultWaypoint> chargerAeroports() {
        Set<DefaultWaypoint> airportWaypoints = new HashSet<>();
        for (Aeroport aeroport : map.getListe_aeroports()) {
            GeoPosition position = new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude());
            airportWaypoints.add(new DefaultWaypoint(position));
        }
        return airportWaypoints;
    }

    /**
     * Méthode qui permet de charger les routes à partir de la liste de vols de l'objet Carte map
     * @return un List de coordonnées qui correspond à chaque vol
     */
    private List<GeoPosition[]> chargerRoutes() {
        List<GeoPosition[]> routes = new ArrayList<>();

        if (map.getGraph_vol() != null) {
            for (Vol vol : map.getListe_vols()) {
                Aeroport depart = vol.getDepart();
                Aeroport arrivee = vol.getArrivee();
                GeoPosition startPos = new GeoPosition(depart.getLatitude(), depart.getLongitude());
                GeoPosition endPos = new GeoPosition(arrivee.getLatitude(), arrivee.getLongitude());


                routes.add(new GeoPosition[]{startPos, endPos});
            }
        }
        return routes;
    }

    /**
     * Methode qui permet de charger les routes à partir de la liste de Vol fournie en parametres
     * @param volsFiltres Liste de Vol qui correspond aux vols filtré par l'heure choisie
     * @return une List de coordonnées qui correspond à chaque vol filtré
     */
    private List<GeoPosition[]> chargerRoutes(List<Vol> volsFiltres) {
        List<GeoPosition[]> routes = new ArrayList<>();

        if (map.getGraph_vol() != null) {
            for (Vol vol : volsFiltres) {
                Aeroport depart = vol.getDepart();
                Aeroport arrivee = vol.getArrivee();
                GeoPosition startPos = new GeoPosition(depart.getLatitude(), depart.getLongitude());
                GeoPosition endPos = new GeoPosition(arrivee.getLatitude(), arrivee.getLongitude());

                routes.add(new GeoPosition[]{startPos, endPos});
            }
        }
        return routes;
    }

    /**
     *  Methode qui permet de filtrer les aéroports qui ont un vol correspondant
     * @param airportWaypoints Set de Waypoints qui correspond aux Waypoint de chaque aéroport
     * @param routes List de coordonnées qui sont des routes ( vols )
     * @return le Set de Waypoints des aéroports qui ont un vol correspondant
     */
    private Set<DefaultWaypoint> filtrerAeroportsAvecVols(Set<DefaultWaypoint> airportWaypoints,List<GeoPosition[]> routes) {
        Set<GeoPosition> aeroportsAvecVols = new HashSet<>();
        for (GeoPosition[] route : routes) {
            aeroportsAvecVols.add(route[0]);
            aeroportsAvecVols.add(route[1]);
        }

        Set<DefaultWaypoint> filteredWaypoints = new HashSet<>();
        for (DefaultWaypoint waypoint : airportWaypoints) {
            if (aeroportsAvecVols.contains(waypoint.getPosition())) {
                filteredWaypoints.add(waypoint);
            }
        }
        return filteredWaypoints;
    }

    /**
     * Methode qui permet de dessiner et afficher les différent aéroports et vols sur la carte de la France
     * @param mapViewer Carte de ka France
     * @param airportWaypoints Set de Waypoint qui correspond aux aéroports
     * @param routes List de Coordonnées qui correspond aux vols
     */
    private void afficherMarqueursEtRoutes(JXMapViewer mapViewer, Set<DefaultWaypoint> airportWaypoints, List<GeoPosition[]> routes) {
        WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(airportWaypoints);

        Painter<JXMapViewer> lineOverlay = new Painter<JXMapViewer>() {
            @Override
            public void paint(Graphics2D g, final JXMapViewer jxmap, final int w, final int h) {
                g = (Graphics2D) g.create();
                final Rectangle rect = mapViewer.getViewportBounds();
                g.translate(-rect.x, -rect.y);

                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(1)); // Épaissir le trait pour une meilleure visibilité

                for (GeoPosition[] route : routes) {
                    Point2D pt1 = jxmap.getTileFactory().geoToPixel(route[0], jxmap.getZoom());
                    Point2D pt2 = jxmap.getTileFactory().geoToPixel(route[1], jxmap.getZoom());

                    g.setColor(Color.BLACK);
                    g.drawLine((int) pt1.getX(), (int) pt1.getY(), (int) pt2.getX(), (int) pt2.getY());
                }
                g.dispose();
            }
        };

        // Ajout des trait de vols et des Waypoints pour les afficher sur la carte
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(waypointPainter);
        painters.add(lineOverlay);
        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(compoundPainter);
    }

    /**
     * Methode qui crée un JOptionPane afin de laisser le choix à l'utilisateur d'afficher tout les vols du fichier
     * ou de les afficher pour une heure précise
     */
    private void demanderAffichageParHeure() {
        // Mise en place de la JOptionPane pour laisser le choix à l'utilisateur
        Object[] options = {"Tous les vols", "Filtrer par heure"};
        int choix = JOptionPane.showOptionDialog(
                null,
                "Voulez-vous afficher tous les vols ou filtrer par heure?",
                "Choisir l'option d'affichage",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choix == 1) {
            // si l'utilisateur choisi d'afficher les vols par heure
            String heureStr = JOptionPane.showInputDialog(null, "Entrez l'heure (HH):");
            if (heureStr != null && !heureStr.trim().isEmpty()) {
                try {
                    double heure = Double.parseDouble(heureStr);
                    if (heure>=0 && heure < 24) {
                        afficherCarteFiltreParHeure(heure);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "L'heure doit être comprise entre 0 et 23 inclus. Veuillez entrer une heure valide.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Format de l'heure invalide. Veuillez entrer une heure au format HH.");
                }
            }
        } else if (choix == 0){
            // Si l'utilisateur choisi d'afficher tout les vols du fichier de vols
            afficherCarte();
        }
    }


    /**
     * Methode qui permet d'afficher une carte de la France avec des aéroports et des vols
     * qui sont affichés en fonction de l'heure choisie par l'utilisateur
     *
     * @param heure Valeur de l'heure choisie par l'utilisateur
     */
    private void afficherCarteFiltreParHeure(double heure) {
        List<Vol> volsFiltres = filtrerVolsParHeure(heure);

        JFrame fenetreCarte = initFenetre();
        JXMapViewer mapViewer = initMapViewer();
        fenetreCarte.getContentPane().add(mapViewer);
        fenetreCarte.setVisible(true);
        ajouterEcouteurs(mapViewer);

        if (map != null) {
            if (volsFiltres.isEmpty()) {
                WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
                Set<DefaultWaypoint> airportWaypoints = chargerAeroports();
                waypointPainter.setWaypoints(airportWaypoints);
                List<Painter<JXMapViewer>> painters = new ArrayList<>();
                painters.add(waypointPainter);
                CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
                mapViewer.setOverlayPainter(compoundPainter);
            } else {
                Set<DefaultWaypoint> airportWaypoints = chargerAeroports();
                List<GeoPosition[]> routes = chargerRoutes(volsFiltres);
                if (algowelsh == null) {
                    map.RechercheCollision();
                    algowelsh = new WelshPowell(map.getGraph_vol());
                    algowelsh.colorierNoeudsWelsh(maxkmax);
                    map.setGraph_vol(algowelsh.getGraph());
                }
                else {
                    algowelsh = new WelshPowell(map.getGraph_vol());
                    algowelsh.colorierNoeudsWelsh(maxkmax);
                    map.setGraph_vol(algowelsh.getGraph());
                }

                airportWaypoints = filtrerAeroportsAvecVols(airportWaypoints, routes);
                afficherMarqueursEtRoutes(mapViewer, airportWaypoints, routes);
            }
        }
    }

    /**
     * Methode qui filtre les vols en fonction de l'heure qui est donnée en parametre
     * @param heure heure de vol que l'utilisateur veut voir sur la carte
     * @return List de vols qui correspondent à l'heure souhaitée
     */
    private List<Vol> filtrerVolsParHeure(double heure) {
        List<Vol> volsFiltres = new ArrayList<>();
        for (Vol vol : map.getListe_vols()) {
            double heureDepart = vol.getHeure_depart();
            double heureArrivee = vol.getHeure_arrivee();
            if (heureDepart <= heure && heureArrivee >= heure) {
                volsFiltres.add(vol);
            }
        }
        return volsFiltres;
    }


    /**
     * Methode qui ajoute les différents Listeners à l'objet mapViewer
     * @param mapViewer Carte de la France faite avec JXMapViewer
     */
    private void ajouterEcouteurs(JXMapViewer mapViewer) {
        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        // Popup to show airport information
        JPopupMenu popup = new JPopupMenu();
        JLabel label = new JLabel();
        popup.add(label);

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GeoPosition geoPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                if (fichierVols!= null){
                    String info = getAirportInfoForPosition(geoPosition, mapViewer.getZoom());
                    if (info != null) {
                        label.setText(info);
                        popup.show(mapViewer, e.getX(), e.getY());
                    }
                }
            }
        });

        // Ajout d'un MouseMotionListener pour afficher des infos sur l'aéroport
        mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (map != null) {
                    GeoPosition geoPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                    String tooltipText = getTooltipTextForPosition(geoPosition, mapViewer.getZoom());
                    mapViewer.setToolTipText(tooltipText);
                }
            }
        });
    }

    /**
     * Methode qui retourne le texte de l'infobulle pour une position géographique donnée et un niveau de zoom.
     * Si la position est proche d'un aéroport, l'infobulle contiendra le nom de la ville de l'aéroport.
     *
     * @param position la position géographique à vérifier.
     * @param zoomLevel le niveau de zoom actuel de la carte.
     * @return une chaîne contenant le nom de la ville de l'aéroport si la position est proche d'un aéroport, sinon null.
     */
    private String getTooltipTextForPosition(GeoPosition position, int zoomLevel) {
        for (Aeroport aeroport : map.getListe_aeroports()) {
            GeoPosition airportPosition = new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude());
            if (isPositionClose(position, airportPosition, zoomLevel)) {
                return "Aéroport: " + aeroport.getVille();
            }
        }
        return null;
    }

    /**
     * Méthode qui retourne les informations sur l'aéroport pour une position géographique donnée et un niveau de zoom.
     * Si la position est proche d'un aéroport, les informations contiendront le nom de la ville de l'aéroport.
     *
     * @param position la position géographique à vérifier.
     * @param zoomLevel le niveau de zoom actuel de la carte.
     * @return une chaîne contenant le nom de la ville de l'aéroport si la position est proche d'un aéroport, sinon null.
     */
    private String getAirportInfoForPosition(GeoPosition position, int zoomLevel) {
        for (Aeroport aeroport : map.getListe_aeroports()) {
            GeoPosition airportPosition = new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude());
            if (isPositionClose(position, airportPosition, zoomLevel)) {
                return "Aéroport: " + aeroport.getVille();
            }
        }
        return null;
    }

    /**
     * Méthode qui vérifie si deux positions géographiques sont proches l'une de l'autre en fonction du niveau de zoom.
     * La proximité est déterminée en comparant la différence de latitude et de longitude des deux positions
     * avec un seuil ajusté basé sur le niveau de zoom.
     *
     * @param pos1 la première position géographique.
     * @param pos2 la deuxième position géographique.
     * @param zoomLevel le niveau de zoom actuel de la carte.
     * @return true si les deux positions sont considérées comme proches, sinon false.
     */
    private boolean isPositionClose(GeoPosition pos1, GeoPosition pos2, int zoomLevel) {
        double baseThreshold = 0.2; // Valeur seuil de base
        double adjustedThreshold = baseThreshold * (5.0 / (zoomLevel + 5)); // Ajuster le seuil en fonction du niveau de zoom
        double latDiff = Math.abs(pos1.getLatitude() - pos2.getLatitude());
        double lonDiff = Math.abs(pos1.getLongitude() - pos2.getLongitude());
        return latDiff < adjustedThreshold && lonDiff < adjustedThreshold;
    }


    /**
     * Methode qui initialise le tableau avec un modele et qui l'applique au tableau de la page
     */
    private void initializeTable() {
        model = new DefaultTableModel(
                new Object[][]{
                        {"Informations : ", null},
                        {null, null},
                        {"Nb Vol :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Nb Conflits :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Niveaux vol :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                },
                new String[]{"Informations Graphe :", null}
        );
        tableauInfoGraphe = new JTable(model);
    }

    /**
     * Méthode qui met à jour les données du tableau avec les differents parametres
     * @param noeuds nombre de noeuds du graphe, qui correspond au nombre de vols
     * @param aretes nombre d'aretes du graphe, qui correspond au nombre de collisions
     * @param nbChroma Nombre chromatique du graphe, qui correspond au niveau maximal de vol
     */
    public void updateTableData(int noeuds, int aretes, double nbChroma) {
        model.setValueAt(" " + " " + noeuds, 2, 1);
        model.setValueAt(" " + " " + aretes, 4, 1);
        model.setValueAt(" " + " " + nbChroma, 6, 1);
    }

    /**
     * Methode qui permet de return le panelConstruire
     * @return le panelConstruire
     */
    public JPanel getPanel() {
        return panelConstruire;
    }
}