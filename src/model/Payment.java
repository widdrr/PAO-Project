package model;

import java.util.Date;

public final class Payment extends Transaction {

    private final Product product;
    public Payment(Date transactionDate, Account account,Product product, double sum) {
        super(transactionDate, account, sum);
        this.product = product;
    }

    @Override
    public double getSum() {
        return sum;
    }

    public Product getProduct() {
        return product;
    }
}
