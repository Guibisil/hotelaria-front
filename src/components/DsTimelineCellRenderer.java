package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DsTimelineCellRenderer extends DefaultTableCellRenderer {

    private final Border defaultBorder = new MatteBorder(1, 1, 1, 1, ColorPalette.BORDER_VARIANT);
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        label.setOpaque(true);
        label.setBorder(defaultBorder);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setFont(Typography.SMALL_FONT);

        if (column == 0) {
            label.setBackground(ColorPalette.TIMELINE_HEADER_BG);
            label.setForeground(ColorPalette.TEXT_PRIMARY);
            label.setText(value != null ? value.toString() : "");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(Typography.BODY_FONT);
            return label;
        }

        if (value instanceof DsTimelineCell) {
            DsTimelineCell cell = (DsTimelineCell) value;
            label.setBackground(ColorPalette.TIMELINE_CELL_PRIMARY);
            label.setForeground(ColorPalette.ON_PRIMARY);
            label.setText(cell.getText() != null && !cell.getText().isEmpty() ? "  " + cell.getText() : "");
            
            int left = 0, right = 0;
            if (cell.isStart()) left = 1;
            if (cell.isEnd()) right = 1;
            
            if (isSelected) {
                label.setBackground(ColorPalette.PRIMARY_HOVER);
            }
            
            label.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 1, 0, ColorPalette.SURFACE), 
                new MatteBorder(0, left, 0, right, ColorPalette.SURFACE) 
            ));

        } else {
            label.setBackground(ColorPalette.SURFACE);
            label.setText("");
            if (isSelected) {
                label.setBackground(ColorPalette.TIMELINE_SELECTED);
            }
        }

        return label;
    }
}
