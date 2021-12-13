package ir.arg.client.requests;

import ir.arg.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public abstract class RequestWithAuth extends RequestImpl {

    public RequestWithAuth(@NotNull Client client) {
        super(client);
    }

    @NotNull
    public abstract String getMethod();

    @NotNull
    public abstract String getParameters();

    @Override
    public @Nullable String make() {
        final String auth = client.getAuthentication();
        if (auth == null) {
            System.out.println("Please sign-in and try again.");
            return null;
        } else {
            return "{\"method\": \"" + getMethod() + "\", " + auth + ", " + getParameters() + "}";
        }
    }

    @Override
    public void react(@NotNull JSONObject response) {
        if (response.getInt("error_code") == AUTHENTICATION_FAILED) {
            System.out.println("Failed to authenticate; please sign-in again.");
            client.signOut();
        }
    }
}
