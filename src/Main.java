import controller.CartController;
import controller.ProductController;
import controller.ShoppingController;
import model.entities.Cart;
import model.entities.CartItem;
import model.entities.Product;
import model.entities.ShoppingCart;
import model.dao.DaoFactory;

public class Main {
    public static void main(String[] args) {
        ProductController pcontroller = new ProductController(DaoFactory.createProductDao());
        ShoppingController scontroller = new ShoppingController(DaoFactory.createShoppingDao());

        CartController ccontroller = new CartController(DaoFactory.createCartDao());
        System.out.println("1 - Test product insertion:");
        Product newProduct = new Product(null, "Notebook", "Computing", 3500, 2);
        pcontroller.addProduct(newProduct);

//       System.out.println("2 - Test product deletion:");
//       pcontroller.deleteProduct(4);

        System.out.println("3 - Test product update:");
        Product product = DaoFactory.createProductDao().searchId(2);
        product.setName("Laptop");
        pcontroller.updateProduct(product);

        Product product1 = DaoFactory.createProductDao().searchId(5);
        Product product2 = DaoFactory.createProductDao().searchId(6);
        Product product3 = DaoFactory.createProductDao().searchId(7);

        Cart cart = new Cart();
        ccontroller.addCart(cart);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCart(cart);

        CartItem item1 = new CartItem(product1, 1);
        CartItem item2 = new CartItem(product2, 1);
        CartItem item3 = new CartItem(product3, 1);

        scontroller.addProductToCart(shoppingCart, item1);
        scontroller.addProductToCart(shoppingCart, item2);
        scontroller.addProductToCart(shoppingCart, item3);

        for (CartItem cartItem : shoppingCart.getItems()) {
            System.out.println("Product: " + cartItem.getProduct().getName() + ", Quantity: " + cartItem.getAmount());
        }

        System.out.println("Total value of the shopping cart: " + shoppingCart.getTotalValue());
    }
}
