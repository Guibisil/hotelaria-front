package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;
import theme.DesignTokens.Typography;

import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DsTextField extends JTextField {

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
        setBackground(ColorPalette.SURFACE);
        setCaretColor(ColorPalette.PRIMARY);
        
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_VARIANT, 1, true),
            BorderFactory.createEmptyBorder(Spacing.SM, Spacing.SM, Spacing.SM, Spacing.SM)
        ));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.PRIMARY, 2, true),
                    BorderFactory.createEmptyBorder(Spacing.SM - 1, Spacing.SM - 1, Spacing.SM - 1, Spacing.SM - 1)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.BORDER_VARIANT, 1, true),
                    BorderFactory.createEmptyBorder(Spacing.SM, Spacing.SM, Spacing.SM, Spacing.SM)
                ));
            }
        });
    }
}
