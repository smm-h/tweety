package ir.arg.server.auth;

import ir.arg.server.User;
import org.jetbrains.annotations.NotNull;

public interface AuthenticationService {

    PasswordStrengthService getPasswordStrengthService();

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
    SigningUpOutcome signUp(@NotNull final SignUpBundle bundle);

    @NotNull
    SigningInOutcome signIn(@NotNull final SignInBundle bundle);

    /**
     * A deterministic one-way hashing function to hash passwords.
     * @param password The password to hash
     * @return The hash of the password
     */
    @NotNull
    String hashPassword(@NotNull final String password);

    void createSession(@NotNull final User user, @NotNull final String token);

    boolean isSessionValid(@NotNull final String username, @NotNull final String token);
}