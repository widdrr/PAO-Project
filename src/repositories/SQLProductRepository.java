package repositories;

import database.IDBConnection;
import model.Product;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class SQLProductRepository implements IProductRepository{

    private final IDBConnection databaseConnection;
    private static final String ProductsOwnedByAccount =
            "SELECT * " +
                    "FROM account_products JOIN products USING(product_id)" +
                    "JOIN product_tags USING(product_id)" +
                    "WHERE account_id = ?";

    public SQLProductRepository(IDBConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public HashSet<Product> getProductsForUser(int user_id){

    }
    public Optional<Product> getProductById(int product_id){

    }
    public int getProductIdByName(String name){

    }
    @Override
    public List<Product> listProducts() {
        return null;
    }

    @Override
    public Optional<Product> getProductByName(String name) {
        return Optional.empty();
    }

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public void removeProduct() {

    }

    @Override
    public void updateProduct(Product product) {

    }
}
