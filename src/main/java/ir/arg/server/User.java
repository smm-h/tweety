package ir.arg.server;

import org.jetbrains.annotations.NotNull;

public interface User extends StoredOnDisk {
    @NotNull String getUsername();
    @NotNull String getName();
    @NotNull String getBio();
    @NotNull String getPasswordHash();
}
