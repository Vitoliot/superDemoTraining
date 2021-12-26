package ru.vitoliot.desktop.ui;

import ru.vitoliot.desktop.entities.ClientServiceEntity;
import ru.vitoliot.desktop.manager.ClientServiceManager;
import ru.vitoliot.desktop.utilities.AdminCellRenderer;
import ru.vitoliot.desktop.utilities.BaseForm;
import ru.vitoliot.desktop.utilities.DialogUtil;
import ru.vitoliot.desktop.utilities.ExtendenTableModel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;

public class AdminTable extends BaseForm {
    private JPanel mainPanel;
    private JTable adminTable;
    private JButton backButton;
    private JLabel countLabel;

    private ExtendenTableModel<ClientServiceEntity> model;
    private final AdminCellRenderer renderer = new AdminCellRenderer();

    public AdminTable() {
        super(1200, 1000);
        setContentPane(mainPanel);
        initTable();
        initButtons();
        setTimer();
        setVisible(true);
    }

    private void initTable()  {
        adminTable.getTableHeader().setReorderingAllowed(false);
        adminTable.setRowHeight(30);
        this.model = new ExtendenTableModel(ClientServiceEntity.class, new String[]{"ID", "Услуга", "Фамилия", "Имя", "Отчество",
                "email", "телефон", "дата и время записи", "до начала услуги"}){
            @Override
            public void onUpdateRowsEvent() {
                countLabel.setText("Записей: " + model.getFilteredRows().size());
            }
        };
        initFilters();
        try {
            this.model.setAllRows(ClientServiceManager.selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtil.showWarning(this, "Fuck");
        }
        adminTable.setModel(this.model);
        adminTable.getColumnModel().getColumn(8).setCellRenderer(renderer);
    };

    private void setTimer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    model.setAllRows(ClientServiceManager.selectAll());
                } catch (SQLException e) {
                    e.printStackTrace();
                    DialogUtil.showWarning(null, "Fuck");
                }
                adminTable.setModel(model);
                adminTable.getColumnModel().getColumn(8).setCellRenderer(renderer);
            }
        }, 0, 2 * 1000);
    }
    private void initFilters(){
        Predicate<ClientServiceEntity>[] predicates = this.model.getFilters();
        predicates[0] = new Predicate<ClientServiceEntity>() {
            @Override
            public boolean test(ClientServiceEntity clientServiceEntity) {
                Date today = new Date();
                today.setHours(0);
                today.setMinutes(0);
                today.setSeconds(0);
                Date tomorrow = new Date(today.getTime() + 2*24*60*60*1000);
                return clientServiceEntity.getStartTime().after(today) && clientServiceEntity.getStartTime().before(tomorrow);
            }
        };
    }
    private void initButtons(){
        backButton.addActionListener(e -> {
            dispose();
            new ServiceTable();
        });
    }
}
