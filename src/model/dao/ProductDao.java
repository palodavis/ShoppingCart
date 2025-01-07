package model.dao;

import model.Product;

public interface ProductDao {
    void insert(Product obj);
    void update(Product obj);
    void delete(Integer id);
    Product searchId(Integer id);

}
