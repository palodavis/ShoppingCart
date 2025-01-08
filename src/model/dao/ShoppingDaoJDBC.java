package model.dao;

import db.DB;
import db.DbException;
import model.ShoppingCart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingDaoJDBC implements ShoppingDao {
    private Connection conn;

    public ShoppingDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void addProductCart(ShoppingCart obj) throws DbException {
        if (obj == null) {
            throw new IllegalArgumentException("ShoppingCart object cannot be null");
        }

        String insertSql = "INSERT INTO shoppingCart (amount, product_id) VALUES (?, ?)";
        String checkStockSql = "SELECT amount FROM product WHERE id_product = ?";
        String updateStockSql = "UPDATE product SET amount = amount - ? WHERE id_product = ?";

        try (PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            checkStockStmt.setInt(1, obj.getProduct().getIdProduct());
            ResultSet rs = checkStockStmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("amount");

                if (obj.getAmount() > stock) {
                    throw new DbException("Insufficient stock for product ID: " + obj.getProduct().getIdProduct());
                }

                insertStmt.setInt(1, obj.getAmount());
                insertStmt.setInt(2, obj.getProduct().getIdProduct());
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        obj.setIdShoppingCart(generatedKeys.getInt(1));
                    }
                    DB.closeResultSet(generatedKeys);
                } else {
                    throw new DbException("No rows affected during insert. Insertion failed.");
                }

                updateStockStmt.setInt(1, obj.getAmount());
                updateStockStmt.setInt(2, obj.getProduct().getIdProduct());
                updateStockStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DbException("Error inserting into shopping cart: " + e.getMessage());
        }
    }
}