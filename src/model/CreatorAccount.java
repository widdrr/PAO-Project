package model;

import java.util.ArrayList;
import java.util.HashSet;

public final class CreatorAccount extends Account{

    public final GameBuilder createGame;
    public static class GameBuilder{

        private final CreatorAccount creator;
        private final HashSet<GameTags> tags;
        private String name;
        private double price;

        public GameBuilder(CreatorAccount creator) {
            this.creator = creator;
            tags = new HashSet<>();
        }

        public void addTag(GameTags tag){
            this.tags.add(tag);
        }
        public void setName(String name){
            this.name = name;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Product create(){
            Product newGame = new Game(this.price,this.name,this.creator,this.tags);
            clear();
            return  newGame;
        }

        public void clear(){
            tags.clear();
            name = "";
            price = 0.0;
        }
    }
    public CreatorAccount(String username, String passwordHash) {
        super(username, passwordHash);
        createGame = new GameBuilder(this);
    }

    @Override
    public String toString() {
        return "CreatorAccount{" +
                "username='" + username + '\'' +
                ", passwordHash=" + passwordHash +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
