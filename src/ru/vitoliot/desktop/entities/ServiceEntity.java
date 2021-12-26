package ru.vitoliot.desktop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Data
public class ServiceEntity {
    int ID;
    String Title;
    double Cost;
    int DurationInSeconds;
    String Description;
    double Discount;
    String MainImagePath;

    private ImageIcon image;

    public ServiceEntity(int ID, String title, double cost, int durationInSeconds, String description, double discount, String mainImagePath) {
        this.ID = ID;
        this.Title = title;
        this.Cost = cost;
        this.DurationInSeconds = durationInSeconds;
        this.Description = description;
        this.Discount = discount;
        this.MainImagePath = mainImagePath;

        try {
            this.image = new ImageIcon(ImageIO.read(Objects.requireNonNull(ServiceEntity.class.getClassLoader().getResource(mainImagePath)))
                    .getScaledInstance(100,100, Image.SCALE_DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
