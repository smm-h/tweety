package ir.arg.server;

import ir.arg.server.auth.AuthenticationService;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;

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
    Database getUserTweetsDatabase();

    @NotNull
    DateFormat getDateFormat();

    @NotNull
    TweetingService getTweetingService();

    @NotNull
    AuthenticationService getAuthenticationService();
}
