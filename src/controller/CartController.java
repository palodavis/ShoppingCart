package controller;

import model.entities.Cart;
import model.dao.CartDao;

public class CartController {
    private CartDao cartDao;

    public CartController(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    public void addCart(Cart cart) {
        cartDao.insertCart(cart);
        System.out.println("Inserted! IdCart = " + cart.getIdCart());
    }

    public Cart searchIdCart(int cartId) {
        Cart cart = cartDao.searchCart(cartId);
        return cart;
    }
}
