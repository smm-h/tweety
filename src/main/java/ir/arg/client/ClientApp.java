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

    private String username = null;
    private String token = null;

    public ClientApp() {
//        new SignUp(this, "arg", "abcDEF123!@#").send();
        new SignIn(this, "arg", "abcDEF123!@#").send();
//        new CreateTweet("Hello, Tweety!").send();
//        final JSONObject response = requestWithAuth("send_tweet", "\"contents\": " + JSONObject.quote(contents));
    }

    @Nullable
    private JSONObject requestWithAuth(final String method, final String theRest) {
        if (token == null) {
            System.out.println("Please sign-in first.");
            return null;
        } else {
            final JSONObject response = request("{\"method\": \"" + method + "\", \"username\": " + username + ", \"token\": " + token + theRest + "}");
            if (response.getInt("error_code") == AUTHENTICATION_FAILED) {
                token = null;
                System.out.println("Authentication failed. Please sign in again.");
            }
            return response;
        }
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

    @Override
    public void onSignIn(@NotNull String username, @NotNull String token) {
        this.username = username;
        this.token = token;
        System.out.println("Signing in was successful.");
    }

    @Override
    public void onSignOut() {
        this.username = "";
        this.token = "";
        System.out.println("Signed out. Please sign in again.");
    }

    @Override
    public void onError(int errorCode) {
        System.err.println(ErrorCode.getErrorDescription(errorCode));
    }
}
