package ir.arg.client.impl;

import ir.arg.client.Client;
import ir.arg.client.requests.CachedRequest;
import ir.arg.client.requests.SignIn;
import ir.arg.client.requests.SignUp;
import ir.arg.client.requests.UsernameExists;
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
            switchUser("arg", "abcDEF123!@#");
//            createTweet("Hello, Tweety!");
//            createTweet("Can't wait for the new Spider-man movie.");
            request("{\"method\": \"" + GET_TWEETS_OF_USER + "\", \"username\": \"arg\"}");
//            switchUser("other_user123", "]!0.a.z.9![");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean usernameExists(@NotNull final String username) {
        final UsernameExists ue = new UsernameExists(this, username);
        ue.send();
        return ue.exists;
    }

    private void switchUser(@NotNull final String username, @NotNull final String password) {
        if (isSignedIn()) {
            signOut();
        }
        if (!usernameExists(username)) {
            new SignUp(this, username, password).send();
        }
        new SignIn(this, username, password).send();
    }

    private void createTweet(@NotNull final String contents) {
        new CachedRequest(this, CREATE_TWEET, "\"contents\": " + JSONObject.quote(contents)).send();
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
    public void signInWithToken(@NotNull String username, @NotNull String token) {
        this.authentication = "\"my_username\": " + username + ", \"token\": " + token;
        System.out.println("Signing in was successful.");
    }

    @Override
    public void signOut() {
        this.authentication = null;
        System.out.println("Signed out. Please sign in again.");
    }

    @Override
    public void onError(int errorCode) {
    }
}
