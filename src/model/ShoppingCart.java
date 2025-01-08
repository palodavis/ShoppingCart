package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private Integer idShoppingCart;
    private List<CartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public ShoppingCart(Integer idShoppingCart) {
        this.idShoppingCart = idShoppingCart;
        this.items = new ArrayList<>();
    }

    public Integer getIdShoppingCart() {
        return idShoppingCart;
    }

    public void setIdShoppingCart(Integer idShoppingCart) {
        this.idShoppingCart = idShoppingCart;
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
    }

    public double getTotalValue() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getAmount();
        }
        return total;
    }


    @Override
    public String toString() {
        return "ShoppingCart{" +
                "idShoppingCart=" + idShoppingCart +
                ", items=" + items +
                '}';
    }
}
