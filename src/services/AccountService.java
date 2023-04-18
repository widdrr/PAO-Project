package services;

import exceptions.EntityExistentException;
import exceptions.InvalidCredentialsException;
import model.Account;
import model.CreatorAccount;
import model.UserAccount;
import repositories.IAccountRepository;

import java.util.Optional;

//TODO: make this singleton
public class AccountService {
    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerUser(String username, String password) throws EntityExistentException
    {
        Account newAccount = new UserAccount(username,password);
        return registerAccount(newAccount);
    }
    public Account registerCreator(String username, String password) throws EntityExistentException
    {
        Account newAccount = new CreatorAccount(username, password);
        return registerAccount(newAccount);
    }

    private Account registerAccount(Account newAccount) throws EntityExistentException{

        Optional<Account> existentAccount = accountRepository.getAccountByUsername(newAccount.getUsername());
        if(existentAccount.isPresent()){
            throw new EntityExistentException("Username is taken!");
        }
        accountRepository.addAccount(newAccount);
        return newAccount;
    }

    public Account login(String username, String password) throws InvalidCredentialsException{
        Optional<Account> existentAccount = accountRepository.getAccountByUsername(username);
        if(existentAccount.isEmpty() || !existentAccount.get().tryLogin(password)){
            throw new InvalidCredentialsException("Wrong username or password!");
        }
        return existentAccount.get();
    }
}
