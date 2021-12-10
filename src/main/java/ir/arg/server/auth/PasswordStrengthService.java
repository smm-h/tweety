package ir.arg.server.auth;

public interface PasswordStrengthService {

    /**
     * @param password The password
     * @return Whether or not a given password is strong enough.
     */
    boolean isPasswordStrong(final String password);

    /**
     * @param password The password
     * @return The strength of a given password. Any value above 0 is strong enough.
     */
    float getPasswordStrength(final String password);
}
