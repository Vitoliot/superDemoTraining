package ru.vitoliot.desktop.ui;

import ru.vitoliot.desktop.entities.MaterialEntity;
import ru.vitoliot.desktop.entities.ProductEntity;
import ru.vitoliot.desktop.manager.MaterialManager;
import ru.vitoliot.desktop.manager.ProductManager;
import ru.vitoliot.desktop.utilities.BaseForm;
import ru.vitoliot.desktop.utilities.DialogUtil;
import ru.vitoliot.desktop.utilities.ExtendedListModel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * форма для инсерта
 */
public class InsertNamordnikForm extends BaseForm {
    private JPanel mainPanel;
    private JButton backButton;
    private JButton addButton;
    private JTextField titleField;
    private JTextField articleField;
    private JSpinner peopleSpinner;
    private JTextField imageField;
    private JTextPane descPane;
    private JTextField costField;
    private JSpinner workSpinner;
    private JButton materialButton;
    private JComboBox typeComboBox;
    private JList list1;
    private JSpinner spinner1;
    private JComboBox materialComboBox;

    private List<String> types;
    private List<MaterialEntity> materials = new ArrayList<>();
    private List<MaterialEntity> allMaterials;

    public InsertNamordnikForm(List<String> types) {
        super(600, 800);
        setContentPane(mainPanel);
        this.types = types;
        initComboBox();
        initButtons();
        setVisible(true);
    }

    public void initButtons(){
        backButton.addActionListener(e -> {dispose(); new ProductionTableForm();});
        materialButton.addActionListener(e -> {
            MaterialEntity mat = allMaterials.get(materialComboBox.getSelectedIndex());
            int count = (int) spinner1.getValue();
            if (count < 0){
                DialogUtil.showWarning(this, "error");
            }
            else{
                materials.add(new MaterialEntity(mat.getId(), mat.getTitle(), (int) spinner1.getValue()));
                initList();
            }

        });
        addButton.addActionListener(e -> {

            if (titleField.getText().isEmpty() || titleField.getText().length() > 100){
                DialogUtil.showWarning(this, "Error");
                return;
            }
            if (articleField.getText().isEmpty() || articleField.getText().length() > 10){
                DialogUtil.showWarning(this, "Error");
                return;
            }
            if ((int) peopleSpinner.getValue()<=0){
                DialogUtil.showWarning(this, "Error");
                return;
            }
            if ((int) workSpinner.getValue()<=0){
                DialogUtil.showWarning(this, "Error");
                return;
            }
            if (imageField.getText().length() > 100){
                DialogUtil.showWarning(this, "Error");
                return;
            }
            if (Double.parseDouble(costField.getText())<=0){
                DialogUtil.showWarning(this, "Error");
                return;
            }
            System.out.println(materials);
            if (materials.isEmpty()){
                return;
            }
            try {
                ProductManager.insert(new ProductEntity(
                        titleField.getText(),
                        (String) typeComboBox.getSelectedItem(),
                        articleField.getText(),
                        descPane.getText(),
                        imageField.getText(),
                        (int) peopleSpinner.getValue(),
                        (int) workSpinner.getValue(),
                        Double.parseDouble(costField.getText()),
                        materials));
            } catch (SQLException ex) {
                DialogUtil.showWarning(this, "err");
                ex.printStackTrace();
            }
        });

    }

    /**
     * лист для материалов
     */
    public void initList(){
        ExtendedListModel<MaterialEntity> model = new ExtendedListModel<MaterialEntity>(MaterialEntity.class);
        model.setAllRows(materials);
        list1.setModel(model);
    }
    public void initComboBox(){
        for(String type:types){
            typeComboBox.addItem(type);
        }

        try {
            allMaterials = MaterialManager.selectAll();
            for(MaterialEntity material: allMaterials){
                materialComboBox.addItem(material.getTitle());
            }
        } catch (SQLException e) {
            DialogUtil.showWarning(this, "Error" + e.getMessage());
            e.printStackTrace();
        }
    }
}
