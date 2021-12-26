package ru.vitoliot.desktop.entities;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Data
public class ProductEntity {
    int id;
    String title;
    String type; String ArticleNumber; String Description; String imagePath; int personCount; int workshopNumber;
    double minCostForAgent;

    double price = 0;
    List<MaterialEntity> materials;
    ImageIcon image;

    public ProductEntity(int id, String title, String type, String articleNumber, String description, String imagePath, int personCount, int workshopNumber, double minCostForAgent, List<MaterialEntity> materials) {
        this.id = id;
        this.title = title;
        this.type = type;
        ArticleNumber = articleNumber;
        Description = description;
        this.imagePath = imagePath.replace('/', '\\');
        this.personCount = personCount;
        this.workshopNumber = workshopNumber;
        this.minCostForAgent = minCostForAgent;
        this.materials = materials;

        if(!materials.isEmpty()){
            for (MaterialEntity material:materials){
                price += material.cost*material.count;
            }
        }
        System.out.println(this.imagePath);
        try {
            this.image = new ImageIcon(ImageIO.read(ProductEntity.class.getClassLoader().getResource(imagePath)).getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        } catch (Exception e) {
            try {
                this.image = new ImageIcon(ImageIO.read(ProductEntity.class.getClassLoader().getResource("picture.png")).getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public ProductEntity(String title, String type, String articleNumber, String description, String imagePath, int personCount, int workshopNumber, double minCostForAgent){
        this.id = -1;
        this.title = title;
        this.type = type;
        ArticleNumber = articleNumber;
        Description = description;
        this.imagePath = imagePath;
        this.personCount = personCount;
        this.workshopNumber = workshopNumber;
        this.minCostForAgent = minCostForAgent;

        this.price = 0;
        materials = null;
        try {
            image = new ImageIcon(ImageIO.read(Objects.requireNonNull(ProductEntity.class.getClassLoader().getResource(imagePath))).getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ProductEntity(String title, String type, String articleNumber, String description, String imagePath, int personCount, int workshopNumber, double minCostForAgent, List<MaterialEntity> materials){
        this.id = -1;
        this.title = title;
        this.type = type;
        ArticleNumber = articleNumber;
        Description = description;
        this.imagePath = imagePath;
        this.personCount = personCount;
        this.workshopNumber = workshopNumber;
        this.minCostForAgent = minCostForAgent;


        this.price = 0;
        this.materials = materials;
        try {
            image = new ImageIcon(ImageIO.read(Objects.requireNonNull(ProductEntity.class.getClassLoader().getResource(imagePath))).getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
