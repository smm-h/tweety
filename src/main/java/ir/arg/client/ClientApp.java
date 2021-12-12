package ir.arg.client;

import ir.arg.client.requests.*;
import ir.arg.server.Server;
import ir.arg.server.ServerSingleton;
import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Random;

public class ClientApp implements Client {
    public static void main(String[] args) {
        new ClientApp();
    }

    private final Server server = ServerSingleton.getServer();

    private String authentication = null;

    public ClientApp() {
        try {
//            new SignUp(this, "arg", "abcDEF123!@#").send();
            new SignIn(this, "arg", "abcDEF123!@#").send();
            createTweet("Hello, Tweety!").send();
//            createTweet("Can't wait for the new Spider-man movie.").send();
        } catch (RestrictionException e) {
            System.out.print("REQUEST FAILED: ");
            System.out.println(e.getMessage());
        }
    }

    private Request createTweet(@NotNull final String contents) throws RestrictionException {
        return new CachedRequest(this, CREATE_TWEET, "\"contents\": " + RestrictionException.restrictTweetContents(contents));
    }

    private final Random random = new Random();

    @Override
    public String generateToken() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            token.append(Integer.toHexString(random.nextInt()));
        }
        return token.toString().toLowerCase(Locale.ROOT);
    }

    @Override
    public String sendRequest(@NotNull String request) {
        return server.request(request);
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
        System.err.println(ErrorCode.getErrorDescription(errorCode));
    }
}
