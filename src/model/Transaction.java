package model;

import java.util.Date;

public sealed abstract class Transaction permits Deposit, Purchase, Payment, Withdrawal {

    protected final Date transactionDate;
    protected final Account account;
    public final double sum;

    public Transaction(Date transactionDate, Account account, double sum) {
        this.transactionDate = transactionDate;
        this.account = account;
        this.sum = sum;
    }
    public abstract double getSum();
}
