import controller.CartController;
import controller.ProductController;
import controller.ShoppingController;
import db.DbException;
import model.dao.DaoFactory;
import model.entities.Product;
import model.entities.Cart;
import model.entities.CartItem;
import model.entities.ShoppingCart;

import java.util.Scanner;

public class ShoppingApp {
    public static void main(String[] args) {
        ProductController pcontroller = new ProductController(DaoFactory.createProductDao());
        ShoppingController scontroller = new ShoppingController(DaoFactory.createShoppingDao());
        CartController ccontroller = new CartController(DaoFactory.createCartDao());

        Scanner scanner = new Scanner(System.in);

        String continueOption;

        do {
            System.out.println("Choose an option:");
            System.out.println("-------Product Stock---------");
            System.out.println("| 1 - Insert new product     |");
            System.out.println("| 2 - Update product         |");
            System.out.println("| 3 - Delete product         |");
            System.out.println("| 4 - View product details   |");
            System.out.println("------Shopping Cart ----------");
            System.out.println("| 5 - Add product to cart    |");
            System.out.println("| 6 - Update product to cart |");
            System.out.println("| 7 - Remove product to cart |");
            System.out.println("| 8 - View cart details      |");
            System.out.println("| 9 - Exit                   |");
            System.out.println("-----------------------------");
            System.out.print("Enter the option number: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter the new product details:");
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Category: ");
                    String category = scanner.nextLine();
                    System.out.print("Price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Quantity: ");
                    int amount = scanner.nextInt();
                    scanner.nextLine();

                    Product newProduct = new Product(null, name, category, price, amount);
                    pcontroller.addProduct(newProduct);
                    break;

                case 2:
                    System.out.print("Enter the ID of the product to update: ");
                    int productIdToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    Product productToUpdate = pcontroller.searchId(productIdToUpdate);

                    if (productToUpdate != null) {
                        System.out.print("New name: ");
                        String newName = scanner.nextLine();
                        System.out.print("New category: ");
                        String newCategory = scanner.nextLine();
                        System.out.print("New price: ");
                        double newPrice = scanner.nextDouble();
                        System.out.print("New quantity: ");
                        int newAmount = scanner.nextInt();
                        scanner.nextLine();

                        productToUpdate.setName(newName);
                        productToUpdate.setCategory(newCategory);
                        productToUpdate.setPrice(newPrice);
                        productToUpdate.setAmount(newAmount);

                        pcontroller.updateProduct(productToUpdate);
                    }

                    break;

                case 3:
                    System.out.print("Enter the ID of the product to delete: ");
                    int productIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    pcontroller.searchId(productIdToDelete);
                    pcontroller.deleteProduct(productIdToDelete);
                    break;

                case 4:
                    System.out.print("Enter the ID of the product to view: ");
                    int productIdToView = scanner.nextInt();
                    scanner.nextLine();
                    pcontroller.searchId(productIdToView);
                    break;

                case 5:
                    Cart cart = new Cart();
                    ccontroller.addCart(cart);
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setCart(cart);
                    boolean addMoreItems = true;
                    while (addMoreItems) {
                        System.out.print("Enter the product ID to add to the cart: ");
                        int productIdToAdd = scanner.nextInt();
                        scanner.nextLine();
                        Product productToAdd = pcontroller.searchId(productIdToAdd);

                        boolean productExistsInCart = shoppingCart.getItems().stream()
                                .anyMatch(item -> item.getProduct().getIdProduct() == productIdToAdd);

                        if (productToAdd != null) {
                            System.out.print("Enter the quantity to add: ");
                            int quantity = scanner.nextInt();
                            scanner.nextLine();

                            CartItem cartItem = new CartItem(productToAdd, quantity);
                            scontroller.addProductToCart(shoppingCart, cartItem);
                        }

                        System.out.print("Do you want to add another product? (yes/no): ");
                        String response = scanner.nextLine().toLowerCase();

                        if (response.equals("no")) {
                            addMoreItems = false;
                        }
                    }
                    System.out.println("\nCart details:");
                    for (CartItem cartItem : shoppingCart.getItems()) {
                        System.out.println("Product: " + cartItem.getProduct().getName() + ", Quantity: " + cartItem.getAmount());
                    }
                    System.out.println("Total value of the shopping cart: " + shoppingCart.getTotalValue());
                    break;

                case 6:
                    System.out.print("Enter the Cart ID: ");
                    int idCart = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter the Product ID to update: ");
                    int productIdUpdate = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter the new quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();

                    Product productCartToUpdate = pcontroller.searchId(productIdUpdate);

                    if (productCartToUpdate != null) {
                        ShoppingCart shoppingCartToUpdate = new ShoppingCart();
                        shoppingCartToUpdate.getCart().setIdCart(idCart);

                        CartItem itemToUpdate = new CartItem(productCartToUpdate, newQuantity);
                        shoppingCartToUpdate.addItem(itemToUpdate);

                        try {
                            scontroller.updateProductToCart(shoppingCartToUpdate, productIdUpdate);
                        } catch (DbException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;


                case 7:
                    System.out.print("Enter the Cart ID: ");
                    int cartId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter the Product ID to remove: ");
                    int productIdToRemove = scanner.nextInt();
                    scanner.nextLine();

                    ShoppingCart shoppingCartToRemove = new ShoppingCart();
                    shoppingCartToRemove.getCart().setIdCart(cartId);

                    boolean removed = scontroller.deleteProductFromCart(shoppingCartToRemove, productIdToRemove);

                    if (removed) {
                        System.out.println("Product removed from the cart successfully!");
                    } else {
                        System.out.println("Product not found in the cart!");
                    }
                    break;

                case 8:
                    System.out.print("Enter the Cart ID to view: ");
                    int cartIdToView = scanner.nextInt();
                    scanner.nextLine();
                    scontroller.searchIdProuctCard(cartIdToView);
                    System.out.println("\nCart details:");
                    scontroller.totalValueCart(cartIdToView);
                    break;

                case 9:
                    System.out.println("Exiting the system.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
            System.out.print("Do you want to perform another operation? (yes/no): ");
            continueOption = scanner.nextLine().toLowerCase();
        } while (continueOption.equals("yes"));

        System.out.println("Thank you for using the system.");
    }
}
