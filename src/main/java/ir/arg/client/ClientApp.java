package ir.arg.client;

import ir.arg.server.ErrorCode;
import ir.arg.server.ServerAPI;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Random;

public class ClientApp implements ErrorCode {
    public static void main(String[] args) {
        new ClientApp();
    }

    private final ServerAPI api = ServerAPI.getInstance();
    private final Random random = new Random();

    private String username = null;
    private String token = null;

    public ClientApp() {
//        signUp("arg", "abcDEF123!@#");
        signIn("arg", "abcDEF123!@#");
        sendTweet("Hello, Tweety!");
    }

    private JSONObject signUp(final String username, final String password) {
        final JSONObject response = request("{\"method\": \"sign_up\", \"sign_up_bundle\": {\"username\": \"" + username + "\", \"password\": \"" + password + "\"}}");
        if (response.getInt("error_code") == NO_ERROR) {
            System.out.println("Signing up was successful.");
        }
        return response;
    }

    private JSONObject signIn(final String username, final String password) {
        final String token = Integer.toHexString(random.nextInt());
        final JSONObject response = request("{\"method\": \"sign_in\", \"sign_in_bundle\": {\"username\": \"" + username + "\", \"password\": \"" + password + "\", \"token\": \"" + token + "\"}}");
        if (response.getInt("error_code") == NO_ERROR) {
            this.username = username;
            this.token = token;
            System.out.println("Signing in was successful.");
        }
        return response;
    }

    private JSONObject sendTweet(final String contents) {
        final JSONObject response = requestWithAuth("send_tweet", "\"contents\": " + JSONObject.quote(contents));
        return response;
    }

    private JSONObject deleteTweet(final int index) {
        final JSONObject response = requestWithAuth("delete_tweet", "\"index\": " + index);
        return response;
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

    private JSONObject request(final String json) {
//        System.out.print("REQUEST  :\t");
//        System.out.println(json);
        final JSONObject response = new JSONObject(new JSONTokener(api.request(json)));
//        System.out.print("RESPONSE :\t");
//        System.out.print(response.getString("description") + " (" + response.getInt("error_code") + ")\n\n");
        return response;
    }
}
