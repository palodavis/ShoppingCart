package model.dao;

import model.entities.Product;

public interface ProductDao {
    void insert(Product obj);
    void update(Product obj);
    void delete(Integer id);
    Product searchId(Integer id);

}
