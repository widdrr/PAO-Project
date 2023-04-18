package repositories;

import model.Product;

import java.util.*;


public class InMemoryProductRepository implements IProductRepository {
    private final TreeSet<Product> products;

    public InMemoryProductRepository() {
        this.products = new TreeSet<Product>();
    }
    @Override
    public List<Product> listProducts() {
        return products.stream().toList();
    }

    @Override
    public Optional<Product> getProductByName() {
        return Optional.empty();
    }

    @Override
    public void addProduct() {

    }

    @Override
    public void removeProduct() {

    }
}
