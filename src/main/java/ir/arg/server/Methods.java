package ir.arg.server;

import ir.arg.server.impl.TweetImpl;
import ir.arg.server.shared.APIMethods;
import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;

public interface Methods extends APIMethods, ErrorCode {

    Method signUp = (server, object) -> server.err(server.getAuthenticationService().signUp(object));
    Method signIn = (server, object) -> server.err(server.getAuthenticationService().signIn(object));

    static String randomSuffix() {
        return Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
    }

    MethodWithAuth createTweet = (server, user, object) -> {
        final String sentOn = ServerSingleton.getServer().getDateFormat().format(Date.from(Instant.now()));
        final String username = user.getUsername();
        final int index = user.incrementLastTweetIndex();
        final String filename = sentOn + "-" + username + "-" + index + "-" + randomSuffix();
        final TweetImpl tweet = new TweetImpl(username, index, sentOn, contents, new LinkedHashSet<>(), filename);
        ServerSingleton.getServer().getTweetDatabase().writeFile(filename, tweet.serialize().toString());
    };

    MethodWithAuth deleteTweet = (server, user, object) -> {
        final String filename;
        try {
            filename = object.getString("tweet_id");
        } catch (JSONException e) {
            return server.err(INADEQUATE_REQUEST);
        }
        final Database db = ServerSingleton.getServer().getTweetDatabase();
        return db.fileExists(filename) && db.deleteFile(filename);
    };

    static Method find(String method) {
        switch (method) {
            case SIGN_UP:
                return signUp;
            case SIGN_IN:
                return signIn;
            case CREATE_TWEET:
                return createTweet;
            default:
                return null;
        }
    }

    interface Method extends ErrorCode {
        @NotNull
        JSONObject process(@NotNull final Server server, @NotNull final JSONObject object);
    }

    interface MethodWithAuth extends Method {

        @Override
        default @NotNull JSONObject process(final @NotNull Server server, final @NotNull JSONObject object) {
            final User me;
            try {
                final String myUsername = object.getString("my_username");
                final String token = object.getString("token");
                if (server.getAuthenticationService().authenticate(myUsername, token)) {
                    me = server.getUserStorage().findUser(myUsername);
                    if (me == null) {
                        return server.err(USER_NOT_FOUND);
                    } else {
                        return processWithAuth(server, me, object);
                    }
                } else {
                    return server.err(AUTHENTICATION_FAILED);
                }
            } catch (JSONException e) {
                return server.err(UNAUTHORIZED_REQUEST, e);
            }
        }

        @NotNull
        JSONObject processWithAuth(@NotNull final Server server, @NotNull final User user, @NotNull final JSONObject object);
    }
}
