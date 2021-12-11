package ir.arg.server.auth.impl;

import ir.arg.server.ServerSingleton;
import ir.arg.server.User;
import ir.arg.server.UserStorage;
import ir.arg.server.auth.AuthenticationService;
import ir.arg.server.auth.PasswordStrengthService;
import ir.arg.server.auth.SignInBundle;
import ir.arg.server.auth.SignUpBundle;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordStrengthService passwordStrengthService = new PasswordStrengthServiceImpl();

    @Override
    public PasswordStrengthService getPasswordStrengthService() {
        return passwordStrengthService;
    }

    @NotNull
    private final MessageDigest md = createMessageDigest();

    @NotNull
    private MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("failed to get an instance of SHA-256");
            return null;
        }
    }

    @Override
    public @NotNull String hashPassword(@NotNull String password) {
        return new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public @NotNull int signUp(@NotNull SignUpBundle bundle) {
        final String enteredUsername = bundle.getEnteredUsername().toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getEnteredPassword();
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return USERNAME_EMPTY;
        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return PASSWORD_EMPTY;
        if (isUsernameInvalid(enteredUsername))
            return BAD_USERNAME;
        final UserStorage userStorage = ServerSingleton.getServer().getUserStorage();
        if (userStorage.usernameExists(enteredUsername))
            return USERNAME_ALREADY_EXISTS;
        if (!getPasswordStrengthService().isPasswordStrong(enteredPassword))
            return PASSWORD_TOO_WEAK;
        JSONObject object = new JSONObject();
        object.put("name", "");
        object.put("bio", "");
        object.put("passwordHash", hashPassword(enteredPassword));
        object.put("lastTweetIndex", -1);
        ServerSingleton.getServer().getUserDatabase().writeFile(enteredUsername, object.toString());
        return NO_ERROR;
    }

    @Override
    public @NotNull int signIn(@NotNull SignInBundle bundle) {
        final String enteredUsername = bundle.getEnteredUsername().toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getEnteredPassword();
        final String generatedToken = bundle.getGeneratedToken();
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return USERNAME_EMPTY;
        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return PASSWORD_EMPTY;
        if (generatedToken.isEmpty() || generatedToken.isBlank())
            return INVALID_CLIENT;
        if (isUsernameInvalid(enteredUsername))
            return BAD_USERNAME;
        final UserStorage userStorage = ServerSingleton.getServer().getUserStorage();
        if (!userStorage.usernameExists(enteredUsername))
            return USERNAME_DOES_NOT_EXIST;
        final User user = userStorage.findUser(enteredUsername);
        assert user != null;
        if (!hashPassword(enteredPassword).equals(user.getPasswordHash()))
            return INCORRECT_PASSWORD;
        createSession(user, generatedToken);
        return NO_ERROR;
    }

    private final Map<String, Set<String>> sessions = new HashMap<>();

    @Override
    public void createSession(@NotNull User user, @NotNull String token) {
        final Set<String> tokens = sessions.computeIfAbsent(user.getUsername(), s -> new HashSet<>());
        tokens.add(token);
    }

    @Override
    public boolean isSessionValid(@NotNull String username, @NotNull String token) {
        final Set<String> tokens = sessions.get(username);
        return tokens != null && tokens.contains(token);
    }
}
