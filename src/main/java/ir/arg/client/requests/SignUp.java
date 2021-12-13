package ir.arg.client.requests;

import ir.arg.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class SignUp extends RequestImpl {

    @NotNull
    private final String username, password;

    public SignUp(@NotNull final Client client, @NotNull final String username, @NotNull final String password) {
        super(client);
        this.username = JSONObject.quote(username);
        this.password = JSONObject.quote(password);
    }

    @Override
    public @Nullable String make() {
        return "{\"method\": \"" + SIGN_UP + "\", \"username\": " + username + ", \"password\": " + password + "}";
    }

    @Override
    public void react(@NotNull JSONObject response) {
        final int errorCode = response.getInt("error_code");
        if (errorCode == NO_ERROR) {
            System.out.println("Signing up was successful.");
        } else {
            client.onError(errorCode);
        }
    }
}
