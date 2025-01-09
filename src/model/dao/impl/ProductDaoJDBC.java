package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.ProductDao;
import model.entities.Product;

import java.sql.*;

public class ProductDaoJDBC implements ProductDao {

    private Connection conn;

    public ProductDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product obj) throws DbException {

        if (obj == null) {
            throw new IllegalArgumentException("Product object cannot be null");
        }

        String sql = "INSERT INTO product (name, category, price, amount) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getCategory());
            ps.setDouble(3, obj.getPrice());
            ps.setInt(4, obj.getAmount());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setIdProduct(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("No rows affected during insert. Insertion failed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product obj) throws DbException {
        if (obj == null) {
            throw new IllegalArgumentException("Product object cannot be null");
        }

        String sql = ("UPDATE product SET name = ?, category = ?, price = ?, amount = ? WHERE id_product = ?");
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, obj.getName());
            ps.setString(2, obj.getCategory());
            ps.setDouble(3, obj.getPrice());
            ps.setInt(4, obj.getAmount());
            ps.setInt(5, obj.getIdProduct());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Product searchId(Integer id) {
        String sql = ("SELECT * FROM product WHERE product.id_product = ?");
        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Product obj = new Product();
                obj.setIdProduct(rs.getInt("id_product"));
                obj.setName(rs.getString("name"));
                obj.setCategory(rs.getString("category"));
                obj.setPrice(rs.getDouble("price"));
                obj.setAmount(rs.getInt("amount"));
                return obj;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(Integer id) {

        String sql = ("DELETE FROM product WHERE id_product = ?");

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

}
