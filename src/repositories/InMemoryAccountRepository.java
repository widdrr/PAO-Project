package repositories;

import model.Account;

import java.util.ArrayList;
import java.util.Optional;


public class InMemoryAccountRepository implements IAccountRepository {

    private final ArrayList<Account> accounts;

    public InMemoryAccountRepository() {
        this.accounts = new ArrayList<>();
    }

    @Override
    public Optional<Account> getAccountByUsername(String username){
        return accounts.stream().filter((account) -> username.equals(account.getUsername())).findAny();
    }

    @Override
    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public void removeAccount(Account account) {
        accounts.remove(account);
    }


}
