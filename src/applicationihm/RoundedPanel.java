package applicationihm;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Dimension customSize;

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false); // Important pour que le fond soit transparent
    }

    public void setCustomSize(Dimension size) {

        this.customSize = size;
        setPreferredSize(size);
        setMaximumSize(size);
        revalidate();
        repaint();
    }

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
