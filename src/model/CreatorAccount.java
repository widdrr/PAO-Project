package model;

import exceptions.FundsException;

import java.time.LocalDateTime;
import java.util.HashSet;

public final class CreatorAccount extends Account{

    public final GameBuilder createGame;
    public final ContentBuilder createContent;

    public static class GameBuilder{

        protected final CreatorAccount creator;
        protected final HashSet<GameTags> tags;
        protected String name;
        protected double price;

        public GameBuilder(CreatorAccount creator) {
            this.creator = creator;
            tags = new HashSet<>();
        }
        public void addTag(GameTags tag){
            this.tags.add(tag);
        }
        public void setName(String name){
            this.name = name.toLowerCase();
        }
        public void setPrice(double price) {
            this.price = price;
        }

        public Product create(){
            Product newGame = new Game(price,name,creator,tags);
            clear();
            return newGame;
        }
        public void clear(){
            tags.clear();
            name = "";
            price = 0.0;
        }
    }
    public static class ContentBuilder extends GameBuilder{

        private Game dependency = null;
        public ContentBuilder(CreatorAccount creator) {
            super(creator);
        }

        public void setDependency(Game dependency) {
            this.dependency = dependency;
            this.tags.addAll(dependency.tags);
        }

        public Product create(){
            Product newGameContent = new GameContent(price,name,creator,tags,dependency);
            clear();
            return newGameContent;
        }
        public void clear(){
            tags.clear();
            name = "";
            price = 0.0;
            dependency = null;
        }
    }
    public CreatorAccount(String username, String passwordHash) {
        super(username, passwordHash);
        createGame = new GameBuilder(this);
        createContent = new ContentBuilder(this);
    }
    public CreatorAccount(String username, int passwordHash, LocalDateTime lastLogin){
        super(username, passwordHash, lastLogin);
        createGame = new GameBuilder(this);
        createContent = new ContentBuilder(this);
    }
    public void withdraw(double sum) throws FundsException{
        if (getBalance() < sum){
            throw new FundsException("Sum exceeds balance!");
        }
        Transaction withdrawal = new Withdrawal(LocalDateTime.now(),this,sum);
        addTransaction(withdrawal);
    }

    @Override
    public String toString() {
        return "Creator: " + this.username
                + "\nBalance: " + this.getBalance()
                + "\nLast Login: " + this.lastLogin;
    }
}
