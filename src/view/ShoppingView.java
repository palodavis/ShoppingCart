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

            if (productToAdd != null) {
                if (shoppingCart.getItems().stream().anyMatch(item -> item.getProduct().getIdProduct() == productIdToAdd)) {
                    System.out.println("Product '" + productToAdd.getName() + "' is already in the cart.");
                    continue;
                }
                if (productToAdd.getAmount() == 0) {
                    System.out.println("Product '" + productToAdd.getName() + "' is out of stock. Please choose another product.");
                    continue;
                }
                boolean validQuantity = false;
                int quantity = 0;
                while (!validQuantity) {
                    System.out.println("Available stock: " + productToAdd.getAmount());
                    System.out.print("Enter the quantity to add: ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();

                    if (quantity > productToAdd.getAmount()) {
                        System.out.println("Quantity exceeds available stock. Please enter a quantity <= " + productToAdd.getAmount());
                    } else if (quantity <= 0) {
                        System.out.println("Invalid quantity. Please enter a value greater than 0.");
                    } else {
                        validQuantity = true;
                    }
                }
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
        Cart cart = getCartById();
        if (cart == null) return;

        Product productCartToUpdate = getProductById();

        if (productCartToUpdate == null) {
            System.out.println("Product not found in the cart!");
            return;
        }

        System.out.print("Enter the new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine();

        ShoppingCart shoppingCartToUpdate = new ShoppingCart();
        shoppingCartToUpdate.getCart().setIdCart(cart.getIdCart());

        CartItem itemToUpdate = new CartItem(productCartToUpdate, newQuantity);
        shoppingCartToUpdate.addItem(itemToUpdate);

        try {
            shoppingController.updateProductToCart(shoppingCartToUpdate, productCartToUpdate.getIdProduct());
            shoppingController.searchIdProuctCard(cart.getIdCart());
            shoppingController.totalValueCart(cart.getIdCart());
        } catch (DbException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void removeProductFromCart() {
        Cart cart = getCartById();
        if (cart == null) return;

        Product productToRemove = getProductById();

        if (productToRemove == null) {
            System.out.println("Product not found in the cart!");
            return;
        }

        Product productIdDelete = productController.searchId(productToRemove.getIdProduct());

        if (productIdDelete != null) {
            ShoppingCart shoppingCartToRemove = new ShoppingCart();
            shoppingCartToRemove.getCart().setIdCart(cart.getIdCart());
            shoppingController.deleteProductFromCart(shoppingCartToRemove, productToRemove.getIdProduct());
            System.out.println("Product removed from the cart successfully!");
        } else {
            System.out.println("Error: Product not found!");
        }
    }

    private Product getProductById() {
        System.out.print("Enter the Product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = productController.searchId(productId);
        return product;
    }

    private Cart getCartById() {
        System.out.print("Enter the Cart ID: ");
        int idCart = scanner.nextInt();
        scanner.nextLine();
        Cart cart = cartController.searchIdCart(idCart);
        if (cart == null) {
            System.out.println("Error: Cart not found for ID: " + idCart);
        }
        return cart;
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
