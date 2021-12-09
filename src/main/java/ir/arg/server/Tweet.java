package ir.arg.server;

import ir.arg.server.impl.TweetImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;

public interface Tweet extends StoredOnDisk {
    @NotNull
    String getSender();

    @NotNull
    String getContents();

    @NotNull
    String getSentOn();

    @NotNull
    Collection<String> getLikes();

    default int getLikeCount() {
        return getLikes().size();
    }

    @NotNull
    static Tweet create(@NotNull final User user, @NotNull final String contents) {
        return TweetImpl.create(user, contents);
    }
}
