package ir.arg.server;

import org.jetbrains.annotations.NotNull;

public interface StoredOnDisk {
    @NotNull
    String getFilename();

    @NotNull
    String serialize();
}
