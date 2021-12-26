package ru.vitoliot.desktop.ui;

import ru.vitoliot.desktop.entities.ServiceEntity;
import ru.vitoliot.desktop.manager.ServiceManager;
import ru.vitoliot.desktop.utilities.BaseForm;
import ru.vitoliot.desktop.utilities.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class InsertForm extends BaseForm {
    private JPanel mainPanel;
    private JTextField costField;
    private JTextField titleField;
    private JSpinner durationSpinner;
    private JTextField pathField;
    private JEditorPane descPane;
    private JButton exitButton;
    private JButton confirmButton;
    private JSpinner discSpinner;

    public InsertForm() {
        super(500, 600);

        setContentPane(mainPanel);
        initButtons();
        setVisible(true);
    }

    public void initButtons(){
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
