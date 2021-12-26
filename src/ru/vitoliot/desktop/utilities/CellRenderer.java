package ru.vitoliot.desktop.utilities;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class CellRenderer extends DefaultTableCellRenderer {
    private List<Integer> rows;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component l =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!(value instanceof ImageIcon) && rows.contains(row)){
            l.setBackground(new Color(231, 250, 191));
        }
        else {
            l.setBackground(Color.WHITE);
        }
        if (isSelected){
            l.setBackground(new Color(4, 160, 255));
        }
//        if (rows.contains(row) && column == 2){
//            System.out.println(value);
//            value = (double) value * (double) table.getModel().getValueAt(row, 5)/100;
//            System.out.println(value);
////            System.out.println(table.getModel().getValueAt(row, 5));
//        }
        return l;
    }

    public void setRows(List<Integer> rows) {
        this.rows = rows;
    }
}
