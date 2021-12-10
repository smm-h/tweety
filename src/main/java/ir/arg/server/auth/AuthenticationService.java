package ir.arg.server.auth;

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

    @NotNull
    String hashPassword(final String password);
}