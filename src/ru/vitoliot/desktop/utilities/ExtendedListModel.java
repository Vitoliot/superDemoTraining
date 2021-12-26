package ru.vitoliot.desktop.utilities;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ExtendedListModel<T> extends AbstractListModel {
    private Class cls;
    private List<T> rows = new ArrayList<>();
    private List<T> filteredRows;

    private Comparator sorter;
    private Predicate<T> [] filters = new Predicate[10];

    public ExtendedListModel(Class cls) {
        this.cls = cls;
    }

    public void update(){
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
    }


    @Override
    public int getSize() {
        return filteredRows.size();
    }

    @Override
    public Object getElementAt(int index) {
        return rows.get(index);
    }
    public void onUpdateRowsEvent() {
    }

    public void setAllRows(List<T> rows){
        this.rows =rows;
        this.update();
    }

    public void setSorter(Comparator<T> sorter){
        this.sorter = sorter;
        this.update();
    }
}
