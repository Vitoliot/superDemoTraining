package ru.vitoliot.desktop.manager;

import ru.vitoliot.desktop.Main;
import ru.vitoliot.desktop.entities.ClientServiceEntity;
import ru.vitoliot.desktop.entities.ServiceEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientServiceManager  {

    static public List<ClientServiceEntity> selectAll() throws SQLException{

        try (Connection con = Main.getConnection()){
            String sql =
                    "select ClientService.ID, service.Title, client.FirstName, client.LastName, client.patronymic, client.email, client.phone, StartTime from clientservice \n" +
                    "JOIN  service ON ServiceID = Service.ID\n" +
                    "JOIN  client ON ClientID = Client.ID;";
            Statement statement = con.createStatement();
            ResultSet res = statement.executeQuery(sql);
            List<ClientServiceEntity> entities = new ArrayList<>();
            while (res.next()){
                entities.add(new ClientServiceEntity(
                        res.getInt("ID"),
                        res.getString("Title"),
                        res.getString("FirstName"),
                        res.getString("LastName"),
                        res.getString("patronymic"),
                        res.getString("email"),
                        res.getString("phone"),
                        res.getTimestamp("StartTime")
                ));
            }
            return entities;
        }
    }
}
