package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;

import javax.swing.*;
import java.awt.*;

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

    public DsButton(String text, ButtonType type) {
        super(text);
        
        // Configurações visuais padrão para um botão premium
        setBackground(type.getBackgroundColor());
        setForeground(type.getForegroundColor());
        setFocusPainted(false);
        setFont(Typography.BUTTON_FONT);
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); // Added padding for better M3 look
        
        // Micro-animação de hover (efeito visual interativo)
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(type.getHoverColor());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(type.getBackgroundColor());
            }
        });
    }
}
