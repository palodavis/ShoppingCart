package model.dao;

import model.entities.Cart;

public interface CartDao {
    void insertCart(Cart obj);
    Cart searchCart(Integer id);
}
