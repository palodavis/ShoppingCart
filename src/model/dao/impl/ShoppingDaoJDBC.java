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
        validateCart(cart);

        String checkCartSql = "SELECT product_id FROM shoppingCart WHERE cart_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shoppingCart (cart_id, product_id, amount, total_value) VALUES (?, ?, ?, ?)";
        String checkStockSql = "SELECT amount, price FROM product WHERE id_product = ?";
        String updateStockSql = "UPDATE product SET amount = amount - ? WHERE id_product = ?";

        try (PreparedStatement checkCartStmt = conn.prepareStatement(checkCartSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            for (CartItem item : cart.getItems()) {
                if (isProductInCart(checkCartStmt, cart.getCart().getIdCart(), item.getProduct().getIdProduct())) {
                    //System.out.println("Product with ID " + item.getProduct().getIdProduct() + " is already in the cart.");
                    continue;
                }

                processStockAndAddProduct(checkStockStmt, insertStmt, updateStockStmt, cart, item);
            }
        } catch (SQLException e) {
            throw new DbException("Error inserting into shopping cart: " + e.getMessage());
        }
    }

    private void validateCart(ShoppingCart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Shopping Cart or items cannot be null/empty");
        }
    }

    private boolean isProductInCart(PreparedStatement checkCartStmt, int cartId, int productId) throws SQLException {
        checkCartStmt.setInt(1, cartId);
        checkCartStmt.setInt(2, productId);
        try (ResultSet rs = checkCartStmt.executeQuery()) {
            return rs.next();
        }
    }

    private void processStockAndAddProduct(PreparedStatement checkStockStmt, PreparedStatement insertStmt,
                                           PreparedStatement updateStockStmt, ShoppingCart cart, CartItem item) throws SQLException {
        checkStockStmt.setInt(1, item.getProduct().getIdProduct());
        try (ResultSet rs = checkStockStmt.executeQuery()) {
            if (rs.next()) {
                int stock = rs.getInt("amount");
                double price = rs.getDouble("price");

                validateItemQuantity(item, stock);

                double totalValue = item.getAmount() * price;

                insertProductIntoCart(insertStmt, cart, item, totalValue);
                updateProductStock(updateStockStmt, item);
            } else {
                throw new DbException("Product not found in stock for ID: " + item.getProduct().getIdProduct());
            }
        }
    }

    private void validateItemQuantity(CartItem item, int stock) {
        while (item.getAmount() <= 0) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Invalid quantity for product ID " + item.getProduct().getIdProduct() +
                    ". Please enter a valid quantity (1 or more): ");
            item.setAmount(sc.nextInt());
        }

        if (item.getAmount() > stock) {
            throw new DbException("Insufficient stock for product ID: " + item.getProduct().getIdProduct());
        }
    }

    private void insertProductIntoCart(PreparedStatement insertStmt, ShoppingCart cart, CartItem item, double totalValue) throws SQLException {
        insertStmt.setInt(1, cart.getCart().getIdCart());
        insertStmt.setInt(2, item.getProduct().getIdProduct());
        insertStmt.setInt(3, item.getAmount());
        insertStmt.setDouble(4, totalValue);
        insertStmt.executeUpdate();
    }

    private void updateProductStock(PreparedStatement updateStockStmt, CartItem item) throws SQLException {
        updateStockStmt.setInt(1, item.getAmount());
        updateStockStmt.setInt(2, item.getProduct().getIdProduct());
        updateStockStmt.executeUpdate();
    }

    @Override
    public void updateProductCart(ShoppingCart cart, int productId) throws DbException {
        validateCart(cart);

        String selectCurrentSql = "SELECT amount FROM shoppingCart WHERE cart_id = ? AND product_id = ?";
        String checkStockSql = "SELECT amount FROM product WHERE id_product = ?";
        String updateCartSql = "UPDATE shoppingCart SET amount = ?, total_value = ? WHERE cart_id = ? AND product_id = ?";
        String updateStockSql = "UPDATE product SET amount = amount + ? WHERE id_product = ?";

        try (PreparedStatement selectCurrentStmt = conn.prepareStatement(selectCurrentSql);
             PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement updateCartStmt = conn.prepareStatement(updateCartSql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            int cartId = cart.getCart().getIdCart();
            CartItem item = cart.getItems().get(0);

            int currentAmount = getCurrentProductAmount(selectCurrentStmt, cartId, productId);
            int stock = getProductStock(checkStockStmt, productId);

            int stockChange = currentAmount - item.getAmount();
            validateStockAvailability(stock, stockChange, productId);

            double totalValue = item.getAmount() * item.getProduct().getPrice();

            updateCartProduct(updateCartStmt, cartId, productId, item.getAmount(), totalValue);
            updateProductStock(updateStockStmt, stockChange, productId);

        } catch (SQLException e) {
            throw new DbException("Error updating product in the shopping cart: " + e.getMessage());
        }
    }

    private int getCurrentProductAmount(PreparedStatement stmt, int cartId, int productId) throws SQLException {
        stmt.setInt(1, cartId);
        stmt.setInt(2, productId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("amount");
            } else {
                throw new DbException("Product not found in the cart for ID: " + productId);
            }
        }
    }

    private int getProductStock(PreparedStatement stmt, int productId) throws SQLException {
        stmt.setInt(1, productId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("amount");
            } else {
                throw new DbException("Product not found in stock for ID: " + productId);
            }
        }
    }

    private void validateStockAvailability(int stock, int stockChange, int productId) {
        if (stock + stockChange < 0) {
            throw new DbException("Insufficient stock for product ID: " + productId);
        }
    }

    private void updateCartProduct(PreparedStatement stmt, int cartId, int productId, int amount, double totalValue) throws SQLException {
        stmt.setInt(1, amount);
        stmt.setDouble(2, totalValue);
        stmt.setInt(3, cartId);
        stmt.setInt(4, productId);
        stmt.executeUpdate();
    }

    private void updateProductStock(PreparedStatement stmt, int stockChange, int productId) throws SQLException {
        stmt.setInt(1, stockChange);
        stmt.setInt(2, productId);
        stmt.executeUpdate();
    }

    @Override
    public void deleteProductCart(int cartId, int productId) throws DbException {
        String selectAmountSql = "SELECT amount FROM shoppingCart WHERE cart_id = ? AND product_id = ?";
        String deleteSql = "DELETE FROM shoppingCart WHERE cart_id = ? AND product_id = ?";
        String updateStockSql = "UPDATE product SET amount = amount + ? WHERE id_product = ?";

        try (PreparedStatement selectAmountStmt = conn.prepareStatement(selectAmountSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

            int amount = getCurrentProductAmount(selectAmountStmt, cartId, productId);

            deleteCartProduct(deleteStmt, cartId, productId);
            updateProductStock(updateStockStmt, amount, productId);

            System.out.println("Product removed from cart successfully: Product ID " + productId);

        } catch (SQLException e) {
            throw new DbException("Error deleting product from cart and updating stock: " + e.getMessage());
        }
    }

    private void deleteCartProduct(PreparedStatement stmt, int cartId, int productId) throws SQLException {
        stmt.setInt(1, cartId);
        stmt.setInt(2, productId);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new DbException("No product found with the given cart ID and product ID.");
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

    @Override
    public ShoppingCart totalValueCart(int cartId) {
        String sql = "SELECT sc.cart_id AS cart_id, " +
                "       SUM(sc.total_value) AS total_cart_value " +
                "FROM shoppingCart sc " +
                "WHERE sc.cart_id = ? " +
                "GROUP BY sc.cart_id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.getCart().setIdCart(cartId);
            if (rs.next()) {
                double totalValue = rs.getDouble("total_cart_value");
                shoppingCart.setTotalValue(totalValue);
                System.out.println("Total Value of Cart ID " + cartId + ": " + totalValue);
            } else {
                System.out.println("Cart ID " + cartId + " is empty or does not exist.");
            }

            DB.closeResultSet(rs);
            return shoppingCart;
        } catch (SQLException e) {
            throw new DbException("Error calculating total value of cart: " + e.getMessage());
        }
    }
}