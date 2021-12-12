package ir.arg.server.impl;

import ir.arg.server.*;
import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;

public class TweetingServiceImpl implements TweetingService {

    private static String randomSuffix() {
        return Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
    }

    @Override
    public int createTweet(@NotNull final User user, @NotNull final JSONObject object) {
        final String sentOn = ServerSingleton.getServer().getDateFormat().format(Date.from(Instant.now()));
        final String username = user.getUsername();
        final int index = user.incrementLastTweetIndex();
        final String filename = sentOn + "-" + username + "-" + index + "-" + randomSuffix();
        final TweetImpl tweet = new TweetImpl(username, index, sentOn, contents, new LinkedHashSet<>(), filename);
        ServerSingleton.getServer().getTweetDatabase().writeFile(filename, tweet.serialize().toString());
    }

    @Override
    public int deleteTweet(@NotNull final User user, @NotNull final JSONObject object) {
        final String filename;
        try {
            filename = object.getString("tweet_id");
        }catch (JSONException e) {
            return ServerAPI.err(INADEQUATE_REQUEST);
        }
        final Database db = ServerSingleton.getServer().getTweetDatabase();
        return db.fileExists(filename) && db.deleteFile(filename);
    }
}
