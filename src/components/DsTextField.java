package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;
import theme.DesignTokens.Typography;
import theme.DesignTokens.Radius;

import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DsTextField extends JTextField {

    private boolean isFocused = false;

    public DsTextField() {
        super();
        setupStyle();
    }

    public DsTextField(int columns) {
        super(columns);
        setupStyle();
    }

    private void setupStyle() {
        setFont(Typography.BODY_FONT);
        setForeground(ColorPalette.TEXT_PRIMARY);
        setCaretColor(ColorPalette.PRIMARY);
        setOpaque(false); // Make it transparent to paint rounded background
        
        setBorder(BorderFactory.createEmptyBorder(Spacing.SM, Spacing.MD, Spacing.SM, Spacing.MD));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(ColorPalette.SURFACE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Radius.MEDIUM, Radius.MEDIUM);
        
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isFocused) {
            g2.setColor(ColorPalette.PRIMARY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Radius.MEDIUM, Radius.MEDIUM);
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, Radius.MEDIUM - 1, Radius.MEDIUM - 1); // Thicker border
        } else {
            g2.setColor(ColorPalette.BORDER_VARIANT);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Radius.MEDIUM, Radius.MEDIUM);
        }
        
        g2.dispose();
    }
}
