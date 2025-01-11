package model.entities;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private Cart cart;
    private List<CartItem> items;
    private double totalValue;

    public ShoppingCart() {
        this.items = new ArrayList<>();
        this.cart = new Cart();
    }

    public ShoppingCart(Cart cart) {
        this.cart = cart;
        this.items = new ArrayList<>();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem item) {
        boolean productFound = false;
        for (CartItem cartItem : items) {
            if (cartItem.getProduct().getIdProduct().equals(item.getProduct().getIdProduct())) {
                cartItem.setAmount(cartItem.getAmount() + item.getAmount());
                productFound = true;
                break;
            }
        }
        if (!productFound) {
            this.items.add(item);
        }
        updateTotalValue();
    }


    public double getTotalValue() {
        double total = 0.0;

        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getAmount();
        }

        return total;
    }


    public void updateTotalValue() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getAmount() * item.getProduct().getPrice();
        }
        this.totalValue = total;
    }


    @Override
    public String toString() {
        return "ShoppingCart{" +
                "cart=" + cart +
                ", items=" + items +
                '}';
    }
}
