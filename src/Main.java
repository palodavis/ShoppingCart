import controller.MainController;
import controller.ProductController;
import controller.ShoppingController;
import model.Product;
import model.ShoppingCart;
import model.dao.DaoFactory;

public class Main {
    public static void main(String[] args) {
        ProductController pcontroller = new ProductController(DaoFactory.createProductDao());
        ShoppingController scontroller = new ShoppingController(DaoFactory.createShoppingDao());

//        System.out.println("1 - Test product insertion:");
//        Product newProduct = new Product(null, "Notebook", "Computing", 3500, 2);
//        pcontroller.addProduct(newProduct);
//
//        System.out.println("2 - Test product deletion:");
//        pcontroller.deleteProduct(3);
//
//        System.out.println("3 - Test product update:");
//        Product product = DaoFactory.createProductDao().searchId(2);
//        product.setName("Laptop");
//        pcontroller.updateProduct(product);

        Product product = DaoFactory.createProductDao().searchId(11);
        System.out.println("4 - Search product by ID:");

        ShoppingCart cart = new ShoppingCart(null, 1, product);
        scontroller.addProductToCart(cart);
    }
}
