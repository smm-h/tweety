package ir.arg.server.impl;

import ir.arg.server.*;
import ir.arg.server.auth.AuthenticationService;
import ir.arg.server.auth.impl.AuthenticationServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

public class ServerImpl implements Server {

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final UserStorage userStorage = new UserStorageImpl();
    private final Database userDb = new DatabaseImpl("db/users/");
    private final Database tweetDb = new DatabaseImpl("db/tweets/");
    private final Database userTweetsDb = new DatabaseImpl("db/usertweets/");
    private final Properties props = new PropertiesImpl();
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl();
    private final PrintStream log = getLoggingPrintStream();

    {
        log.println();
        log("SERVER STARTED");
    }

    private PrintStream getLoggingPrintStream() {
        try {
            return new PrintStream(new FileOutputStream("LOG.TXT", true));
        } catch (FileNotFoundException e) {
            System.err.println("failed to open log file, using err instead");
            return System.err;
        }
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
    public @NotNull Database getUserTweetsDatabase() {
        return userTweetsDb;
    }

    @Override
    public @NotNull DateFormat getDateFormat() {
        return DATE_FORMAT;
    }

    @Override
    public @NotNull ZoneId getZoneId() {
        return zoneId;
    }

    @Override
    public @NotNull AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Override
    public @NotNull PrintStream getLog() {
        return log;
    }
}
