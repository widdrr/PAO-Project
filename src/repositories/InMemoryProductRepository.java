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
    public Optional<Product> getProductByName(String name) {
        return products.stream().filter((product) -> name.equalsIgnoreCase(product.getName())).findFirst();
    }

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public void removeProduct() {

    }
}
