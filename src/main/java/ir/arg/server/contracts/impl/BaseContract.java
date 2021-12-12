package ir.arg.server.contracts.impl;

import ir.arg.server.contracts.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class BaseContract<T> implements Contract<T> {
    @NotNull
    protected final String title;

    protected BaseContract(@NotNull final String title) {
        this.title = title;
    }

    @Override
    public @NotNull String getQuantityTitle() {
        return title;
    }
}
