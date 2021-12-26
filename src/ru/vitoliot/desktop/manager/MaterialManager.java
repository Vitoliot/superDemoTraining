package ru.vitoliot.desktop.manager;

import ru.vitoliot.desktop.Main;
import ru.vitoliot.desktop.entities.MaterialEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialManager {
    static public List<MaterialEntity> selectAll() throws SQLException {
        try(Connection con = Main.getSecondConnection()){
            String sql = "Select * from Material";
            Statement st = con.createStatement();
            st.executeQuery(sql);
            ResultSet resultSet = st.getResultSet();
            List<MaterialEntity> materialEntities = new ArrayList<>();
            while(resultSet.next()){
                materialEntities.add(new MaterialEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getInt("CountInPack"),
                        resultSet.getString("Unit"),
                        resultSet.getInt("CountInStock"),
                        resultSet.getInt("MinCount"),
                        resultSet.getString("Description"), resultSet.getDouble("Cost"),
                        resultSet.getString("MaterialTypeID")
                ));
            }
            return materialEntities;
        }
    }

    static public List<MaterialEntity> selectAllToProduct(int ProductId) throws SQLException {
        try(Connection con = Main.getSecondConnection()){
            String sql =
                    "SELECT `ID`, `Title`, `CountInPack`, `Unit`, `CountInStock`, `MinCount`, `Description`, `Cost`, `Image`, `MaterialTypeID`, `productmaterial`.`Count` " +
                            "FROM material inner join productmaterial ON productmaterial.MaterialID = Material.ID WHERE MaterialID = material.ID and ProductID = " + ProductId + ";";
            PreparedStatement st = con.prepareStatement(sql);
            st.executeQuery(sql);
            ResultSet resultSet = st.getResultSet();
            List<MaterialEntity> materialEntities = new ArrayList<>();
            while(resultSet.next()){
                materialEntities.add(new MaterialEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Title"),
                        resultSet.getInt("CountInPack"),
                        resultSet.getString("Unit"),
                        resultSet.getInt("CountInStock"),
                        resultSet.getInt("MinCount"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("Cost"),
                        resultSet.getString("MaterialTypeID"),
                        resultSet.getInt("Count")
                ));
            }
            return materialEntities;
        }
    }
}
