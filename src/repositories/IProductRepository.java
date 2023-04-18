package repositories;

import model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {

    List<Product> listProducts();

    Optional<Product> getProductByName();
    void addProduct();
    void removeProduct();

}
