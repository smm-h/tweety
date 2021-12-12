package ir.arg.server.contracts.impl;

import org.jetbrains.annotations.NotNull;

public class LengthLimitContract extends BaseContract<String> {

    private final int minLength, maxLength;

    protected LengthLimitContract(@NotNull final String title, final int minLength, final int maxLength) {
        super(title);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean verify(@NotNull final String data) {
        final int length = data.length();
        return (minLength == -1 || length >= minLength) && (maxLength == -1 || length <= maxLength);
    }

    @Override
    public @NotNull String getError(@NotNull final String data) {
        final int length = data.length();
        if (minLength != -1)
            if (length < minLength)
                return title + " must be at least " + minLength + " characters long.";
        if (maxLength != -1)
            if (length > maxLength)
                return title + " cannot be more than " + maxLength + " characters long.";
        return "";
    }
}
