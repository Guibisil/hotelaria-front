package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class DsModal extends JDialog {

    protected JPanel contentPanel;

    public DsModal(String title, int width, int height) {
        setTitle(title);
        setModal(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(ColorPalette.BACKGROUND);

        contentPanel = new JPanel();
        contentPanel.setBackground(ColorPalette.BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));
        
        add(contentPanel, BorderLayout.CENTER);
    }

    protected void setModalLayout(LayoutManager layout) {
        contentPanel.setLayout(layout);
    }

    protected void addComponent(Component comp) {
        contentPanel.add(comp);
    }
    
    protected void addComponent(Component comp, Object constraints) {
        contentPanel.add(comp, constraints);
    }
}
