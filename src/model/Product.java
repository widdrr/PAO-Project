package model;

import java.util.Date;

public abstract class Product implements Comparable<Product> {

    protected double price;
    protected final String name;
    protected final CreatorAccount creator;
    protected final Date releaseDate;

    public Product(double price, String name, CreatorAccount creator) {
        this.price = price;
        this.name = name;
        this.creator = creator;
        this.releaseDate = new Date();
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
    public Date getReleaseDate() {
        return releaseDate;
    }
    //TODO: sort by product type too
    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.getName());
    }
}
