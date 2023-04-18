package model;

import exceptions.EntityExistentException;

import java.util.Date;

sealed public abstract class Account permits UserAccount, CreatorAccount {

    protected String username;
    protected int passwordHash;
    protected Date lastLogin;

    public Account(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash.hashCode(); //this would be a proper cryptographic hash in practice
        this.lastLogin = new Date();
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
}
