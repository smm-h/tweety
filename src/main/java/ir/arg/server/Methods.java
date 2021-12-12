package ir.arg.server;

import ir.arg.server.impl.TweetImpl;
import org.json.JSONException;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;

import static ir.arg.server.ServerAPI.err;
import static ir.arg.server.shared.ErrorCode.INADEQUATE_REQUEST;

public interface Methods {

    Method signUp = (server, object) -> err(server.getAuthenticationService().signUp(object));
    Method signIn = (server, object) -> err(server.getAuthenticationService().signIn(object));

    static String randomSuffix() {
        return Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
    }

    Method createTweet = (AuthMethod) (server, user, object) -> {
        final String sentOn = ServerSingleton.getServer().getDateFormat().format(Date.from(Instant.now()));
        final String username = user.getUsername();
        final int index = user.incrementLastTweetIndex();
        final String filename = sentOn + "-" + username + "-" + index + "-" + randomSuffix();
        final TweetImpl tweet = new TweetImpl(username, index, sentOn, contents, new LinkedHashSet<>(), filename);
        ServerSingleton.getServer().getTweetDatabase().writeFile(filename, tweet.serialize().toString());
    };

    Method deleteTweet = (AuthMethod) (server, user, object) -> {
        final String filename;
        try {
            filename = object.getString("tweet_id");
        } catch (JSONException e) {
            return err(INADEQUATE_REQUEST);
        }
        final Database db = ServerSingleton.getServer().getTweetDatabase();
        return db.fileExists(filename) && db.deleteFile(filename);
    };
}
