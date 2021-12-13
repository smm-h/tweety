package ir.arg.client;

import ir.arg.shared.APIMethods;
import ir.arg.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Client extends ErrorCode, APIMethods {
    String sendRequest(@NotNull final String request);

    String getDeviceInfo();

    String generateToken();

    void signInWithToken(@NotNull final String username, @NotNull final String token);

    void signOut();

    @Nullable
    String getAuthentication();

    default boolean isSignedIn() {
        return getAuthentication() != null;
    }

    void onError(final int errorCode);
}
