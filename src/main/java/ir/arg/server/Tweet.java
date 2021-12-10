package ir.arg.server;

import ir.arg.server.impl.TweetImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;

public interface Tweet extends StoredOnDisk {
    @NotNull
    String getSender();

    int getIndex();

    @NotNull
    String getContents();

    @NotNull
    String getSentOn();

    @NotNull
    Collection<String> getLikes();

    default int getLikeCount() {
        return getLikes().size();
    }
}
