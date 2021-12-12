package ir.arg.server.auth.contracts;

import ir.arg.server.contracts.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PasswordStrengthContract extends Contract<String> {

    /**
     * @param data The password
     * @return Whether or not a given password is strong enough, as defined in contracts.md#password-strength
     */
    @Override
    boolean verify(final @NotNull String data);

    @Override
    default @NotNull String getQuantityTitle() {
        return "A password";
    }

    @Override
    default @Nullable String getError(final @NotNull String data) {
        return verify(data) ? null : "A secure password must be at least 8 characters long, and contain at least one lowercase letter, one uppercase letter, one digit, and a character not from any of these.";
    }

    /**
     * @param password The password
     * @return The strength of a given password. Any value above 0 is strong enough.
     */
    float getPasswordStrength(final String password);
}
