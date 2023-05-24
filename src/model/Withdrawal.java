package model;

import java.time.LocalDateTime;

public final class Withdrawal extends Transaction {

    public Withdrawal(LocalDateTime transactionDate, Account account, double sum) {
        super(transactionDate, account, sum);
    }

    @Override
    public double getSum() {
        return -sum;
    }
}
