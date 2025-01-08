package model.dao;

import db.DB;
import db.DbException;
import model.CartItem;
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
    public void addProductCart(ShoppingCart cart) throws DbException {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("ShoppingCart or items cannot be null/empty");
        }

        String insertSql = "INSERT INTO shoppingCart (id_shopping, product_id, amount) VALUES (?, ?, ?)";
        String checkStockSql = "SELECT amount FROM product WHERE id_product = ?";
        String updateStockSql = "UPDATE product SET amount = amount - ? WHERE id_product = ?";

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            for (CartItem item : cart.getItems()) {
                checkStockStmt.setInt(1, item.getProduct().getIdProduct());
                ResultSet rs = checkStockStmt.executeQuery();

                if (rs.next()) {
                    int stock = rs.getInt("amount");
                    if (item.getAmount() > stock) {
                        throw new DbException("Insufficient stock for product ID: " + item.getProduct().getIdProduct());
                    }

                    insertStmt.setInt(1, cart.getIdShoppingCart());
                    insertStmt.setInt(2, item.getProduct().getIdProduct());
                    insertStmt.setInt(3, item.getAmount());
                    insertStmt.executeUpdate();

                    updateStockStmt.setInt(1, item.getAmount());
                    updateStockStmt.setInt(2, item.getProduct().getIdProduct());
                    updateStockStmt.executeUpdate();
                }
                DB.closeResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DbException("Error inserting into shopping cart: " + e.getMessage());
        }
    }
}
