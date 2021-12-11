package ir.arg.client;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class SignUp extends Request {

    @NotNull
    private final String username, password;

    public SignUp(@NotNull final Client client, @NotNull final String username, @NotNull final String password) {
        super(client);
        this.username = username;
        this.password = password;
    }

    @Override
    public @NotNull String make() {
        return "{\"method\": \"sign_up\", \"sign_up_bundle\": {\"username\": \"" + username + "\", \"password\": \"" + password + "\"}}";
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
