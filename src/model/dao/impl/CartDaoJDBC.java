package model.dao.impl;

import db.DbException;
import model.dao.CartDao;
import model.entities.Cart;

import java.sql.*;

public class CartDaoJDBC implements CartDao {
    private Connection conn;

    public CartDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insertCart(Cart cart) {
        String sql = "INSERT INTO cart () VALUES ()";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                int idCart = rs.getInt(1);
                cart.setIdCart(idCart);
            }
        } catch (SQLException e) {
            throw new DbException("Error creating cart: " + e.getMessage());
        }
    }
}
