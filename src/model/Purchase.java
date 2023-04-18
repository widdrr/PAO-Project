package model;

import java.util.Date;

public final class Purchase extends Transaction {
    public Purchase(Date transactionDate, Account account, double sum) {
        super(transactionDate, account, sum);
    }
}
