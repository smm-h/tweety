package ir.arg.server.contracts;

import org.jetbrains.annotations.NotNull;

public interface TokenDiversityContract extends Contract<String> {

    /**
     * @param data The token
     * @return Whether or not a given token is diverse enough, as defined in contracts.md#token-diversity
     */
    @Override
    boolean verify(final @NotNull String data);

    @Override
    default @NotNull String getQuantityTitle() {
        return "A token";
    }

    @Override
    default @NotNull String getError(final @NotNull String data) {
        return verify(data) ? "" : "A randomly-generated token must be at least 16 characters long, contain only hex characters, and at least 12 different digits.";
    }
}
