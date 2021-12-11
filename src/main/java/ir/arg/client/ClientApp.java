package ir.arg.client;

import ir.arg.server.ErrorCode;
import ir.arg.server.ServerAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Random;

public class ClientApp implements Client {
    public static void main(String[] args) {
        new ClientApp();
    }

    private final ServerAPI api = ServerAPI.getInstance();

    private String authentication = null;

    public ClientApp() {
//        new SignUp(this, "arg", "abcDEF123!@#").send();
        new SignIn(this, "arg", "abcDEF123!@#").send();
//        new CreateTweet("Hello, Tweety!").send();
//        final JSONObject response = requestWithAuth("send_tweet", "\"contents\": " + JSONObject.quote(contents));
    }

    private final Random random = new Random();

    @Override
    public String generateToken() {
        return Integer.toHexString(random.nextInt());
    }

    @Override
    public String sendRequest(@NotNull String request) {
        return api.request(request);
    }

    @Override
    public String getClientInfo() {
        return "{\"name\": \"Default Client\"}";
        // TODO Client ID
    }

    @Nullable
    @Override
    public String getAuthentication() {
        return authentication;
    }

    @Override
    public void onSignIn(@NotNull String username, @NotNull String token) {
        this.authentication = "\"username\": " + username + ", \"token\": " + token;
        System.out.println("Signing in was successful.");
    }

    @Override
    public void onSignOut() {
        this.authentication = null;
        System.out.println("Signed out. Please sign in again.");
    }

    @Override
    public void onError(int errorCode) {
        System.err.println(ErrorCode.getErrorDescription(errorCode));
    }
}
