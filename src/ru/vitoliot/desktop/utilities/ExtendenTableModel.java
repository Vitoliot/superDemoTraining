package ru.vitoliot.desktop.utilities;

import lombok.Getter;
import lombok.Setter;
import ru.vitoliot.desktop.entities.MaterialEntity;
import ru.vitoliot.desktop.entities.ServiceEntity;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * модель таблицы, основанная на Дженериках и Рефлексии
 * @param <T>
 */
public class ExtendenTableModel<T> extends AbstractTableModel {
    private Class<T> cls;
    private String[] columns;
    @Getter
    private List<T> rows = new ArrayList<>();
    @Getter
    private List<T> filteredRows;

    @Getter
    private Predicate<T>[] filters = new Predicate[10];

    private Comparator<T> sorter;

    public ExtendenTableModel(Class<T> cls, String[] columns) {
        this.cls = cls;
        this.columns = columns;
    }

    /**
     * фильтрация и сортировки происходят здесь
     */
    public void updateTable(){
        filteredRows = new ArrayList<>(rows);

        for (Predicate<T> filter:filters){
            if (filter != null) {
                filteredRows.removeIf(row -> !filter.test(row));
            }
        }

        if(sorter != null) {
            filteredRows.sort(sorter);
        }

        onUpdateRowsEvent();
        fireTableDataChanged();
    }

   public void onUpdateRowsEvent() {
    }

    public void setAllRows(List<T> rows){
        this.rows =rows;
        this.updateTable();
    }

    public void setSorter(Comparator<T> sorter){
        this.sorter =sorter;
        this.updateTable();
    }

    @Override
    public int getRowCount() {
        return filteredRows.size();
    }

    @Override
    public int getColumnCount() {
        return cls.getDeclaredFields().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Field field = cls.getDeclaredFields()[columnIndex];
            field.setAccessible(true);
            if (field.getName().equals("Cost")){
                Field fieldDisc = cls.getDeclaredField("Discount");
                fieldDisc.setAccessible(true);
                T service = this.filteredRows.get(rowIndex);
                double disc = (double) fieldDisc.get(service);
                double res = disc > 0 ? (double) field.get(service) * (100 - disc)/100 : (double) field.get(service);
                return disc > 0 ? String.format("%.2f -> %.2f", (double) field.get(service), res): res;
            }
            return field.get(this.filteredRows.get(rowIndex));
            }
        catch (Exception e) {
                e.printStackTrace();
                DialogUtil.showWarning(null, "Warning");
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    @Override
    public String getColumnName(int column) {
        return column < columns.length ? columns[column] : "Доп. столбец";
    }


}
