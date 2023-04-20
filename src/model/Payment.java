package model;

import java.util.Date;

public final class Payment extends Transaction {

    private final Product product;
    public Payment(Date transactionDate, Account account,Product product) {
        super(transactionDate, account, product.price*0.8);
        this.product = product;
    }

    @Override
    public double getSum() {
        return sum;
    }
}
