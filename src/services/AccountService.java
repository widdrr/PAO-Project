package services;

import exceptions.EntityException;
import exceptions.CredentialsException;
import exceptions.FundsException;
import model.*;
import repositories.IAccountRepository;

import java.util.Date;
import java.util.Optional;

//TODO: make this singleton
public final class AccountService {
    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerUser(String username, String password) throws EntityException
    {
        Account newAccount = new UserAccount(username,password);
        return registerAccount(newAccount);
    }
    public Account registerCreator(String username, String password) throws EntityException
    {
        Account newAccount = new CreatorAccount(username, password);
        return registerAccount(newAccount);
    }

    private Account registerAccount(Account newAccount) throws EntityException {

        Optional<Account> existentAccount = accountRepository.getAccountByUsername(newAccount.getUsername());
        if(existentAccount.isPresent()){
            throw new EntityException("Username is taken!");
        }
        accountRepository.addAccount(newAccount);
        return newAccount;
    }

    public Account login(String username, String password) throws CredentialsException {
        Optional<Account> existentAccount = accountRepository.getAccountByUsername(username);
        if(existentAccount.isEmpty() || !existentAccount.get().tryLogin(password)){
            throw new CredentialsException("Wrong username or password!");
        }
        return existentAccount.get();
    }

    public void makeDeposit(UserAccount account, double sum) throws FundsException {
        account.deposit(sum);
    }

    public void makeWithrdawal(CreatorAccount account, double sum) throws FundsException{
        account.withdraw(sum);
    }
}
