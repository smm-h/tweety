package ir.arg.server;

import ir.arg.server.impl.ServerImpl;
import ir.arg.server.impl.TweetImpl;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface Server {
    @NotNull
    Properties getProperties();

    @NotNull
    Database getUserDatabase();

    @NotNull
    Database getTweetDatabase();

    @NotNull
    UserStorage getUserStorage();

    @NotNull
    DateFormat getDateFormat();

    @NotNull
    TweetingService getTweetingService();

    @NotNull
    AuthenticationService AuthenticationService();
}
