package model;

import java.time.LocalDateTime;

public final class Purchase extends Transaction {

    private final Product product;
    public Purchase(LocalDateTime transactionDate, Account account, Product product) {
        super(transactionDate, account, product.price);
        this.product = product;
    }

    @Override
    public double getSum() {
        return -sum;
    }

    public Product getProduct() {
        return product;
    }
}
