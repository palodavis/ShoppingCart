package model.dao.impl;

import db.DbException;
import model.dao.CartDao;
import model.entities.Cart;
import model.entities.Product;

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

    @Override
    public Cart searchCart(Integer id) {
        String sql = ("SELECT * FROM cart WHERE cart.id_cart = ?");
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Cart obj = new Cart();
                obj.setIdCart(rs.getInt("id_cart"));
                return obj;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return null;
    }
}
