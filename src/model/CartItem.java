package model;

public class CartItem {
    private Product product;
    private Integer amount;

    public CartItem(Product product, Integer amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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
