package model;

import java.util.Date;

public final class Purchase extends Transaction {

    private final Product product;
    public Purchase(Date transactionDate, Account account, Product product) {
        super(transactionDate, account, product.price);
        this.product = product;
    }

    @Override
    public double getSum() {
        return -sum;
    }
}
