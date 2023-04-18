import exceptions.EntityExistentException;
import exceptions.InvalidCredentialsException;
import model.Account;
import repositories.IAccountRepository;
import repositories.InMemoryAccountRepository;
import services.AccountService;

import java.util.Scanner;

public class Main {

    private static Account currentAccount = null;
    private static AccountService accountService;
    private static Scanner consoleInput;
    private static boolean quit;
    private static Menus currentMenu;
    private static final String MainMenu = "1. Account Menu\n2. Store Menu";
    private static final String NotLogged = "1. Register\n2. Login\n3. Back";
    private static final String UserLogged ="1. Account Details\n2. Logout\n3. Back";
    private static void handleCommand(String command){
        if(command.equalsIgnoreCase("quit")){
            quit = true;
            return;
        }
        switch (currentMenu){
            case Main -> {
                switch (command){
                    case "1" -> {
                        if(currentAccount == null)
                            currentMenu = Menus.NotLogged;
                        else
                            currentMenu = Menus.UserLogged;
                    }
                    case "2" -> currentMenu = Menus.Store;
                    default -> System.out.println("Invalid Command");
                }
            }
            case NotLogged -> {
                switch (command){
                    case "1" -> {
                        System.out.println("Username:");
                        String username = consoleInput.nextLine();
                        System.out.println("Password:");
                        String password = consoleInput.nextLine();
                        System.out.println("1. Register as User\n2. Register as Creator");
                        String choice = consoleInput.nextLine();
                        try{
                            Account newAccount;
                            if(choice.equals("1"))
                                newAccount = accountService.registerUser(username,password);
                            else
                                newAccount = accountService.registerCreator(username,password);

                            currentAccount = newAccount;
                            currentMenu = Menus.Main;
                            System.out.println("Account registered successfully!");
                        }
                        catch(EntityExistentException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case "2" -> {
                        System.out.println("Username:");
                        String username = consoleInput.nextLine();
                        System.out.println("Password:");
                        String password = consoleInput.nextLine();
                        try{
                            currentAccount = accountService.login(username,password);
                            currentMenu = Menus.Main;
                            System.out.println("Successful login!");
                        }
                        catch(InvalidCredentialsException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case "3" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
            case UserLogged -> {
                switch (command){
                    case "1" -> System.out.println(currentAccount);
                    case "2" -> {
                        currentAccount = null;
                        currentMenu = Menus.Main;
                    }
                    case "3" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
        }
    }
    private static void printPrompt(){
        switch (currentMenu){
            case Main -> System.out.println(MainMenu);
            case NotLogged -> System.out.println(NotLogged);
            case UserLogged -> System.out.println(UserLogged);
        }
    }
    public static void main(String[] args) {
        quit = false;
        currentMenu = Menus.Main;
        IAccountRepository repo = new InMemoryAccountRepository();
        accountService = new AccountService(repo);
        consoleInput = new Scanner(System.in);

        //TODO: pretty printing
        while(!quit){
            printPrompt();
            String command = consoleInput.nextLine();
            handleCommand(command);
        }
    }
}