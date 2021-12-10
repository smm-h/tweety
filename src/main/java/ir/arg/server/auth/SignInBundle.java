package ir.arg.server.auth;

import org.jetbrains.annotations.NotNull;

public interface SignInBundle {
    @NotNull
    String getEnteredUsername();

    @NotNull
    String getEnteredPassword();

    @NotNull
    String getGeneratedToken();
}
