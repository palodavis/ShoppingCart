import controller.MainController;
import controller.ProductController;
import controller.ShoppingController;
import db.DB;
import model.Cart;
import model.CartItem;
import model.Product;
import model.ShoppingCart;
import model.dao.CartDao;
import model.dao.CartDaoJDBC;
import model.dao.DaoFactory;

public class Main {
    public static void main(String[] args) {
        //ProductController pcontroller = new ProductController(DaoFactory.createProductDao());
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

        CartDao cartDao = new CartDaoJDBC(DB.getConnection());

        Cart cart = new Cart();
        cartDao.insertCart(cart);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCart(cart);

        CartItem item1 = new CartItem(product1, 2);
        CartItem item2 = new CartItem(product2, 2);

        scontroller.addProductToCart(shoppingCart, item1);
        scontroller.addProductToCart(shoppingCart, item2);

        for (CartItem cartItem : shoppingCart.getItems()) {
            System.out.println("Product: " + cartItem.getProduct().getName() + ", Quantity: " + cartItem.getAmount());
        }

        System.out.println("Total value of the shopping cart: " + shoppingCart.getTotalValue());
    }
}
