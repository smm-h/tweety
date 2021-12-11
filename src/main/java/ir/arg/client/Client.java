package ir.arg.client;

import ir.arg.server.ErrorCode;
import org.jetbrains.annotations.NotNull;

public interface Client extends ErrorCode {
    String sendRequest(@NotNull final String request);

    String getClientInfo();

    String generateToken();

    void onSignIn(@NotNull final String username, @NotNull final String token);

    void onSignOut();

    void onError(final int errorCode);
}
