package ir.arg.server.contracts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Contract<T> {

    @NotNull
    String getQuantityTitle();

    boolean verify(@NotNull final T data);

    @Nullable
    String getError(@NotNull final T data);
}
