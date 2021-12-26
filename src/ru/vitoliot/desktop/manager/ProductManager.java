package ru.vitoliot.desktop.manager;

import ru.vitoliot.desktop.Main;
import ru.vitoliot.desktop.entities.MaterialEntity;
import ru.vitoliot.desktop.entities.ProductEntity;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    public static List<ProductEntity> selectAll() throws SQLException {
        try(Connection con = Main.getSecondConnection()){
            String sql = "Select * from Product";
            Statement st = con.createStatement();
            st.executeQuery(sql);
            ResultSet resultSet = st.getResultSet();
            List<ProductEntity> productEntities = new ArrayList<>();
            while(resultSet.next()){
                int prod = resultSet.getInt("ID");
                productEntities.add(new ProductEntity(
                        prod,
                        resultSet.getString("Title"),
                        resultSet.getString("ProductTypeID"),
                        resultSet.getString("ArticleNumber"),
                        resultSet.getString("Description"),
                        resultSet.getString("Image"),
                        resultSet.getInt("ProductionPersonCount"),
                        resultSet.getInt("ProductionWorkshopNumber"),
                        resultSet.getDouble("MinCostForAgent"),
                        MaterialManager.selectAllToProduct(prod)
                ));
            }
            return productEntities;
        }
    }

    public static void insert(ProductEntity productEntity) throws SQLException {
        try(Connection connection = Main.getSecondConnection()){
            String sql = "INSERT INTO Product(`Title`, `ProductTypeID`, `ArticleNumber`, `Description`, `Image`, `ProductionPersonCount`, `ProductionWorkshopNumber`, `MinCostForAgent`)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, productEntity.getTitle());
            preparedStatement.setString(2, productEntity.getType());
            preparedStatement.setString(3, productEntity.getArticleNumber());
            preparedStatement.setString(4, productEntity.getDescription());
            preparedStatement.setString(5, productEntity.getImagePath());
            preparedStatement.setInt(6, productEntity.getPersonCount());
            preparedStatement.setInt(7, productEntity.getWorkshopNumber());
            preparedStatement.setDouble(8, productEntity.getMinCostForAgent());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                productEntity.setId(resultSet.getInt(1));
                for(MaterialEntity material:productEntity.getMaterials()){
                    sql = "INSERT INTO `productmaterial` VALUES (?, ?, ?)";
                    PreparedStatement materialStatement = connection.prepareStatement(sql);
                    materialStatement.setInt(1, productEntity.getId());
                    materialStatement.setInt(2, material.getId());
                    materialStatement.setInt(3, material.getCount());
                    materialStatement.executeUpdate();
                }
                return;
            }
            throw new SQLException("Not insert");
        }
    }

    public static void update(ProductEntity productEntity) throws SQLException {
        try(Connection connection = Main.getSecondConnection()){
            String sql = "Update INTO Product SET `Title` = ?, `ProductTypeID`= ?, `ArticleNumber`= ?, `Description`= ?, `Image`= ?, `ProductionPersonCount`= ?, `ProductionWorkshopNumber`= ?, `MinCostForAgent`= ?)" +
                    "WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(9, productEntity.getId());
            preparedStatement.setString(1, productEntity.getTitle());
            preparedStatement.setString(2, productEntity.getType());
            preparedStatement.setString(3, productEntity.getArticleNumber());
            preparedStatement.setString(4, productEntity.getDescription());
            preparedStatement.setString(5, productEntity.getImagePath());
            preparedStatement.setInt(6, productEntity.getPersonCount());
            preparedStatement.setInt(7, productEntity.getWorkshopNumber());
            preparedStatement.setDouble(8, productEntity.getMinCostForAgent());
            preparedStatement.executeUpdate();
            for(MaterialEntity material:productEntity.getMaterials()){
                sql = "UPDATE INTO `productmaterial` SET `MaterialID` = ?, `Count` = ? WHERE `ProductID`=?";
                PreparedStatement materialStatement = connection.prepareStatement(sql);
                materialStatement.setInt(3, productEntity.getId());
                materialStatement.setInt(1, material.getId());
                materialStatement.setInt(2, material.getCount());
                materialStatement.executeUpdate();
            }
        }
    }

    public static void delete(int productId) throws SQLException {
        try(Connection con = Main.getSecondConnection()){
            String sql = "Delete from product where ID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.executeUpdate();
        }
    }
}
