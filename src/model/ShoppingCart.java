package model;

import java.io.Serializable;
import java.util.Objects;

public class ShoppingCart implements Serializable {
    private Integer idShoppingCart;
    private Integer amount;
    private Product product;

    public ShoppingCart() {}

    public ShoppingCart(Integer idShoppingCart, Integer amount, Product product) {
        this.idShoppingCart = idShoppingCart;
        this.amount = amount;
        this.product = product;
    }

    public Integer getIdShoppingCart() {
        return idShoppingCart;
    }

    public void setIdShoppingCart(Integer idShoppingCart) {
        this.idShoppingCart = idShoppingCart;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(idShoppingCart, that.idShoppingCart);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idShoppingCart);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "idShoppingCart=" + idShoppingCart +
                ", amount=" + amount +
                ", product=" + product +
                '}';
    }
}
