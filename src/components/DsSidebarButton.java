package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;
import theme.DesignTokens.Radius;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DsSidebarButton extends JButton {

    private boolean isHovered = false;
    private boolean isActive = false;

    public DsSidebarButton(String text) {
        super(text);
        
        setForeground(ColorPalette.SIDEBAR_TEXT_MUTED);
        setFont(Typography.BUTTON_FONT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                isHovered = false;
                repaint();
            }
        });
    }

    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            setForeground(ColorPalette.SIDEBAR_TEXT);
            setFont(Typography.BUTTON_FONT.deriveFont(Font.BOLD));
        } else {
            setForeground(ColorPalette.SIDEBAR_TEXT_MUTED);
            setFont(Typography.BUTTON_FONT.deriveFont(Font.PLAIN));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isActive) {
            g2.setColor(ColorPalette.SIDEBAR_ITEM_ACTIVE);
            g2.fillRoundRect(8, 0, getWidth() - 16, getHeight(), Radius.MEDIUM, Radius.MEDIUM);
            
            // Draw a primary color indicator on the left
            g2.setColor(ColorPalette.PRIMARY);
            g2.fillRoundRect(8, 6, 4, getHeight() - 12, Radius.SMALL, Radius.SMALL);
        } else if (isHovered) {
            g2.setColor(ColorPalette.SIDEBAR_ITEM_HOVER);
            g2.fillRoundRect(8, 0, getWidth() - 16, getHeight(), Radius.MEDIUM, Radius.MEDIUM);
        }
        
        g2.dispose();
        
        super.paintComponent(g);
    }
}
