package model;

import java.util.ArrayList;

public class Game extends Product{

    protected ArrayList<GameTags> tags;
    public Game(double price, String name, CreatorAccount creator, ArrayList<GameTags> tags) {
        super(price, name, creator);
        this.tags = new ArrayList<GameTags>(tags);
    }

    @Override
    public String toString() {
        return "Game{" +
                "tags=" + tags +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", creator=" + creator +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
