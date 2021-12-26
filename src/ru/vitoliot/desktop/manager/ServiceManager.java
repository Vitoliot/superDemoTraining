package ru.vitoliot.desktop.manager;

import ru.vitoliot.desktop.Main;
import ru.vitoliot.desktop.entities.ServiceEntity;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager {
    public static void insert(ServiceEntity service) throws SQLException {
        try(Connection con = Main.getConnection()){
        String sql = "INSERT INTO Service(Title, Cost, DurationInSeconds, Description, Discount, MainImagePath) VALUES (?,?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, service.getTitle());
        ps.setDouble(2, service.getCost());
        ps.setDouble(3, service.getDurationInSeconds());
        ps.setString(4, service.getDescription());
            ps.setDouble(5, service.getDiscount());
            ps.setString(6, service.getMainImagePath());

        ps.executeUpdate();

        ResultSet k = ps.getGeneratedKeys();
        if (k.next()) {
            service.setID(k.getInt(1));
            return;
        }
        throw new SQLException("enitity not insert");
        }
    }

    public static void update(ServiceEntity service) throws SQLException {
        try(Connection con = Main.getConnection()){
            String sql = "UPDATE INTO Service SET Title = ?, Cost = ?, DurationInSeconds = ?, Description = ?, Discount = ?, MainImagePath = ?) WHERE ID = ?";

            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, service.getTitle());
            ps.setDouble(2, service.getCost());
            ps.setDouble(3, service.getDurationInSeconds());
            ps.setString(4, service.getDescription());
            ps.setDouble(5, service.getDiscount());
            ps.setString(6, service.getMainImagePath());
            ps.setInt(7, service.getID());
            ps.executeUpdate();
        }
    }

    public static List<ServiceEntity> selectAll() throws SQLException
    {
        try(Connection c = Main.getConnection())
        {
            String sql = "SELECT * FROM Service";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<ServiceEntity> list = new ArrayList<>();
            while(resultSet.next()) {
                list.add(new ServiceEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getDouble("Cost"),
                        resultSet.getInt("DurationInSeconds"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("Discount"),
                        resultSet.getString("MainImagePath")
                ));
            }

            return list;
        }
    }

    public static void delete(ServiceEntity service) throws SQLException {
        try (Connection con = Main.getConnection()) {
            String sql = "DELETE FROM Service WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, service.getID());
            ps.executeUpdate();
        }
    }
}
