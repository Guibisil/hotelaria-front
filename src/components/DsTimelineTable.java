package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class DsTimelineTable extends JTable {

    public DsTimelineTable() {
        super();
        setupUI();
    }

    public DsTimelineTable(javax.swing.table.TableModel dm) {
        super(dm);
        setupUI();
    }

    private void setupUI() {
        this.setRowHeight(48); // Taller rows for timeline
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setShowGrid(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellSelectionEnabled(true);
        
        JTableHeader header = this.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(ColorPalette.SURFACE_VARIANT);
        header.setForeground(ColorPalette.TEXT_PRIMARY);
        header.setFont(Typography.BODY_FONT.deriveFont(Font.BOLD));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER));
        
        this.setBackground(ColorPalette.BACKGROUND);
    }
}
