package repositories;

import model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {

    List<Product> listProducts();

    Optional<Product> getProductByName(String name);
    void addProduct(Product product);
    void removeProduct();

}
