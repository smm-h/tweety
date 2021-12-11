package ir.arg.server;

import ir.arg.server.auth.AuthenticationService;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.text.DateFormat;
import java.time.Instant;
import java.time.ZoneId;

public interface Server {
    @NotNull Properties getProperties();

    @NotNull Database getUserDatabase();

    @NotNull Database getTweetDatabase();

    @NotNull UserStorage getUserStorage();

    @NotNull Database getUserTweetsDatabase();

    @NotNull DateFormat getDateFormat();

    @NotNull ZoneId getZoneId();

    @NotNull TweetingService getTweetingService();

    @NotNull AuthenticationService getAuthenticationService();

    @NotNull PrintStream getLog();

    default void log(@NotNull final String text) {
        final PrintStream p = getLog();
        p.print(Instant.now());
        p.print(" \t ");
        p.println(text);
    }
}
