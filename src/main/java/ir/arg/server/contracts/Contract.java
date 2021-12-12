package ir.arg.server.contracts;

import org.jetbrains.annotations.NotNull;

public interface Contract<T> {

    @NotNull
    String getQuantityTitle();

    boolean verify(@NotNull final T data);

    @NotNull
    String getError(@NotNull final T data);
}
