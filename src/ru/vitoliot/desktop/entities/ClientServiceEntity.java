package ru.vitoliot.desktop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.vitoliot.desktop.utilities.MyFirstTime;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class ClientServiceEntity {
    int ID;
    String Title;
    String FirstName;
    String LastName;
    String patronymic;
    String email;
    String phone;
    Timestamp StartTime;
    MyFirstTime timeToBegin;

    public ClientServiceEntity(int ID, String title, String firstName, String lastName, String patronymic, String email, String phone, Timestamp startTime) {
        this.ID = ID;
        this.Title = title;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.StartTime = startTime;
        this.timeToBegin = new MyFirstTime(StartTime.getTime() - new Date().getTime());
    }
}
