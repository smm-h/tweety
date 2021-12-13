package ir.arg.server;

import org.jetbrains.annotations.NotNull;

public interface DatabaseElement extends JSONSerializable {
    @NotNull
    String getFilename();

    @NotNull
    Database getDatabase();

    default void markAsModified() {
        getDatabase().enqueueForRewrite(this);
    }
}
