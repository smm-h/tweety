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
    private final Database userTweetsDb = new DatabaseImpl("db/usertweets/");
    private final TweetingService tweetingService = new TweetingServiceImpl();
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl();

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
    public @NotNull Database getUserTweetsDatabase() {
        return userTweetsDb;
    }

    @Override
    public @NotNull DateFormat getDateFormat() {
        return DATE_FORMAT;
    }

    @Override
    public @NotNull TweetingService getTweetingService() {
        return tweetingService;
    }

    @Override
    public @NotNull AuthenticationService AuthenticationService() {
        return authenticationService;
    }
}
