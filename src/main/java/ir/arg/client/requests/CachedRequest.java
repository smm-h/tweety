package ir.arg.client.requests;

import ir.arg.client.Client;
import org.jetbrains.annotations.NotNull;

public class CachedRequest extends RequestWithAuth {

    @NotNull
    private final String method, params;

    public CachedRequest(@NotNull final Client client, @NotNull final String method, @NotNull final String params) {
        super(client);
        this.method = method;
        this.params = params;
    }

    @Override
    public @NotNull String getMethod() {
        return method;
    }

    @Override
    public @NotNull String getParameters() {
        return params;
    }
}
