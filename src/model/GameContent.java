package model;

import java.util.Date;
import java.util.Set;

public class GameContent extends Game{

    protected final Game dependency;
    public GameContent(double price, String name, CreatorAccount creator, Set<GameTags> tags, Game dependency) {

        super(price, name, creator, tags);
        this.dependency = dependency;
    }

    @Override
    public void payCreator() {
        Transaction gamePayment = new Payment(new Date(), dependency.creator, this,price*0.5);
        Transaction contentPayment = new Payment(new Date(), creator, this, price*0.3);
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
