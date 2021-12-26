package ru.vitoliot.desktop.utilities;

import ru.vitoliot.desktop.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class BaseFormProductApp extends JFrame {
    public static String APP_TITLE = Main.isAdmin ? "Магазин масок [Администратор]" : "Магазин масок" ;
    public static Image APP_ICON = null;

    static {
        try {
            APP_ICON = ImageIO.read(Objects.requireNonNull(BaseFormProductApp.class.getClassLoader().getResource("Намордник.png")));
        } catch (IOException e) {
            e.printStackTrace();
            DialogUtil.showWarning(null, "Нет лого в ресурсах");
        }
    }
    public BaseFormProductApp(int width, int height){
        setTitle(APP_TITLE);
        setMinimumSize(new Dimension(width, height));
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width/2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height/2);
        if (APP_ICON != null) {
            setIconImage(APP_ICON);
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
