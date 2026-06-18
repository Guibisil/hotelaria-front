package components;

import theme.DesignTokens.Spacing;

import javax.swing.*;
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
        this.setRowHeight(Spacing.XL + 8); // 40
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setShowGrid(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellSelectionEnabled(true);
        this.getTableHeader().setReorderingAllowed(false);
    }
}
