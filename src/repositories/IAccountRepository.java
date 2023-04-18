package repositories;

import model.Account;

import java.util.Optional;

public interface IAccountRepository {

    Optional<Account> getAccountByUsername(String username);
    void addAccount(Account account);
    void removeAccount(Account account);
}
