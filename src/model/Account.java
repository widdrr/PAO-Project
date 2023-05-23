package model;

import java.util.ArrayList;
import java.util.Date;

sealed public abstract class Account permits UserAccount, CreatorAccount {

    protected String username;
    protected int passwordHash;
    protected Date lastLogin;
    protected final ArrayList<Transaction> transactions;
    protected double cachedBalance;
    protected boolean cacheValid = false;

    public Account(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash.hashCode(); //this would be a proper cryptographic hash in practice
        this.lastLogin = new Date();
        this.transactions = new ArrayList<>();
    }

    public Account(String username, int passwordHash, Date lastLogin){
        this.username = username;
        this.passwordHash = passwordHash;
        this.lastLogin = lastLogin;
        this.transactions = new ArrayList<>();
    }

    public boolean tryLogin(String password){
        if(password.hashCode() == this.passwordHash){
            lastLogin = new Date();
            return true;
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPasswordHash() {
        return passwordHash;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public double getBalance() {
        if(!cacheValid)
            computeBalance();

        return cachedBalance;

    }
    public void addTransaction(Transaction t){
        transactions.add(t);
        cacheValid = false;
    }
    private void computeBalance(){
        cachedBalance = transactions.stream().map(Transaction::getSum).reduce(0.0, Double::sum);
        cacheValid = true;
    }
}
