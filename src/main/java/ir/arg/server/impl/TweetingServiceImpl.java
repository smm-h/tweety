package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;

public class TweetingServiceImpl implements TweetingService {
    @Override
    public @NotNull Tweet sendTweet(@NotNull User user, @NotNull String contents) {
        return TweetImpl.create(user, contents);
    }

    @Override
    public boolean deleteTweet(@NotNull String filename) {
        final Database db = ServerSingleton.getServer().getTweetDatabase();
        return db.fileExists(filename) && db.deleteFile(filename);
    }
}
