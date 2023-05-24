package model;

import java.time.LocalDateTime;
import java.util.Date;

public final class Payment extends Transaction {

    private final String product;
    public Payment(LocalDateTime transactionDate, Account account, String product, double sum) {
        super(transactionDate, account, sum);
        this.product = product;
    }

    @Override
    public double getSum() {
        return sum;
    }

    public String getProduct() {
        return product;
    }
}
