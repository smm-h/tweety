package ir.arg.client.requests;

import ir.arg.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class SignIn extends RequestImpl {

    @NotNull
    private final String username, password, token;

    public SignIn(@NotNull final Client client, @NotNull final String username, @NotNull final String password) {
        super(client);
        this.username = JSONObject.quote(username);
        this.password = JSONObject.quote(password);
        this.token = JSONObject.quote(client.generateToken());
    }

    @Override
    public @Nullable String make() {
        return "{\"method\": \"" + SIGN_IN + "\", \"username\": " + username + ", \"password\": " + password + ", \"generated_token\": " + token + ", \"device_info\": " + JSONObject.quote(client.getDeviceInfo()) + "}";
    }

    @Override
    public void react(@NotNull JSONObject response) {
        final int errorCode = response.getInt("error_code");
        if (errorCode == NO_ERROR) {
            client.signInWithToken(username, token);
        } else {
            client.onError(errorCode);
        }
    }
}
