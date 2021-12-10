package ir.arg.server.auth;

import org.jetbrains.annotations.NotNull;

public interface SignUpBundle {
    @NotNull
    String getEnteredUsername();
    @NotNull
    String getEnteredPassword();
}
