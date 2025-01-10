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

//        System.out.println("1 - Test product insertion:");
//        Product newProduct1 = new Product(null, "Notebook", "Computing", 3500, 10);
//        pcontroller.addProduct(newProduct1);
//
//        Product newProduct2 = new Product(null, "PC", "Computing", 5000, 10);
//        pcontroller.addProduct(newProduct2);


//        System.out.println("3 - Test product update:");
//        Product product = DaoFactory.createProductDao().searchId(2);
//        product.setName("Laptop");
//        pcontroller.updateProduct(product);

        Product product1 = pcontroller.searchId(1);
        Product product2 = pcontroller.searchId(2);
        Product product3 = pcontroller.searchId(3);
//
        Cart cart = new Cart();
        ccontroller.addCart(cart);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCart(cart);

        CartItem item1 = new CartItem(product1, 3);
        CartItem item2 = new CartItem(product2, 3);
        CartItem item3 = new CartItem(product3, 3);

        scontroller.addProductToCart(shoppingCart, item1);
        scontroller.addProductToCart(shoppingCart, item2);
        scontroller.addProductToCart(shoppingCart, item3);

        System.out.println("Cart before deletion:");
        for (CartItem cartItem : shoppingCart.getItems()) {
            System.out.println("Product: " + cartItem.getProduct().getName() + ", Quantity: " + cartItem.getAmount());
        }

        //scontroller.deleteProductFromCart(shoppingCart, 2);

        System.out.println("Cart after deletion:");
        for (CartItem cartItem : shoppingCart.getItems()) {
            System.out.println("Product: " + cartItem.getProduct().getName() + ", Quantity: " + cartItem.getAmount());
        }

        //item2.setAmount(1);
        //scontroller.updateProductToCart(shoppingCart);

        System.out.println("Total value of the shopping cart: " + shoppingCart.getTotalValue());
    }
}
