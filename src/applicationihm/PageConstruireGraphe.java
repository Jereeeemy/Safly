package applicationihm;

import collisions.Aeroport;
import collisions.Carte;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;
import javax.swing.*;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.io.File;
import java.util.List;

import collisions.Vol;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.util.*;


public class PageConstruireGraphe {
    private final JPanel panelConstruire;
    private Color couleurPrincipale = Color.decode("#D9D9D9");
    private Color couleurSecondaire = Color.decode("#2C5789");
    private Color couleurTertiaire = Color.decode("#122A47");
    public File selectedFile;

    public File fichierVols;

    private Carte map;
    private JTable tableauInfoGraphe;
    private DefaultTableModel model;

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
        panelConstruireCentre.setBackground(couleurPrincipale);
        panelConstruireCentre.setOpaque(false);
        panelConstruireCentre.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));

        JPanel panelConstruireBas = new JPanel();
        panelConstruireBas.setOpaque(false);

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
        labelDeposeFichiers.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
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
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(panelConstruire);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    try {
                        map = new Carte(selectedFile);
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
                        map.setListe_vols(fichierVols);
                        for (Vol vol : map.getListe_vols()) {
                            System.out.println(vol.getCode() + " " + vol.getDepart() + " " + vol.getArrivee());
                        }
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


        boutonFichierVol.setFocusable(false);
        boutonFichierVol.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        boutonFichierVol.setForeground(Color.WHITE);
        boutonFichierVol.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonFichierVol.setBackground(Color.decode("#696767"));
        boutonFichierVol.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonFichierVol.setMaximumSize(new Dimension(400,50));

        centrePanel1.add(boutonFichierVol);
        centrePanel1.add(labelListeVol);

        JPanel centrePanel2 = new JPanel();
        centrePanel2.setBackground(couleurPrincipale);
        centrePanel2.setLayout(new GridBagLayout());
        centrePanel2.setBorder(new EmptyBorder(0, 20, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelChoixParam = new JLabel("<html><div style='text-align: left'> Après avoir chargé votre graphe,<br> vous pouvez personnaliser <br>la coloration du graphe : </div></html>");
        labelChoixParam.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        labelChoixParam.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        centrePanel2.add(labelChoixParam, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        RoundedPanel miniPanelCentral = new RoundedPanel(25);
        miniPanelCentral.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 0));
        miniPanelCentral.setLayout(new BoxLayout(miniPanelCentral, BoxLayout.PAGE_AXIS));
        miniPanelCentral.setBackground(Color.decode("#696767"));

        JLabel labelChoixAlgo = new JLabel("Choix Algorithme de coloration :");
        labelChoixAlgo.setForeground(Color.WHITE);
        labelChoixAlgo.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
        miniPanelCentral.add(labelChoixAlgo);

        ButtonGroup groupAlgos = new ButtonGroup();
        JRadioButton WelshBouton = new JRadioButton("Welsh & Powell");
        WelshBouton.setForeground(Color.WHITE);
        WelshBouton.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
        WelshBouton.setBackground(Color.decode("#696767"));
        WelshBouton.setSelected(true);
        WelshBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JRadioButton DsaturBouton = new JRadioButton("D-Satur");
        DsaturBouton.setForeground(Color.WHITE);
        DsaturBouton.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
        DsaturBouton.setBackground(Color.decode("#696767"));
        DsaturBouton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        groupAlgos.add(WelshBouton);
        groupAlgos.add(DsaturBouton);

        miniPanelCentral.add(WelshBouton);
        miniPanelCentral.add(DsaturBouton);

        JLabel labelChoixKmax = new JLabel("Choix de K-max :");
        labelChoixKmax.setForeground(Color.WHITE);
        labelChoixKmax.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
        miniPanelCentral.add(labelChoixKmax);

        JSpinner spinnerKMax = getSpinnerKMax();
        miniPanelCentral.add(spinnerKMax);

        centrePanel2.add(miniPanelCentral, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 0, 0, 0);

        RoundedButton boutonColoration = new RoundedButton("Effectuer la coloration", 50);
        boutonColoration.setFocusable(false);
        boutonColoration.setFont(new Font("Lucida Sans", Font.PLAIN, 30));
        boutonColoration.setForeground(Color.WHITE);
        boutonColoration.setPreferredSize(new Dimension(400, 80));
        boutonColoration.setBackground(couleurTertiaire);
        boutonColoration.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(panelConstruire, "Veuillez d'abord charger un fichier de vols.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ajoutez ici la logique pour effectuer la coloration
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

        initializeTable();


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

        centrePanel3.add(tableauInfoGraphe);
        centrePanel3.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));


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
        spinnerKMax.setModel(new SpinnerNumberModel(10,1,200,1));
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
        JFrame fenetreCarte = initFenetre();
        JXMapViewer mapViewer = initMapViewer();
        fenetreCarte.getContentPane().add(mapViewer);
        fenetreCarte.setVisible(true);
        ajouterEcouteurs(mapViewer);

        if (map != null && map.getListe_aeroports() != null) {
            Set<DefaultWaypoint> airportWaypoints = chargerAeroports();
            List<GeoPosition[]> routes = chargerRoutes();

            if (map.getListe_vols() != null) {
                airportWaypoints = filtrerAeroportsAvecVols(airportWaypoints, routes);
            }

            afficherMarqueursEtRoutes(mapViewer, airportWaypoints, routes);

        }
    }

    private JFrame initFenetre() {
        JFrame fenetreCarte = new JFrame();
        fenetreCarte.setTitle("Carte de la France");
        fenetreCarte.setSize(800, 600);
        fenetreCarte.setLocationRelativeTo(null);
        fenetreCarte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return fenetreCarte;
    }

    private JXMapViewer initMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new TileFactoryInfo(1, 12, 11, 256, true, true,
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
        return mapViewer;
    }

    private Set<DefaultWaypoint> chargerAeroports() {
        Set<DefaultWaypoint> airportWaypoints = new HashSet<>();
        for (Aeroport aeroport : map.getListe_aeroports()) {
            GeoPosition position = new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude());
            airportWaypoints.add(new DefaultWaypoint(position));
        }
        return airportWaypoints;
    }

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

    private Set<DefaultWaypoint> filtrerAeroportsAvecVols(Set<DefaultWaypoint> airportWaypoints, List<GeoPosition[]> routes) {
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

    private void afficherMarqueursEtRoutes(JXMapViewer mapViewer, Set<DefaultWaypoint> airportWaypoints, List<GeoPosition[]> routes) {
        WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(airportWaypoints);

        Painter<JXMapViewer> lineOverlay = new Painter<JXMapViewer>() {
            @Override
            public void paint(Graphics2D g, final JXMapViewer map, final int w, final int h) {
                g = (Graphics2D) g.create();
                final Rectangle rect = mapViewer.getViewportBounds();
                g.translate(-rect.x, -rect.y);
                g.setColor(Color.BLUE);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(1));

                for (GeoPosition[] route : routes) {
                    Point2D pt1 = map.getTileFactory().geoToPixel(route[0], map.getZoom());
                    Point2D pt2 = map.getTileFactory().geoToPixel(route[1], map.getZoom());
                    g.drawLine((int) pt1.getX(), (int) pt1.getY(), (int) pt2.getX(), (int) pt2.getY());
                }
                g.dispose();
            }
        };

        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(waypointPainter);
        painters.add(lineOverlay);
        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(compoundPainter);
    }

    private void ajouterEcouteurs(JXMapViewer mapViewer) {
        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GeoPosition geoPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                System.out.println("Position : " + geoPosition.getLatitude() + ", " + geoPosition.getLongitude());
            }
        });
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

    public JPanel getPanel() {
        return panelConstruire;
    }
}