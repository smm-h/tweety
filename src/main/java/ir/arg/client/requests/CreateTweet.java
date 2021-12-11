package ir.arg.client.requests;

import ir.arg.client.Client;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class CreateTweet extends RequestWithAuth {

    @NotNull
    private final String contents;

    public CreateTweet(@NotNull final Client client, @NotNull final String contents) throws RequestConstructionException {
        super(client);
        this.contents = RequestConstructionException.restrictTweetContents(contents);
    }

    @Override
    public @NotNull String getMethod() {
        return "create_tweet";
    }

    @Override
    public @NotNull String getParameters() {
        return "\"contents\": " + contents;
    }
}
