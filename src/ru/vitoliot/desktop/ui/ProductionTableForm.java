package ru.vitoliot.desktop.ui;

import ru.vitoliot.desktop.entities.ProductEntity;
import ru.vitoliot.desktop.manager.ProductManager;
import ru.vitoliot.desktop.utilities.BaseFormProductApp;
import ru.vitoliot.desktop.utilities.ExtendenTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;


public class ProductionTableForm extends BaseFormProductApp {
    private JPanel mainPanel;
    private JComboBox filterComboBox;
    private JComboBox sortComboBox;
    private JTextField titleField;
    private JRadioButton sortRadioButton;
    private JTable table;
    private JLabel countLabel;
    private JButton breakButton;
    private JButton addButton;

    private boolean sortBool = false;
    private ExtendenTableModel<ProductEntity> model;
    private List<String> filterChoice = new ArrayList<>();
    private Predicate<ProductEntity> [] filters;

    public ProductionTableForm() {
        super(1200, 1000);
        setContentPane(mainPanel);
        initTable();
        initComboBox();
        initButtons();
        initFilters();
        setVisible(true);
    }

    /**
     * инициализация таблицы
     */
    public void initTable(){
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(60);
        model = new ExtendenTableModel<>(ProductEntity.class, new String[]{
                "Id", "Название", "Тип", "Номер", "Описание",
                "Путь к картинке", "Количество рабочих", "Номер фабрики",
                "Цена для агента", "Цена", "Материалы", "Картинка"}){
            @Override
            public void onUpdateRowsEvent() {
                countLabel.setText(model.getFilteredRows().size() + "/" + model.getRows().size());
            }
        };

        try {
            model.setAllRows(ProductManager.selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setModel(model);

        TableColumn cal = table.getColumn("Путь к картинке");
        cal.setMinWidth(0);
        cal.setMaxWidth(0);
        cal.setPreferredWidth(0);

        cal = table.getColumn("Id");
        cal.setMinWidth(0);
        cal.setMaxWidth(0);
        cal.setPreferredWidth(0);

        cal = table.getColumn("Тип");
        cal.setMinWidth(3);
        cal.setPreferredWidth(15);

        cal = table.getColumn("Номер");
        cal.setMinWidth(3);
        cal.setPreferredWidth(15);

        cal = table.getColumn("Количество рабочих");
        cal.setMinWidth(10);
        cal.setPreferredWidth(20);

        cal = table.getColumn("Номер фабрики");
        cal.setMinWidth(10);
        cal.setPreferredWidth(20);

        cal = table.getColumn("Цена для агента");
        cal.setMinWidth(10);
        cal.setPreferredWidth(20);

        cal = table.getColumn("Цена");
        cal.setMinWidth(10);
        cal.setPreferredWidth(20);

        cal = table.getColumn("Материалы");
        cal.setMinWidth(10);
        cal.setPreferredWidth(50);
    };

    /**
     * инициализация фильтров и сортировок
     */
    public void initFilters(){
        filters = model.getFilters();
        filters[0] = new Predicate<ProductEntity>() {
            @Override
            public boolean test(ProductEntity productEntity) {
                String selected = (String) filterComboBox.getSelectedItem();
                if (selected.equals("Все типы")){
                    return true;
                }
                else {
                    return productEntity.getType().equals(selected);
                }
            }
        };
        filters[1] = new Predicate<ProductEntity>() {
            @Override
            public boolean test(ProductEntity productEntity) {
                String text = titleField.getText().toLowerCase(Locale.ROOT);
                if (!text.isEmpty()){
                   boolean titleBool = productEntity.getTitle().toLowerCase(Locale.ROOT).startsWith(text);
                   if (!titleBool && productEntity.getDescription() != null){
                       titleBool = productEntity.getDescription().toLowerCase(Locale.ROOT).startsWith(text);
                   }
                   return titleBool;
                }
                return true;
            }
        };
    }

    public void initButtons(){
        sortRadioButton.addActionListener(e -> {
            sortBool = !sortBool;
            sortComboBox.setSelectedIndex(sortComboBox.getSelectedIndex());
        });
        breakButton.addActionListener(e -> {
            titleField.setText("");
            sortComboBox.setSelectedIndex(0);
            filterComboBox.setSelectedIndex(0);
            sortBool = false;
            sortRadioButton.setSelected(false);
        });
        addButton.addActionListener(e -> {
            dispose();
            new InsertNamordnikForm(filterChoice);
        });
    }

    /**
     * настройка комбобоксов
     */
    public void initComboBox(){
        this.filterChoice.add("Все типы");
        for (ProductEntity product : model.getRows()){
            String type = product.getType();
            System.out.println(type);
            if (type != null && !this.filterChoice.contains(type)) {
                this.filterChoice.add(product.getType());
            }
        }
        System.out.println(filterChoice);
        for (String filter: filterChoice){
            filterComboBox.addItem(filter);
        }

        sortComboBox.addItem("Все");
        sortComboBox.addItem("по названию");
        sortComboBox.addItem("по номеру цеха");
        sortComboBox.addItem("по стоимости для агента");

        sortComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (sortComboBox.getSelectedIndex() == 1){
                    if (sortBool){
                        model.setSorter(new Comparator<ProductEntity>() {
                            @Override
                            public int compare(ProductEntity o1, ProductEntity o2) {
                                return Character.compare(o1.getTitle().toLowerCase().charAt(0), o2.getTitle().toLowerCase().charAt(0));
                            }
                        });
                    }
                    else {
                        model.setSorter(new Comparator<ProductEntity>() {
                            @Override
                            public int compare(ProductEntity o1, ProductEntity o2) {
                                return Character.compare(o2.getTitle().toLowerCase().charAt(0), o1.getTitle().toLowerCase().charAt(0));
                            }
                        });
                    }
                }
                else if (sortComboBox.getSelectedIndex() == 2){
                    if (sortBool){
                        model.setSorter(new Comparator<ProductEntity>() {
                            @Override
                            public int compare(ProductEntity o1, ProductEntity o2) {
                                return Integer.compare(o1.getWorkshopNumber(),o2.getWorkshopNumber());
                            }
                        });
                    }
                    else {
                        model.setSorter(new Comparator<ProductEntity>() {
                            @Override
                            public int compare(ProductEntity o1, ProductEntity o2) {
                                return Integer.compare(o2.getWorkshopNumber(),o1.getWorkshopNumber());
                            }
                        });
                    }
                }
                else if (sortComboBox.getSelectedIndex() == 3){
                    if (sortBool){
                            model.setSorter(new Comparator<ProductEntity>() {
                                @Override
                                public int compare(ProductEntity o1, ProductEntity o2) {
                                    return Double.compare(o1.getMinCostForAgent(), o2.getMinCostForAgent());
                                }
                            });
                    }
                    else {
                        model.setSorter(new Comparator<ProductEntity>() {
                            @Override
                            public int compare(ProductEntity o1, ProductEntity o2) {
                                return Double.compare(o2.getMinCostForAgent(), o1.getMinCostForAgent());
                            }
                        });
                    }
                }
                else model.setSorter(null);
            }
        });

        filterComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                model.updateTable();
            }
        });

        titleField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                model.updateTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                model.updateTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                model.updateTable();
            }
        });
    }
}
