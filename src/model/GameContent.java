package model;

import java.time.LocalDateTime;
import java.util.HashSet;

public class GameContent extends Game{

    protected final Game dependency;
    public GameContent(double price, String name, CreatorAccount creator, HashSet<GameTags> tags, Game dependency) {
        super(price, name, creator, tags);
        this.dependency = dependency;
    }

    public GameContent(double price, String name, CreatorAccount creator,LocalDateTime releaseDate, HashSet<GameTags> tags, Game dependency) {
        super(price, name, creator, releaseDate, tags);
        this.dependency = dependency;
    }



    public Game getDependency() {
        return dependency;
    }

    @Override
    public void payCreator() {
        Transaction gamePayment = new Payment(LocalDateTime.now(), dependency.creator, this.name,price*0.5);
        Transaction contentPayment = new Payment(LocalDateTime.now(), creator, this.name, price*0.3);
        creator.addTransaction(contentPayment);
        dependency.creator.addTransaction(gamePayment);
   }

    @Override
    public String toString() {
        return "Type = Content" +
                "\nName: " + this.name +
                "\nForGame: " + this.dependency.getName() +
                "\nCreator: " + this.creator.getUsername() +
                "\nPrice: " + this.price +
                "\nTags " + this.tags +"\n\n";
    }
}
