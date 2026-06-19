package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;
import theme.DesignTokens.Spacing;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;

public class DsTable extends JTable {

    public DsTable(TableModel model) {
        super(model);
        setupStyle();
    }

    private void setupStyle() {
        setRowHeight(44);
        setIntercellSpacing(new Dimension(0, 0));
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setGridColor(ColorPalette.BORDER_VARIANT);
        setSelectionBackground(ColorPalette.TIMELINE_SELECTED);
        setSelectionForeground(ColorPalette.TEXT_PRIMARY);
        setFont(Typography.BODY_FONT);
        setBackground(ColorPalette.SURFACE);
        setForeground(ColorPalette.TEXT_PRIMARY);
        
        JTableHeader header = getTableHeader();
        header.setBackground(ColorPalette.SURFACE_VARIANT);
        header.setForeground(ColorPalette.TEXT_PRIMARY);
        header.setFont(Typography.BODY_FONT.deriveFont(Font.BOLD));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER));
        
        // Remove focus border
        setFocusable(false);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (!isRowSelected(row)) {
            c.setBackground(row % 2 == 0 ? ColorPalette.SURFACE : new Color(248, 250, 252)); // Slightly different white/gray
        }
        
        if (c instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) c).setBorder(BorderFactory.createEmptyBorder(0, Spacing.MD, 0, Spacing.MD));
        }
        
        return c;
    }
}
