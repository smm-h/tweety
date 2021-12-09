package ir.arg.server;

import org.jetbrains.annotations.NotNull;

public interface StoredOnDisk {
    String getFilename();

    boolean isOnDisk();

    @NotNull
    String serialize();

    default void writeDataToDisk() {
        // TODO
    }

    default String readDataFromDisk() {
        // TODO
        return null;
    }
}
