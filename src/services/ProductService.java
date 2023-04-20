package services;

import exceptions.EntityException;
import model.Product;
import repositories.IProductRepository;

import java.util.List;
import java.util.Optional;

public final class ProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listProducts(){
        return productRepository.listProducts();
    }
    public Product getProduct(String name) throws EntityException{
        Optional<Product> potentialProduct = productRepository.getProductByName(name);
        if(potentialProduct.isEmpty())
            throw new EntityException("Product does not exists!");
        return potentialProduct.get();
    }
    public void addProduct(Product newProduct){
        Optional<Product> existentProduct = productRepository.getProductByName(newProduct.getName());
        if(existentProduct.isPresent()){
            throw new EntityException("There is already a product with this name!");
        }
        productRepository.addProduct(newProduct);
    }
}
