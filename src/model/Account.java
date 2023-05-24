package model;

import java.util.ArrayList;
import java.time.LocalDateTime;

sealed public abstract class Account permits UserAccount, CreatorAccount {

    protected String username;
    protected int passwordHash;
    protected LocalDateTime lastLogin;
    protected final ArrayList<Transaction> transactions;
    protected double cachedBalance;
    protected boolean cacheValid = false;

    public Account(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash.hashCode(); //this would be a proper cryptographic hash in practice
        this.transactions = new ArrayList<>();
        this.lastLogin = LocalDateTime.now();
    }

    public Account(String username, int passwordHash, LocalDateTime lastLogin){
        this.username = username;
        this.passwordHash = passwordHash;
        this.lastLogin = lastLogin;
        this.transactions = new ArrayList<>();
    }

    public boolean tryLogin(String password){
        if(password.hashCode() == this.passwordHash){
            lastLogin = LocalDateTime.now();
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

    public LocalDateTime getLastLogin() {
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
