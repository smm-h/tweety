package ir.arg.server;

import ir.arg.server.contracts.Contract;
import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.PrintStream;
import java.text.DateFormat;
import java.time.Instant;
import java.time.ZoneId;

public interface Server extends ErrorCode {

    @NotNull Contract<String> getNameContract();

    @NotNull Contract<String> getBioContract();

    @NotNull Properties getProperties();

    @NotNull Database getUserDatabase();

    @Nullable User findUser(@NotNull String username);

    @NotNull Database getTweetDatabase();

    @Nullable Tweet findTweet(@NotNull String tweetId);

    @NotNull UserStorage getUserStorage();

    @NotNull Database getUserTweetsDatabase();

    @NotNull DateFormat getDateFormat();

    @NotNull ZoneId getZoneId();

    @NotNull AuthenticationService getAuthenticationService();

    @NotNull PrintStream getLog();

    @NotNull String request(@NotNull String request);

    default void log(@NotNull final String text) {
        final PrintStream p = getLog();
        p.print(Instant.now());
        p.print(" \t ");
        p.println(text);
    }

    default JSONObject err(final int errorCode) {
        final JSONObject response = new JSONObject();
        response.put("error_code", errorCode);
        response.put("description", ErrorCode.getErrorDescription(errorCode));
        log("ERROR: " + errorCode + " (" + ErrorCode.getErrorDescription(errorCode) + ")");
        return response;
    }

    default JSONObject err(final int errorCode, final Throwable error) {
        final JSONObject response = err(errorCode);
        response.put("details", error.getMessage());
        log("DETAILS: " + error.getMessage());
        error.printStackTrace();
        return response;
    }

    default JSONObject err(@NotNull final String details) {
        final JSONObject response = err(CONTRACT_VOIDED);
        response.put("details", details);
        log("CONTRACT-VOID: " + details);
        return response;
    }
}
