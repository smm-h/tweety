package ir.arg.server.auth.impl;

import ir.arg.server.auth.SignUpBundle;
import org.jetbrains.annotations.NotNull;

public class SignUpBundleImpl implements SignUpBundle {

    @NotNull
    private final String enteredUsername, enteredPassword;

    public SignUpBundleImpl(@NotNull String enteredUsername, @NotNull String enteredPassword) {
        this.enteredUsername = enteredUsername;
        this.enteredPassword = enteredPassword;
    }

    @Override
    public @NotNull String getEnteredUsername() {
        return enteredUsername;
    }

    @Override
    public @NotNull String getEnteredPassword() {
        return enteredPassword;
    }
}
