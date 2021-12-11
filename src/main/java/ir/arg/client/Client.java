package ir.arg.client;

import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Client extends ErrorCode {
    String sendRequest(@NotNull final String request);

    String getDeviceInfo();

    String generateToken();

    void onSignIn(@NotNull final String username, @NotNull final String token);

    void onSignOut();

    @Nullable
    String getAuthentication();

    void onError(final int errorCode);
}
