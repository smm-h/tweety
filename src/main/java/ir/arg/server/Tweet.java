package ir.arg.server;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Tweet extends JSONSerializable {
    @NotNull
    String getSender();

    int getIndex();

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
