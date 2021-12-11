package ir.arg.client.requests;

import ir.arg.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class SignIn extends Request {

    @NotNull
    private final String username, password, token;

    public SignIn(@NotNull final Client client, @NotNull final String username, @NotNull final String password) throws RequestConstructionException {
        super(client);
        this.username = username;
        this.password = password;
        this.token = client.generateToken();
        RestrictionException.restrictUsername(username);
    }

    @Override
    public @Nullable String make() {
        return "{\"method\": \"" + SIGN_IN + "\", \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"token\": \"" + token + ", \"deviceInfo\": \"" + JSONObject.quote(client.getDeviceInfo()) + "\"}";
    }

    @Override
    public void react(@NotNull JSONObject response) {
        final int errorCode = response.getInt("error_code");
        if (errorCode == NO_ERROR) {
            client.onSignIn(username, token);
        } else {
            client.onError(errorCode);
        }
    }
}
