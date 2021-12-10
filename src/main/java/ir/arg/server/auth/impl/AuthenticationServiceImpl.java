package ir.arg.server.auth.impl;

import ir.arg.server.ServerSingleton;
import ir.arg.server.User;
import ir.arg.server.UserStorage;
import ir.arg.server.auth.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Locale;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordStrengthService passwordStrengthService = new PasswordStrengthServiceImpl();

    @Override
    public PasswordStrengthService getPasswordStrengthService() {
        return passwordStrengthService;
    }

    @Override
    public @NotNull String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
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
        JSONObject object = new JSONObject();
        object.put("name", "");
        object.put("bio", "");
        object.put("passwordHash", hashPassword(enteredPassword));
        object.put("lastTweetIndex", 0);
        ServerSingleton.getServer().getUserDatabase().writeFile(enteredUsername, object.toString());
        return SigningUpOutcome.SUCCESSFUL;
    }

    @Override
    public @NotNull SigningInOutcome signIn(@NotNull SignInBundle bundle) {
        final String enteredUsername = bundle.getEnteredUsername().toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getEnteredPassword();
        final String generatedToken = bundle.getGeneratedToken();
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return SigningInOutcome.USERNAME_EMPTY;
        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return SigningInOutcome.PASSWORD_EMPTY;
        if (generatedToken.isEmpty() || generatedToken.isBlank())
            return SigningInOutcome.EMPTY_TOKEN;
        if (isUsernameInvalid(enteredUsername))
            return SigningInOutcome.BAD_USERNAME;
        final UserStorage userStorage = ServerSingleton.getServer().getUserStorage();
        if (!userStorage.usernameExists(enteredUsername))
            return SigningInOutcome.USERNAME_DOES_NOT_EXIST;
        final User user = userStorage.findUser(enteredUsername);
        assert user != null;
        if (!hashPassword(enteredPassword).equals(user.getPasswordHash()))
            return SigningInOutcome.INCORRECT_PASSWORD;
        // TODO actual sign in
        return SigningInOutcome.SUCCESSFUL;
    }
}
