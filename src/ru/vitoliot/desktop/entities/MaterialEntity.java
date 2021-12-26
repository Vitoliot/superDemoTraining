package ru.vitoliot.desktop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MaterialEntity {
    int id;
    String Title; int countInPack; String Unit; int countInStock; int minCount; String description; double cost;
    String type;

    int count;

    public MaterialEntity(int id, String title, int countInPack, String unit, int countInStock, int minCount, String description, double cost, String type) {
        this.id = id;
        Title = title;
        this.countInPack = countInPack;
        Unit = unit;
        this.countInStock = countInStock;
        this.minCount = minCount;
        this.description = description;
        this.cost = cost;
        this.type = type;
        this.count = 0;
    }

    public MaterialEntity(int id, String title, int countInPack, String unit, int countInStock, int minCount, String description, double cost, String type, int count) {
        this.id = id;
        Title = title;
        this.countInPack = countInPack;
        Unit = unit;
        this.countInStock = countInStock;
        this.minCount = minCount;
        this.description = description;
        this.cost = cost;
        this.type = type;
        this.count = count;
    }

    public MaterialEntity(int id, String title, int count) {
        this.id = id;
        Title = title;
        this.count = count;
    }

    @Override
    public String toString() {
        return Title + " " + count;
    }
}
