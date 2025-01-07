import model.Product;
import model.ShoppingCart;
import model.dao.DaoFactory;
import model.dao.ProductDao;
import model.dao.ShoppingDao;

public class Main {
    public static void main(String[] args) {

        System.out.println("1 - Test product inserction: ");
        ProductDao productDao = DaoFactory.createProductDao();
        Product newProduct = new Product(null, "Notebook", "Computing", 3500, 2);
        productDao.insert(newProduct);
        System.out.println("Inserted! Id = " + newProduct.getIdProduct());

        System.out.println("2 - Test product deletion: ");
        productDao.delete(3);
        System.out.println("Product Deleted!");

        System.out.println("3 - Test product update: ");
        Product product = new Product();
        product = productDao.searchId(2);
        product.setName("Laptop");
        productDao.update(product);
        System.out.println("Product Updated!");

        System.out.println("4 - Search product by id: ");
        Product prod = productDao.searchId(2);
        System.out.println(prod);

        //Add product cart
        ShoppingDao shoppingDao =  DaoFactory.createShoppingDao();
        ShoppingCart cart = new ShoppingCart(null, 1, product);
        shoppingDao.addProductCart(cart);
        System.out.println("Product added to cart successfully!");
        System.out.println("Shopping Cart ID: " + cart.getIdShoppingCart());
    }
}