package model;

import java.util.Date;

public final class Deposit extends Transaction {
    public Deposit(Date transactionDate, Account account, double sum) {
        super(transactionDate, account, sum);
    }
}
