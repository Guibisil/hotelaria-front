package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;

import javax.swing.*;

public class DsTitleLabel extends JLabel {

    public DsTitleLabel(String text) {
        super(text);
        setFont(Typography.TITLE_FONT);
        setForeground(ColorPalette.TEXT_PRIMARY);
    }
}
