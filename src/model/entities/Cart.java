package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Cart implements Serializable {
    private Integer idCart;

    public Cart() {
    }

    public Cart(Integer idCart) {
        this.idCart = idCart;
    }

    public Integer getIdCart() {
        return idCart;
    }

    public void setIdCart(Integer idCart) {
        this.idCart = idCart;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(idCart, cart.idCart);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCart);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idCart=" + idCart +
                '}';
    }
}
