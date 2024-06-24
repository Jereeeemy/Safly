package applicationihm;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * La classe RoundedPanel est une extension de JPanel qui permet de dessiner un panneau avec des coins arrondis.
 */
public class RoundedPanel extends JPanel {
    /**
     * Rayon des coins arrondis
     */
    private int cornerRadius;

    /**
     * Taille personnalisée du panneau
     */
    private Dimension customSize;

    /**
     * Constructeur de la classe RoundedPanel.
     *
     * @param radius Rayon des coins arrondis
     */
    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false); // Important pour que le fond soit transparent
    }

    /**
     * Définit la taille personnalisée du panneau.
     *
     * @param size La taille personnalisée
     */
    public void setCustomSize(Dimension size) {
        this.customSize = size;
        setPreferredSize(size);
        setMaximumSize(size);
        revalidate();
        repaint();
    }

    /**
     * Dessine le composant avec des coins arrondis.
     *
     * @param g L'objet Graphics utilisé pour le dessin
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = customSize != null ? customSize.width : getWidth();
        int height = customSize != null ? customSize.height : getHeight();

        // Dessiner le panneau avec des coins arrondis
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
    }

    /**
     * Dessine la bordure du composant avec des coins arrondis.
     *
     * @param g L'objet Graphics utilisé pour le dessin de la bordure
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = customSize != null ? customSize.width : getWidth();
        int height = customSize != null ? customSize.height : getHeight();

        // Dessiner la bordure avec des coins arrondis
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
    }
}