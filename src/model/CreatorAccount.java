package model;

public final class CreatorAccount extends Account{
    public CreatorAccount(String username, String passwordHash) {
        super(username, passwordHash);
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
