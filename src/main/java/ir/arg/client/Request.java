package ir.arg.client;

import ir.arg.server.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class Request implements ErrorCode {
    final Client client;

    public Request(@NotNull final Client client) {
        this.client = client;
    }

    @NotNull
    public abstract String make();

    public void send() {
        react(new JSONObject(new JSONTokener(client.sendRequest(make()))));
    }

    public abstract void react(@NotNull final JSONObject response);
}
