package ir.arg.server.impl;

import ir.arg.server.*;
import ir.arg.server.contracts.PasswordStrengthContract;
import ir.arg.server.contracts.TokenDiversityContract;
import ir.arg.server.contracts.impl.PasswordStrengthContractImpl;
import ir.arg.server.contracts.impl.TokenDiversityContractImpl;
import ir.arg.server.shared.RandomHex;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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


        final User user = new UserImpl();
        server.getUserStorage().addUserToMemory(user);

        JSONObject object = new JSONObject();
        object.put("passwordHash", hashPassword(enteredPassword));
        try {
            server.getUserDatabase().writeFile(enteredUsername, object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
