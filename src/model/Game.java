package model;

import java.util.HashSet;
import java.util.Set;

public class Game extends Product{

    protected HashSet<GameTags> tags;
    public Game(double price, String name, CreatorAccount creator, Set<GameTags> tags) {
        super(price, name, creator);
        this.tags = new HashSet<>(tags);
    }

    @Override
    public String toString() {
        return "Type = Game" +
                "\nName: " + this.name +
                "\nCreator: " + this.creator.getUsername() +
                "\nPrice: " + this.price +
                "\nTags " + this.tags;
    }
}
