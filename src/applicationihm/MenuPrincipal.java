package applicationihm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class MenuPrincipal extends JFrame {
    private Image background;
    private final JPanel panelAccueil;
    private final JToggleButton boutonTheme;

    public MenuPrincipal() {
        String titre = "Safly";
        this.setTitle(titre);
        this.setSize(1250, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/background1.png"))).getImage();
        }catch(Exception e){
            setBackground(Color.GRAY);
        }
        panelAccueil = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panelAccueil.setLayout(new BoxLayout(panelAccueil, BoxLayout.PAGE_AXIS));

        JPanel panelHaut = new JPanel();
        panelHaut.setOpaque(false);
        RoundedPanel panelCentre = new RoundedPanel(25);
        panelCentre.setOpaque(false);
        panelCentre.setCustomSize(new Dimension(850, 400)); // Définir la taille personnalisée ici

        JPanel panelBas = new JPanel();
        panelBas.setOpaque(false);

        panelCentre.setLayout(new BoxLayout(panelCentre, BoxLayout.PAGE_AXIS));
        panelCentre.setBackground(Color.decode("#D9D9D9"));
        panelCentre.setBorder(BorderFactory.createEmptyBorder(50, 20, 30, 20));
        panelBas.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 30));


        boutonTheme = new JToggleButton("Thème");
        boutonTheme.setBackground(Color.decode("#696767"));
        boutonTheme.setSelected(true);
        boutonTheme.setFont(new Font("Lucida Sans",Font.PLAIN,15));
        boutonTheme.setFocusable(false);
        boutonTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelHaut.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelHaut.add(boutonTheme);
        boutonTheme.addActionListener(e -> setBackgroundImage(background));

        JLabel texteAccueil = new JLabel("<html><div style:'text-align: center'>Bienvenue sur Safly, l'application de gestion de conflits aériens,<br> vous pouvez effectuer diverses manipulations de graphes <br>en cliquant sur le bouton de votre choix.</div></html>");
        texteAccueil.setFont(new Font("Lucida Sans",Font.PLAIN,26));
        JPanel panelCentral1 = new JPanel();
        panelCentral1.setBackground(Color.decode("#D9D9D9"));
        JPanel panelCentral2 = new JPanel();
        panelCentral2.setBackground(Color.decode("#D9D9D9"));
        JPanel panelCentral2Gauche = new JPanel();
        panelCentral2Gauche.setBackground(Color.decode("#D9D9D9"));
        JPanel panelCentral2Droite = new JPanel();
        panelCentral2Droite.setBackground(Color.decode("#D9D9D9"));


        ImageIcon imageChargerGraphe = new ImageIcon(Objects.requireNonNull(getClass().getResource("/chargergraphe.png")));
        Image imageChargerGrapheScale = imageChargerGraphe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon IconeCharger = new ImageIcon(imageChargerGrapheScale);

        ImageIcon imageConstruireGraphe = new ImageIcon(Objects.requireNonNull(getClass().getResource("/construiregraphe.png")));
        Image imageConstruireGrapheScale = imageConstruireGraphe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon IconeConstruire = new ImageIcon(imageConstruireGrapheScale);

        RoundedButton boutonChargerGraphe = new RoundedButton("Charger un graphe", 25 ,IconeCharger);
        boutonChargerGraphe.setBackground(Color.decode("#696767"));
        boutonChargerGraphe.setFocusable(false);
        boutonChargerGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonChargerGraphe.setForeground(Color.WHITE);
        RoundedButton boutonConstruireGraphe = new RoundedButton("Construire un graphe", 25 ,IconeConstruire);
        boutonConstruireGraphe.setBackground(Color.decode("#696767"));
        boutonConstruireGraphe.setFocusable(false);
        boutonConstruireGraphe.setFont(new Font("Lucida Sans",Font.PLAIN,20));
        boutonConstruireGraphe.setForeground(Color.WHITE);
        boutonChargerGraphe.setHorizontalTextPosition(SwingConstants.CENTER);
        boutonChargerGraphe.setVerticalTextPosition(SwingConstants.BOTTOM);
        boutonChargerGraphe.setPreferredSize(new Dimension(250, 200));
        boutonChargerGraphe.setContentAreaFilled(false);
        boutonChargerGraphe.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boutonConstruireGraphe.setHorizontalTextPosition(SwingConstants.CENTER);
        boutonConstruireGraphe.setVerticalTextPosition(SwingConstants.BOTTOM);
        boutonConstruireGraphe.setPreferredSize(new Dimension(250, 200));
        boutonConstruireGraphe.setContentAreaFilled(false);


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

        panelCentral2Gauche.add(boutonChargerGraphe);
        panelCentral2Gauche.setBorder(new EmptyBorder(0, 0, 0, 10));
        panelCentral2Droite.add(boutonConstruireGraphe);
        panelCentral2Droite.setBorder(new EmptyBorder(0, 10, 0, 0));

        panelCentral1.add(texteAccueil);
        panelCentral1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelCentral2.add(panelCentral2Gauche);
        panelCentral2.add(panelCentral2Droite);

        JLabel nomAppli = new JLabel("SAFLY");
        nomAppli.setFont(new Font("Lucida Sans",Font.BOLD,60));

        panelBas.add(nomAppli);
        panelBas.setPreferredSize(new Dimension(getWidth(), 120));

        panelCentre.add(panelCentral1);
        panelCentre.add(panelCentral2);

        panelAccueil.add(panelHaut);
        panelAccueil.add(panelCentre);
        panelAccueil.add(panelBas);

        setContentPane(panelAccueil);
    }

    public void setBackgroundImage(Image backgroundimage) {
        if (!boutonTheme.isSelected()) {
            background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/background2.png"))).getImage();
            panelAccueil.repaint();
        } else {
            background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/background1.png"))).getImage();
            panelAccueil.repaint();
        }
    }

    public Image getBackgroundImage() {
        return background;
    }

    public Container getPanelAccueil() {
        return panelAccueil;
    }


}