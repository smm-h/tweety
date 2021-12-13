package ir.arg.server.impl;

import ir.arg.server.Database;
import ir.arg.server.DatabaseElement;
import ir.arg.server.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DatabaseImpl implements Database {

    private final String directory;
    private final PrintStream log;

    public DatabaseImpl(@NotNull final String directory, @NotNull final String log) {
        this.directory = directory;
        this.log = Logger.getLog(log);
    }

    @NotNull
    @Override
    public PrintStream getLog() {
        return log;
    }

    @Override
    public boolean fileExists(@NotNull String filename) {
        final Path path = Path.of(directory, filename);
        return Files.exists(path);
    }

    @Nullable
    @Override
    public String readFile(@NotNull String filename) throws IOException {
        return Files.readString(Path.of(directory, filename));
    }

    @Override
    public void writeFile(@NotNull String filename, @NotNull String contents) throws IOException {
        Files.writeString(Path.of(directory, filename), contents);
    }

    @Override
    public void deleteFile(@NotNull String filename) throws IOException {
        Files.delete(Path.of(directory, filename));
    }

    private final Set<DatabaseElement> queueForRewrite = new LinkedHashSet<>();
    private final long idleWait = TimeUnit.SECONDS.toMillis(1);

    {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(idleWait);
                    for (DatabaseElement element : queueForRewrite) {
                        rewrite(element);
                    }
                    queueForRewrite.clear();
                } catch (Throwable e) {
                    log(e);
                }
            }
        }).start();
    }

    private void rewrite(@NotNull DatabaseElement element) {
        try {
            writeFile(element.getFilename(), element.serialize().toString());
        } catch (Throwable e) {
            log(e);
            rewrite(element);
        }
    }

    @Override
    public void enqueueForRewrite(@NotNull DatabaseElement element) {
        try {
            queueForRewrite.remove(element);
            queueForRewrite.add(element);
        } catch (Throwable e) {
            log(e);
            rewrite(element);
        }
    }
}
