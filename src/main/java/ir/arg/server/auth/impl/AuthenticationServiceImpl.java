package ir.arg.server.auth.impl;

import ir.arg.server.ServerSingleton;
import ir.arg.server.User;
import ir.arg.server.UserStorage;
import ir.arg.server.auth.*;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordStrengthService passwordStrengthService = new PasswordStrengthServiceImpl();

    @Override
    public PasswordStrengthService getPasswordStrengthService() {
        return passwordStrengthService;
    }

    @Override
    public @NotNull String hashPassword(String password) {

    }

    @Override
    public @NotNull SigningUpOutcome signUp(@NotNull SignUpBundle bundle) {
        final String enteredUsername = bundle.getEnteredUsername().toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getEnteredPassword();
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return SigningUpOutcome.USERNAME_EMPTY;
        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return SigningUpOutcome.PASSWORD_EMPTY;
        if (isUsernameInvalid(enteredUsername))
            return SigningUpOutcome.BAD_USERNAME;
        final UserStorage userStorage = ServerSingleton.getServer().getUserStorage();
        if (userStorage.usernameExists(enteredUsername))
            return SigningUpOutcome.USERNAME_ALREADY_EXISTS;
        // SigningUpOutcome.RESERVED_USERNAME TODO reserved usernames
        if (!getPasswordStrengthService().isPasswordStrong(enteredPassword))
            return SigningUpOutcome.PASSWORD_TOO_WEAK;
        // TODO actual sign up
        return SigningUpOutcome.SUCCESSFUL;
    }

    @Override
    public @NotNull SigningInOutcome signIn(@NotNull SignInBundle bundle) {
        final String enteredUsername = bundle.getEnteredUsername().toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getEnteredPassword();
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return SigningInOutcome.USERNAME_EMPTY;
        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return SigningInOutcome.PASSWORD_EMPTY;
        if (isUsernameInvalid(enteredUsername))
            return SigningInOutcome.BAD_USERNAME;
        final UserStorage userStorage = ServerSingleton.getServer().getUserStorage();
        if (!userStorage.usernameExists(enteredUsername))
            return SigningInOutcome.USERNAME_DOES_NOT_EXIST;
        final User user = userStorage.findUser(enteredUsername);
        assert user != null;
        if (!hashPassword(enteredPassword).equals(user.getPasswordHash()))
            return SigningInOutcome.INCORRECT_PASSWORD;
        final String generatedToken = bundle.getGeneratedToken();
        // TODO actual sign in
        return SigningInOutcome.SUCCESSFUL;
    }
}
