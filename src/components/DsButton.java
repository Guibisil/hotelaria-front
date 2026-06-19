package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;
import theme.DesignTokens.Radius;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DsButton extends JButton {

    public enum ButtonType {
        PRIMARY(ColorPalette.PRIMARY, ColorPalette.ON_PRIMARY, ColorPalette.PRIMARY_HOVER),
        SECONDARY(ColorPalette.SECONDARY, ColorPalette.ON_SECONDARY, ColorPalette.SECONDARY_HOVER),
        DANGER(ColorPalette.DANGER, ColorPalette.ON_DANGER, ColorPalette.DANGER_HOVER);

        private final Color backgroundColor;
        private final Color foregroundColor;
        private final Color hoverColor;

        ButtonType(Color backgroundColor, Color foregroundColor, Color hoverColor) {
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
            this.hoverColor = hoverColor;
        }

        public Color getBackgroundColor() {
            return backgroundColor;
        }

        public Color getForegroundColor() {
            return foregroundColor;
        }
        
        public Color getHoverColor() {
            return hoverColor;
        }
    }

    private boolean isHovered = false;
    private final ButtonType type;

    public DsButton(String text, ButtonType type) {
        super(text);
        this.type = type;
        
        setForeground(type.getForegroundColor());
        setFocusPainted(false);
        setContentAreaFilled(false); 
        setBorderPainted(false); 
        setOpaque(false); 
        
        setFont(Typography.BUTTON_FONT);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
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
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (getModel().isPressed()) {
            g2.setColor(type.getHoverColor().darker());
        } else if (isHovered) {
            g2.setColor(type.getHoverColor());
        } else {
            g2.setColor(type.getBackgroundColor());
        }
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), Radius.MEDIUM, Radius.MEDIUM);
        
        g2.dispose();
        
        super.paintComponent(g);
    }
}
