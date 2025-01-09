package controller;

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
        shoppingDao.addProductCart(cart);
        System.out.println("Product added to cart successfully: " + item.getProduct().getName());
    }

    public void updateProductToCart(ShoppingCart shoppingCart) {
        shoppingDao.updateProductCart(shoppingCart);
        System.out.println("Product Updated!");
    }
}
