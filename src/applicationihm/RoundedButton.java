package applicationihm;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * La classe RoundedButton est une extension de JButton qui permet de dessiner un bouton avec des coins arrondis.
 */
public class RoundedButton extends JButton {
    /**
     * Raypn des coins arrondis
     */
    private int cornerRadius;

    /**
     * Constructeur de la classe RoundedButton.
     *
     * @param label  Le texte du bouton
     * @param radius Rayon des coins arrondis
     */
    public RoundedButton(String label, int radius) {
        super(label);
        this.cornerRadius = radius;
        setContentAreaFilled(false); // Important pour un bouton transparent
    }

    /**
     * Constructeur de la classe RoundedButton avec une icône.
     *
     * @param label  Le texte du bouton
     * @param radius Rayon des coins arrondis
     * @param icon   L'icône du bouton
     */
    public RoundedButton(String label, int radius, Icon icon) {
        super(label);
        this.cornerRadius = radius;
        setContentAreaFilled(false); // Important pour un bouton transparent
        this.setIcon(icon);
    }

    /**
     * Dessine le composant avec des coins arrondis.
     *
     * @param g L'objet Graphics utilisé pour le dessin
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner le bouton avec des coins arrondis
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        super.paintComponent(g2);
        g2.dispose();
    }

    /**
     * Dessine la bordure du composant avec des coins arrondis.
     * @param g L'objet Graphics utilisé pour le dessin de la bordure
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner la bordure avec des coins arrondis
        g2.setColor(getForeground());
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
        g2.dispose();
    }
}