package ru.vitoliot.desktop.utilities;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AdminCellRenderer extends DefaultTableCellRenderer {
    public AdminCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component l = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value.equals(new MyFirstTime(0))){
            l.setBackground(Color.RED);
        }
        else {
            l.setBackground(Color.WHITE);
        }
        return l;
    }
}
