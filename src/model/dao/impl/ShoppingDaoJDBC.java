package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.ShoppingDao;
import model.entities.CartItem;
import model.entities.ShoppingCart;

import java.sql.*;

public class ShoppingDaoJDBC implements ShoppingDao {
    private Connection conn;

    public ShoppingDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void addProductCart(ShoppingCart cart) throws DbException {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Shopping Cart or items cannot be null/empty");
        }

        String insertSql = "INSERT INTO shoppingCart (cart_id, product_id, amount, total_value) VALUES (?, ?, ?, ?)";
        String checkStockSql = "SELECT amount, price FROM product WHERE id_product = ?";
        String updateStockSql = "UPDATE product SET amount = amount - ? WHERE id_product = ?";

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            for (CartItem item : cart.getItems()) {
                //System.out.println("Processing product ID: " + item.getProduct().getIdProduct());
                checkStockStmt.setInt(1, item.getProduct().getIdProduct());
                ResultSet rs = checkStockStmt.executeQuery();

                if (rs.next()) {
                    int stock = rs.getInt("amount");
                    double price = rs.getDouble("price");
                    if (item.getAmount() > stock) {
                        throw new DbException("Insufficient stock for product ID: " + item.getProduct().getIdProduct());
                    }

                    double totalValue = item.getAmount() * price;

                    insertStmt.setInt(1, cart.getCart().getIdCart());
                    insertStmt.setInt(2, item.getProduct().getIdProduct());
                    insertStmt.setInt(3, item.getAmount());
                    insertStmt.setDouble(4, totalValue);

                    updateStockStmt.setInt(1, item.getAmount());
                    updateStockStmt.setInt(2, item.getProduct().getIdProduct());
                    updateStockStmt.executeUpdate();

                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            item.getProduct().setIdProduct(id);
                        }
                        DB.closeResultSet(generatedKeys);
                    }
                }
                DB.closeResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DbException("Error inserting into shopping cart: " + e.getMessage());
        }
    }


    @Override
    public void updateProductCart(ShoppingCart cart) throws DbException {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Shopping Cart or items cannot be null/empty");
        }

        String selectCurrentSql = "SELECT amount FROM shoppingCart WHERE cart_id = ? AND product_id = ?";
        String checkStockSql = "SELECT amount FROM product WHERE id_product = ?";
        String updateCartSql = "UPDATE shoppingCart SET amount = ?, total_value = ? WHERE cart_id = ? AND product_id = ?";
        String updateStockSql = "UPDATE product SET amount = amount + ? WHERE id_product = ?";

        try (PreparedStatement selectCurrentStmt = conn.prepareStatement(selectCurrentSql);
             PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement updateCartStmt = conn.prepareStatement(updateCartSql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            for (CartItem item : cart.getItems()) {
                int productId = item.getProduct().getIdProduct();
                int newAmount = item.getAmount();

                selectCurrentStmt.setInt(1, cart.getCart().getIdCart());
                selectCurrentStmt.setInt(2, productId);
                ResultSet currentRs = selectCurrentStmt.executeQuery();

                int currentAmount = 0;
                if (currentRs.next()) {
                    currentAmount = currentRs.getInt("amount");
                } else {
                    throw new DbException("Product not found in the cart: ID " + productId);
                }
                DB.closeResultSet(currentRs);

                checkStockStmt.setInt(1, productId);
                ResultSet stockRs = checkStockStmt.executeQuery();

                int stock = 0;
                if (stockRs.next()) {
                    stock = stockRs.getInt("amount");
                } else {
                    throw new DbException("Product not found: ID " + productId);
                }
                DB.closeResultSet(stockRs);

                int stockChange = currentAmount - newAmount;

                if (stock + stockChange < 0) {
                    throw new DbException("Insufficient stock for product ID: " + productId);
                }
                double totalValue = newAmount * item.getProduct().getPrice();
                updateCartStmt.setInt(1, newAmount);
                updateCartStmt.setDouble(2, totalValue);
                updateCartStmt.setInt(3, cart.getCart().getIdCart());
                updateCartStmt.setInt(4, productId);
                updateCartStmt.executeUpdate();

                updateStockStmt.setInt(1, stockChange);
                updateStockStmt.setInt(2, productId);
                updateStockStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DbException("Error updating product in the shopping cart: " + e.getMessage());
        }
    }

    @Override
    public void deleteProductCart(ShoppingCart cart) {

    }
}