package model;

import java.util.Set;

public class GameContent extends Game{

    protected Game dependency;
    public GameContent(double price, String name, CreatorAccount creator, Set<GameTags> tags, Game dependency) {

        super(price, name, creator, tags);
        this.dependency = dependency;
    }
}
