package view;

import controller.ProductController;
import model.entities.Product;

import java.util.Scanner;

public class ProductView {
    private ProductController productController;
    private Scanner scanner;

    public ProductView(ProductController productController) {
        this.productController = productController;
        this.scanner = new Scanner(System.in);
    }

    public void insertProduct() {
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
        productController.addProduct(newProduct);
    }

    public void updateProduct() {
        System.out.print("Enter the ID of the product to update: ");
        int productIdToUpdate = scanner.nextInt();
        scanner.nextLine();
        Product productToUpdate = productController.searchId(productIdToUpdate);

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

            productController.updateProduct(productToUpdate);
        }
    }

    public void deleteProduct() {
        System.out.print("Enter the ID of the product to delete: ");
        int productIdToDelete = scanner.nextInt();
        scanner.nextLine();
        productController.deleteProduct(productIdToDelete);
    }

    public void viewProduct() {
        System.out.print("Enter the ID of the product to view: ");
        int productIdToView = scanner.nextInt();
        scanner.nextLine();
        productController.searchId(productIdToView);
    }
}
