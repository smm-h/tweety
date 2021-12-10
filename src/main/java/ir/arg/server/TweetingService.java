package ir.arg.server;

import org.jetbrains.annotations.NotNull;

public interface TweetingService {

    @NotNull
    Tweet sendTweet(@NotNull final User user, @NotNull final String contents);

    boolean deleteTweet(@NotNull final String filename);

    default boolean deleteTweet(@NotNull final User user, final int index) {
        final String filename = user.getTweetAtIndex(index);
        return filename != null && deleteTweet(filename);
    }


}
