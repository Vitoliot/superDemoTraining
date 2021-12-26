package ru.vitoliot.desktop.ui;

import ru.vitoliot.desktop.Main;
import ru.vitoliot.desktop.entities.ServiceEntity;
import ru.vitoliot.desktop.manager.ServiceManager;
import ru.vitoliot.desktop.utilities.BaseForm;
import ru.vitoliot.desktop.utilities.CellRenderer;
import ru.vitoliot.desktop.utilities.DialogUtil;
import ru.vitoliot.desktop.utilities.ExtendenTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class ServiceTable extends BaseForm {
    private JTable table;
    private JTextField searchFieldTitle;
    private JButton sort;
    private JComboBox<String> filter;
    private JButton breakButton;
    private JPanel mainPanel;
    private JLabel rowCountLabel;
    private JTextField searchFieldDesc;
    private JButton insertButton;
    private JButton adminButton;
    private JRadioButton addRadioButton;

    private ExtendenTableModel<ServiceEntity> model;
    private final CellRenderer render = new CellRenderer();
    private Character costSort = '0';
    private boolean addClient = false;

    public ServiceTable() {
        super(1200, 1000);
        setContentPane(mainPanel);
        initTable();
        initFilters();
        initButtons();
        setVisible(true);
    }

    public static boolean minmax(double min, double max, double num) {
        return min <= num && num < max;
    }

    public void initTable() {
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(30);

        try {
            model = new ExtendenTableModel<>(ServiceEntity.class, new String[]{"ID", "Название услуги", "Цена", "Продолжительность", "Описание", "Скидка", "Путь до картинки", "Картика"}) {
                @Override
                public void onUpdateRowsEvent() {
                    List<Integer> index = new ArrayList<>();
                    List<ServiceEntity> rows = model.getFilteredRows();
                    for (ServiceEntity service:rows){
                        if (service.getDiscount()>0){
                            index.add(rows.indexOf(service));
                        }
                    }
                    render.setRows(index);
                    rowCountLabel.setText("Записей: " + model.getFilteredRows().size() + "/" + model.getRows().size());
                }
            };
            model.setAllRows(ServiceManager.selectAll());
            table.setModel(model);
            for (int i = 0; i < table.getColumnModel().getColumnCount()-1; i++){
                table.getColumnModel().getColumn(i).setCellRenderer(render);
            }
        } catch (SQLException e) {
            DialogUtil.showWarning(this, "Запрос не прошёл"+e.getMessage());
        }

        TableColumn cal = table.getColumn("Путь до картинки");
        cal.setMinWidth(0);
        cal.setMaxWidth(0);
        cal.setPreferredWidth(0);

        Predicate<ServiceEntity>[] filters = model.getFilters();
        filters[0] = service -> {
            String searchText = searchFieldTitle.getText();
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            String title = service.getTitle().toLowerCase(Locale.ROOT);
            return title.startsWith(searchText.toLowerCase(Locale.ROOT));
        };
        filters[1] = new Predicate<ServiceEntity>() {
            @Override
            public boolean test(ServiceEntity service) {
                String searchText = searchFieldDesc.getText();
                if (searchText == null || searchText.isEmpty()) {
                    return true;
                }
                String title = service.getDescription();
                if (title == null) {return false;}
                return title.toLowerCase(Locale.ROOT).startsWith(searchText.toLowerCase(Locale.ROOT));
            }
        };
        filters[2] = new Predicate<ServiceEntity>() {
            @Override
            public boolean test(ServiceEntity service) {
                int selected = filter.getSelectedIndex();
                if(selected==1){
                    return minmax(0, 5, service.getDiscount());
                }
                else if (selected==2) {
                    return minmax(5, 15, service.getDiscount());
                }
                else if (selected==3) {
                    return minmax(15, 30, service.getDiscount());
                }
                else if (selected==4) {
                    return minmax(30, 70, service.getDiscount());
                }
                else if (selected==5) {
                    return minmax(70, 100, service.getDiscount());
                }
                else {
                    return true;
                }
            }
        };
    }
    public void initButtons(){
        if (Main.isAdmin){
            addRadioButton.addActionListener(e -> {
                addClient = !addClient;
            });
            adminButton.addActionListener(e->{
                dispose();
                new AdminTable();
            });
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount()==2&&!addClient){
                        int row = table.rowAtPoint(e.getPoint());
                        if (row!=-1){
                            dispose();
                            new UpdateForm(model.getRows().get(row));
                        }
                    }
                }
            });
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount()==2&&addClient){
                        int row = table.rowAtPoint(e.getPoint());
                        if (row!=-1){
                            dispose();
                            new ClientServiceInput(model.getRows().get(row));
                        }
                    }
                }
            });
            insertButton.addActionListener(e -> {
                dispose();
                new InsertForm();
            });
        }
        else {
            insertButton.setVisible(false);
            adminButton.setVisible(false);
        }
        sort.addActionListener(e -> {
                if (costSort == null) {
                    costSort = '0';
                }
            if (costSort == '0') {
                model.setSorter((o1, o2) -> Double.compare(o1.getCost(), o2.getCost()));
                costSort = '1';
            }
            else {
                model.setSorter((o1, o2) -> Double.compare(o2.getCost(), o1.getCost()));
                costSort = '0';
            }
        });
        breakButton.addActionListener(e -> {
            searchFieldTitle.setText("");
            searchFieldDesc.setText("");
            filter.setSelectedIndex(0);
            model.setSorter(null);
        });
    }

    public void initFilters()
    {
        searchFieldTitle.getDocument().addDocumentListener(new DocumentListener() {
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

        searchFieldDesc.getDocument().addDocumentListener(new DocumentListener() {
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

        filter.addItem("Все");
        filter.addItem("от 0% до 5%");
        filter.addItem("от 5% до 15%");
        filter.addItem("от 15% до 30%");
        filter.addItem("от 30% до 70%");
        filter.addItem("от 70% до 100%");

        filter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                model.updateTable();
            }
        });
    }
}
