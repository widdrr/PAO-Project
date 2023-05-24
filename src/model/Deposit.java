package model;

import java.time.LocalDateTime;

public final class Deposit extends Transaction {
    public Deposit(LocalDateTime transactionDate, Account account, double sum) {
        super(transactionDate, account, sum);
    }

    @Override
    public double getSum(){
        return sum;
    }
}
