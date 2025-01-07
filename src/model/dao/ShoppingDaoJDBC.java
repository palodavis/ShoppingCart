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
    public void addProductCart(ShoppingCart obj) {
        if (obj == null) {
            throw new IllegalArgumentException("ShoppingCart object cannot be null");
        }

        String insertSql = "INSERT INTO shoppingCart (amount, product_id) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, obj.getAmount());
            ps.setInt(2, obj.getProduct().getIdProduct());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setIdShoppingCart(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("No rows affected during insert. Insertion failed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
