package ru.vitoliot.desktop.ui;

import ru.vitoliot.desktop.entities.ServiceEntity;
import ru.vitoliot.desktop.manager.ServiceManager;
import ru.vitoliot.desktop.utilities.BaseForm;
import ru.vitoliot.desktop.utilities.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class UpdateForm extends BaseForm {
    private JPanel mainPanel;
    private JTextField costField;
    private JTextField titleField;
    private JSpinner durationSpinner;
    private JTextField pathField;
    private JEditorPane descPane;
    private JButton exitButton;
    private JButton confirmButton;
    private JSpinner discSpinner;
    private JButton deleteButton;
    private JTextField idField;
    private JLabel iconLabel;

    private ServiceEntity service;
    public UpdateForm(ServiceEntity service) {
        super(500, 600);
        this.service = service;
        setContentPane(mainPanel);
        initFields();
        initButtons();
        setVisible(true);
    }
    public void initFields(){
        idField.setEditable(false);
        idField.setText(String.valueOf(service.getID()));
        titleField.setText(service.getTitle());
        costField.setText(String.valueOf(service.getCost()));
        durationSpinner.setValue(service.getDurationInSeconds());
        pathField.setText(service.getMainImagePath());
        discSpinner.setValue(service.getDiscount());
        descPane.setText(service.getDescription());
        iconLabel.setIcon(service.getImage());
        iconLabel.setText("Картинка");
    }
    public void initButtons(){
        deleteButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Вы уверены?", "Удаление", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                try {
                    ServiceManager.delete(service);
                    DialogUtil.showInfo(this, "Success delete");
                    dispose();
                    new ServiceTable();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                    DialogUtil.showWarning(this, "Not today" + ex.getMessage());
                }
            }
        });
        exitButton.addActionListener(e -> {
            dispose();
            new ServiceTable();
        });
        confirmButton.addActionListener(e -> {
            String title = titleField.getText();
            if (title == null || title.isEmpty() || title.length() > 100){
                DialogUtil.showWarning(this, "Поле названия услуги не ведено или слишком длинное");
                return;
            }
            double cost = -1;
            try {
                cost = Double.parseDouble(costField.getText());
            }
            catch (Exception ex) {
                DialogUtil.showWarning(this, "где цена, брат?");
                return;
            }
            if (cost < 0) {
                DialogUtil.showWarning(this, "это не цены на нефть, чтобы меньше нуля падать");
                return;
            }
            int dur = (int) durationSpinner.getValue();
            if (dur > 4 * 60 * 60 || dur < 0) {
                DialogUtil.showWarning(this, "Продолжительность больше 4 часов, либо отрицательна");
                return;
            }
            String desc = descPane.getText();
            int disc = (int) discSpinner.getValue();
            if (disc > 100 || disc < 0) {
                DialogUtil.showWarning(this, "Прикольные у вас проценты");
                return;
            }
            String path = pathField.getText();
            if (path.length() > 1000){
                    DialogUtil.showWarning(this, "Путь твой длинный");
                    return;
                }
            try {
                ServiceManager.insert(new ServiceEntity(-1, title, cost, dur, desc, disc, path));
                DialogUtil.showInfo(this, "Вы это видели? Запись добавлена. Нажмите ОК, взгляните самостоятельно!");
                dispose();
                new ServiceTable();
            } catch (SQLException ex) {
                DialogUtil.showWarning(this, "Query Error");
            }
        });
    }
}
