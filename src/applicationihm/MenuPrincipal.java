package applicationihm;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 * Classe principale de l'application qui montre un menu d'accueil avec plusieurs boutons pour accéder aux différentes fonctionnalités de l'application
 */
public class MenuPrincipal extends JFrame {

    /**
     * Image qui sert de fond d'ecran a l'application
     */
    private Image background;

    /**
     * panel principal du menu qui contient tout les autres panels
     */
    private final JPanel panelAccueil;

    /**
     * Bouton on/off qui permet d'active/désactiver le theme sombre sur l'application
     */
    private final JToggleButton boutonTheme;

    /**
     * Constructeur du menu principal
     */
    public MenuPrincipal() {
        String titre = "Safly";
        this.setTitle(titre);
        this.setSize(1250, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
        Image logo = logoIcon.getImage();
        this.setIconImage(logo);

        // Chargement l'image de fond, met en place un fond d'acran gris si l'image n'est pas trouvée
        try {
            background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/background1.png"))).getImage();
        } catch (Exception e) {
            setBackground(Color.GRAY);
        }

        // Mise en place de l'image de fond sur le panelAccueil
        panelAccueil = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panelAccueil.setLayout(new BoxLayout(panelAccueil, BoxLayout.PAGE_AXIS));

        // Création des panels de la fenêtre
        JPanel panelHaut = new JPanel();
        panelHaut.setOpaque(false);
        RoundedPanel panelCentre = new RoundedPanel(25);
        panelCentre.setOpaque(false);
        panelCentre.setCustomSize(new Dimension(850, 400));

        JPanel panelBas = new JPanel();
        panelBas.setOpaque(false);

        panelCentre.setLayout(new BoxLayout(panelCentre, BoxLayout.PAGE_AXIS));
        panelCentre.setBackground(Color.decode("#D9D9D9"));
        panelCentre.setBorder(BorderFactory.createEmptyBorder(50, 20, 30, 20));
        panelBas.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 30));

        // Bouton de thème
        boutonTheme = new JToggleButton("Thème");
        boutonTheme.setBackground(Color.decode("#696767"));
        boutonTheme.setSelected(true);
        boutonTheme.setFont(new Font("Lucida Sans", Font.PLAIN, 15));
        boutonTheme.setFocusable(false);
        boutonTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelHaut.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelHaut.add(boutonTheme);
        boutonTheme.addActionListener(e -> setBackgroundImage(background));

        // Texte d'accueil
        JLabel texteAccueil = new JLabel("<html><div style='text-align: center'>Bienvenue sur Safly, l'application de gestion de conflits aériens,<br> vous pouvez effectuer diverses manipulations de graphes <br>en cliquant sur le bouton de votre choix.</div></html>");
        texteAccueil.setFont(new Font("Lucida Sans", Font.PLAIN, 26));

        // Panels centraux pour les boutons
        JPanel panelCentral1 = new JPanel();
        panelCentral1.setBackground(Color.decode("#D9D9D9"));
        JPanel panelCentral2 = new JPanel(new BorderLayout(20, 0));
        panelCentral2.setBackground(Color.decode("#D9D9D9"));
        JPanel panelCentral2Gauche = new JPanel();
        panelCentral2Gauche.setBackground(Color.decode("#D9D9D9"));
        JPanel panelCentralMilieu = new JPanel();
        panelCentralMilieu.setBackground(Color.decode("#D9D9D9"));
        JPanel panelCentral2Droite = new JPanel();
        panelCentral2Droite.setBackground(Color.decode("#D9D9D9"));

        // Charger les icônes des boutons
        ImageIcon imageChargerGraphe = new ImageIcon(Objects.requireNonNull(getClass().getResource("/chargergraphe.png")));
        Image imageChargerGrapheScale = imageChargerGraphe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon IconeCharger = new ImageIcon(imageChargerGrapheScale);

        ImageIcon imageConstruireGraphe = new ImageIcon(Objects.requireNonNull(getClass().getResource("/construiregraphe.png")));
        Image imageConstruireGrapheScale = imageConstruireGraphe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon IconeConstruire = new ImageIcon(imageConstruireGrapheScale);

        ImageIcon imageModeEval = new ImageIcon(Objects.requireNonNull(getClass().getResource("/IconeModeEval.png")));
        Image imageModeEvalScale = imageModeEval.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon IconeEval = new ImageIcon(imageModeEvalScale);

        // Boutons principaux avec icônes et configurations
        RoundedButton boutonChargerGraphe = new RoundedButton("Charger un graphe", 25, IconeCharger);
        boutonChargerGraphe.setBackground(Color.decode("#696767"));
        boutonChargerGraphe.setFocusable(false);
        boutonChargerGraphe.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonChargerGraphe.setForeground(Color.WHITE);
        boutonChargerGraphe.setHorizontalTextPosition(SwingConstants.CENTER);
        boutonChargerGraphe.setVerticalTextPosition(SwingConstants.BOTTOM);
        boutonChargerGraphe.setPreferredSize(new Dimension(250, 200));
        boutonChargerGraphe.setContentAreaFilled(false);
        boutonChargerGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedButton boutonConstruireGraphe = new RoundedButton("Construire un graphe", 25, IconeConstruire);
        boutonConstruireGraphe.setBackground(Color.decode("#696767"));
        boutonConstruireGraphe.setFocusable(false);
        boutonConstruireGraphe.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonConstruireGraphe.setForeground(Color.WHITE);
        boutonConstruireGraphe.setHorizontalTextPosition(SwingConstants.CENTER);
        boutonConstruireGraphe.setVerticalTextPosition(SwingConstants.BOTTOM);
        boutonConstruireGraphe.setPreferredSize(new Dimension(250, 200));
        boutonConstruireGraphe.setContentAreaFilled(false);

        RoundedButton boutonModeEval = new RoundedButton("Mode Evaluation", 25, IconeEval);
        boutonModeEval.setBackground(Color.decode("#696767"));
        boutonModeEval.setFocusable(false);
        boutonModeEval.setFont(new Font("Lucida Sans", Font.PLAIN, 20));
        boutonModeEval.setForeground(Color.WHITE);
        boutonModeEval.setHorizontalTextPosition(SwingConstants.CENTER);
        boutonModeEval.setVerticalTextPosition(SwingConstants.BOTTOM);
        boutonModeEval.setPreferredSize(new Dimension(250, 200));
        boutonModeEval.setContentAreaFilled(false);
        boutonModeEval.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ajout des MouseListener aux boutons
        boutonChargerGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContentPane(new PageChargerGraphe(MenuPrincipal.this).getPanel());
                revalidate();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boutonChargerGraphe.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boutonChargerGraphe.setBackground(Color.decode("#696767"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boutonChargerGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boutonChargerGraphe.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        boutonConstruireGraphe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContentPane(new PageConstruireGraphe(MenuPrincipal.this).getPanel());
                revalidate();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boutonConstruireGraphe.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boutonConstruireGraphe.setBackground(Color.decode("#696767"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boutonConstruireGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boutonConstruireGraphe.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        boutonModeEval.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContentPane(new ModeEvaluation(MenuPrincipal.this).getPanel());
                revalidate();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boutonModeEval.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boutonModeEval.setBackground(Color.decode("#696767"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boutonModeEval.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boutonModeEval.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Ajouter les boutons aux panels correspondants
        panelCentral2Gauche.add(boutonChargerGraphe);
        panelCentral2Gauche.setBorder(new EmptyBorder(0, 0, 0, 0));

        panelCentralMilieu.add(boutonModeEval);
        panelCentralMilieu.setBorder(new EmptyBorder(0, 15, 0, 15));

        panelCentral2Droite.add(boutonConstruireGraphe);
        panelCentral2Droite.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Ajout des panels centraux au panel centre
        panelCentral1.add(texteAccueil);
        panelCentral1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelCentral2.add(panelCentral2Gauche, BorderLayout.WEST);
        panelCentral2.add(panelCentralMilieu, BorderLayout.CENTER);
        panelCentral2.add(panelCentral2Droite, BorderLayout.EAST);

        // Label pour le nom de l'application
        JLabel nomAppli = new JLabel("SAFLY");
        nomAppli.setFont(new Font("Lucida Sans", Font.BOLD, 60));

        panelBas.add(nomAppli);
        panelBas.setPreferredSize(new Dimension(getWidth(), 120));

        // Ajouter les panels au panel d'accueil
        panelCentre.add(panelCentral1);
        panelCentre.add(panelCentral2);

        panelAccueil.add(panelHaut);
        panelAccueil.add(panelCentre);
        panelAccueil.add(panelBas);

        setContentPane(panelAccueil);
    }

    // Change l'image de fond en fonction de l'état du bouton de thème
    public void setBackgroundImage(Image backgroundimage) {
        if (!boutonTheme.isSelected()) {
            background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/background2.png"))).getImage();
            panelAccueil.repaint();
        } else {
            background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/background1.png"))).getImage();
            panelAccueil.repaint();
        }
    }

    // Retourne l'image de fond actuelle
    public Image getBackgroundImage() {
        return background;
    }

    // Retourne le panel d'accueil
    public Container getPanelAccueil() {
        return panelAccueil;
    }
}