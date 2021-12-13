package ir.arg.server.impl;

import ir.arg.server.*;
import ir.arg.server.contracts.Contract;
import ir.arg.server.contracts.impl.LengthLimitContract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

public class ServerImpl implements Server {

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final UserStorage userStorage = new UserStorageImpl();
    private final Database userDb = new DatabaseImpl("db/users/", "users-db");
    private final Database tweetDb = new DatabaseImpl("db/tweets/", "tweets-db");
    private final Properties props = new PropertiesImpl();
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl();
    private final PrintStream log = Logger.getLog("server");
    private final Contract<String> nameContract = new LengthLimitContract("Your name", 0, 64);
    private final Contract<String> bioContract = new LengthLimitContract("Your bio", 0, 256);
    private final Contract<String> tweetContentsContract = new LengthLimitContract("A tweet", 1, 256);
    private final PaginationService paginationService = new PaginationServiceImpl();

    {
        log.println("--------".repeat(8));
        log("SERVER STARTED");
    }

    @Override
    public @NotNull Contract<String> getNameContract() {
        return nameContract;
    }

    @Override
    public @NotNull Contract<String> getBioContract() {
        return bioContract;
    }

    @Override
    public @NotNull Contract<String> getTweetContentsContract() {
        return tweetContentsContract;
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
    public @Nullable User findUser(@NotNull String username) {
        return getUserStorage().findUser(username);
    }

    @Override
    public @NotNull Database getTweetDatabase() {
        return tweetDb;
    }

    @Override
    public @Nullable Tweet findTweet(@NotNull String tweetId) {
        try {
            return TweetImpl.fromFile(tweetId);
        } catch (IOException e) {
            log(e);
            return null;
        }
    }

    @Override
    public @NotNull UserStorage getUserStorage() {
        return userStorage;
    }

    @Override
    public @NotNull PaginationService getPaginationService() {
        return paginationService;
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

    @Override
    @NotNull
    public String request(@NotNull final String request) {
        try {
            final JSONObject input;
            try {
                input = new JSONObject(new JSONTokener(request));
            } catch (JSONException e) {
                return err(FAILED_TO_PARSE_REQUEST).toString();
            }
            final String methodName;
            try {
                methodName = input.getString("method");
            } catch (JSONException e) {
                return err(METHOD_MISSING, e).toString();
            }
            final Methods.Method method = Methods.find(methodName);
            if (method == null) {
                return err(UNDEFINED_METHOD).toString();
            } else {
                return method.process(this, input).toString();
            }
        } catch (Throwable e) {
            return err(UNCAUGHT, e).toString();
        }
    }
}
