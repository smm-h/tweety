package ir.arg.server;

import org.jetbrains.annotations.NotNull;

public interface Database {
    boolean fileExists(@NotNull final String filename);
    String readFile(@NotNull final String filename);
    void writeFile(@NotNull final String filename, @NotNull final String contents);
}
