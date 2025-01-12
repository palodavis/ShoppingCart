package view;

import controller.CartController;
import controller.ProductController;
import controller.ShoppingController;
import db.DbException;
import model.entities.Cart;
import model.entities.CartItem;
import model.entities.Product;
import model.entities.ShoppingCart;

import java.util.Scanner;

public class ShoppingView {
    private ProductController productController;
    private ShoppingController shoppingController;
    private CartController cartController;
    private Scanner scanner;

    public ShoppingView(ProductController productController, ShoppingController shoppingController, CartController cartController) {
        this.productController = productController;
        this.shoppingController = shoppingController;
        this.cartController = cartController;
        this.scanner = new Scanner(System.in);
    }

    public void addProductToCart() {
        Cart cart = new Cart();
        cartController.addCart(cart);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCart(cart);
        boolean addMoreItems = true;
        while (addMoreItems) {
            System.out.print("Enter the product ID to add to the cart: ");
            int productIdToAdd = scanner.nextInt();
            scanner.nextLine();
            Product productToAdd = productController.searchId(productIdToAdd);

            boolean productExistsInCart = shoppingCart.getItems().stream()
                    .anyMatch(item -> item.getProduct().getIdProduct() == productIdToAdd);

            if (productToAdd != null) {
                System.out.print("Enter the quantity to add: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();

                CartItem cartItem = new CartItem(productToAdd, quantity);
                shoppingController.addProductToCart(shoppingCart, cartItem);
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
    }

    public void updateProductInCart() {
        System.out.print("Enter the Cart ID: ");
        int idCart = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the Product ID to update: ");
        int productIdUpdate = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine();

        Product productCartToUpdate = productController.searchId(productIdUpdate);

        if (productCartToUpdate != null) {
            ShoppingCart shoppingCartToUpdate = new ShoppingCart();
            shoppingCartToUpdate.getCart().setIdCart(idCart);

            CartItem itemToUpdate = new CartItem(productCartToUpdate, newQuantity);
            shoppingCartToUpdate.addItem(itemToUpdate);

            try {
                shoppingController.updateProductToCart(shoppingCartToUpdate, productIdUpdate);
            } catch (DbException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    public void removeProductFromCart() {
        System.out.print("Enter the Cart ID: ");
        int cartId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the Product ID to remove: ");
        int productIdToRemove = scanner.nextInt();
        scanner.nextLine();

        ShoppingCart shoppingCartToRemove = new ShoppingCart();
        shoppingCartToRemove.getCart().setIdCart(cartId);

        shoppingController.deleteProductFromCart(shoppingCartToRemove, productIdToRemove);
    }

    public void viewCartDetails() {
        System.out.print("Enter the Cart ID to view: ");
        int cartIdToView = scanner.nextInt();
        scanner.nextLine();
        shoppingController.searchIdProuctCard(cartIdToView);
        System.out.println("\nCart details:");
        shoppingController.totalValueCart(cartIdToView);
    }
}
