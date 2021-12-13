package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintStream;

public interface Database extends Logger {

    boolean fileExists(@NotNull final String filename);

    @Nullable
    String readFile(@NotNull final String filename) throws IOException;

    void writeFile(@NotNull final String filename, @NotNull final String contents) throws IOException;

    void deleteFile(@NotNull final String filename) throws IOException;

    void enqueueForRewrite(@NotNull final DatabaseElement databaseElement);
}
