import model.Account;
import repositories.IAccountRepository;
import repositories.InMemoryIAccountRepository;
import services.AccountService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        IAccountRepository repo = new InMemoryIAccountRepository();
        AccountService service = new AccountService(repo);

        Scanner console = new Scanner(System.in);

        String username = console.nextLine();
        String password = console.nextLine();

        Account account;
        try{
            account = service.registerAccount(username,password);
            System.out.println(account);
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

    }
}