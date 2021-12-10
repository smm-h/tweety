package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;

public class TweetingServiceImpl implements TweetingService {

    private static String randomSuffix() {
        return Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
    }

    @Override
    public @NotNull Tweet sendTweet(final @NotNull User user, final @NotNull String contents) {
        final String sentOn = ServerSingleton.getServer().getDateFormat().format(Date.from(Instant.now()));
        final String username = user.getUsername();
        final int index = user.incrementLastTweetIndex();
        final String filename = sentOn + "-" + username + "-" + index + "-" + randomSuffix();
        final TweetImpl tweet = new TweetImpl(username, index, sentOn, contents, new LinkedHashSet<>(), filename);
        ServerSingleton.getServer().getTweetDatabase().writeFile(filename, tweet.serialize().toString());
        return tweet;
    }

    @Override
    public boolean deleteTweet(@NotNull String filename) {
        final Database db = ServerSingleton.getServer().getTweetDatabase();
        return db.fileExists(filename) && db.deleteFile(filename);
    }
}
