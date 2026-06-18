package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;

import javax.swing.JLabel;

public class DsLabel extends JLabel {

    public DsLabel(String text) {
        super(text);
        setFont(Typography.BODY_FONT);
        setForeground(ColorPalette.TEXT_PRIMARY);
    }
}
