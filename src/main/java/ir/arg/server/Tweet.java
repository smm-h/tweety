package ir.arg.server;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Tweet extends DatabaseElement {

    @Override
    default @NotNull Database getDatabase() {
        return App.getInstance().getTweetDatabase();
    }

    @NotNull
    String getSender();

    @NotNull
    String getContents();

    @NotNull
    String getSentOn();

    @NotNull
    Set<String> getLikes();

    default int getLikeCount() {
        return getLikes().size();
    }
}
