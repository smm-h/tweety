package ir.arg.server;

import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface TweetingService extends ErrorCode {

    @NotNull
    int createTweet(@NotNull final User user, @NotNull final JSONObject object);

    int deleteTweet(@NotNull final User user, @NotNull final JSONObject object);
}
