package model;

import exceptions.EntityException;
import exceptions.FundsException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public final class UserAccount extends Account {

    private final Set<Product> ownedProducts;
    public UserAccount(String username, String passwordHash) {
        super(username, passwordHash);
        this.ownedProducts = new HashSet<>();
    }
    public UserAccount(String username, int passwordHash, LocalDateTime lastLogin, HashSet<Product> products){
        super(username, passwordHash, lastLogin);
        this.ownedProducts = new HashSet<>(products);
    }
    public void addProduct(Product product) throws EntityException, FundsException {
        if(ownedProducts.contains(product))
            throw new EntityException("Product already owned!");

        if(product instanceof GameContent && !ownedProducts.contains(((GameContent) product).dependency)){
            throw new EntityException("Product dependency not owned!");
        }

        if(this.getBalance() < product.price)
            throw new FundsException("Insufficient funds!");
        ownedProducts.add(product);
        addTransaction(new Purchase(LocalDateTime.now(), this, product));
        product.payCreator();
    }

    public Set<Product> getOwnedProducts() {
        return ownedProducts;
    }

    public void deposit(double sum) throws FundsException{
        if(sum <= 0.0){
            throw new FundsException("Invalid sum!");
        }

        Transaction deposit = new Deposit(LocalDateTime.now(),this, sum);
        addTransaction(deposit);
    }

    @Override
    public String toString() {
        return "User: " + this.username
                + "\nBalance: " + this.getBalance()
                + "\nLast Login: " + this.lastLogin
                + "\nOwned Products: " + this.ownedProducts;
    }
}
