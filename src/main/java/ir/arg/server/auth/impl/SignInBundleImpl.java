package ir.arg.server.auth.impl;

import ir.arg.server.auth.SignInBundle;
import org.jetbrains.annotations.NotNull;

public class SignInBundleImpl implements SignInBundle {

    @NotNull
    private final String enteredUsername, enteredPassword, generatedToken;

    public SignInBundleImpl(@NotNull String enteredUsername, @NotNull String enteredPassword, @NotNull String generatedToken) {
        this.enteredUsername = enteredUsername;
        this.enteredPassword = enteredPassword;
        this.generatedToken = generatedToken;
    }

    @Override
    public @NotNull String getEnteredUsername() {
        return enteredUsername;
    }

    @Override
    public @NotNull String getEnteredPassword() {
        return enteredPassword;
    }

    @Override
    public @NotNull String getGeneratedToken() {
        return generatedToken;
    }
}
