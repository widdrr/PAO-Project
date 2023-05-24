package repositories;

import database.IDBConnection;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

//these repos are a horrible mess
public class SQLProductRepository implements IProductRepository{

    private final IDBConnection databaseConnection;

    //This is HORRIBLE, but I can't be bothered
    private SQLAccountRepository accountRepository = null;
    private static final String ProductsOwnedByAccount =
            "SELECT  product_name, price, creator, release_date, dependency " +
                    "FROM account_products JOIN products USING(product_name)" +
                    "WHERE account_name = ?";

    private static final String TagsForProduct =
            "SELECT tag FROM product_tags WHERE product_name = ?";
    private static final String AllProducts =
            "SELECT product_name, product_name, price, creator, release_date, dependency " +
                    "FROM products";

    private static final String GetProductByName =
            "SELECT * " +
                    "FROM products " +
                    "WHERE product_name = ?";


    private static final String InsertProduct =
            "INSERT INTO products VALUES (?, ?, ?, ?, ?)";
    private static final String InsertTag =
            "INSERT INTO product_tags VALUES (?, ?)";
    public void addHack(SQLAccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    public SQLProductRepository(IDBConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public HashSet<GameTags> getTagsForProduct(String product) throws SQLException{

        PreparedStatement stmt = databaseConnection.getCon().prepareStatement(TagsForProduct);
        stmt.setString(1,product);

        ResultSet rs = stmt.executeQuery();
        HashSet<GameTags> tags = new HashSet<>();

        while(rs.next()){
            tags.add(GameTags.valueOf(rs.getString("tag")));
        }

        return tags;
    }
    public HashSet<Product> getProductsForUser(String user){
        try {
            PreparedStatement stmt = databaseConnection.getCon().prepareStatement(ProductsOwnedByAccount);
            stmt.setString(1,user);
            ResultSet rs = stmt.executeQuery();

            HashSet<Product> products = new HashSet<>();
            while(rs.next()){
                String dependency = rs.getString("dependency");
                String product_name = rs.getString("product_name");

                Product product;
                HashSet<GameTags> tags = getTagsForProduct(product_name);
                if(dependency == null){
                    product = new Game(
                            rs.getDouble("price"),
                            product_name,
                            (CreatorAccount) accountRepository.getAccountByUsername(rs.getString("creator")).get(),
                            rs.getObject("release_date", LocalDateTime.class),
                            tags
                    );
                }
                else
                {
                    product = new GameContent(
                            rs.getDouble("price"),
                            product_name,
                            (CreatorAccount) accountRepository.getAccountByUsername(rs.getString("creator")).get(),
                            rs.getObject("release_date", LocalDateTime.class),
                            tags,
                            (Game) getProductByName(dependency).get()
                    );
                }



                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<Product>();
        }
    }
    @Override
    public List<Product> listProducts() {
        try {
            Statement stmt = databaseConnection.getCon().createStatement();
            ResultSet rs = stmt.executeQuery(AllProducts);

            List<Product> products = new ArrayList<>();
            while(rs.next()){
                String dependency = rs.getString("dependency");
                String name = rs.getString("product_name");

                Product product;
                HashSet<GameTags> tags = getTagsForProduct(name);
                if(dependency == null){
                    product = new Game(
                            rs.getDouble("price"),
                            name,
                            (CreatorAccount) accountRepository.getAccountByUsername(rs.getString("creator")).get(),
                            rs.getObject("release_date", LocalDateTime.class),
                            tags
                    );
                }
                else
                {
                    product = new GameContent(
                            rs.getDouble("price"),
                            name,
                            (CreatorAccount) accountRepository.getAccountByUsername(rs.getString("creator")).get(),
                            rs.getObject("release_date", LocalDateTime.class),
                            tags,
                            (Game) getProductByName(dependency).get()
                    );
                }
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @Override
    public Optional<Product> getProductByName(String name) {
        try {
            PreparedStatement stmt = databaseConnection.getCon().prepareStatement(GetProductByName);
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();

            if(!rs.next())
                return Optional.empty();

            String dependency = rs.getString("dependency");

            Product product;
            HashSet<GameTags> tags = getTagsForProduct(name);
            if(dependency == null){
                product = new Game(
                        rs.getDouble("price"),
                        name,
                        (CreatorAccount) accountRepository.getAccountByUsername(rs.getString("creator")).get(),
                        rs.getObject("release_date", LocalDateTime.class),
                        tags
                );
            }
            else
            {
                product = new GameContent(
                        rs.getDouble("price"),
                        name,
                        (CreatorAccount) accountRepository.getAccountByUsername(rs.getString("creator")).get(),
                        rs.getObject("release_date", LocalDateTime.class),
                        tags,
                        (Game) getProductByName(dependency).get()
                );
            }
            return Optional.of(product);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }
    @Override
    public void addProduct(Product product) {
        Product dependency = null;
        if(product instanceof GameContent)
            dependency = ((GameContent) product).getDependency();

        try {
            PreparedStatement stmt = databaseConnection.getCon().prepareStatement(InsertProduct);
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getCreator().getUsername());
            stmt.setObject(4, product.getReleaseDate());
            if(dependency == null){
                stmt.setNull(5,Types.INTEGER);
            }
            else{
                stmt.setString(5,((GameContent) product).getDependency().getName());
            }

            stmt.executeUpdate();

            for(GameTags tag : ((Game) product).getTags()){
                addTags(product.getName(),tag);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addTags(String product, GameTags tag) throws SQLException{
        PreparedStatement stmt = databaseConnection.getCon().prepareStatement(InsertTag);
        stmt.setString(1,product);
        stmt.setString(2,tag.name());

        stmt.executeUpdate();
    }
    @Override
    public void removeProduct() {

    }

    @Override
    public void updateProduct(Product product) {

    }
}
