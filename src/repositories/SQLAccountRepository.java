package repositories;

import database.IDBConnection;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

public class SQLAccountRepository implements IAccountRepository{

    private final SQLProductRepository productRepository;
    private final IDBConnection databaseConnection;
    private static final String AccountByUsername = "SELECT * FROM accounts WHERE username = ?";
    private static final String TransactionsByAccount = "SELECT * FROM transactions WHERE account_id = ?";
    private static final String InsertAccount = "INSERT INTO accounts (username, password_hash, last_login, account_type) VALUES (?,?,?,?)";
    private static final String InsertTransaction = "INSERT INTO transactions VALUES (?,?,?,?,?)";


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
            int id = rs.getInt("account_id");
            int hash = rs.getInt("password_hash");
            Date login = rs.getDate("last_login");
            int account_type = rs.getInt("account_type");

            //creator
            if(account_type == 1){
                CreatorAccount account = new CreatorAccount(username,hash,login);
                addTransactions(account,id);

                return Optional.of(account);
            }
            //user
            else{
                HashSet<Product> ownedProducts = productRepository.getProductsForUser(id);
                UserAccount account = new UserAccount(username,hash,login,ownedProducts);
                addTransactions(account,id);

                return Optional.of(account);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private void addTransactions(Account account, int account_id) throws SQLException{
        PreparedStatement stmt = databaseConnection.getCon().prepareStatement(TransactionsByAccount);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            switch (rs.getInt("transaction_type")){
                case 0 -> account.addTransaction(
                        new Deposit(
                                rs.getDate("transaction_date"),
                                account,
                                rs.getDouble("transaction_sum")
                ));
                case 1 -> account.addTransaction(
                        new Purchase(
                                rs.getDate("transaction_date"),
                                account,
                                productRepository.getProductById(rs.getInt("product_id")).get()
                        ));
                case 2 -> account.addTransaction(
                        new Withdrawal(
                                rs.getDate("transaction_date"),
                                account,
                                rs.getDouble("transaction_sum")
                        ));
                case 3 -> account.addTransaction(
                        new Payment(
                                rs.getDate("transaction_date"),
                                account,
                                productRepository.getProductById(rs.getInt("product_id")).get(),
                                rs.getDouble("transaction_sum")
                        ));
            }
        }
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
            stmt.setDate(3, (java.sql.Date) account.getLastLogin());
            stmt.setInt(4,account_type);

            stmt.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTransaction(Transaction transaction, int user_id){
        double sum = 0;
        int type = 0;
        Product product = null;

        if(transaction instanceof Deposit){
            type = 0;
            sum = transaction.getSum();
        } else if(transaction instanceof Purchase){
            type = 1;
            product = ((Purchase) transaction).getProduct();
            sum = product.getPrice();
        } else if (transaction instanceof Withdrawal) {
            type = 2;
            sum = -transaction.getSum();
        } else if (transaction instanceof Payment) {
            type = 3;
            product = ((Payment) transaction).getProduct();
            sum = product.getPrice();
        }

        PreparedStatement stmt = null;
        try {
            stmt = databaseConnection.getCon().prepareStatement(InsertTransaction);
            stmt.setInt(1,user_id);
            stmt.setDate(2, (java.sql.Date) transaction.getTransactionDate());
            stmt.setInt(3,type);
            stmt.setDouble(4,sum);
            if(product == null){
                stmt.setNull(5, Types.INTEGER);
            }
            else{
                stmt.setInt(5,productRepository.getProductIdByName(product.getName()));
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void updateAccount(Account account) {

    }

    @Override
    public void removeAccount(Account account) {

    }
}
