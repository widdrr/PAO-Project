import exceptions.EntityException;
import exceptions.CredentialsException;
import exceptions.FundsException;
import model.*;
import repositories.IAccountRepository;
import repositories.IProductRepository;
import repositories.InMemoryAccountRepository;
import repositories.InMemoryProductRepository;
import services.AccountService;
import services.ProductService;

import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static Account currentAccount = null;
    private static AccountService accountService;
    private static ProductService productService;
    private static Scanner consoleInput;
    private static boolean quit;
    private static Menus currentMenu;
    private static final String MainMenu = "1. Account Menu\n2. Store Menu";
    private static final String NotLoggedMenu = "1. Register\n2. Login\nB. Back";
    private static final String UserLoggedMenu ="1. Account Details\n2. Deposit\n3. Logout\nB. Back";
    private static final String CreatorLoggedMenu ="1. Account Details\n2. Withdraw\n3. Logout\nB. Back";
    private static final String StoreMenu ="1. List products\nB. Back";
    private static final String CreatorStoreMenu ="1. List products\n2. Add product\nB. Back";
    private static final String UserStoreMenu = "1. List products\n2. Purchase product\nB. Back";
    private static final String CreatorMenu="1. Create a game\n2. Create game content\nB. Back";
    //TODO: try and refactor this monster
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
                        else {
                            if (currentAccount instanceof UserAccount)
                                currentMenu = Menus.UserLogged;
                            else
                                currentMenu = Menus.CreatorLogged;
                        }
                    }
                    case "2" -> {
                        if (currentAccount == null)
                            currentMenu = Menus.Store;
                        else {
                            if (currentAccount instanceof UserAccount)
                                currentMenu = Menus.UserStore;
                            else
                                currentMenu = Menus.CreatorStore;
                        }
                    }
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
                        catch(EntityException e){
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
                        catch(CredentialsException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case "B" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
            case UserLogged -> {
                switch (command){
                    case "1" -> System.out.println(currentAccount);
                    case "2" -> {
                        System.out.println("Enter sum:");
                        double sum = consoleInput.nextDouble();
                        consoleInput.nextLine();
                        try {
                            accountService.makeDeposit((UserAccount) currentAccount, sum);
                            System.out.println("Deposit successful!");
                        }
                        catch(FundsException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case "3" -> {
                        currentAccount = null;
                        currentMenu = Menus.Main;
                    }
                    case "B" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
            case CreatorLogged -> {
                switch (command){
                    case "1" -> System.out.println(currentAccount);
                    case "2" -> {
                        System.out.println("Enter sum:");
                        double sum = consoleInput.nextDouble();
                        consoleInput.nextLine();
                        try {
                            accountService.makeWithrdawal((CreatorAccount) currentAccount,sum);
                        }
                        catch (FundsException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case "3" -> {
                        currentAccount = null;
                        currentMenu = Menus.Main;
                    }
                    case "B" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
            case Store ->{
                switch (command){
                    case "1" -> System.out.println(productService.listProducts());
                    case "B" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
            case CreatorStore -> {
                switch (command){
                    case "1" -> System.out.println(productService.listProducts());
                    case "2" -> currentMenu = Menus.Creator;
                    case "B" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
            case UserStore -> {
                switch (command){
                    case "1" -> System.out.println(productService.listProducts());
                    case "2" -> {
                        System.out.println("Product name:");
                        String name = consoleInput.nextLine();

                        try{
                            Product product = productService.getProduct(name);

                            UserAccount user = (UserAccount) currentAccount;
                            user.addProduct(product);
                            System.out.println("Purchase successful!");
                        }
                        catch(EntityException e){
                            System.out.println(e.getMessage());
                        }
                        catch(FundsException e){
                            System.out.println(e.getMessage());
                            System.out.println("Do you wish to deposit?\n1.Yes\n2.No");
                            command = consoleInput.nextLine();
                            if(command.equalsIgnoreCase("1")){
                                System.out.println("Enter sum:");
                                double sum = consoleInput.nextDouble();
                                consoleInput.nextLine();

                                try {
                                    accountService.makeDeposit((UserAccount) currentAccount, sum);
                                    System.out.println("Deposit successful!");
                                }
                                catch(FundsException e2){
                                    System.out.println(e2.getMessage());
                                }
                            }

                        }
                        finally {
                            currentMenu = Menus.UserStore;
                        }
                    }
                    case "B" -> currentMenu = Menus.Main;
                    default -> System.out.println("Invalid Command");
                }
            }
            case Creator ->{
                switch (command){
                    case "1" -> {
                        System.out.println("Game name:");
                        String name = consoleInput.nextLine();
                        CreatorAccount creator = (CreatorAccount) currentAccount;
                        creator.createGame.setName(name);

                        System.out.println("Price:");
                        double price = consoleInput.nextDouble();
                        consoleInput.nextLine();
                        creator.createGame.setPrice(price);

                        System.out.println("Available tags: "+ Arrays.toString(GameTags.values()));
                        System.out.println("Add tag one at a time or enter 'done' when finished");
                        String tag = consoleInput.nextLine();
                        while(tag.compareToIgnoreCase("done")!=0){
                            try {
                                GameTags gameTag = GameTags.valueOf(tag);
                                creator.createGame.addTag(gameTag);
                            }
                            catch (IllegalArgumentException e){
                                System.out.println("Tag does not exist");
                            }
                            finally {
                                System.out.println("Add tag one at a time or enter 'done' when finished");
                                tag = consoleInput.nextLine();
                            }
                        }

                        try{
                            productService.addProduct(creator.createGame.create());
                            System.out.println("Product added successfully!");
                        }
                        catch (EntityException e){
                            System.out.println(e.getMessage());
                        }
                        finally {
                            currentMenu = Menus.CreatorStore;
                        }
                    }
                    case "2" -> {
                        System.out.println("Game:");
                        String gameName = consoleInput.nextLine();
                        CreatorAccount creator = (CreatorAccount) currentAccount;
                        try {
                            Game game = (Game) productService.getProduct(gameName);
                            creator.createContent.setDependency(game);

                        }
                        catch(EntityException e){
                            System.out.println(e.getMessage());
                        }
                        catch (ClassCastException e){
                            System.out.println("Product is not a game");
                        }

                        System.out.println("Name:");
                        String name = consoleInput.nextLine();
                        creator.createContent.setName(name);

                        System.out.println("Price:");
                        double price = consoleInput.nextDouble();
                        consoleInput.nextLine();
                        creator.createContent.setPrice(price);

                        System.out.println("Available tags: "+ Arrays.toString(GameTags.values()));
                        System.out.println("Add tag one at a time or enter 'done' when finished");
                        String tag = consoleInput.nextLine();
                        while(tag.compareToIgnoreCase("done")!=0){
                            try {
                                GameTags gameTag = GameTags.valueOf(tag);
                                creator.createContent.addTag(gameTag);
                            }
                            catch (IllegalArgumentException e){
                                System.out.println("Tag does not exist");
                            }
                            finally {
                                System.out.println("Add tag one at a time or enter 'done' when finished");
                                tag = consoleInput.nextLine();
                            }
                        }

                        try{
                            productService.addProduct(creator.createContent.create());
                            System.out.println("Product added successfully!");
                        }
                        catch (EntityException e){
                            System.out.println(e.getMessage());
                        }
                        finally {
                            currentMenu = Menus.CreatorStore;
                        }
                    }
                    case "B" -> currentMenu = Menus.CreatorStore;
                    default -> System.out.println("Invalid Command");
                }
            }
        }
    }
    private static void printPrompt(){
        switch (currentMenu){
            case Main -> System.out.println(MainMenu);
            case NotLogged -> System.out.println(NotLoggedMenu);
            case UserLogged -> System.out.println(UserLoggedMenu);
            case CreatorLogged -> System.out.println(CreatorLoggedMenu);
            case Store -> System.out.println(StoreMenu);
            case CreatorStore -> System.out.println(CreatorStoreMenu);
            case UserStore -> System.out.println(UserStoreMenu);
            case Creator -> System.out.println(CreatorMenu);
        }
    }
    public static void main(String[] args) {
        quit = false;
        currentMenu = Menus.Main;
        IAccountRepository accountRepository = new InMemoryAccountRepository();
        IProductRepository productRepository = new InMemoryProductRepository();
        accountService = new AccountService(accountRepository);
        productService = new ProductService(productRepository);
        consoleInput = new Scanner(System.in);

        //TODO: pretty printing
        while(!quit){
            printPrompt();
            String command = consoleInput.nextLine();
            handleCommand(command);
        }
    }
}