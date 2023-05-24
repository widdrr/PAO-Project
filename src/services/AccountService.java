package services;

import exceptions.EntityException;
import exceptions.CredentialsException;
import exceptions.FundsException;
import logging.FileLogger;
import logging.ILogger;
import model.*;
import repositories.IAccountRepository;

import java.util.Optional;

//TODO: make this singleton
public final class AccountService {
    private static final String logPath = "D:\\Data\\Repos\\IDEA\\Project\\src\\logging\\AccountLog.txt";
    private final IAccountRepository accountRepository;
    private final ILogger logger;

    public AccountService(IAccountRepository accountRepository){
        this.accountRepository = accountRepository;
        this.logger = new FileLogger(logPath);
    }

    public Account registerUser(String username, String password) throws EntityException
    {
        Account newAccount = new UserAccount(username,password);
        Account registeredAccount = registerAccount(newAccount);
        logger.log("Registered user: " + newAccount.getUsername());
        return registeredAccount;
    }
    public Account registerCreator(String username, String password) throws EntityException
    {
        Account newAccount = new CreatorAccount(username, password);
        Account registeredAccount = registerAccount(newAccount);
        logger.log("Registered creator: " + newAccount.getUsername());
        return registeredAccount;
    }

    private Account registerAccount(Account newAccount) throws EntityException {

        Optional<Account> existentAccount = accountRepository.getAccountByUsername(newAccount.getUsername());
        if(existentAccount.isPresent()){
            throw new EntityException("Username is taken!");
        }
        accountRepository.addAccount(newAccount);

        return newAccount;
    }

    public String getAccountDetails(Account account){
        logger.log("Account " + account.getUsername() + " checked details");
        return account.toString();
    }

    public Account login(String username, String password) throws CredentialsException {
        Optional<Account> existentAccount = accountRepository.getAccountByUsername(username.toLowerCase());
        if(existentAccount.isEmpty() || !existentAccount.get().tryLogin(password)){
            throw new CredentialsException("Wrong username or password!");
        }
        logger.log("User username logged in.");
        return existentAccount.get();
    }

    public void logout(Account account){
        logger.log("User " + account.getUsername() + " logged out");
        accountRepository.updateAccount(account);
    }

    public void purchaseProduct(UserAccount user, Product product) throws FundsException,EntityException {
        user.addProduct(product);
        accountRepository.updateAccount(product.getCreator());
        logger.log("User " + user.getUsername() + " Purchased product with name " + product.getName());
    }

    public void makeDeposit(UserAccount account, double sum) throws FundsException {
        account.deposit(sum);
        logger.log("User " + account.getUsername() + " deposited " + sum);
    }

    public void makeWithdrawal(CreatorAccount account, double sum) throws FundsException{
        account.withdraw(sum);
        logger.log("Creator " + account.getUsername() + " withdrew " + sum);
    }

}
