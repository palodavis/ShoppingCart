package view;

import controller.CartController;
import controller.ProductController;
import controller.ShoppingController;
import model.dao.DaoFactory;

import java.util.Scanner;

public class MainView {
    private ProductController productController;
    private ShoppingController shoppingController;
    private CartController cartController;
    private Scanner scanner;

    public MainView() {
        this.productController = new ProductController(DaoFactory.createProductDao());
        this.shoppingController = new ShoppingController(DaoFactory.createShoppingDao());
        this.cartController = new CartController(DaoFactory.createCartDao());
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        ProductView productView = new ProductView(productController);
        ShoppingView shoppingView = new ShoppingView(productController, shoppingController, cartController);

        String continueOption;
        do {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            menuOption(choice, productView, shoppingView);

            System.out.print("Do you want to perform another operation? (yes/no): ");
            continueOption = scanner.nextLine().toLowerCase();
        } while (continueOption.equals("yes"));

        System.out.println("Thank you for using the system.");
    }

    private void displayMenu() {
        System.out.println("===============================");
        System.out.println("     Welcome to the Shopping   ");
        System.out.println("         Cart System!          ");
        System.out.println("===============================");

        System.out.println("-------Product Stock-----------");
        System.out.println("| [1] - Insert new product    |");
        System.out.println("| [2] - Update product        |");
        System.out.println("| [3] - Delete product        |");
        System.out.println("| [4] - View product details  |");
        System.out.println("------Shopping Cart -----------");
        System.out.println("| [5] - Add product to cart   |");
        System.out.println("| [6] - Update product to cart|");
        System.out.println("| [7] - Remove product to cart|");
        System.out.println("| [8] - View cart details     |");
        System.out.println("| [9] - Exit                  |");
        System.out.println("-------------------------------");
        System.out.print("Choose an option - Enter the option number (1-9): ");
    }

    private void menuOption(int choice, ProductView productView, ShoppingView shoppingView) {
        switch (choice) {
            case 1 -> productView.insertProduct();
            case 2 -> productView.updateProduct();
            case 3 -> productView.deleteProduct();
            case 4 -> productView.viewProduct();
            case 5 -> shoppingView.addProductToCart();
            case 6 -> shoppingView.updateProductInCart();
            case 7 -> shoppingView.removeProductFromCart();
            case 8 -> shoppingView.viewCartDetails();
            case 9 -> System.out.println("Exiting the system.");
            default -> System.out.println("Invalid option, try again.");
        }
    }
}
