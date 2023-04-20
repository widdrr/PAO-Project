package model;

import exceptions.EntityException;
import exceptions.FundsException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public final class UserAccount extends Account {

    private final Set<Product> ownedProducts;
    public UserAccount(String username, String passwordHash) {
        super(username, passwordHash);
        this.ownedProducts = new HashSet<>();
    }
    public void addProduct(Product product) throws EntityException, FundsException {
        if(ownedProducts.contains(product))
            throw new EntityException("Product already owned!");

        if(this.getBalance() < product.price)
            throw new FundsException("Insufficient funds!");
        ownedProducts.add(product);
        product.payCreator();
    }

    @Override
    public String toString() {
        return "User: " + this.username
                + "\nBalance: " + this.getBalance()
                + "\nLast Login: " + this.lastLogin;
    }
}
