package ru.vitoliot.desktop.utilities;

import javax.swing.*;
import java.awt.*;

public class DialogUtil {
    public static void showInfo(Component comp, String text){
        JOptionPane.showMessageDialog(comp, text, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(Component comp, String text){
        JOptionPane.showMessageDialog(comp, text, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
