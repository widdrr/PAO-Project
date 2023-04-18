package model;

import exceptions.EntityExistentException;

import java.util.HashSet;
import java.util.Set;

public final class UserAccount extends Account {

    private final Set<Product> ownedProducts;
    public UserAccount(String username, String passwordHash) {
        super(username, passwordHash);
        this.ownedProducts = new HashSet<>();
    }
    public void addProduct(Product product) throws EntityExistentException {
        if(ownedProducts.contains(product))
            throw new EntityExistentException("Product already owned!");

        ownedProducts.add(product);
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                ", passwordHash=" + passwordHash +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
