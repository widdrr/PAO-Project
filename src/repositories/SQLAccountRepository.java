package repositories;

import database.IDBConnection;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;

//these repos are a horrible mess
public class SQLAccountRepository implements IAccountRepository{

    private final SQLProductRepository productRepository;
    private final IDBConnection databaseConnection;
    private static final String AccountByUsername = "SELECT * FROM accounts WHERE account_name = ?";
    private static final String TransactionsByAccount = "SELECT * FROM transactions WHERE account_name = ?";
    private static final String InsertAccount = "INSERT INTO accounts VALUES (?,?,?,?)";
    private static final String InsertTransaction = "INSERT INTO transactions VALUES (?,?,?,?,?)";
    private static final String InsertProductOwned = "INSERT INTO account_products VALUES(?,?)";


    public SQLAccountRepository(IDBConnection databaseConnection, SQLProductRepository sqlProductRepository) {
        this.databaseConnection = databaseConnection;
        this.productRepository = sqlProductRepository;
    }

    @Override
    public Optional<Account> getAccountByUsername(String username) {
        try {
            PreparedStatement stmt = databaseConnection.getCon().prepareStatement(AccountByUsername);
            stmt.setString(1,username);
            ResultSet rs= stmt.executeQuery();
            if(!rs.next()){
                return Optional.empty();
            }
            int hash = rs.getInt("password_hash");
            LocalDateTime login = rs.getObject("last_login", LocalDateTime.class);
            int account_type = rs.getInt("account_type");

            Account account = null;
            //creator
            if(account_type == 1){
                account = new CreatorAccount(username,hash,login);
            }
            //user
            else{
                HashSet<Product> ownedProducts = productRepository.getProductsForUser(username);
                account = new UserAccount(username,hash,login,ownedProducts);
            }
            List<Transaction> transactionList = getTransactions(account);
            for(Transaction t : transactionList)
                account.addTransaction(t);

            return Optional.of(account);


        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    private List<Transaction> getTransactions(Account account) throws SQLException{
        PreparedStatement stmt = databaseConnection.getCon().prepareStatement(TransactionsByAccount);
        stmt.setString(1, account.getUsername());
        ResultSet rs = stmt.executeQuery();
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        while(rs.next()){
            Transaction t = null;
            switch (rs.getInt("transaction_type")){
                case 0 -> t =
                        new Deposit(
                                rs.getObject("transaction_date", LocalDateTime.class),
                                account,
                                rs.getDouble("transaction_sum")
                        );
                case 1 -> t =
                        new Purchase(
                                rs.getObject("transaction_date", LocalDateTime.class),
                                account,
                                productRepository.getProductByName(rs.getString("product_name")).get()
                        );
                case 2 -> t =
                        new Withdrawal(
                                rs.getObject("transaction_date", LocalDateTime.class),
                                account,
                                rs.getDouble("transaction_sum")
                        );
                case 3 -> t =
                        new Payment(
                                rs.getObject("transaction_date", LocalDateTime.class),
                                account,
                                rs.getString("product_name"),
                                rs.getDouble("transaction_sum")
                        );
            }
            transactionsList.add(t);
        }
        return transactionsList;
    }

    @Override
    public void addAccount(Account account) {
        int account_type = 0;
        if(account instanceof CreatorAccount)
            account_type = 1;

        try {
            PreparedStatement stmt = databaseConnection.getCon().prepareStatement(InsertAccount);
            stmt.setString(1,account.getUsername());
            stmt.setInt(2,account.getPasswordHash());
            stmt.setObject(3, account.getLastLogin());
            stmt.setInt(4,account_type);

            stmt.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTransaction(Transaction transaction){
        double sum = 0;
        int type = 0;
        String product = null;

        if(transaction instanceof Deposit){
            type = 0;
            sum = transaction.getSum();
        } else if(transaction instanceof Purchase){
            type = 1;
            product = ((Purchase) transaction).getProduct().getName();
            sum = -transaction.getSum();
        } else if (transaction instanceof Withdrawal) {
            type = 2;
            sum = -transaction.getSum();
        } else if (transaction instanceof Payment) {
            type = 3;
            product = ((Payment) transaction).getProduct();
            sum = transaction.getSum();
        }

        PreparedStatement stmt = null;
        try {
            stmt = databaseConnection.getCon().prepareStatement(InsertTransaction);
            stmt.setString(1, transaction.getAccount().getUsername());
            stmt.setObject(2, transaction.getTransactionDate());
            stmt.setInt(3,type);
            stmt.setDouble(4,sum);
            if(product == null){
                stmt.setNull(5, Types.INTEGER);
            }
            else{
                stmt.setString(5,product);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void addProductPurchase(String account,String product) throws SQLException{
        PreparedStatement stmt = databaseConnection.getCon().prepareStatement(InsertProductOwned);
        stmt.setString(1,account);
        stmt.setString(2,product);
        stmt.executeUpdate();
    }
    @Override
    public void updateAccount(Account account) {
        //we currently don't allow updating account values, so all we have to do is insert the new transactions/owned games

        try {
            List<Transaction> transactions = account.getTransactions();

            List<Transaction> existingTransactions = getTransactions(account);
            transactions.removeAll(existingTransactions);

            for(Transaction t : transactions){
                addTransaction(t);
            }

            if(account instanceof UserAccount){
                Set<Product> products = ((UserAccount) account).getOwnedProducts();
                Set<Product> existingProducts = productRepository.getProductsForUser(account.getUsername());

                products.removeAll(existingProducts);
                for(Product p : products){
                    addProductPurchase(account.getUsername(),p.getName());
                }

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }


    }

    @Override
    public void removeAccount(Account account) {

    }
}
