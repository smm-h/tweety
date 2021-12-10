package ir.arg.server;

import org.jetbrains.annotations.NotNull;

/**
 * Enums that implement this interface must have a SUCCESSFUL instance whose getCode() returns 1, and 0 means an unknown error.
 */
public interface Outcome {
    int getCode();

    @NotNull
    String getDescription();
}
