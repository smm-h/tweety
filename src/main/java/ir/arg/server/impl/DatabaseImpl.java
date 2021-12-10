package ir.arg.server.impl;

import ir.arg.server.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseImpl implements Database {

    private final String directory;

    public DatabaseImpl(final String directory) {
        this.directory = directory;
    }

    @Override
    public boolean fileExists(@NotNull String filename) {
        final Path path = Path.of(directory, filename);
        return Files.exists(path);
    }

    @Nullable
    @Override
    public String readFile(@NotNull String filename) {
        final Path path = Path.of(directory, filename);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.err.println("file not found: " + path);
            return null;
        }
    }

    @Override
    public void writeFile(@NotNull String filename, @NotNull String contents) {
        final Path path = Path.of(directory, filename);
        try {
            Files.writeString(path, contents);
        } catch (IOException e) {
            System.err.println("failed writing to file: " + path);
        }
    }

    @Override
    public boolean deleteFile(@NotNull String filename) {
        final Path path = Path.of(directory, filename);
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            System.err.println("failed to delete file: " + path);
            return false;
        }
    }
}
