package controller;

import db.DbException;
import model.entities.CartItem;
import model.entities.Product;
import model.entities.ShoppingCart;
import model.dao.ShoppingDao;

public class ShoppingController {
    private ShoppingDao shoppingDao;

    public ShoppingController(ShoppingDao shoppingDao) {
        this.shoppingDao = shoppingDao;
    }

    public void addProductToCart(ShoppingCart cart, CartItem item) {
        cart.addItem(item);
        try {
            shoppingDao.addProductCart(cart);
        } catch (DbException e) {
            System.err.println("Failed to add product to cart: " + e.getMessage());
        }
    }

    public void updateProductToCart(ShoppingCart shoppingCart) {
        shoppingDao.updateProductCart(shoppingCart);
        System.out.println("Product Updated!");
    }

    public boolean deleteProductFromCart(ShoppingCart cart, Integer productId) {
        if (cart == null || productId == null) {
            System.out.println("Invalid cart or product ID.");
            return false;
        }
        shoppingDao.deleteProductCart(cart.getCart().getIdCart(), productId);
        cart.updateTotalValue();
        System.out.println("Product removed from cart successfully: Product ID " + productId);
        return true;
    }


    public ShoppingCart searchIdProuctCard(int productId) {
        ShoppingCart shoppingCart = shoppingDao.listProductsInCart(productId);
        if (shoppingCart != null) {
            System.out.println("Product in the cart Details: " + shoppingCart);
        }

        return shoppingCart;
    }
}
