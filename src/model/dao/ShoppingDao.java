package model.dao;

import model.entities.ShoppingCart;

public interface ShoppingDao {
    void addProductCart(ShoppingCart obj);
    void updateProductCart(ShoppingCart obj);
    void deleteProductCart(int cartId, int productId);
    ShoppingCart listProductsInCart(int cartId);
}
