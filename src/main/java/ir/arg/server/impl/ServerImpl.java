package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ServerImpl implements Server {

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final UserStorage userStorage = new UserStorageImpl();
    private final Database userDb = new DatabaseImpl("db/users/");
    private final Database tweetDb = new DatabaseImpl("db/tweets/");
    private final Properties props = new PropertiesImpl();

    @Override
    public @NotNull Tweet sendTweet(@NotNull User user, @NotNull String contents) {
        return TweetImpl.create(user, contents);
    }

    @Override
    public @NotNull Properties getProperties() {
        return props;
    }

    @Override
    public @NotNull Database getUserDatabase() {
        return userDb;
    }

    @Override
    public @NotNull Database getTweetDatabase() {
        return tweetDb;
    }

    @Override
    public @NotNull UserStorage getUserStorage() {
        return userStorage;
    }

    @Override
    public @NotNull DateFormat getDateFormat() {
        return DATE_FORMAT;
    }
}
