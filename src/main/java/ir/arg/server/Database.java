package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Database {
    boolean fileExists(@NotNull final String filename);

    @Nullable
    String readFile(@NotNull final String filename);

    void writeFile(@NotNull final String filename, @NotNull final String contents);

    boolean deleteFile(@NotNull final String filename);
}
