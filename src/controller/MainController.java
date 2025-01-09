package controller;

import model.entities.Product;
import model.entities.ShoppingCart;
import model.dao.ProductDao;
import model.dao.ShoppingDao;

public class MainController {
    private ProductDao productDao;
    private ShoppingDao shoppingDao;

    public MainController(ProductDao productDao, ShoppingDao shoppingDao) {
        this.productDao = productDao;
        this.shoppingDao = shoppingDao;
    }

    public void addProduct(Product product) {
        productDao.insert(product);
        System.out.println("Inserted! Id = " + product.getIdProduct());
    }

    public void deleteProduct(int productId) {
        productDao.delete(productId);
        System.out.println("Product Deleted!");
    }

    public void updateProduct(Product product) {
        productDao.update(product);
        System.out.println("Product Updated!");
    }

    public void searchId(int productId) {
        Product product = productDao.searchId(productId);
        if (product != null) {
            System.out.println("Product Details: " + product);
        } else {
            System.out.println("Product not found!");
        }
    }

    public void addProductToCart(ShoppingCart cart) {
        shoppingDao.addProductCart(cart);
        System.out.println("Product added to cart successfully!");
        System.out.println("Shopping Cart ID: " + cart.getCart());
    }
}
