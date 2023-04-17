package model;

import exceptions.EntityExistentException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Account {

    protected String username;
    protected int passwordHash;
    protected Date lastLogin;
    protected Set<Product> ownedProducts;

    public Account(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash.hashCode(); //this would be a proper cryptographic hash in practice
        this.lastLogin = new Date();
        this.ownedProducts = new HashSet<>();
    }

    public boolean tryLogin(String password){
        return password.hashCode() == this.passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addProduct(Product product) throws EntityExistentException {
        if(ownedProducts.contains(product))
            throw new EntityExistentException("Product already owned!");

        ownedProducts.add(product);
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
