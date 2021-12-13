package ir.arg.server.impl;

import ir.arg.server.*;
import ir.arg.server.contracts.PasswordStrengthContract;
import ir.arg.server.contracts.TokenDiversityContract;
import ir.arg.server.contracts.impl.PasswordStrengthContractImpl;
import ir.arg.server.contracts.impl.TokenDiversityContractImpl;
import ir.arg.shared.RandomHex;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    private interface PasswordHash {
        @NotNull String hash(@NotNull String password);
    }

    @NotNull
    private final PasswordHash passwordHash = createMessageDigest();

    @NotNull
    private PasswordHash createMessageDigest() {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            return password -> new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("FAILED TO GET SHA-256; SERVER OPERATING WITH NO PASSWORD HASH");
            return password -> password;
        }
    }

    @Override
    public @NotNull String hashPassword(@NotNull String password) {
        return passwordHash.hash(password);
    }

    @Override
    public int signUp(@NotNull final JSONObject bundle) {

        final Server server = ServerSingleton.getServer();

        final String enteredUsername = bundle.getString("username").toLowerCase(Locale.ROOT);
        final String enteredPassword = bundle.getString("password");
        if (enteredUsername.isEmpty() || enteredUsername.isBlank())
            return USERNAME_EMPTY;

        if (enteredPassword.isEmpty() || enteredPassword.isBlank())
            return PASSWORD_EMPTY;

        if (isUsernameInvalid(enteredUsername))
            return BAD_USERNAME;

        final UserStorage userStorage = server.getUserStorage();
        if (userStorage.usernameExists(enteredUsername))
            return USERNAME_ALREADY_EXISTS;

        if (!getPasswordStrengthContract().verify(enteredPassword))
            return PASSWORD_TOO_WEAK;

        final User user = UserImpl.newBlank(enteredUsername, hashPassword(enteredPassword));
        server.getUserStorage().addUserToMemory(user);
        server.getUserDatabase().enqueueForRewrite(user);
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
        if (user == null)
            return USER_NOT_FOUND;

        if (!hashPassword(enteredPassword).equals(user.getPasswordHash()))
            return INCORRECT_PASSWORD;

        createSession(user, generatedToken);

        return NO_ERROR;
    }

    private final Map<String, Map<String, String>> sessions = new HashMap<>();

    @Override
    public void createSession(@NotNull User user, @NotNull String token) {
        final Map<String, String> mySessions = sessions.computeIfAbsent(user.getUsername(), s -> new HashMap<>());
        String id = null;
        while (id == null || mySessions.containsKey(id)) {
            id = RandomHex.generate(8);
        }
        mySessions.put(id, token);
    }

    @Override
    public JSONArray getSessions(@NotNull User user) {
        final JSONArray array = new JSONArray();
        final Map<String, String> mySessions = sessions.computeIfAbsent(user.getUsername(), s -> new HashMap<>());
        for (String key : mySessions.keySet()) {
            array.put(key);
        }
        return array;
    }

    @Override
    public int terminateSession(@NotNull User user, @NotNull String sessionId) {
        final Map<String, String> mySessions = sessions.computeIfAbsent(user.getUsername(), s -> new HashMap<>());
        if (mySessions.containsKey(sessionId)) {
            // TODO check to see if the current session overpowers this one
            mySessions.remove(sessionId);
            return NO_ERROR;
        } else {
            return SESSION_NOT_FOUND;
        }
    }

    @Override
    public boolean authenticate(@NotNull String username, @NotNull String token) {
        final Map<String, String> tokens = sessions.get(username);
        return tokens != null && tokens.containsValue(token);
    }
}
