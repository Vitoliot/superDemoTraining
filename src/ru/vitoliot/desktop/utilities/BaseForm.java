package ru.vitoliot.desktop.utilities;

import ru.vitoliot.desktop.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class BaseForm extends JFrame {
    public static String APP_TITLE = Main.isAdmin ? "Школа языков Леарн [Администратор]" : "Школа языков Леарн" ;
    public static Image APP_ICON = null;

    static {
        try {
            APP_ICON = ImageIO.read(Objects.requireNonNull(BaseForm.class.getClassLoader().getResource("school_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
            DialogUtil.showWarning(null, "Нет лого в ресурсах");
        }
    }

    /**
     * базовый конструктор для всех форм, устанавливает ширину, высоту, картинку и название формы
     * @param width
     * @param height
     */
    public BaseForm(int width, int height){
        setTitle(APP_TITLE);
        setMinimumSize(new Dimension(width, height));
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width/2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height/2);
        if (APP_ICON != null) {
            setIconImage(APP_ICON);
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
