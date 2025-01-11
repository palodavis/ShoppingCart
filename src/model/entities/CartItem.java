package model.entities;

public class CartItem {
    private Product product;
    private int amount;

    public CartItem() {
    }

    public CartItem(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CarItem{" +
                "product=" + product +
                ", amount=" + amount +
                '}';
    }
}
