package model;

import java.util.Date;

public sealed abstract class Transaction permits Deposit, Purchase, Withdrawal {

    protected final Date transactionDate;
    protected final Account account;
    protected final double sum;

    public Transaction(Date transactionDate, Account account, double sum) {
        this.transactionDate = transactionDate;
        this.account = account;
        this.sum = sum;
    }
}
