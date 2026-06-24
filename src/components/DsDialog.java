package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;
import theme.DesignTokens.Typography;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DsDialog extends JDialog {

    public enum DialogType {
        SUCCESS(ColorPalette.PRIMARY, "Sucesso"),
        ERROR(ColorPalette.DANGER, "Erro"),
        WARNING(ColorPalette.WARNING, "Aviso");

        private final Color color;
        private final String defaultTitle;

        DialogType(Color color, String defaultTitle) {
            this.color = color;
            this.defaultTitle = defaultTitle;
        }

        public Color getColor() { return color; }
        public String getDefaultTitle() { return defaultTitle; }
    }

    private DsDialog(Component parent, String message, String title, DialogType type) {
        super(SwingUtilities.getWindowAncestor(parent), title != null ? title : type.getDefaultTitle(), ModalityType.APPLICATION_MODAL);
        
        getContentPane().setBackground(ColorPalette.BACKGROUND);
        setResizable(false);
        setSize(400, 224);
        setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel(new BorderLayout(Spacing.MD, Spacing.MD));
        contentPanel.setBackground(ColorPalette.BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(Spacing.LG, Spacing.LG, Spacing.LG, Spacing.LG));

        // Icon placeholder or colored bar
        JPanel colorIndicator = new JPanel();
        colorIndicator.setBackground(type.getColor());
        colorIndicator.setPreferredSize(new Dimension(8, 0));

        DsTitleLabel titleLabel = new DsTitleLabel(title != null ? title : type.getDefaultTitle());
        titleLabel.setForeground(type.getColor());

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(Typography.BODY_FONT);
        messageArea.setForeground(ColorPalette.TEXT_PRIMARY);
        messageArea.setBackground(ColorPalette.BACKGROUND);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setBorder(null);

        JPanel textPanel = new JPanel(new BorderLayout(0, Spacing.SM));
        textPanel.setBackground(ColorPalette.BACKGROUND);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(messageArea, BorderLayout.CENTER);

        DsButton okButton = new DsButton("OK", type == DialogType.ERROR ? DsButton.ButtonType.DANGER : DsButton.ButtonType.PRIMARY);
        okButton.addActionListener((ActionEvent e) -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ColorPalette.BACKGROUND);
        buttonPanel.add(okButton);

        contentPanel.add(colorIndicator, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel);
    }

    public static void showSuccess(Component parent, String message) {
        new DsDialog(parent, message, null, DialogType.SUCCESS).setVisible(true);
    }

    public static void showError(Component parent, String message, String title) {
        new DsDialog(parent, message, title, DialogType.ERROR).setVisible(true);
    }

    public static void showWarning(Component parent, String message, String title) {
        new DsDialog(parent, message, title, DialogType.WARNING).setVisible(true);
    }
}
