package services;

import exceptions.EntityExistentException;
import model.Account;
import repositories.IAccountRepository;

import java.util.Optional;

//TODO: make this singleton
public class AccountService {
    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(String username, String password){

        Optional<Account> existentAccount = accountRepository.getAccountByUsername(username);
        if(existentAccount.isPresent()){
            throw new EntityExistentException("Username is taken!");
        }

        Account newAccount = new Account(username,password);
        accountRepository.addAccount(newAccount);
        return newAccount;
    }

}
