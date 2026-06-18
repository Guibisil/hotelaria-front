package components;

import javax.swing.*;
import java.awt.*;

public class DsButton extends JButton {

    public enum ButtonType {
        PRIMARY(new Color(40, 167, 69), Color.WHITE),
        SECONDARY(new Color(0, 123, 255), Color.WHITE),
        DANGER(new Color(220, 53, 69), Color.WHITE);

        private final Color backgroundColor;
        private final Color foregroundColor;

        ButtonType(Color backgroundColor, Color foregroundColor) {
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
        }

        public Color getBackgroundColor() {
            return backgroundColor;
        }

        public Color getForegroundColor() {
            return foregroundColor;
        }
    }

    public DsButton(String text, ButtonType type) {
        super(text);
        
        // Configurações visuais padrão para um botão premium
        setBackground(type.getBackgroundColor());
        setForeground(type.getForegroundColor());
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 12));
        
        // Micro-animação de hover (efeito visual interativo)
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(type.getBackgroundColor().brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(type.getBackgroundColor());
            }
        });
    }
}
