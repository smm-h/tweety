package ir.arg.server.auth.impl;

import ir.arg.server.ServerSingleton;
import ir.arg.server.User;
import ir.arg.server.UserStorage;
import ir.arg.server.auth.AuthenticationService;
import ir.arg.server.auth.contracts.PasswordStrengthContract;
import ir.arg.server.auth.contracts.TokenDiversityContract;
import ir.arg.server.auth.contracts.impl.PasswordStrengthContractImpl;
import ir.arg.server.auth.contracts.impl.TokenDiversityContractImpl;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordStrengthContract passwordStrengthContract = new PasswordStrengthContractImpl();
    private final TokenDiversityContract tokenDiversityContract = new TokenDiversityContractImpl();

    @Override
    public PasswordStrengthContract getPasswordStrengthContract() {
        return passwordStrengthContract;
    }

    @Override
    public TokenDiversityContract getTokenDiversityContract() {
        return tokenDiversityContract;
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
    public int signUp(@NotNull final JSONObject bundle) {
        final String enteredUsername = bundle.getString("username").toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getString("password");
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return USERNAME_EMPTY;
        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return PASSWORD_EMPTY;
        if (isUsernameInvalid(enteredUsername))
            return BAD_USERNAME;
        final UserStorage userStorage = ServerSingleton.getServer().getUserStorage();
        if (userStorage.usernameExists(enteredUsername))
            return USERNAME_ALREADY_EXISTS;
        if (!getPasswordStrengthContract().verify(enteredPassword))
            return PASSWORD_TOO_WEAK;
        JSONObject object = new JSONObject();
        object.put("passwordHash", hashPassword(enteredPassword));
        ServerSingleton.getServer().getUserDatabase().writeFile(enteredUsername, object.toString());
        return NO_ERROR;
    }

    @Override
    public int signIn(@NotNull final JSONObject bundle) {
        final String enteredUsername = bundle.getString("username").toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getString("password");
        final String generatedToken = bundle.getString("generated_token");
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return USERNAME_EMPTY;
        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return PASSWORD_EMPTY;
        if (generatedToken.isEmpty() || generatedToken.isBlank() || !getTokenDiversityContract().verify(generatedToken))
            return BAD_TOKEN;
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
    public boolean authenticate(@NotNull String username, @NotNull String token) {
        final Set<String> tokens = sessions.get(username);
        return tokens != null && tokens.contains(token);
    }
}
