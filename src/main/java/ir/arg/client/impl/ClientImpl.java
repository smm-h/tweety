package ir.arg.client.impl;

import ir.arg.client.Client;
import ir.arg.client.requests.CachedRequest;
import ir.arg.client.requests.Request;
import ir.arg.shared.RandomHex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import static ir.arg.server.ServerSingleton.request;

public class ClientImpl implements Client {
    public static void main(String[] args) {
        new ClientImpl();
    }

    private String authentication = null;

    public ClientImpl() {
        try {
//            new SignUp(this, "arg", "abcDEF123!@#").send();
//            new SignIn(this, "arg", "abcDEF123!@#").send();
//            createTweet("Hello, Tweety!").send();
//            createTweet("Can't wait for the new Spider-man movie.").send();
//            request("{\"method\": \"username_exists\", \"username\": \"arg\"}");
//            request("{\"method\": \"username_exists\", \"username\": \"arg2\"}");
            request("{\"method\": \"get_user_info\", \"username\": \"arg\"}");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private Request createTweet(@NotNull final String contents) {
        return new CachedRequest(this, CREATE_TWEET, "\"contents\": " + JSONObject.quote(contents));
    }

    @Override
    public String generateToken() {
        return RandomHex.generate(32);
    }

    @Override
    public String sendRequest(@NotNull String request) {
        return request(request);
    }

    @Override
    public String getDeviceInfo() {
        return "Test Device";
    }

    @Nullable
    @Override
    public String getAuthentication() {
        return authentication;
    }

    @Override
    public void onSignIn(@NotNull String username, @NotNull String token) {
        this.authentication = "\"my_username\": " + username + ", \"token\": " + token;
        System.out.println("Signing in was successful.");
    }

    @Override
    public void onSignOut() {
        this.authentication = null;
        System.out.println("Signed out. Please sign in again.");
    }

    @Override
    public void onError(int errorCode) {
    }
}
