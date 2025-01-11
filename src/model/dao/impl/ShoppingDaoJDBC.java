package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.ShoppingDao;
import model.entities.CartItem;
import model.entities.ShoppingCart;

import java.sql.*;
import java.util.Scanner;

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

        String checkCartSql = "SELECT product_id FROM shoppingCart WHERE cart_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shoppingCart (cart_id, product_id, amount, total_value) VALUES (?, ?, ?, ?)";
        String checkStockSql = "SELECT amount, price FROM product WHERE id_product = ?";
        String updateStockSql = "UPDATE product SET amount = amount - ? WHERE id_product = ?";

        try (PreparedStatement checkCartStmt = conn.prepareStatement(checkCartSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            for (CartItem item : cart.getItems()) {
                //System.out.println("Processing product ID: " + item.getProduct().getIdProduct());

                checkCartStmt.setInt(1, cart.getCart().getIdCart());
                checkCartStmt.setInt(2, item.getProduct().getIdProduct());
                ResultSet rsCartCheck = checkCartStmt.executeQuery();

                //check product id in the cart
                if (rsCartCheck.next()) {
                    System.out.println("Product with ID " + item.getProduct().getIdProduct() + " is already in the cart.");
                    continue;
                }

                checkStockStmt.setInt(1, item.getProduct().getIdProduct());
                ResultSet rs = checkStockStmt.executeQuery();

                //check stock in the cart
                if (rs.next()) {
                    int stock = rs.getInt("amount");
                    double price = rs.getDouble("price");

                    while (item.getAmount() <= 0) {
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Invalid quantity for product ID " + item.getProduct().getIdProduct() +
                                ". Please enter a valid quantity (1 or more): ");
                        item.setAmount(sc.nextInt());
                        sc.nextLine();
                    }


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
                    System.out.println("Product added to cart successfully: " + item.getProduct().getName());
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
                    throw new DbException("Product not found in the cart for ID: " + productId);
                }
                DB.closeResultSet(currentRs);

                checkStockStmt.setInt(1, productId);
                ResultSet stockRs = checkStockStmt.executeQuery();

                int stock = 0;
                if (stockRs.next()) {
                    stock = stockRs.getInt("amount");
                } else {
                    throw new DbException("Product not found in stock for ID: " + productId);
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
    public void deleteProductCart(int cartId, int productId) throws DbException {
        String deleteSql = "DELETE FROM shoppingCart WHERE cart_id = ? AND product_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("No product found with the given cart ID and product ID.");
            }
        } catch (SQLException e) {
            throw new DbException("Error deleting product from cart: " + e.getMessage());
        }
    }

    @Override
    public ShoppingCart listProductsInCart(int cartId) {
        String sql = "SELECT p.id_product, p.name, sc.amount, sc.total_value " +
                "FROM shoppingCart sc " +
                "INNER JOIN product p ON sc.product_id = p.id_product " +
                "WHERE sc.cart_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Products in Cart ID " + cartId + ":");
            while (rs.next()) {
                int productId = rs.getInt("id_product");
                String productName = rs.getString("name");
                int amount = rs.getInt("amount");
                double totalValue = rs.getDouble("total_value");

                System.out.println("ID: " + productId + ", Name: " + productName +
                        ", Amount: " + amount + ", Total Value: " + totalValue);
            }
            DB.closeResultSet(rs);
        } catch (SQLException e) {
            throw new DbException("Error listing products in cart: " + e.getMessage());
        }
        return null;
    }

}