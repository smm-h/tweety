package ir.arg.client.requests;

import ir.arg.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class UsernameExists extends RequestImpl {
    @NotNull
    private final String username;
    public boolean exists;

    public UsernameExists(@NotNull Client client, @NotNull String username) {
        super(client);
        this.username = username;
    }

    @Override
    public @Nullable String make() {
        return "{\"method\": \"" + USERNAME_EXISTS + "\", \"username\": \"" + username + "\"}";
    }

    @Override
    public void react(@NotNull JSONObject response) {
        this.exists = response.getBoolean("exists");
    }
}
