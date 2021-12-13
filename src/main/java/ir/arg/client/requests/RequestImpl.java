package ir.arg.client.requests;

import ir.arg.client.Client;
import ir.arg.client.Request;
import ir.arg.shared.APIMethods;
import ir.arg.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class RequestImpl implements Request {
    final Client client;

    public RequestImpl(@NotNull final Client client) {
        this.client = client;
    }

    @NotNull
    @Override
    public Client getClient() {
        return client;
    }
}
