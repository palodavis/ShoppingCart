package model.dao;

import db.DbException;
import model.entities.ShoppingCart;

public interface ShoppingDao {
    void addProductCart(ShoppingCart obj);
    void updateProductCart(ShoppingCart cart, int productId);
    void deleteProductCart(int cartId, int productId);
    ShoppingCart listProductsInCart(int cartId);

    ShoppingCart totalValueCart(int cartId);
}
