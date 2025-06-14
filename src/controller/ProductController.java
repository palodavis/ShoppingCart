package controller;

import model.entities.Product;
import model.dao.ProductDao;

public class ProductController {
    private ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void addProduct(Product product) {
        productDao.insert(product);
        System.out.println("Product inserted! Id = " + product.getIdProduct());
    }

    public void deleteProduct(int productId) {
        Product product = productDao.searchId(productId);
        if (product != null) {
            productDao.delete(productId);
            System.out.println("Product Deleted! Id = " + productId);
        } else {
            System.out.println("Product not found! Unable to delete.");
        }
    }

    public void updateProduct(Product product) {
        productDao.update(product);
        System.out.println("Product Updated!");
    }

    public Product searchId(int productId) {
        Product product = productDao.searchId(productId);
        if (product != null) {
            System.out.println("Product Details: " + product);
        } else {
            System.out.println("Product not found!");
        }
        return product;
    }

}
