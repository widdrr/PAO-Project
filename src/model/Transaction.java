package model;

import java.time.LocalDateTime
;
public sealed abstract class Transaction permits Deposit, Purchase, Payment, Withdrawal {

    protected final LocalDateTime transactionDate;
    protected final Account account;
    public final double sum;

    public Transaction(LocalDateTime transactionDate, Account account, double sum) {
        this.transactionDate = transactionDate;
        this.account = account;
        this.sum = sum;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public Account getAccount() {
        return account;
    }
    @Override
    public boolean equals(Object obj) {
        return (account.getUsername().compareTo(((Transaction)obj).getAccount().username) == 0)
                &&
                (this.transactionDate.compareTo(((Transaction) obj).transactionDate) == 0);
    }

    public abstract double getSum();
}
