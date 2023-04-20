package model;

import java.util.Date;

public final class Withdrawal extends Transaction {

    public Withdrawal(Date transactionDate, Account account, double sum) {
        super(transactionDate, account, sum);
    }

    @Override
    public double getSum() {
        return -sum;
    }
}
