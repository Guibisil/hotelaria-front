package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Radius;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import java.awt.*;

public class DsCard extends JPanel {

    public DsCard() {
        super();
        setOpaque(false);
        setBackground(ColorPalette.SURFACE);
        setBorder(BorderFactory.createEmptyBorder(Spacing.LG, Spacing.LG, Spacing.LG, Spacing.LG));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Radius.LARGE, Radius.LARGE);

        // Draw subtle border
        g2.setColor(ColorPalette.BORDER_VARIANT);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Radius.LARGE, Radius.LARGE);

        g2.dispose();
        super.paintComponent(g);
    }
}
