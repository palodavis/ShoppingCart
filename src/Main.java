import controller.MainController;
import model.Product;
import model.ShoppingCart;
import model.dao.DaoFactory;

public class Main {
    public static void main(String[] args) {
        MainController controller = new MainController(
                DaoFactory.createProductDao(),
                DaoFactory.createShoppingDao()
        );

        System.out.println("1 - Test product insertion:");
        Product newProduct = new Product(null, "Notebook", "Computing", 3500, 2);
        controller.addProduct(newProduct);

        System.out.println("2 - Test product deletion:");
        controller.deleteProduct(3);

        System.out.println("3 - Test product update:");
        Product product = DaoFactory.createProductDao().searchId(2);
        product.setName("Laptop");
        controller.updateProduct(product);

        System.out.println("4 - Search product by ID:");
        controller.searchId(2);

        ShoppingCart cart = new ShoppingCart(null, 1, product);
        controller.addProductToCart(cart);
    }
}
