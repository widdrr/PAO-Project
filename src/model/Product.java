package model;

import java.time.LocalDateTime;

public abstract class Product implements Comparable<Product> {

    protected double price;
    protected final String name;
    protected final CreatorAccount creator;
    protected final LocalDateTime releaseDate;

    public Product(double price, String name, CreatorAccount creator) {
        this.price = price;
        this.name = name;
        this.creator = creator;
        this.releaseDate = LocalDateTime.now();
    }

    public Product(double price, String name, CreatorAccount creator, LocalDateTime releaseDate) {
        this.price = price;
        this.name = name;
        this.creator = creator;
        this.releaseDate = releaseDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public CreatorAccount getCreator() {
        return creator;
    }
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public abstract void payCreator();
    //TODO: sort by product type too


    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object obj) {
        return this.compareTo((Product)obj) == 0;
    }
}
