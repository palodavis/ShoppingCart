package controller;

import model.ShoppingCart;
import model.dao.ShoppingDao;

public class ShoppingController {
    private ShoppingDao shoppingDao;

    public ShoppingController(ShoppingDao shoppingDao) {
        this.shoppingDao = shoppingDao;
    }

    public void addProductToCart(ShoppingCart cart) {
        shoppingDao.addProductCart(cart);
        System.out.println("Product added to cart successfully!");
        System.out.println("Shopping Cart ID: " + cart.getIdShoppingCart());
    }
}
