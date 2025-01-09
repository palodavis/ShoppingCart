package model.dao;

import model.entities.ShoppingCart;

public interface ShoppingDao {
    void addProductCart(ShoppingCart obj);
    void updateProductCart(ShoppingCart obj);

}
