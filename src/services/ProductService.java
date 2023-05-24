package services;

import exceptions.EntityException;
import exceptions.FundsException;
import logging.FileLogger;
import logging.ILogger;
import model.Product;
import model.UserAccount;
import repositories.IProductRepository;

import java.util.List;
import java.util.Optional;

public final class ProductService {

    private static final String logPath = "D:\\Data\\Repos\\IDEA\\Project\\src\\logging\\ProductLog.txt";
    private final IProductRepository productRepository;
    private final ILogger logger;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
        logger = new FileLogger(logPath);
    }

    public List<Product> listProducts(){
        logger.log("Listed all products");
        return productRepository.listProducts();

    }
    public Product getProduct(String name) throws EntityException{
        Optional<Product> potentialProduct = productRepository.getProductByName(name.toLowerCase());
        if(potentialProduct.isEmpty())
            throw new EntityException("Product does not exists!");
        Product product = potentialProduct.get();
        logger.log("Queried product " + product.getName());
        return product;
    }
    public void addProduct(Product newProduct){
        Optional<Product> existentProduct = productRepository.getProductByName(newProduct.getName().toLowerCase());
        if(existentProduct.isPresent()){
            throw new EntityException("There is already a product with this name!");
        }
        productRepository.addProduct(newProduct);
        logger.log("Creator " + newProduct.getCreator().getUsername() + " created a new product");
    }
}
