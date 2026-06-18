package components;

import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Typography;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.awt.Dimension;

public class DsTable extends JTable {

    public DsTable(TableModel model) {
        super(model);
        setupStyle();
    }

    private void setupStyle() {
        setRowHeight(40);
        setIntercellSpacing(new Dimension(0, 0));
        setShowVerticalLines(false);
        setSelectionBackground(ColorPalette.PRIMARY.brighter());
        setSelectionForeground(ColorPalette.TEXT_PRIMARY);
        setFont(Typography.BODY_FONT);
    }
}
