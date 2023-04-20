package model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Game extends Product{

    protected final HashSet<GameTags> tags;
    public Game(double price, String name, CreatorAccount creator, Set<GameTags> tags) {
        super(price, name, creator);
        this.tags = new HashSet<>(tags);
    }

    @Override
    public void payCreator() {
        Transaction payment = new Payment(new Date(), creator,this,this.price*0.8);
        creator.addTransaction(payment);
    }

    @Override
    public String toString() {
        return "Type = Game" +
                "\nName: " + this.name +
                "\nCreator: " + this.creator.getUsername() +
                "\nPrice: " + this.price +
                "\nTags " + this.tags +"\n\n";
    }
}
