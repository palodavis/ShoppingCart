import controller.MainController;
import controller.ProductController;
import controller.ShoppingController;
import model.CartItem;
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
////
//        System.out.println("2 - Test product deletion:");
//        pcontroller.deleteProduct(3);
//
//        System.out.println("3 - Test product update:");
//        Product product = DaoFactory.createProductDao().searchId(2);
//        product.setName("Laptop");
//        pcontroller.updateProduct(product);

        Product product1 = DaoFactory.createProductDao().searchId(1);
        Product product2 = DaoFactory.createProductDao().searchId(2);

        ShoppingCart cart = new ShoppingCart(1);

        CartItem item1 = new CartItem(product1, 2);
        CartItem item2 = new CartItem(product2, 2);
        scontroller.addProductToCart(cart, item1);
        scontroller.addProductToCart(cart, item2);

        for (CartItem cartItem : cart.getItems()) {
            System.out.println("Product: " + cartItem.getProduct().getName() + ", Quantity: " + cartItem.getAmount());
        }

        System.out.println("Total value of the shopping cart: " + cart.getTotalValue());
    }
}
