package ru.vitoliot.desktop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ClientEntity {
    int ID;
    String firstName;
    String lastName;
    String patronymic;
    Date registrationDate;
    String email;
    String phone;
}
