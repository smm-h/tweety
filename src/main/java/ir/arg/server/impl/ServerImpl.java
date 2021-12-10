package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ServerImpl implements Server {

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final UserStorage userStorage = new UserStorageImpl();

    @Override
    public @NotNull Tweet sendTweet(@NotNull User user, @NotNull String contents) {
        return TweetImpl.create(user, contents);
    }

    @Override
    public @NotNull Properties getProperties() {
        return null;
    }

    @Override
    public @NotNull Database getUserDatabase() {
        return null;
    }

    @Override
    public @NotNull Database getTweetDatabase() {
        return null;
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
