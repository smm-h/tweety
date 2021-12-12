package ir.arg.server;

import ir.arg.server.contracts.PasswordStrengthContract;
import ir.arg.server.contracts.TokenDiversityContract;
import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface AuthenticationService extends ErrorCode {

    PasswordStrengthContract getPasswordStrengthContract();

    TokenDiversityContract getTokenDiversityContract();

    default boolean isUsernameCharacterValid(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_';
    }

    default boolean isUsernameInvalid(@NotNull final String username) {
        for (char c : username.toCharArray()) {
            if (!isUsernameCharacterValid(c)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    int signUp(@NotNull final JSONObject bundle);

    @NotNull
    int signIn(@NotNull final JSONObject bundle);

    /**
     * A deterministic one-way hashing function to hash passwords.
     *
     * @param password The password to hash
     * @return The hash of the password
     */
    @NotNull
    String hashPassword(@NotNull final String password);

    void createSession(@NotNull final User user, @NotNull final String token);

    /**
     * Check and see if the provided token is a valid session for the provided user
     * @param username The username of the user
     * @param token The authentication token generated during sign-up
     * @return Whether or not the session is valid
     */
    boolean authenticate(@NotNull final String username, @NotNull final String token);
}