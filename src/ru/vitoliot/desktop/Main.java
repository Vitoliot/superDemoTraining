package ru.vitoliot.desktop;

import ru.vitoliot.desktop.entities.ClientServiceEntity;
import ru.vitoliot.desktop.manager.ClientServiceManager;
import ru.vitoliot.desktop.ui.ProductionTableForm;
import ru.vitoliot.desktop.ui.ServiceTable;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * запускащий скрипт
 */
public class Main {
    public static boolean isAdmin;
    public static void main(String[] args)  {
        isAdmin = "0000".equals(JOptionPane.showInputDialog(null, "Введите парольку", "Режим администратора", JOptionPane.QUESTION_MESSAGE));
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeAllFonts(new FontUIResource("Comic Sans MS", Font.TRUETYPE_FONT, 12));
    new ProductionTableForm();
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/testask17", "root", "1234");
    }

    public static Connection getSecondConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/testask27", "root", "1234");
    }

    /**
     * смена всех шрифтов в приложении на требуемый
     * @param font
     */
    public static void changeAllFonts(Font font)
    {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, font);
        }
    }
}
