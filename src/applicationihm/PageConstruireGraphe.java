package applicationihm;

import collisions.Aeroport;
import collisions.Carte;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.List;
import java.util.Set;

import collisions.Vol;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.util.*;


public class PageConstruireGraphe {
    private final JPanel panelConstruire;
    public File selectedFile;

    public File fichierVols;

    private Carte map;

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
        boutonFichierAeroport.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelConstruire);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    try {
                        // Initialiser la carte avec la liste des aéroports
                        map = new Carte(selectedFile);

                        // Debug: vérifier les aéroports chargés
                        for (Aeroport aeroport : map.getListe_aeroports()) {
                            System.out.println(aeroport.getCode() + " - " + aeroport.getVille() + " - " + aeroport.getLatitude() + " - " + aeroport.getLongitude());
                        }

                        labelNomFichier.setText(fileName);
                        labelNomFichier.setForeground(Color.BLUE);
                        labelNomFichier.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                        boutonFichierAeroport.setForeground(Color.decode("#77E59B"));
                        panelConstruire.revalidate();
                        panelConstruire.repaint();
                    } catch (Exception ex) {
                        // Afficher un message d'erreur si le fichier ne peut pas être lu
                        JOptionPane.showMessageDialog(panelConstruire, "Attention ! votre fichier d'aeroports ne correspond pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
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

        boutonFichierAeroport.setFocusable(false);
        boutonFichierAeroport.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonFichierAeroport.setForeground(Color.WHITE);
        boutonFichierAeroport.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        boutonFichierAeroport.setBackground(Color.decode("#696767"));
        centrePanel1.add(boutonFichierAeroport);
        centrePanel1.add(labelNomFichier);
        RoundedButton boutonFichierVol = new RoundedButton("Importer une liste de vols",50);

        boutonFichierVol.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelConstruire);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    fichierVols = fileChooser.getSelectedFile();
                    String fileName = fichierVols.getName();
                    try {

                        map.setListe_vols(fichierVols);

                        labelListeVol.setText(fileName);
                        labelListeVol.setForeground(Color.BLUE);
                        labelListeVol.setFont(new Font("Lucida Sans", Font.ITALIC, 20));
                        boutonFichierVol.setForeground(Color.decode("#77E59B"));
                        panelConstruire.revalidate();
                        panelConstruire.repaint();
                    } catch (Exception ex) {
                        // Afficher un message d'erreur si le fichier ne peut pas être lu
                        JOptionPane.showMessageDialog(panelConstruire, "Attention ! votre fichier de vols ne correspond pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
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
        boutonColoration.setBackground(Color.decode("#122A47"));
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelConstruire, "Veuillez d'abord charger un fichier de vols.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
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
                        {"Noeuds :", " "+ " "+ 280},
                        {"",null},
                        {"Arêtes :", " "+ " "+ 7},
                        {"",null},
                        {"Degré Moyen :", " "+ " "+ 6},
                        {"",null},
                        {"Nb Composantes :", " "+ " "+ 12},
                        {"",null},
                        {"Diamètre", " "+ " "+ 6},
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

        boutonAfficherGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                afficherCarte();

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
        boutonAfficherGraphe.setForeground(Color.WHITE);
        boutonAfficherGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,30));
        boutonAfficherGraphe.setPreferredSize(new Dimension(320,80));
        boutonAfficherGraphe.setBackground(Color.decode("#122A47"));
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

    private void initCarteFrance(){

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

    private void afficherCarte() {
        JFrame fenetreCarte = new JFrame();
        fenetreCarte.setTitle("Carte de la France");
        fenetreCarte.setSize(800, 600);
        fenetreCarte.setLocationRelativeTo(null);
        fenetreCarte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Créer un JXMapViewer
        JXMapViewer mapViewer = new JXMapViewer();

        // Définir le fournisseur de tuiles (TileFactory) pour le JXMapViewer
        TileFactoryInfo info = new TileFactoryInfo(1, 12, 11,
                256, true, true,
                "http://tile.openstreetmap.org",
                "x", "y", "z") {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                int invZoom = getTotalMapZoom() - zoom;
                return this.baseURL + "/" + invZoom + "/" + x + "/" + y + ".png";
            }
        };
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Centrer la carte sur la France
        GeoPosition france = new GeoPosition(46.603354, 1.888334);
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(france);

        // Ajouter les marqueurs des aéroports
        Set<DefaultWaypoint> airportWaypoints = new HashSet<>();
        if (map != null) {
            for (Aeroport aeroport : map.getListe_aeroports()) {
                GeoPosition position = new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude());
                airportWaypoints.add(new DefaultWaypoint(position));
            }
        }

// Créer un WaypointPainter et ajouter les Waypoints
        WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(airportWaypoints);

// Liste des routes entre aéroports pour les dessiner
        List<Route> routes = new ArrayList<>();
        if (map != null) {
            for (Vol vol : map.getListe_vols()) {
                Aeroport depart = vol.getDepart();
                Aeroport arrivee = vol.getArrivee();
                GeoPosition startPos = new GeoPosition(depart.getLatitude(), depart.getLongitude());
                GeoPosition endPos = new GeoPosition(arrivee.getLatitude(), arrivee.getLongitude());
                routes.add(new Route(startPos, endPos));
            }
        }

// Créer un Painter pour dessiner les routes
        Painter<JXMapViewer> routePainter = new Painter<JXMapViewer>() {
            @Override
            public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
                // Set the color and stroke for the routes
                g.setColor(Color.RED);
                g.setStroke(new BasicStroke(2));

                for (Route route : routes) {
                    GeoPosition start = route.getStart();
                    GeoPosition end = route.getEnd();

                    Point2D startPt = map.getTileFactory().geoToPixel(start, map.getZoom());
                    Point2D endPt = map.getTileFactory().geoToPixel(end, map.getZoom());

                    Path2D path = new Path2D.Double();
                    path.moveTo(startPt.getX(), startPt.getY());
                    path.lineTo(endPt.getX(), endPt.getY());

                    g.draw(path);
                }
            }
        };

// Ajouter les painters au JXMapViewer
        mapViewer.setOverlayPainter(new CompoundPainter<>());


        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);

        // Écouteur pour recentrer la carte sur un clic
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        // Écouteur pour zoomer avec la molette de la souris
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        // Optionnel : écouteur pour afficher la position géographique en cliquant
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GeoPosition geoPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                System.out.println("Position : " + geoPosition.getLatitude() + ", " + geoPosition.getLongitude());
            }
        });



        // Ajouter les marqueurs à la carte
        mapViewer.setOverlayPainter(waypointPainter);

        // Ajouter le JXMapViewer à la fenêtre
        fenetreCarte.getContentPane().add(mapViewer);
        fenetreCarte.setVisible(true);
    }



    public JPanel getPanel() {
        return panelConstruire;
    }
}