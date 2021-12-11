package ir.arg.client.requests;

import ir.arg.client.Client;
import ir.arg.server.shared.APIMethods;
import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class Request implements ErrorCode, APIMethods {
    final Client client;

    public Request(@NotNull final Client client) throws RequestConstructionException {
        this.client = client;
    }

    @Nullable
    public abstract String make();

    public void send() {
        final String made = make();
        if (made != null)
            react(new JSONObject(new JSONTokener(client.sendRequest(made))));
    }

    public abstract void react(@NotNull final JSONObject response);
}
